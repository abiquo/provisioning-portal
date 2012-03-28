package controllers;

import java.sql.Timestamp;
import java.util.Properties;

import models.UserDetails;
import models.sc_offer;

import org.apache.ivy.ant.IvyAntSettings.Credentials;
import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.AbiquoContextFactory;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.enterprise.User;
import org.jclouds.abiquo.features.services.AdministrationService;
import org.jclouds.abiquo.features.services.CloudService;
import org.jclouds.abiquo.predicates.cloud.VirtualMachinePredicates;

import play.cache.Cache;
import play.mvc.Controller;
import portal.util.Context;

public class Helper extends Controller{
	
	//private static UserDetails userDetails = new ;
	
	static String vdcNameGen(String username ){
		
		//POR-%user-%userdefinedname-%UTC
		java.util.Date date= new java.util.Date();
		Timestamp tstamp = new Timestamp(date.getTime());
		String prefix = "POR";
		String vdc_generated_name = prefix +"-"+ username +"-"+ tstamp;
		 		
		return vdc_generated_name;
	}
	
	public static void displayIcon (Integer id)
	{
		
			System.out.println("------ id ---" + id );
			   final sc_offer offer = sc_offer.findById(id);
			   notFoundIfNull(offer);
			   response.setContentTypeIfNotSet(offer.getIcon().type());
			   renderBinary(offer.getIcon().get());
	}
	public static void displayImage (Integer id)
	{
		
			System.out.println("------ id ---" + id );
			   final sc_offer offer = sc_offer.findById(id);
			   notFoundIfNull(offer);
			   response.setContentTypeIfNotSet(offer.getImage().type());
			   renderBinary(offer.getImage().get());
	}
	
	static String getUser(){
		System.out.println(" ----------INSIDE GETUSER()------");
		String user = (String) Cache.get(session.getId());
		System.out.println(" CACHE USER : " + user);
		System.out.println("-----------EXITING GETUSER()------");
		return user;
		/* String username = userDetails.getUsername();
		 String password = userDetails.getPassword();
		String username = session.get("username");
		String password = session.get("password");
		 System.out.println("---Helper.getUser() Username password ---" + username );
		 if (username !=null)
		 {
		 AbiquoContext context = Context.getContext(username,password);
		
		 AdministrationService adminService = context.getAdministrationService();
		 User user =  adminService.getCurrentUserInfo();
		 //context.close();
		 return user; 
		 }
		 else 
		 {
			 flash.error("Session expired. Login again");
			 Login.login_page();
			 return null;
		 }*/
	}
	
		static VirtualMachine getVMDetails(Integer vdc_id ,Integer vapp_id,  Integer vm_id)
	{
	/*	String username = userDetails.getUsername();
		String password = userDetails.getPassword();*/
			System.out.println("---------INSIDE getVMDetais()----------");
			//String user = (String) Cache.get(session.getId());
			/*String username = session.get("username");
			String password = session.get("password");*/
		//System.out.println("---Helper.getVMDetails() Username password virtual-machine ---" + username + " "  + password + " " + vdc_id + " " +  vapp_id +" " + vm_id);
		AbiquoContext context = Context.getContext("demo-scan","demo");
		
		CloudService cloudService = context.getCloudService();	
		System.out.println(" ----getVMDetails() context ---" + context +" cloudservice " + cloudService ) ;
		VirtualMachine vm = cloudService.getVirtualDatacenter(vdc_id).getVirtualAppliance(vapp_id).getVirtualMachine(vm_id);
		 System.out.println("newly deployed machine " + vm );
		 if (vm != null)
		 {
			 System.out.println("vmName after serach :: " + vm);
			// context.close();
			 return vm;
		 }
		 else {
			 System.out.println("------NULL ----");
			 //context.close();
			 return null;
		 }
	}
}
