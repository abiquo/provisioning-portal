package controllers;
 
import play.*;
import play.db.jpa.GenericModel.JPAQuery;
import play.db.jpa.JPA;
import play.db.jpa.NoTransaction;
import play.db.jpa.Transactional;
import play.mvc.*;

import portal.util.Context;
// static play.mvc.Scope.Session.session;
//import play.mvc.Scope.Session.session;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import models.Deploy_Bundle;
import models.Deploy_Bundle_Nodes;
import models.MKT_Configuration;
import models.User_Consumption;
import models.sc_offer;
import models.sc_offers_subscriptions;

import org.apache.commons.mail.EmailAttachment;
//import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.VirtualAppliance;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.enterprise.User;
import play.mvc.Controller;


public class Mails extends Mailer {

	/** 
	 * Confirmation Email sent to user after deployment success.
	 * @param vncPort
	 * @param vncAddress
	 * @param password
	 * @param name
	 * @param offerName
	 * @param useremail
	 * @param exp_date
	 */
  public static void sendEmail(Integer vncPort ,String vncAddress ,String password, String name , String offerName, String useremail, Date exp_date)
  {
	  
	  	Logger.info("INSIDE MAILS SENDEMAIL()....");
	  	setSubject("Abiquo Confirmation");
	    addRecipient(useremail);
	    setFrom("Admin <provisioning-portal@abiquo.com>");
		Logger.info("SENDING EMAIL TO  ...." + useremail);
		send(offerName, name, vncPort , vncAddress , useremail, password, exp_date);
		

 }
  
  /**
   * Email sent if deployment fails.
   * @param offerName
   * @param name
   * @param useremail
   */
  public static void sendFailureEmail(String offerName, String name , String useremail)
  {
	  
		Logger.info("INSIDE Mails.sendFailureEmail()....");
		setSubject("Abiquo Confirmation");
		addRecipient(useremail);
		setFrom("Admin <provisioning-portal@abiquo.com>");
	 	Logger.info("SENDING EMAIL TO  ...." + useremail);
		send(name, offerName , useremail );
 }	
  
	
/**
 * Updation made after deployment.
 * @param vdc_id Deployed virtual machine's Virtual datacenter
 * @param vapp   Deployed virtual machine's virtual appliance 
 * @param vm_id  Deployed virtual machine id
 * @param vmName Deployed virtual machine name
 */
  public static void updateUserConsumption_onSuccess(Integer vdc_id ,VirtualAppliance vapp,  Integer vm_id, VirtualMachine vmName) 
  {
		Logger.info(" Inside Consumer.updateDeployBundleNode() ......");
		Logger.info(" CREATED VM : " + vmName );
		try
		{ 
					/*After deployment , JPA em() and session variables are lost */
				    if(JPA.local.get() == null)
	                {
				    	JPA.local.set(new JPA());
	                    JPA.local.get().entityManager = JPA.newEntityManager();
	                } 
				    
				    String offerName  = vapp.getName();
				    Integer vapp_id = vapp.getId();
				    Enterprise enterprise = vapp.getEnterprise();
				    Logger.info(" vapp enterprise :" +  enterprise );
				    VirtualMachine vm = Helper.getVMDetails(vdc_id, vapp_id, vm_id, enterprise);
					
				    /* get virtual machine detials : password, port, address */
				    String vmpassword = vm.getPassword();
					int port =  vm.getVncPort();
					String ip = vm.getVncAddress();
					String name = vm.getName();
					Logger.info(" PASSWORD : " + vmpassword + "  IP :" + ip + "  PORT :" + port);
				
					/* update portal database */
					EntityManager em  = JPA.em();
					 Query query=	em.createQuery(" select p from Deploy_Bundle_Nodes as p where p.node_id = ?1");
					 query.setParameter(1, vm_id);
					 Integer id= null;
					 List<Deploy_Bundle_Nodes> bundleNodes = query.getResultList();
					 for ( Deploy_Bundle_Nodes node :  bundleNodes)
					 {
						 	id = node.getIdbundle_nodes();
						 	
					 }
					 Logger.info("id :" + id );
					 
					 Deploy_Bundle_Nodes nodes=  Deploy_Bundle_Nodes.findById(id);
					 Logger.info("nodes ::; "+ nodes  );
					 em.getTransaction().begin();
					 Logger.info(vmpassword);
					 nodes.setVdrp_password(vmpassword);
					 Logger.info(ip);
					 nodes.setVdrpPort(port);  
					 nodes.setVdrpIP(ip);
					 nodes.save();
					 em.getTransaction().commit();
					/* get user email id and expiration date */
					 Query query1 =	em.createQuery(" select p from User_Consumption as p where p.vdc_id = ?1");
					 query1.setParameter(1, vdc_id);
					 String emailID= null;
					 Date exp_date = null;
					 List<User_Consumption> consumption = query1.getResultList();
					 for( User_Consumption userCon : consumption )
					 {
						 emailID = userCon.getUserid();
						 exp_date = userCon.getExpiration_date();
						 
					 }
					 Logger.info(" preparaing to send mail.....");
					 Mails.sendEmail( port , ip , vmpassword , name, offerName , emailID ,exp_date);
			}
			finally 
			{
	                JPA.local.get().entityManager.close();
	                JPA.local.remove();
	         } 
		
	}

  
  /**
   * Update( i.e delete) database entries if deployment fails. Sends failure email to user.
   * @param vdc_id
   * @param vapp
   * @param vm
   */
  public static void updateUserConsumption_onFailure(Integer vdc_id , VirtualAppliance vapp , VirtualMachine vm) 
  {
		Logger.info(" Inside Consumer.updateDeployBundleNode() ......");
		Logger.info(" CREATED VM : " + vm );
		try
		{ 
				 if(JPA.local.get() == null)
	                {
	                	
	                Logger.info("NULL JAP");
	                    JPA.local.set(new JPA());
	                    JPA.local.get().entityManager = JPA.newEntityManager();
	                } 
				    
				    String offerName  = vapp.getName();
				    String name = vm.getName();
					/* get user email id */
					EntityManager em  = JPA.em();
					Query query1 =	em.createQuery(" select p from User_Consumption as p where p.vdc_id = ?1");
					 query1.setParameter(1, vdc_id);
					
					 String emailID= null;
					 Integer user_consumption_id = null;
					 List<User_Consumption> consumption = query1.getResultList();
					 for( User_Consumption userCon : consumption )
					 {
						 emailID = userCon.getUserid();
						 user_consumption_id = userCon.getIduser_consumption();
						 						 
					 }
					 /* delete that entry from database  and send email to user about deployment failure*/
					 em.getTransaction().begin();
					 User_Consumption userConsumption  = JPA.em().find(User_Consumption.class, user_consumption_id);
					 userConsumption.delete();
					 em.getTransaction().commit();
					 Logger.info(" preparaing to send mail.....");
					 Mails.sendFailureEmail(offerName, name, emailID);
			}
			finally 
			{
	                JPA.local.get().entityManager.close();
	                JPA.local.remove();
	         } 
		
	}


}
 


