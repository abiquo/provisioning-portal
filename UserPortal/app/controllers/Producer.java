package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.persistence.Query;

import models.Nodes;
import models.Nodes_Resources;
//import models.User;
import models.sc_offer;
import models.sc_offers_subscriptions;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.AbiquoContextFactory;
import org.jclouds.abiquo.domain.cloud.HardDisk;
import org.jclouds.abiquo.domain.cloud.VirtualAppliance;
import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.cloud.VirtualMachineTemplate;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.domain.network.PrivateNetwork;
import org.jclouds.abiquo.features.services.AdministrationService;
import org.jclouds.abiquo.features.services.CloudService;
import org.jclouds.rest.AuthorizationException;

import com.abiquo.model.enumerator.HypervisorType;

import play.Logger;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.JPA;
import play.db.jpa.JPABase;
import play.libs.MimeTypes;
import play.mvc.Controller;
import portal.util.Context;


/**
 * Enables Producer(Admin) to add entries to service catalog.
 *
 * @author Harpreet Kaur
 *
 */
public class Producer extends Controller{

		
	
	public static void subscribedOffers(){
			Logger.info(" -----INSIDE PRODUCER SUBSCRIBEDOFFERS()------");
			String user =session.get("username");
			Query query = JPA.em().createNamedQuery("getSubscribedOffersGroupByServiceLevels");
			List<sc_offers_subscriptions> resultSet = query.getResultList();
			Logger.info(" -----EXITING PRODUCER SUBSCRIBEDOFFERS()------");
			render(resultSet,user);
		
	}
	
	public static void displayOffer(String service_level){
		Logger.info(" -----INSIDE PRODUCER DISPLAYOFFER()------");
		 String user =session.get("username");
		 Query query = JPA.em().createNamedQuery("getSubscribedOffersGroupByServiceLevels");
		  List<sc_offers_subscriptions> resultSet = query.getResultList();
		  
		  Query query1 = JPA.em().createNamedQuery("getSubscribedOffers");
		  query1.setParameter(1, service_level);
		  List<sc_offers_subscriptions> resultSet1 = query1.getResultList();
		  Logger.info(" -----INSIDE PRODUCER DISPLAYOFFER()------");
		  render("/Producer/subscribedOffers.html",resultSet,resultSet1,user );
	}
	public static void disableOffer(Long scOfferId)
	{
		Logger.info(" -----INSIDE PRODUCER DISABLEOFFER()------");
		String user =session.get("username");
		Logger.info(" Offer Id to delete : " + scOfferId );
		sc_offers_subscriptions offerSub = JPA.em().find(sc_offers_subscriptions.class, scOfferId);
		offerSub.delete();
		Logger.info(" -----EXITING PRODUCER DISABLEOFFER()------");
		render("/Producer/saveConfigure.html",user);
		
	}

public static void listVDC(){
		Logger.info("-----INSIDE PRODUCER LISTVDC()------ ");
	Logger.info(" -----INSIDE PRODUCER LISTVDC()------");
		String user =session.get("username");
		String password =session.get("password");
		Logger.info("Session user in listVDC(): "+ user);
		AbiquoContext context = Context.getContext(user,password);
		
		try{
			String enterprise = context.getAdministrationService().getCurrentUserInfo().getEnterprise().toString();
			Logger.info("enterprise for cloud admim is :" + enterprise);
					
			CloudService cloudService = context.getCloudService();
			Iterable<VirtualDatacenter> vdc_list = cloudService.listVirtualDatacenters();
						
			if (vdc_list != null)
			{
							
				render(vdc_list,user);
			}
			
			else
			{
				String msg = "";
				render(msg);
			}
		
			Logger.info(" -----EXITING PRODUCER LISTVDC()------");
		}
		catch (AuthorizationException e )
			{
				flash.error("Wrong credentials or Un-authorized user !");
				Login.login_page();
			}
		finally
			{ 
			context.close();
				context= null;
				
			}
		

}

	public static void listVA(Integer id_vdc)
	{
		Logger.info(" -----INSIDE PRODUCER LISTVA()------");
		String user =session.get("username");
		String password =session.get("password");
		Logger.info("Session user in listVA(): "+ user);
		AbiquoContext context = Context.getContext(user,password);
		
		try {
			if (id_vdc != null )
			{
				Iterable<VirtualDatacenter> vdc_list = context.getCloudService().listVirtualDatacenters();
				VirtualDatacenter virtualDatacenter = context.getCloudService().getVirtualDatacenter(id_vdc);
				List<VirtualAppliance> vaList = virtualDatacenter.listVirtualAppliances();
				
					Iterator<VirtualAppliance> vaList_it = vaList.iterator();
					VirtualAppliance virtualAppliance = null;
					List<VirtualAppliance> vaNotInDB = new LinkedList<VirtualAppliance>();
					List<VirtualAppliance> vaWithVm = new LinkedList<VirtualAppliance>();
					while(vaList_it.hasNext())
					{
						virtualAppliance = vaList_it.next();
						Integer va_id = virtualAppliance.getId();
						 Query query = JPA.em().createNamedQuery("getOfferDetails");
						 query.setParameter(1,va_id);
						 List<sc_offer> scOffer = query.getResultList();
						 if ( scOffer.size() == 0)
						 {
							 //vaNotInDB.add(virtualAppliance);
						 
						List<VirtualMachine> vmList = virtualAppliance.listVirtualMachines();
					
						if (vmList.size()>0 )
						{
							vaWithVm.add(virtualAppliance);
							
						}
						 }
					}
					
					Iterator<VirtualAppliance> i = vaWithVm.iterator();
					while(i.hasNext())
					{
						VirtualAppliance v = i.next();
						Logger.info(" 4. va with vms " + v );
					}
					Logger.info(" -----EXITING PRODUCER LISTVA()------");
					render("/Producer/listVDC.html",vaWithVm,virtualDatacenter,vdc_list, user);
			}
		} 
		finally{
			context.close();
			context = null;
		}

}
	public static void listVM( Integer id_vdc_param, Integer id_va_param)
	{
		Logger.info(" -----INSIDE PRODUCER LISTVM()------");
		String user =session.get("username");
		String password =session.get("password");
		
		AbiquoContext context = Context.getContext(user,password);
		try {
			VirtualDatacenter virtualDatacenter = context.getCloudService().getVirtualDatacenter(id_vdc_param);
			VirtualAppliance virtualAppliance  = virtualDatacenter.getVirtualAppliance(id_va_param);
		
			if (virtualAppliance != null )
			{
				List<VirtualMachine> vmList = virtualAppliance.listVirtualMachines();
		
				render(vmList,virtualAppliance, virtualDatacenter,user);
				
			}
		
		else {
				String msg = " No Virtual Machine to display!!";
				render(msg,user);
		}
			Logger.info(" -----EXITING PRODUCER LISTVM()------");
		} 
		
		finally{
			context.close();
			context = null;
			
		}

}
	public static void vmDetails(Integer vdc, Integer va, Integer vm)
	{
		Logger.info(" -----INSIDE PRODUCER VMDETAILS()------");
		String user =session.get("username");
		String password =session.get("password");
		Logger.info("Session user in vmDetails(): "+ user);
		AbiquoContext context = Context.getContext(user,password);
		try {
			VirtualMachine virtualMachine = context.getCloudService().getVirtualDatacenter(vdc).getVirtualAppliance(va).getVirtualMachine(vm);
		
			Integer cpu = virtualMachine.getCpu();
			Integer ram = virtualMachine.getRam();
			long hd = virtualMachine.getHdInBytes();
			List<HardDisk> harddisk =virtualMachine.listAttachedHardDisks();
			
			VirtualMachineTemplate template = virtualMachine.getTemplate();
			String template_name = template.getName();
			String template_path = template.getPath();
			render(vdc,va,vm, cpu,ram,hd,harddisk, template_name,template_path,user);
			Logger.info(" -----EXITING PRODUCER VMDETAILS()------");
		//render("/Services/listVDC.html",vdc,va,vm, cpu,ram,hd,harddisk, template_name,template_path);
		} 
		finally{
			context.close();
			context = null;
			
		}

}
	public static void configure (Integer vdc_id_param , Integer id_va_param)
	{
		Logger.info(" -----INSIDE PRODUCER CONFIGURE()------");
		String user =session.get("username");
		String password =session.get("password");
		
		AbiquoContext context = Context.getContext(user,password);
		CloudService cloudService = context.getCloudService();
		VirtualDatacenter virtualDC = cloudService.getVirtualDatacenter(vdc_id_param);
 		VirtualAppliance va = virtualDC.getVirtualAppliance(id_va_param);
 		List<VirtualMachine> vmList = va.listVirtualMachines();
 		Logger.info(" -----EXITING PRODUCER CONFIGURE()------");
		render(va, virtualDC,vmList, user);
	}
	
	public static void configureExistingOffer (Integer sc_offer_id)
	{
		
		Logger.info("------ INSIDE CONFIGURE EXISTING OFFER -------");
		String user =session.get("username");
		Logger.info(" Id to configure : " + sc_offer_id );
		 Query query = JPA.em().createNamedQuery("getOfferDetails");
		 query.setParameter(1,sc_offer_id);
		 List<sc_offer> scOffer = query.getResultList();
		 Query query1 =JPA.em().createNativeQuery("select * from sc_offers_subscriptions where sc_offer_sc_offer_id = ?1",sc_offers_subscriptions.class);
		 query1.setParameter(1,sc_offer_id);
		 List<sc_offers_subscriptions> subscribedOffers = query1.getResultList();
		 Iterator<sc_offers_subscriptions> subscribedOffers_it = subscribedOffers.iterator();
		 Date expireDate = null;
		 Date startDate = null;
		 while (subscribedOffers_it.hasNext())
		 {
			 sc_offers_subscriptions subscribedOffer = subscribedOffers_it.next();
			  expireDate = subscribedOffer.getExpiration_date();
			  startDate = subscribedOffer.getStart_date();
		 }
		 //String expire_date = expireDate.toString();
		 String start_date = startDate.toString();
			
		Logger.info("------ EXITING CONFIGURE EXISTING OFFER -------");
		 render(scOffer,user,expireDate, start_date,startDate );
	}

	public static void saveConfigure (sc_offer sc_offers , File icon, File image ) throws FileNotFoundException 
	{
		String user =session.get("username");
		Logger.info("-----------INSIDE SAVECONFIGURE()------------");
		Logger.info("------ saveConfigure() id------- " + sc_offers.getSc_offer_id());
		Logger.info("------ saveConfigure() Offer name ------- " + sc_offers.getSc_offer_name());
		Logger.info("------ saveConfigure() short description------- " + sc_offers.getShort_description());
		Logger.info("------ saveConfigure() description------- " + sc_offers.getDescription());
		
		Logger.info("------ saveConfigure() icon ----" + icon);
		Logger.info("------ saveConfigure() image ----" + image );
		sc_offer scOffer= sc_offer.findById(sc_offers.getSc_offer_id());
		
		scOffer.setSc_offer_id(sc_offers.getSc_offer_id());
		scOffer.setSc_offer_name(sc_offers.getSc_offer_name());
		if (icon != null)
		{
			scOffer.setIcon_name(sc_offers.getIcon_name());
			scOffer.setIcon_name(icon.getName());
			scOffer.setIcon(new Blob());
			scOffer.getIcon().set(new FileInputStream(icon), MimeTypes.getContentType(icon.getName()));
		}
		if (image  != null)
		{
			scOffer.setImage(new Blob());
			scOffer.getImage().set(new FileInputStream(image), MimeTypes.getContentType(image.getName()));
		}
		scOffer.setShort_description(sc_offers.getShort_description());
		scOffer.setDescription(sc_offers.getDescription());
		scOffer.save();
		Logger.info("-----------EXITING SAVECONFIGURE()------------");
		render(user);
	}
	
	
	public static void addToServiceCatalog(Integer vdc_id_param , Integer id_va_param, sc_offer sc_offers , @Required File icon, File image)
	{
		if(validation.hasErrors()) {
	        flash.error("Icon is required to enable an Offer ");
	        configure(vdc_id_param,id_va_param);
	    }
		else 
		{
			Logger.info(" -----INSIDE PRODUCER ADDTOSERVICECATALOG()------");
		String user =session.get("username");
		String password =session.get("password");
		Logger.info("Session user in addToServiceCatalog(): "+ user);
		AbiquoContext context = Context.getContext(user,password);
		 try{
			 		
			 	    CloudService cloudService = context.getCloudService();
			 		VirtualDatacenter virtualDC = cloudService.getVirtualDatacenter(vdc_id_param);
			 		VirtualAppliance va = virtualDC.getVirtualAppliance(id_va_param);
			 		
			 		Integer id_datacenter = virtualDC.getDatacenter().getId();
			 		HypervisorType hypervisor = virtualDC.getHypervisorType();
			 		
			 		PrivateNetwork network =  PrivateNetwork.builder(context)
			 				 					.name("10.80.0.0") 
			 				 					.gateway("10.80.0.1") 
			 				 					.address("10.80.0.0") 
			 				 					.mask(22)               
			 				 					.build();
					 
					 sc_offer scOffer = new sc_offer();
					 scOffer.setSc_offer_id(va.getId());
					 scOffer.setSc_offer_name(va.getName());
					 if (icon != null)
						{
							scOffer.setIcon_name(sc_offers.getIcon_name());
							scOffer.setIcon_name(icon.getName());
							scOffer.setIcon(new Blob());
							scOffer.getIcon().set(new FileInputStream(icon), MimeTypes.getContentType(icon.getName()));
						}
						if (image  != null)
						{
							scOffer.setImage(new Blob());
							scOffer.getImage().set(new FileInputStream(image), MimeTypes.getContentType(image.getName()));
						}
					// scOffer.setIcon(null);
					 scOffer.setShort_description(sc_offers.getShort_description());
					// scOffer.setDescription("not available");
					 scOffer.setDatacenter(id_datacenter);
					 scOffer.setHypervisorType(hypervisor.toString());
					 scOffer.setDefault_network_type(network.getAddress());
					 scOffer.setId_VirtualDatacenter_ref(va.getVirtualDatacenter().getId());
					 scOffer.setService_type("AUTO_EXPIRE");
				   	 scOffer.setVirtualDatacenter_name(virtualDC.getName());
					
					 List<VirtualMachine> vmlist_todeploy = va.listVirtualMachines();
					 Set<Nodes> node_set = new HashSet<Nodes>();
					 Nodes node = null;
					 Iterator vmlist_todeploy_it = vmlist_todeploy.iterator();
						 while (vmlist_todeploy_it.hasNext())
						 {
							 VirtualMachine aVM = (VirtualMachine) vmlist_todeploy_it.next();
							 
							 VirtualMachineTemplate vm_template_todeploy = aVM.getTemplate();
							 //String template_path = vm_template_todeploy.getPath();
							// String vmName = aVM.getName();
							 int cpu = aVM.getCpu();
							 int ram = aVM.getRam();
							// Long hd = aVM.getHdInBytes();
							 String description =   aVM.getDescription();
							
							 List<HardDisk> attached_disks = aVM.listAttachedHardDisks();
									
							 node= new Nodes();
							 node.setId_node(aVM.getId());
							 node.setCpu(cpu);
							 node.setRam(ram);
							 node.setIdImage(vm_template_todeploy.getId());
							 node.setIcon("icon");
							 node.setDescription(description);
							 node.setNode_name(vm_template_todeploy.getName());
							 Logger.info(" description : " + sc_offers.getDescription());
							 scOffer.setDescription(sc_offers.getDescription());
							 node_set.add(node);
							 Set<Nodes_Resources> node_resource_set = new HashSet<Nodes_Resources>();
							
							 Iterator<HardDisk> attacheddisks_it = attached_disks.iterator();
						    	while (attacheddisks_it.hasNext())
								 {
									 HardDisk hdisk =  attacheddisks_it.next();
									 Long size = hdisk.getSizeInMb();
									 Integer sequence =  hdisk.getSequence();
									 Logger.info(" hard disk sequence :" + sequence );
									 Nodes_Resources node_resource = new Nodes_Resources();
									 node_resource.setSequence(sequence);
									 node_resource.setResourceType(17);
									 node_resource.setValue(size);
									 node_resource.save();
									 node_resource_set.add(node_resource);
									
								 }
								 node.setResources(node_resource_set);
								
												 
						 }
						
						 scOffer.setNodes(node_set);
						 sc_offers_subscriptions offerSub = new sc_offers_subscriptions();
						 offerSub.setSc_offer(scOffer);
						 offerSub.setStart_date(new Date());
						 offerSub.setService_level(virtualDC.getName());
						 offerSub.save();
						 Logger.info("-----------EXITING ADDTOSERVICECATALOG()------------");
			 		render(user);
		 }
		 catch (Exception e) 
			{
			 	e.printStackTrace();
			 	flash.error("Entry already exists");
			 	render();
			 }
			
		 finally
				 {
			 		
					 context.close();
				 }
		 
	}
	}

}
