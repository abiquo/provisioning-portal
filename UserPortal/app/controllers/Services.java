package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.mail.Session;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.VirtualAppliance;
import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.cloud.VirtualMachineTemplate;
import org.jclouds.abiquo.domain.cloud.Volume;
import org.jclouds.abiquo.features.services.AdministrationService;
import org.jclouds.abiquo.features.services.CloudService;
import org.jclouds.abiquo.predicates.cloud.VirtualAppliancePredicates;
import org.jclouds.abiquo.predicates.cloud.VirtualDatacenterPredicates;
import org.jclouds.abiquo.predicates.cloud.VirtualMachinePredicates;
import org.jclouds.rest.AuthorizationException;

import play.data.validation.Required;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Scope;
import portal.util.Context;

public class Services extends Controller {

	public static void connect(@Required String username, @Required String password)
	{
		if(validation.hasError(username)){
			flash.error("Oops, please enter your name!");
			login_page();
		}
		/*else if(validation.hasError(password)){
			flash.error("Oops, please enter your password!");
			login_page();
		}
		else if(validation.hasErrors()) {
	        flash.error("Oops, please enter your name and password !");
	        login_page();
	    }*/
		session.put("username", username);
		session.put("password", password);
		listVDC();
		
	}
	
	public static void listVDC(){
		
		String username =session.get("username");
		String password =session.get("password");
		
		AbiquoContext context = Context.getContext(username,password);
		System.out.println("contect at listVDC " + context);
		try{
			
			CloudService cloudService = context.getCloudService();
			
			Iterable<VirtualDatacenter> vdc_list = cloudService.listVirtualDatacenters();
			render(vdc_list,username,password);
			/*if (vdc_list != null)
			{
							
			render(vdc_list,username,password);
			}
			else 
			{
				flash.error("Sorry, There are no virtual datacenters to display.!");
				listVDC();
			}
		
			*/
		}
		catch (AuthorizationException e )
			{
				flash.error("Wrong credentials or Un-authorized user !");
				login_page();
			}
		finally
			{ 
				context= null;
				System.out.println("context afee##### " + context);
			}
		

}

	public static void listVA(String vdc)
	{
		String username =session.get("username");
		String password =session.get("password");
		session.put("vdc", vdc) ;
		System.out.println("----vdc-----"+ vdc);
		AbiquoContext context = Context.getContext(username,password);
		try {
			if (vdc != null )
			{
				VirtualDatacenter virtualDatacenter = context.getCloudService().findVirtualDatacenter(VirtualDatacenterPredicates.name(vdc));
				List<VirtualAppliance> vaList = virtualDatacenter.listVirtualAppliances();
				if (vaList.size()>0)
				{
						render(vaList,vdc);
				}
				else
				{
						String msg = " NO VA to display!!";
						render(msg);
				}
			}
		} 
		finally{
			context = null;
		}

}
	public static void listVM(String va)
	{
		String username =session.get("username");
		String password =session.get("password");
		System.out.println("----va-----"+ va);
		AbiquoContext context = Context.getContext(username,password);
		try {
			VirtualAppliance virtualAppliance = context.getCloudService().findVirtualAppliance(VirtualAppliancePredicates.name(va));
		
		List<VirtualMachine> vmList = virtualAppliance.listVirtualMachines();
		if (vmList.size()>0)
		{
		render(vmList, virtualAppliance);
		}
		else {
			String msg = " NO VA to display!!";
			render(msg);
		}
		} 
		finally{
			context = null;
		}

}

	public static void vmDetails(String vm)
	{
		String username =session.get("username");
		String password =session.get("password");
		System.out.println("----va-----"+ vm);
		AbiquoContext context = Context.getContext(username,password);
		try {
			VirtualMachine virtualMachine = context.getCloudService().findVirtualMachine(VirtualMachinePredicates.name(vm));
		
			Integer cpu = virtualMachine.getCpu();
			Integer ram = virtualMachine.getRam();
			long hd = virtualMachine.getHdInBytes();
			VirtualMachineTemplate template = virtualMachine.getTemplate();
			String template_path = template.getPath();
			List <Volume> vols = virtualMachine.listAttachedVolumes();
		render(vm, cpu,ram,hd,vols, template_path);
		} 
		finally{
			context = null;
		}

}
	
	
	public static void login_page()
	{
		render();
	}
	
	public static void logout()
	{
		session.remove("username");
		session.remove("password");
		
		
	}
}
