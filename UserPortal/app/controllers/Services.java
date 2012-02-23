package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Session;

import monitor.VmEventHandler;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.AbiquoContextFactory;
import org.jclouds.abiquo.domain.cloud.HardDisk;
import org.jclouds.abiquo.domain.cloud.VirtualAppliance;
import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;
import org.jclouds.abiquo.domain.cloud.VirtualDatacenter.Builder;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.cloud.VirtualMachineTemplate;
import org.jclouds.abiquo.domain.cloud.Volume;
import org.jclouds.abiquo.domain.config.Icon;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.enterprise.User;
import org.jclouds.abiquo.domain.exception.AbiquoException;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.domain.network.PrivateNetwork;
import org.jclouds.abiquo.features.services.AdministrationService;
import org.jclouds.abiquo.features.services.CloudService;
import org.jclouds.abiquo.monitor.VirtualMachineMonitor;
import org.jclouds.abiquo.predicates.cloud.VirtualAppliancePredicates;
import org.jclouds.abiquo.predicates.cloud.VirtualDatacenterPredicates;
import org.jclouds.abiquo.predicates.cloud.VirtualMachinePredicates;
import org.jclouds.abiquo.predicates.config.IconPredicates;
import org.jclouds.abiquo.predicates.enterprise.EnterprisePredicates;
import org.jclouds.rest.AuthorizationException;

import com.abiquo.model.enumerator.HypervisorType;
import com.google.common.base.Predicate;

import play.data.validation.Required;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Scope;
import portal.util.Context;
import java.sql.Timestamp;
import java.util.Date;
/**
 * @author bal
 *
 */
public class Services extends Controller {
	

	public static void connect(@Required String username, @Required String password)
	{
		if(validation.hasErrors()) {
	        flash.error(" Both Name and password are required!");
	        login_page();
	    }
		else 
		{
		session.put("username", username);
		session.put("password", password);
			
			listVDC();
		}
		
		
	}
	
	public static void listVDC(){
		
		
		String username =session.get("username");
		String password =session.get("password");
		
		AbiquoContext context = Context.getContext(username,password);
		
		try{
			
			 CloudService cloudService = context.getCloudService();
			
			Iterable<VirtualDatacenter> vdc_list = cloudService.listVirtualDatacenters();
						
			if (vdc_list != null)
			{
							
				render(vdc_list,username);
			}
			else 
			{
				String msg = " No Virtual Datacenters to display!!";
				render(msg);
				/*flash.error("Sorry, There are no virtual datacenters to display.!");
				listVDC();*/
			}
		
			
		}
		catch (AuthorizationException e )
			{
				flash.error("Wrong credentials or Un-authorized user !");
				login_page();
			}
		finally
			{ 
			context.close();
				context= null;
				
			}
		

}

	public static void listVA(String vdc)
	{
		String username =session.get("username");
		String password =session.get("password");
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
						String msg = " No Virtual Appliance to display!!";
						render(msg);
				}
			}
		} 
		finally{
			context.close();
			context = null;
		}

}
	public static void listVM( String vdc, String va)
	{
		String username =session.get("username");
		String password =session.get("password");
		
		AbiquoContext context = Context.getContext(username,password);
		try {
			VirtualAppliance virtualAppliance = context.getCloudService().findVirtualAppliance(VirtualAppliancePredicates.name(va));
			if (virtualAppliance != null )
			{
				List<VirtualMachine> vmList = virtualAppliance.listVirtualMachines();
		
				render(vmList,virtualAppliance, vdc);
			}
		
		else {
				String msg = " No Virtual Machine to display!!";
				render(msg);
		}
		} 
		
		finally{
			context.close();
			context = null;
			
		}

}

	public static void vmDetails(String vdc, String va, String vm)
	{
		String username =session.get("username");
		String password =session.get("password");
		
		AbiquoContext context = Context.getContext(username,password);
		try {
			VirtualMachine virtualMachine = context.getCloudService().findVirtualMachine(VirtualMachinePredicates.name(vm));
		
			Integer cpu = virtualMachine.getCpu();
			Integer ram = virtualMachine.getRam();
			long hd = virtualMachine.getHdInBytes();
			List<HardDisk> harddisk =virtualMachine.listAttachedHardDisks();
			
			VirtualMachineTemplate template = virtualMachine.getTemplate();
			String template_name = template.getName();
			String template_path = template.getPath();
			
			render(vdc,va,vm, cpu,ram,hd,harddisk, template_name,template_path);
		
		} 
		finally{
			context.close();
			context = null;
			
		}

}
	
	
	static String vdcNameGen(String username ){
		
		//POR-%user-%userdefinedname-%UTC
		java.util.Date date= new java.util.Date();
		Timestamp tstamp = new Timestamp(date.getTime());
		String prefix = "POR";
		String vdc_generated_name = prefix +"-"+ username +"-"+ tstamp;
		 		
		return vdc_generated_name;
	}
	
	
	
	static String getUsername(){
		String username =session.get("username");
		String password =session.get("password");
		
		 AbiquoContext context = Context.getContext(username,password);
		 AdministrationService adminService = context.getAdministrationService();
 		 String user =  adminService.getCurrentUserInfo().getName();
 		 context.close();
 		//User userinfo = adminService.getCurrentUserInfo();
 		 return user;
	}
	/*public static void DeployVA(String vdc, String selectedVA)
	{
		
		String username =session.get("username");
		String password =session.get("password");
		
		 AbiquoContext context = Context.getContext(username,password);
		 
		 VirtualDatacenter vdc_toDeploy = null;
		 VirtualAppliance virtualapp_todeploy= null;
		 VirtualMachine vm_todeploy=null;
		 
		 
		 try{
			 		System.out.println("VDC  "+ vdc + "  selectedVA " + selectedVA);
			 
			 		CloudService cloudService = context.getCloudService();
			 		AdministrationService adminService = context.getAdministrationService();
			 		String user1 =  adminService.getCurrentUserInfo().getName();
			 		
			 		System.out.println(" Current User : " + user1);
			 		VirtualAppliance virtualapp 	=	cloudService.findVirtualAppliance(VirtualAppliancePredicates.name(selectedVA));
			 		
			 		System.out.println(" Is VA found, if yes - name " + virtualapp);
			 		
			 		VirtualDatacenter virtualDC = virtualapp.getVirtualDatacenter();
			 		
			 		Datacenter datacenter = virtualDC.getDatacenter();
			 		HypervisorType hypervisor = virtualDC.getHypervisorType();
			 		
			 		
			 		Enterprise enterprise = getUserEnterprise();
			 		
			 		 PrivateNetwork network =  PrivateNetwork.builder(context)
			 				 					.name("10.80.0.0") 
			 				 					.gateway("10.80.0.1") 
			 				 					.address("10.80.0.0") 
			 				 					.mask(22)               
			 				 					.build();

			 		String user = getUsername();
			 		System.out.println("----USER---" + user);
					String vdcname = vdcNameGen(user);
					
					vdc_toDeploy = VirtualDatacenter.builder(context, datacenter, enterprise).name(vdcname)
							 .cpuCountLimits(0,0 )               
						        .hdLimitsInMb(0,0)  
						        .publicIpsLimits(0,0)               
						        .ramLimits(0,0)                
						        .storageLimits(0,0)  
						        .vlansLimits(0,0)                   
						        .hypervisorType(hypervisor)         
						        .network(network)                    
						        .build();
					 vdc_toDeploy.save();
			
					 
					 //VirtualAppliance vappliance=  cloudService.findVirtualAppliance(VirtualAppliancePredicates.name(va));
					 String vappliance_name = virtualapp.getName();
					
					  virtualapp_todeploy = VirtualAppliance.builder(context,vdc_toDeploy)
							 											.name(vappliance_name)
							 									     	.build();
					 virtualapp_todeploy.save();
					
					 List<VirtualMachine> vmlist_todeploy = virtualapp.listVirtualMachines();
					if (vmlist_todeploy.size() == 0)
					{
						System.out.println(" NO VMS ");
					}
					 Iterator vmlist_todeploy_it = vmlist_todeploy.iterator();
						 while (vmlist_todeploy_it.hasNext())
						 {
							 VirtualMachine aVM = (VirtualMachine) vmlist_todeploy_it.next();
							 
							 VirtualMachineTemplate vm_template_todeploy = aVM.getTemplate();
							 String vmName = aVM.getName();
							 int cpu = aVM.getCpu();
							 int ram = aVM.getRam();
							 Long hd = aVM.getHdInBytes();
							
							  vm_todeploy = VirtualMachine.builder(context, virtualapp_todeploy, vm_template_todeploy)
								        .name(vmName) 
								        .cpu(cpu)                 
								        .ram(ram)              
								        .build();
							 vm_todeploy.save();
							 List<HardDisk> hdisks = aVM.listAttachedHardDisks();
							 Iterator<HardDisk> hdisks_it = hdisks.iterator();
							 while (hdisks_it.hasNext())
							 {
								 HardDisk hdisk =  hdisks_it.next();
								 System.out.println(hdisk);
								 vm_todeploy.attachHardDisks(hdisk);
							 }
							
						 }
						 VmEventHandler handler = new VmEventHandler(context, vm_todeploy);
							
				          
			             VirtualMachineMonitor monitor =  context.getMonitoringService().getVirtualMachineMonitor();
			             monitor.register(handler);
		           
		 	            
						//virtualapp_todeploy.deploy();
						
						monitor.monitorDeploy(vm_todeploy);
						render();
		 }
		 catch (AbiquoException a) 
			{
			 if (vdc_toDeploy != null)
			 {
			   vdc_toDeploy.delete();
			   if (virtualapp_todeploy != null)
			   virtualapp_todeploy.delete();
			   if (vm_todeploy != null)
				   vm_todeploy.delete();
				flash.error("Sorry! There are some deployment issues with server");
				render();
			 }
			}
		 finally
				 {
					 context.close();
				 }
		 
	}
*/	
	public static void DeployVAalt( String selectedVA)
	{
		
		Properties props = new Properties();
		props.put("abiquo.endpoint", "http://67.111.53.253/api");
		AbiquoContext context = new AbiquoContextFactory().createContext("demo-deploy" ,"demo" ,props);
		
		 VirtualDatacenter vdc_toDeploy = null;
		 VirtualAppliance virtualapp_todeploy= null;
		 VirtualMachine vm_todeploy=null;
		 
		 
		 try{
			 		
			 		CloudService cloudService = context.getCloudService();
			 		AdministrationService adminService = context.getAdministrationService();
			 		String user1 =  adminService.getCurrentUserInfo().getName();
			 		
			 		VirtualAppliance virtualapp 	=	cloudService.findVirtualAppliance(VirtualAppliancePredicates.name(selectedVA));
			 			
			 		VirtualDatacenter virtualDC = virtualapp.getVirtualDatacenter();
			 		
			 		Datacenter datacenter = virtualDC.getDatacenter();
			 		HypervisorType hypervisor = virtualDC.getHypervisorType();
			 		
			 		
			 		Enterprise enterprise = adminService.getCurrentUserInfo().getEnterprise();
			 		
			 		 PrivateNetwork network =  PrivateNetwork.builder(context)
			 				 					.name("10.80.0.0") 
			 				 					.gateway("10.80.0.1") 
			 				 					.address("10.80.0.0") 
			 				 					.mask(22)               
			 				 					.build();

			 		String user = getUsername();
			 		
					String vdcname = vdcNameGen(user);
					if ( datacenter != null && enterprise != null){
					vdc_toDeploy = VirtualDatacenter.builder(context, datacenter, enterprise).name(vdcname)
							 .cpuCountLimits(0,0 )               
						        .hdLimitsInMb(0,0)  
						        .publicIpsLimits(0,0)               
						        .ramLimits(0,0)                
						        .storageLimits(0,0)  
						        .vlansLimits(0,0)                   
						        .hypervisorType(hypervisor)         
						        .network(network)                    
						        .build();
					 vdc_toDeploy.save();
					}
					 
					 String vappliance_name = virtualapp.getName();
					
					 virtualapp_todeploy = VirtualAppliance.builder(context,vdc_toDeploy)
							 											.name(vappliance_name)
							 									     	.build();
					 virtualapp_todeploy.save();
					
					 List<VirtualMachine> vmlist_todeploy = virtualapp.listVirtualMachines();
					
					 Iterator vmlist_todeploy_it = vmlist_todeploy.iterator();
						 while (vmlist_todeploy_it.hasNext())
						 {
							 VirtualMachine aVM = (VirtualMachine) vmlist_todeploy_it.next();
							 
							 VirtualMachineTemplate vm_template_todeploy = aVM.getTemplate();
							 String vmName = aVM.getName();
							 int cpu = aVM.getCpu();
							 int ram = aVM.getRam();
							 Long hd = aVM.getHdInBytes();
							
							  vm_todeploy = VirtualMachine.builder(context, virtualapp_todeploy, vm_template_todeploy)
								        .name(vmName) 
								        .cpu(cpu)                 
								        .ram(ram)              
								        .build();
							 vm_todeploy.save();
							 List<HardDisk> hdisks = aVM.listAttachedHardDisks();
							 Iterator<HardDisk> hdisks_it = hdisks.iterator();
							 while (hdisks_it.hasNext())
							 {
								 HardDisk hdisk =  hdisks_it.next();
								 System.out.println(hdisk);
								 vm_todeploy.attachHardDisks(hdisk);
							 }
							
						 }
						 VmEventHandler handler = new VmEventHandler(context, vm_todeploy);
							
				          
			             VirtualMachineMonitor monitor =  context.getMonitoringService().getVirtualMachineMonitor();
			             monitor.register(handler);
		           
		 	            
						virtualapp_todeploy.deploy();
						
						monitor.monitorDeploy(vm_todeploy);
						render();
			 			
			 		
		 }
		 catch (Exception ae) 
			{
			 /*if (vdc_toDeploy != null)
			 {   vdc_toDeploy.delete();			 }
			    if (virtualapp_todeploy != null)
			    {   virtualapp_todeploy.delete();}
			   if (vm_todeploy != null)
			   {   vm_todeploy.delete();   }*/
			   
				flash.error("Sorry! There are some deployment issues with server");
				render();
			 
			 }
			
		 finally
				 {
			 		
					 context.close();
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
