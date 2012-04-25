package portal.util;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.VirtualAppliance;
import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.enterprise.User;
import org.jclouds.abiquo.features.services.AdministrationService;
import org.jclouds.abiquo.features.services.CloudService;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Scope.Session;


/**
 * 
 * @author Harpreet Kaur
 * This class includes methods that return Abiquo cloud and admin service , resources like enterprise, vdc, vapp and virtual machine 
 */
public class AbiquoUtils {
	
	private static AbiquoContext context;
	
	public static void  setAbiquoUtilsContext ( AbiquoContext contextt)
	{
		
		context=contextt;
		Logger.info(" AbiquoUtils context set to :" + context );
	}

	
	public static CloudService getCloud()
	{
		System.out.println(" AbiquoUtils getCloud() context: " + context);
		CloudService cloudService = null;
		if (context != null)
		{
			    cloudService = context.getCloudService();
				return cloudService;
		}
		else 
		{
				Logger.warn(" Unable to create context in AbiquoUtils.");
				return null;
		}
		
	}
	
	public static AdministrationService getAdmin( )
	{
		System.out.println(" AbiquoUtils getAdmin() context: " + context);
		AdministrationService adminService = null;
		if (context != null)
		{
			adminService = context.getAdministrationService();
			
		}
		else 
		{
			Logger.warn(" Unable to create context in AbiquoUtils.");
			
		}
		return adminService;
	}
	
	public static Iterable<Enterprise> getAllEnterprises()
	{
		System.out.println(" AbiquoUtils getAllEnterprises() context: " + context);
		AdministrationService adminService = getAdmin();
		Iterable<Enterprise> enterpriseList = null;
		if ( adminService != null)
		{
				enterpriseList = adminService.listEnterprises();
			
		}
		else 
		{
				Logger.warn(" Unable to get AdminService in AbiquoUtils.");
			
		}
		return enterpriseList;
	}
	
	
	public static Enterprise getEnterprise(Integer id )
	{
		System.out.println(" AbiquoUtils getAllEnterprises() context: " + context);
		AdministrationService adminService = getAdmin();
		Enterprise enterprise = null;
		if ( adminService != null)
		{
				enterprise = adminService.getEnterprise(id);
			
		}
		else 
		{
				Logger.warn(" Unable to get AdminService in AbiquoUtils.");
			
		}
		return enterprise;
	}
	
	public static VirtualDatacenter getVDCDetails( Integer virtualDatacenterId )
	{
		VirtualDatacenter vdc = null;
		if ( virtualDatacenterId != null)
		{
				System.out.println(" AbiquoUtils getVDCDetails() Context: " + context);
				
				if (context != null)
				{
						CloudService cloudService = getCloud();
						if ( cloudService != null)
							vdc = cloudService.getVirtualDatacenter(virtualDatacenterId);
				}	
		}
		return vdc;
	}
	
	public static VirtualAppliance getVADetails( Integer virtualDatacenterId, Integer va_id )
	{
		VirtualDatacenter vdc = null;
		VirtualAppliance va = null;
		if ( virtualDatacenterId != null && va_id != null )
		{
				vdc = getVDCDetails(virtualDatacenterId);
				if ( vdc != null )
				va = vdc.getVirtualAppliance(va_id);
		}
		return va;
	}
	
	public static VirtualMachine getVMDetails( Integer virtualDatacenterId , Integer va_id , Integer vm_id )
	{
		VirtualAppliance va = null;
		VirtualMachine vm = null;
		if ( virtualDatacenterId != null && va_id != null &&  vm_id != null)
		{
				va =  getVADetails(virtualDatacenterId , va_id);
				if ( va!= null )
					vm = va.getVirtualMachine(vm_id);
		}
		return vm;
	}
	
	public static Iterable<VirtualDatacenter> getAllVDC()
	{
		Iterable<VirtualDatacenter> vdc_list = null;
		CloudService cloudService = getCloud();
		if ( cloudService != null )
		{
			    vdc_list = cloudService.listVirtualDatacenters();
				System.out.println("vdc_list size: " + vdc_list);
		}
		return vdc_list;
	}
	
	public static Iterable<VirtualAppliance> getAllVA()
	{
		Iterable<VirtualAppliance> va_list = null;
		//System.out.println(" AbiquoUtils getAllVA() Context: " + context);
		CloudService cloudService = getCloud();
		if ( cloudService != null )
		{
				va_list = cloudService.listVirtualAppliances();
		}
		return va_list;
	}	
	
	public static Enterprise getCurrentUserEnterprise()
	{
		Enterprise enterprise = null;
		AdministrationService adminService = getAdmin();
		if ( adminService != null)
		{
				User user  = adminService.getCurrentUserInfo();
				if ( user != null )
				{
					enterprise = user.getEnterprise();
				}
			
		}
		return enterprise;
	}
}
