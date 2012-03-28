package controllers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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
import org.jclouds.abiquo.monitor.VirtualMachineMonitor;
import org.jclouds.abiquo.domain.enterprise.User;
import com.abiquo.model.enumerator.HypervisorType;

import models.Deploy_Bundle;
import models.Deploy_Bundle_Nodes;
import models.Deploy_Nodes_Resources;
import models.MKT_Configuration;
import models.Nodes;
import models.Nodes_Resources;

import models.User_Consumption;
import models.sc_offer;
import monitor.VmEventHandler;
import play.Logger;
import play.cache.Cache;
import play.db.jpa.GenericModel.JPAQuery;
import play.db.jpa.JPA;
import play.mvc.Controller;
import portal.util.Context;

/**
 * @author Harpreet Kaur
 *
 */
public class Consumer extends Controller{

	public static void ServiceCatalog()
	{
		Logger.info("---------INSIDE CONSUMER SERVICECATALOG()------------");
		String user = session.get("username");
		//String user = (String) Cache.get(session.getId());
		Logger.info("CURRENT USER EMAIL ID: "+ user);
		
		Query query = JPA.em().createNamedQuery("groupByVDC");
        List<sc_offer> result1 = query.getResultList();
		Logger.info("------------EXITING CONSUMER SERVICECATALOG()--------------");
		
		render(result1,user);
	}
	public static void availableOffers( String vdc_name_param)
	{
		
		Logger.info("---------INSIDE CONSUMER AVAILABLEOFFERS()---------------");
	    String user = session.get("email");
		//String user = (String) Cache.get(session.getId());
		Logger.info("CURRENT USER EMAIL ID: "+ user);
	  	
	    Query query1 = JPA.em().createNamedQuery("groupByVDC");
	    List<sc_offer> result1 = query1.getResultList();
	        
	    Query query2 = JPA.em().createNamedQuery("getVappListForVDC");
		query2.setParameter(1,vdc_name_param);
		List<sc_offer> result2= query2.getResultList();
		
		Logger.info("------------EXITING CONSUMER AVAILABLEOFFERS()--------------");
		render("/Consumer/ServiceCatalog.html", result2,result1,user ); 

		
	}
	
	
		
	
	public static void offerDetails(Integer offer_id)
	{
		Logger.info("---------INSIDE CONSUMER OFFERDETAILS()---------------");
		//String user = (String) Cache.get(session.getId());
		String user = session.get("username");
		Logger.info("CURRENT USER EMAIL ID: "+ user);
		sc_offer offer = null;
		Set<Nodes> nodes_list = null;
		Set<Nodes_Resources> nodes_resources = null;
		String query = "select p from sc_offer as p where p.sc_offer_id = ?1";
		JPAQuery result = sc_offer.find(query, offer_id);

		List<sc_offer> offers  = result.fetch();
		Iterator<sc_offer> offers_it = offers.iterator();
		while(offers_it.hasNext())
		{
			offer = offers_it.next();
			 nodes_list = offer.getNodes();

				Iterator<Nodes> nodes_list_it = nodes_list.iterator();
				while(nodes_list_it.hasNext())
				{
					Nodes node = nodes_list_it.next();
					nodes_resources = node.getResources();
					
				}
		
			
		}
		Logger.info("------------EXITING CONSUMER OFFERDETAILS()--------------");
		render( offers, nodes_list , nodes_resources,user);
	}

	public static void purchaseConfirmation(Integer offer_id)
	{
		Logger.info("---------INSIDE CONSUMER PURCHASECONFIMATION()---------------");
		String user = session.get("email");
		//String user = (String) Cache.get(session.getId());
		Logger.info("CURRENT USER EMAIL ID: "+ user);
		sc_offer offer = null;
		Set<Nodes> nodes_list = null;
		Set<Nodes_Resources> nodes_resources = null;
		String query = "select p from sc_offer as p where p.sc_offer_id = ?1";
		JPAQuery result = sc_offer.find(query, offer_id);

		List<sc_offer> offers  = result.fetch();
		Iterator<sc_offer> offers_it = offers.iterator();
		while(offers_it.hasNext())
		{
			offer = offers_it.next();
			 nodes_list = offer.getNodes();

				Iterator<Nodes> nodes_list_it = nodes_list.iterator();
				while(nodes_list_it.hasNext())
				{
					Nodes node = nodes_list_it.next();
					nodes_resources = node.getResources();
					
				}
		
			
		}
		Logger.info("------------EXITING CONSUMER PURCHASECONFIRMATION()--------------");
		render( offers, nodes_list , nodes_resources,user);
	}
	
	
	public static void Deploy( Integer id_datacenter, Integer vdc_id_param , Integer sc_offer_id, String va_param)
	{
		Logger.info("---------INSIDE CONSUMER DEPLOY()---------------");
		Logger.info(" DEPLOY( INTEGER ID_DATACENTER:: " + id_datacenter + ", INTEGER VDC_ID_PARAM :: "+ vdc_id_param + ", INTEGER SC_OFFER_ID :: "+ sc_offer_id +" , String va_param:: " +va_param + ")");
		String deploy_username=null;
		String deploy_password =null;
		String deploy_enterprise = null;
		String query1 = "select p from MKT_Configuration as p ";
		JPAQuery result1 = sc_offer.find(query1);
		List<MKT_Configuration> mkt_conf = result1.fetch();
		Iterator<MKT_Configuration> mkt_conf_it = mkt_conf.iterator();
		while (mkt_conf_it.hasNext())
		{
			MKT_Configuration mkt = mkt_conf_it.next();
			deploy_username= mkt.getMkt_deploy_user();
			deploy_password = mkt.getMkt_deploy_pw();
			deploy_enterprise = mkt.getMkt_deploy_enterprise();
		}
		Logger.info(" DEPLOY ENTERPRISE  + USERNAME + PASSWORD :" + deploy_enterprise +"  " + deploy_username +"  " + deploy_password );
		/*Properties props = new Properties();
		props.put("abiquo.endpoint", "http://67.111.53.253/api");
		
		AbiquoContext context = new AbiquoContextFactory().createContext("demo-deploy" ,"demo" ,props);*/
		AbiquoContext context = Context.getContext(deploy_username,deploy_password);
		VirtualDatacenter vdc_toDeploy = null;
		VirtualAppliance virtualapp_todeploy= null;
		VirtualMachine vm_todeploy=null;
		 
		
		 try{
				    Logger.info("STARTING DEPLOYMENT..");
				    System.out.println(" context :" + context );
				    
				    CloudService cloudService = context.getCloudService();
				    System.out.println("Cloud Service :" + cloudService );
				    Logger.info("Cloud Service :" + cloudService );
			 		
				    AdministrationService adminService = context.getAdministrationService();
			 		System.out.println("Admin Service :" + adminService);
			 		 Logger.info("Admin Service :" + adminService );
			 		
			 		User currentUser = adminService.getCurrentUserInfo();
			 		Logger.info("CURRENT USER i.e. DEPLOY USER :" + currentUser );
			 		Enterprise enterprise= currentUser.getEnterprise();
			 		Logger.info("DEPLOY ENTERPRRISE :" + enterprise );
			 		
			 		
			 		String useremail = session.get("email");
			 		Logger.info("CURRENT USER EMAIL ID: "+ useremail);
			 		
			 		//String user = Helper.getUser().getName();
			 		String vdc_user = session.get("username");
					String vdcname = Helper.vdcNameGen(vdc_user);
			 		
					Logger.info(" vdcname : " + vdcname);	
					
			 		VirtualDatacenter virtualDC = cloudService.getVirtualDatacenter(vdc_id_param);
			 		System.out.println(" VDC to deploy: " + virtualDC);
			 		Logger.info(" VDC to deploy: " , virtualDC);
			 		
			 		HypervisorType hypervisor = virtualDC.getHypervisorType();
			 		System.out.println(" Hypervisor to deploy: " + hypervisor);
			 		Logger.info(" Hypervisor to deploy: " , hypervisor);
			 		
			 		Datacenter datacenter = virtualDC.getDatacenter();
			 		System.out.println(" Datacenter to deploy: " + datacenter);
			 		Logger.info(" Datacenter to deploy: " , datacenter);
			 		
			 		PrivateNetwork network =  PrivateNetwork.builder(context)
			 				 					.name("10.80.0.0") 
			 				 					.gateway("10.80.0.1") 
			 				 					.address("10.80.0.0") 
			 				 					.mask(22)               
			 				 					.build();	
			 		System.out.println(" Network Built");
					
			 		vdc_toDeploy = VirtualDatacenter.builder(context, datacenter, enterprise)
								.name(vdcname)
								.cpuCountLimits(0,0)               
						        .hdLimitsInMb(0,0)  
						        .publicIpsLimits(0,0)               
						        .ramLimits(0,0)                
						        .storageLimits(0,0)  
						        .vlansLimits(0,0)                   
						        .hypervisorType(hypervisor)         
						        .network(network)                    
						        .build();
					System.out.println(" VDC built");
					Logger.info("VDC built  " );
					 vdc_toDeploy.save();
					Logger.info(" 1. VDC CREATED ");
					 virtualapp_todeploy = VirtualAppliance.builder(context,vdc_toDeploy)
							 											.name(va_param)
							 									     	.build();
					 virtualapp_todeploy.save();
								
					 Logger.info(" 2. VAPP CREATED ");
				   		  User_Consumption user_consumption = new User_Consumption();
								user_consumption.setUserid(useremail);
									Date current = new Date();
									Calendar cal = Calendar.getInstance();
									cal.add(Calendar.DATE, 60);
								user_consumption.setPurchase_date(current);
								user_consumption.setExpiration_date(cal.getTime());
								user_consumption.setVdc_name(vdc_toDeploy.getName());
								user_consumption.setDestroy_date(null);
								user_consumption.setSc_offer_id_ref(sc_offer_id);
								user_consumption.setVdc_id(vdc_toDeploy.getId());
							Set<Deploy_Bundle> deploy_bundle_set = new HashSet<Deploy_Bundle>();		
							Deploy_Bundle deploy_Bundle = new Deploy_Bundle();
									deploy_Bundle.setDeploy_datacenter(datacenter.getId());
									deploy_Bundle.setDeploy_hypervisorType(hypervisor.toString());
									deploy_Bundle.setDeploy_network("");
									deploy_Bundle.setVapp_name(virtualapp_todeploy.getName());
									deploy_Bundle.setVdc_name(vdc_toDeploy.getId());
									deploy_Bundle.setUserConsumption(user_consumption);
									deploy_Bundle.setVapp_id(virtualapp_todeploy.getId());
									deploy_bundle_set.add(deploy_Bundle);
					
					String query = "select p from sc_offer as p where p.sc_offer_id = ?1";
					JPAQuery result = sc_offer.find(query, sc_offer_id);
					List<sc_offer> nodes  = result.fetch();
					Iterator<sc_offer> nodes_it = nodes.iterator();
							
								
					while(nodes_it.hasNext())
					{
						sc_offer node = nodes_it.next();
						Set<Nodes> vmlist_todeploy = node.getNodes();
						Iterator<Nodes> vmlist_todeploy_it = vmlist_todeploy.iterator();
						Set<Deploy_Bundle_Nodes> deploy_Bundle_Nodes_list = new HashSet<Deploy_Bundle_Nodes>();
						 while (vmlist_todeploy_it.hasNext())
						 {
							 Nodes aVM =  vmlist_todeploy_it.next();
							 String vmName = aVM.getNode_name();
							 VirtualMachineTemplate vm_template_todeploy = virtualDC.getAvailableTemplate(aVM.getIdImage());
							 int cpu = aVM.getCpu();
							 int ram = aVM.getRam();
							 //String description =   aVM.getDescription();
							 
							  vm_todeploy = VirtualMachine.builder(context, virtualapp_todeploy, vm_template_todeploy)
								        .name(vmName) 
								        .cpu(cpu)                 
								        .ram(ram)
								        .password("vmpassword")
								        .build();
							 vm_todeploy.save();
							 Logger.info(" 3. VM CREATED");
							 			Deploy_Bundle_Nodes deploy_Bundle_Nodes = new Deploy_Bundle_Nodes();
							 			deploy_Bundle_Nodes.setCpu(cpu);
							 			deploy_Bundle_Nodes.setNode_name(vmName);
							 			deploy_Bundle_Nodes.setNode_name(vm_todeploy.getName());
							 			deploy_Bundle_Nodes.setNode_id(vm_todeploy.getId());
							 			deploy_Bundle_Nodes.setRam(ram);
							 			deploy_Bundle_Nodes.setVdrp_password("");
							 			deploy_Bundle_Nodes.setVdrpIP("");
							 			deploy_Bundle_Nodes.setVdrpPort(0);
							 			deploy_Bundle_Nodes_list.add(deploy_Bundle_Nodes);
							 			//deploy_Bundle_Nodes.setResources(resources);
							Set<Nodes_Resources> resources =  aVM.getResources();
							Iterator<Nodes_Resources> resources_it = resources.iterator();
							List<HardDisk> hardDisk_toattach = new ArrayList<HardDisk>();
							Nodes_Resources resource=null;
							 Set<Deploy_Nodes_Resources> deploy_Nodes_Resources_list = new HashSet<Deploy_Nodes_Resources>();
							while(resources_it.hasNext())
							{
								resource = resources_it.next();
								Long size = resource.getValue();
								HardDisk disk = HardDisk.builder(context, vdc_toDeploy).sizeInMb(size).build();
								disk.save();
								hardDisk_toattach.add(disk);
										Deploy_Nodes_Resources deploy_Nodes_Resources = new Deploy_Nodes_Resources();
										deploy_Nodes_Resources.setResourceType(resource.getResourceType());
										deploy_Nodes_Resources.setResourceType(resource.getSequence());
										deploy_Nodes_Resources.setValue(resource.getValue());
										deploy_Nodes_Resources_list.add(deploy_Nodes_Resources);
							}
										deploy_Bundle_Nodes.setResources(deploy_Nodes_Resources_list);
							HardDisk[] disks = new HardDisk[hardDisk_toattach.size()];
							for (int i = 0; i<hardDisk_toattach.size() ; i++)
							{
								disks[i] = hardDisk_toattach.get(i);
							}
								vm_todeploy.attachHardDisks(disks);
								Logger.info(" 4. HARDDISKS ATTACHED ");
								VmEventHandler handler = new VmEventHandler(context, vm_todeploy);	
								VirtualMachineMonitor monitor =  context.getMonitoringService().getVirtualMachineMonitor();
								monitor.register(handler);
								vm_todeploy.deploy();
								Logger.info("STARTING MONITORING ......");
								monitor.monitorDeploy(vm_todeploy);
								
								
								
							
						 }
						Logger.info("SAVING DEPLOY INFORMATION ......"); 
					deploy_Bundle.setNodes(deploy_Bundle_Nodes_list);
					
					user_consumption.setNodes(deploy_bundle_set);
					user_consumption.save();
					Logger.info("DEPLOY INFO SAVED ......");
					Logger.info("------------EXITING CONSUMER DEPLOY()--------------");
					render();
			}
						
			 			
					
		 }
		 
		 catch (Exception ae) 
			{
			 //logger.warn(ex, "exception thrown while monitoring %s on %s, returning CONTINUE",virtualMachine, getClass().getName());
			 Logger.warn(ae, "EXCEPTION OCCURED", vm_todeploy.getId() );
			//Logger.info(" ERROR: " + ae.printStackTrace());
			 //ae.printStackTrace();
			 if (context!=null)
			 {
				 context.close();
			 }
				 
			 }
		
		  
	}
	

}