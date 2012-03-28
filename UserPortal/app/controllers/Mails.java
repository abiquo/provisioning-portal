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

import models.Deploy_Bundle_Nodes;
import models.MKT_Configuration;
import models.User_Consumption;
import models.sc_offer;

import org.apache.commons.mail.EmailAttachment;
import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.enterprise.User;
import play.mvc.Controller;
public class Mails extends Mailer {
 
/*  
  public static void sendEmail(String virtualMachineName)
  {
	  
	  System.out.println("vm in MAILS :" + virtualMachineName );
	  int vncPort = 0 ;
	  String vncAddress = null;
	  String password = null;
	  String name = null;
	  System.out.println("1.");
	  	VirtualMachine virtulMachine = Helper.getVMDetails(virtualMachineName);
	  	if (virtulMachine != null)
	  	{
	  		password = virtulMachine.getPassword();
	  		vncPort = virtulMachine.getVncPort();
	  	   vncAddress = virtulMachine.getVncAddress();
	  	   name = virtulMachine.getTemplate().getName();
	  	}
		 User user = Helper.getUser();
		 setSubject("Abiquo Confirmation");
		 System.out.println(" email user " + user.getName());
		 addRecipient(user.getEmail());
		 setFrom("Admin <provisioning-portal@abiquo.com>");
		 syst
		 send(name, vncPort , vncAddress , user, password);
 }	
  */
  public static void sendEmail(Integer vncPort ,String vncAddress ,String password, String name , Integer vdc_id, String useremail)
  {
	  
	System.out.println("INSIDE MAILS SENDEMAIL()....");
	 /* int vncPort = 0 ;
	  String vncAddress = null;
	  String password = null;
	  String name = null;

	  String useremail = Helper.getUser();*/
	  System.out.println(" EMAIL USERNAME : " + useremail);
		 setSubject("Abiquo Confirmation");
		
	/*  VirtualMachine virtulMachine = Helper.getVMDetails(vdc_id, vapp_id, vm_id);
	 // Consumer.updateDeployBundleNode(virtulMachine);
	  	if (virtulMachine != null)
	  	{
			//System.out.println(" Mails...." + user.getEmail());
	  		password = virtulMachine.getPassword();
	  		vncPort = virtulMachine.getVncPort();
	  	      vncAddress = virtulMachine.getVncAddress();
	  	      name = virtulMachine.getVirtualAppliance().getName();
	  	 }
	
		 User user = Helper.getUser();
		
		 setSubject("Abiquo Confirmation");
		 System.out.println(" EMAIL USERNAME : " + user.getName());
		 addRecipient(user.getEmail());

		 setFrom("Admin <provisioning-portal@abiquo.com>");
 		 System.out.println("SENDING EMAIL TO  ...." + user.getEmail());
		 send(name, vncPort , vncAddress , user, password);
*/		
		 addRecipient(useremail);

		 setFrom("Admin <provisioning-portal@abiquo.com>");
		 System.out.println("SENDING EMAIL TO  ...." + useremail);
		 send(name, vncPort , vncAddress , useremail, password);
		

 }
  public static void sendFailureEmail(Integer vncPort ,String vncAddress ,String password, String name , Integer vdc_id, String useremail)
  {
	  
	System.out.println("INSIDE Mails.sendFailureEmail()....");
	 /* int vncPort = 0 ;
	  String vncAddress = null;
	  String password = null;
	  String name = null;
	  String useremail = Helper.getUser();*/
	 // System.out.println("GETTING  VM DETAILS.");
	
	  System.out.println(" EMAIL USERNAME : " + useremail);
	 /*// VirtualMachine virtulMachine = Helper.getVMDetails(vdc_id, vapp_id, vm_id);
	 // Consumer.updateDeployBundleNode(virtulMachine);
	  	if (virtulMachine != null)
	  	{
			//System.out.println(" Mails...." + user.getEmail());
	  		password = virtulMachine.getPassword();
	  		vncPort = virtulMachine.getVncPort();
	  	      vncAddress = virtulMachine.getVncAddress();
	  	      name = virtulMachine.getVirtualAppliance().getName();
	  	 }
	
		// User user = Helper.getUser();
	  	;
	*/	 setSubject("Abiquo Confirmation");
		 
		 addRecipient(useremail);

		 setFrom("Admin <provisioning-portal@abiquo.com>");
 		 System.out.println("SENDING EMAIL TO  ...." + useremail);
		 send(name, vncPort , vncAddress , useremail, password);
		

 }	
  
	

  public static void updateDeployBundleNode(Integer vdc_id ,Integer vapp_id,  Integer vm_id, VirtualMachine vmName) {
		System.out.println(" Inside Consumer.updateDeployBundleNode() ......");
		System.out.println(" CREATED VM : " + vmName );
			try{ // wrapping everything around a try catch
				System.out.println(JPA.isEnabled());
				    if(JPA.local.get() == null)
	                {
	                	
	                System.out.println("NULL JAP");
	                    JPA.local.set(new JPA());
	                    JPA.local.get().entityManager = JPA.newEntityManager();
	                } 
	                
		VirtualMachine vm = Helper.getVMDetails(vdc_id, vapp_id, vm_id);
		String password = vm.getPassword();
		int port =  vm.getVncPort();
		String ip = vm.getVncAddress();
		String name = vm.getName();
		System.out.println(" PASSWORD : " + password + "  IP :" + ip + "  PORT :" + port);
		
				
		
		 EntityManager em  = JPA.em();
		 Query query=	em.createQuery(" select p from Deploy_Bundle_Nodes as p where p.node_id = ?1");
		 query.setParameter(1, vm_id);
		 List<Deploy_Bundle_Nodes> bundleNodes = query.getResultList();
		 Iterator<Deploy_Bundle_Nodes> bundleNodes_it = bundleNodes.iterator();
		 Integer id= null;
		 while (bundleNodes_it.hasNext())
		 {
			 Deploy_Bundle_Nodes node = bundleNodes_it.next();	
			 
			 id = node.getIdbundle_nodes();
			 
		 }
		 System.out.println("id :" + id );
		 
		 Deploy_Bundle_Nodes nodes=  Deploy_Bundle_Nodes.findById(id);
		 System.out.println("nodes ::; "+ nodes  );
		 em.getTransaction().begin();
		 System.out.println(password);
		 nodes.setVdrp_password(password);
		 System.out.println(ip);
		 nodes.setVdrpPort(port);  
		 nodes.setVdrpIP(ip);
		 nodes.save();
		 em.getTransaction().commit();
		
		 Query query1 =	em.createQuery(" select p from User_Consumption as p where p.vdc_id = ?1");
		 query1.setParameter(1, vdc_id);
		 List<User_Consumption> consumption = query1.getResultList();
		 Iterator<User_Consumption> consumption_it = consumption.iterator();
		 String emailID= null;
		 while (consumption_it.hasNext())
		 {
			 User_Consumption userCon  = consumption_it.next();	
			 
			 emailID = userCon.getUserid();
			 
		 }
		 System.out.println(" preparaing to send mail.....");
		 Mails.sendEmail(port,ip,password,name,vdc_id, emailID);
				}
				finally {
	                JPA.local.get().entityManager.close();
	                JPA.local.remove();
	            } 
		
	}
   

}
 


