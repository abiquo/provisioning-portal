/*******************************************************************************
 * Abiquo community edition
 * cloud management application for hybrid clouds
 *  Copyright (C) 2008-2010 - Abiquo Holdings S.L.
 * 
 *  This application is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU LESSER GENERAL PUBLIC
 *  LICENSE as published by the Free Software Foundation under
 *  version 3 of the License
 * 
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  LESSER GENERAL PUBLIC LICENSE v.3 for more details.
 * 
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the
 *  Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 *  Boston, MA 02111-1307, USA.
 ******************************************************************************/
package controllers;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Query;

import models.Offer;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.HardDisk;
import org.jclouds.abiquo.domain.cloud.VirtualAppliance;
import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.cloud.VirtualMachineTemplate;
import org.jclouds.abiquo.domain.enterprise.Role;
import org.jclouds.abiquo.domain.enterprise.User;
import org.jclouds.abiquo.features.services.AdministrationService;
import org.jclouds.rest.AuthorizationException;

import play.Logger;
import play.cache.Cache;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.db.jpa.JPA;
import play.mvc.Controller;
import portal.util.AbiquoUtils;
import portal.util.Context;
import portal.util.CurrentUserContext;

public class Mobile extends Controller {

	public static void login() {
		session.remove("username");
		session.remove("password");
		Cache.delete(session.getId());

		render();
	}

	public static void logout() {
		Logger.info("-----INSIDE LOGOUT()-----");
		session.remove("username");
		session.remove("password");
		session.clear();
		Cache.delete(session.getId());
		//login_page();
		Logger.info("-----EXITING LOGOUT()-----");
		render();
	}

	public static void index() {
		Logger.info("-----INSIDE Index()-----");
		String user = session.get("username");
		if ( user != null)
		{
			render(user);	
		}
		else 
		{			
			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
		Logger.info("-----EXITING Index()-----");
	}

	public static void listActiveOffers() {
		Logger.info("-----INSIDE Index()-----");
		String user = session.get("username");
		if ( user != null)
		{
			List<Offer> offers = ConsumerDAO.getPublishedOffers();			
			render(offers,user);
		}
		else 
		{			
			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
		Logger.info("-----EXITING Index()-----");
	}

	public static void listInactiveOffers() {
		Logger.info("-----INSIDE Index()-----");
		String user = session.get("username");
		if ( user != null)
		{	
			ArrayList<VirtualDatacenterFull> vdcList = new ArrayList<VirtualDatacenterFull>(); 
			
				Iterable<VirtualDatacenter> vdc_list = ProducerRemote.listVirtualDatacenters();			
				for (VirtualDatacenter virtualDatacenter : vdc_list) {
					final Iterable<VirtualAppliance> listVirtualAppliances = virtualDatacenter.listVirtualAppliances();
					ArrayList<VirtualAppliance> listVappWithVM = new ArrayList<VirtualAppliance>();
					for ( VirtualAppliance virtualAppliance : listVirtualAppliances )
					{
						 Integer va_id = virtualAppliance.getId();
						 Query query = JPA.em().createNamedQuery("getOfferDetails");
						 query.setParameter(1,va_id);
						 List<Offer> scOffer = query.getResultList();
						 if ( scOffer.size() == 0)
						 {
						 	List<VirtualMachine> vmList = virtualAppliance.listVirtualMachines();
						 	if (vmList.size()>0 )
						 	{
						 		listVappWithVM.add(virtualAppliance);
						 	}
						 }
					}
					VirtualDatacenterFull vdFull = new VirtualDatacenterFull(virtualDatacenter, listVappWithVM);
					vdcList.add(vdFull);					
				}
				render(vdcList,user);
		}
		else 
		{			
			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
		Logger.info("-----EXITING Index()-----");
	}

	

	public static void listAllOffers ( Integer id_vdc , String service_level  )
	{
		Iterable<VirtualDatacenter> vdc_list = null;
		VirtualDatacenter virtualDatacenter = null;
		List<VirtualAppliance> vaList = null;
		List<VirtualAppliance> vaWithVm = new LinkedList<VirtualAppliance>();
		String user =session.get("username");
		String password =session.get("password");
		if ( user != null )
		{
			AbiquoContext context = Context.getContext(user,password);
			AbiquoUtils.setAbiquoUtilsContext(context);
			try {
					if (id_vdc != null )
					{
						vdc_list = AbiquoUtils.getAllVDC();
						 virtualDatacenter = AbiquoUtils.getVDCDetails(id_vdc);
						if (virtualDatacenter != null )
						{
							vaList = virtualDatacenter.listVirtualAppliances();
							for ( VirtualAppliance virtualAppliance : vaList )
							{
								 Integer va_id = virtualAppliance.getId();
								 Query query = JPA.em().createNamedQuery("getOfferDetails");
								 query.setParameter(1,va_id);
								 List<Offer> scOffer = query.getResultList();
								 if ( scOffer.size() == 0)
								 {
								 	List<VirtualMachine> vmList = virtualAppliance.listVirtualMachines();
								 	if (vmList.size()>0 )
								 	{
								 			vaWithVm.add(virtualAppliance);
								 			
								 	}
								 }
							}
						}
							
					}
				  //List<OfferPurchased> resultSet = ProducerDAO.getSubscribedOffersGroupByServiceLevels();
				  List<Offer> resultSet1 = ProducerDAO.getSubscribedOffers(service_level);
				  Logger.info(" resultSet1 size " + resultSet1);
				  Logger.info(" -----INSIDE PRODUCER DISPLAYOFFER()------");
				  render(resultSet1,user, vaWithVm, virtualDatacenter , vdc_list);
				} 
				catch(Exception e)
				{
							flash.error("Unable to create context");
						   // e.printStackTrace();
							//Logger.info(" -----EXITING PRODUCER VMDETAILS()------" + e.printStackTrace(), "");
							render("/ProducerRemote/listVDC.html");
					
					
				}
				finally{
							flash.clear();
							if (context!= null)
								context.close();
				}
			
		}
		else 
		{
			
			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
	}

	/**
	 * Displays details about virtual machine such as ram, cpu, hardisks etc.
	 * 
	 * @param vdc
	 *            The virtual datacentre id
	 * @param va
	 *            The virtual appliance id
	 * @param vm
	 *            The virtual machine id
	 */
	public static void details(final Integer vdcId, final Integer vaId) {
		Logger.info(" -----INSIDE PRODUCER VMDETAILS()------");

		String user = session.get("username");
		String password = session.get("password");
		Logger.info("Session user in vmDetails(): " + user);
		if (user != null) {
			AbiquoContext context = Context.getContext(user, password);
			AbiquoUtils.setAbiquoUtilsContext(context);
			try {
				
				VirtualDatacenter virtualDatacenter = context.getCloudService().getVirtualDatacenter(vdcId);
				VirtualAppliance vapp = virtualDatacenter.getVirtualAppliance(vaId);
				
				ArrayList<VirtualMachineFull> listVM = new ArrayList<VirtualMachineFull>(); 				
				for (VirtualMachine virtualMachine : vapp.listVirtualMachines()) {
					VirtualMachineFull vmfull = new VirtualMachineFull(virtualMachine);
					
					vmfull.setCpu(virtualMachine.getCpu());
					vmfull.setRam(virtualMachine.getRam());
					vmfull.setHd((int) (virtualMachine.getHdInBytes() / (1024 * 1024)));
					
					VirtualMachineTemplate vtemplate = virtualMachine.getTemplate();
					vmfull.setTemplate_name(vtemplate.getName());
					vmfull.setTemplate_path(vtemplate.getPath());				
					
					listVM.add(vmfull);
				}
				
				VirtualApplianceFull vappFull = new VirtualApplianceFull(vapp, listVM);
				
								
				
				/*List<HardDisk> harddisk = virtualMachine
						.listAttachedHardDisks();*/

				//VirtualMachineTemplate template = virtualMachine.getTemplate();
				// String template_path = template.getIconUrl();
				//String template_name = template.getName();
				//String template_path = template.getPath();
				render(vappFull, user);
				Logger.info(" -----EXITING PRODUCER VMDETAILS()------");

			} catch (Exception e) {
				flash.error("Unable to create contaxt");
				render();
			} finally {
				if (context != null) {
					context.close();
				}
			}

		} else {

			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
	}
	
	public static void listVM(final Integer id_vdc_param,
			final Integer id_va_param) {
		Logger.info(" -----INSIDE PRODUCER LISTVM()------");

		String user = session.get("username");
		String password = session.get("password");
		if (user != null) {
			AbiquoContext context = Context.getContext(user, password);
			AbiquoUtils.setAbiquoUtilsContext(context);
			try {
				VirtualDatacenter virtualDatacenter = AbiquoUtils
						.getVDCDetails(id_vdc_param);
				VirtualAppliance virtualAppliance = AbiquoUtils.getVADetails(
						id_vdc_param, id_va_param);

				if (virtualAppliance != null) {
					List<VirtualMachine> vmList = virtualAppliance
							.listVirtualMachines();

					for (VirtualMachine virtualMachine : vmList) {
						virtualMachine.listAttachedNics();
					}
					
					List<Offer> offers = ProducerDAO.getOfferDetails(id_va_param);
					
					//Price
					final String price = AbiquoUtils.getVAPrice(id_vdc_param, id_va_param);					
					render(vmList, virtualAppliance, virtualDatacenter, user, offers, price, id_vdc_param, id_va_param );
				}

				else {
					String msg = " No Virtual Machine to display!!";
					render(msg, user);
				}
				Logger.info(" -----EXITING PRODUCER LISTVM()------");
			} catch (Exception e) {
				flash.error("Unable to create contaxt");
				render();
				// e.printStackTrace();

			} finally {
				if (context != null) {
					context.close();
				}

			}
		} else {

			flash.error("You are not connected.Please Login");
			Login.login_page();
		}

	}

	public static void connect(@Required final String username,
			@Required final String password) {
		if (Validation.hasErrors()) {
			flash.error("Username and password required");
			login();
		} else {
			Logger.info("-------------- INSIDE LOGIN.CONNECT()--------------");

			session.put("username", username);
			session.put("password", password);

			AbiquoContext context = Context.getContext(username, password);
			// Cache.set(session.getId() + "-context", context, "30mn");
			// AbiquoUtils.setContext(context);
			// PortalContext userContext = new PortalContext();
			// userContext.setContext(context);
			CurrentUserContext.setContext(context);
			CurrentUserContext.setUser(username);

			try {
				Logger.info("context: " + context);

				if (context != null) {
					AdministrationService adminService = context
							.getAdministrationService();
					if (adminService != null) {
						User currentUser = adminService.getCurrentUserInfo();
						flash.put("currentUserInfo", currentUser);
						Integer enterpriseID = currentUser.getEnterprise()
								.getId();

						String useremail = currentUser.getEmail();
						session.put("email", useremail);
						if (currentUser != null) {
							Role role = currentUser.getRole();
							Logger.info("Role of user" + role);

							if (role.getName().contentEquals("CLOUD_ADMIN")) {
								//ProducerLocal.admin();
								index();
							} else {
								//Consumer.ServiceCatalog(enterpriseID);
								index();
							}
						}
					}

				}

				else {
					flash.error("Unable to get the context");
					login();
				}
			} catch (AuthorizationException ae) {
				// ae.printStackTrace();
				flash.error("Unauthorized User");
				login();
			} catch (Exception e) {
				// e.printStackTrace();
				flash.error("Server Unreachable");
				login();
			} finally {
				flash.clear();
				if (context != null) {
					context.close();
				}
			}
		}
	}

	public static void purchaseConfirmation() {
		Logger.info("-----INSIDE Index()-----");
		String user = session.get("username");
		if ( user != null)
		{
			render(user);
		}
		else 
		{			
			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
		Logger.info("-----EXITING Index()-----");
	}
	
	
	// public static void register(@Required final String username,
	// 		@Required final String password, @Required final String email) {
	// 	if (Validation.hasErrors()) {
	// 		flash.error("Username and password required");
	// 		login_page();
	// 	} else {
	// 		Logger.info("-------------- INSIDE REGISTER.CONNECT()--------------");
	// 	///////////
	// 		// First of all create the Abiquo context pointing to the Abiquo API
	// 		/*AbiquoContext context = ContextBuilder.newBuilder(new AbiquoApiMetadata())
	// 		    .endpoint("http://10.60.21.33/api")
	// 		    .credentials("user", "password")
	// 		    .modules(ImmutableSet.<Module> of(new SLF4JLoggingModule()))
	// 		    .build(AbiquoContext.class);*/

	// 		AbiquoContext context = null;				  
	// 		try
	// 		{
	// 			Properties props = new Properties();
	// 			props.load(new FileInputStream(Play.getFile("conf/config.properties")));
				
	// 			final String admin = props.getProperty("admin");
	// 			final String passwordAdmin = props.getProperty("password");
	// 			final String datacenterName = props.getProperty("datacenter");
	// 			final String rolePortal = props.getProperty("role");
				
	// 			context = Context.getContext (admin, passwordAdmin);
	// 		    // Create a new enterprise with a given set of limits
	// 		    Enterprise enterprise = Enterprise.builder(context)
	// 		        .name(username)
	// 		        .cpuCountLimits(5, 10)      // Number of CPUs: Maximum 10, warn when 5 are in use
	// 		        .ramLimits(2048, 4096)      // Ram in MB: 4GB total, warn when 2GB are in use
	// 		        .publicIpsLimits(5, 10)     // Available public IPs: 10, warn when 5 are in use
	// 		        //.storageLimits(5120 * 1204 * 1024, 10240 * 1204 * 1024) // External storage capacity: 10GB, warn when 5GB are in use
	// 		        .build();

	// 		    enterprise.save();

	// 		    // Allow the enterprise to use a Datacenter
	// 		   Datacenter datacenter =
	// 		        context.getAdministrationService().findDatacenter(DatacenterPredicates.name(datacenterName));
			   
	// 		   enterprise.allowDatacenter(datacenter);
			    
	// 		    /*Datacenter datacenter =
	// 			        context.getAdministrationService().getDatacenter(0);

	// 		    enterprise.allowDatacenter(datacenter);*/

	// 		    // Create an Enterprise administrator, so it can begin using the cloud infrastructure
	// 		    // and can start creating the users of the enterprise
	// 		    Role role =
	// 		        context.getAdministrationService().findRole(RolePredicates.name(rolePortal));

	// 		    // Create the user with the selected role in the just created enterprise
	// 		    User enterpriseUser = User.builder(context, enterprise, role) 
	// 		        .name(username, username)       // The name and surname
	// 		        .email(email) // The email address
	// 		        .nick(username)              // The login username
	// 		        .password(password)       // The password
	// 		        .build();

	// 		    enterpriseUser.save();
			    
	// 		    connect(username, password);

	// 		    // At this point, the new Enterprise is created and ready to begin consuming the resources
	// 		    // of the cloud
	// 		} catch (AbiquoException ae) {
	// 			// ae.printStackTrace();				
	// 			if (ae.getMessage().trim().equals("ENTERPRISE-4 - Duplicate name for an enterprise")) {
	// 				flash.error("User already registered");
	// 			} else {
	// 				flash.error("Error creating new account");
	// 			}
	// 			login_page();
	// 		} catch (AuthorizationException ae) {
	// 			// ae.printStackTrace();
	// 			flash.error("Unauthorized User");
	// 			login_page();
	// 		} catch (FileNotFoundException e) {
	// 			// TODO Auto-generated catch block
	// 			e.printStackTrace();
	// 		} catch (IOException e) {
	// 			// TODO Auto-generated catch block
	// 			e.printStackTrace();
	// 		} catch (Exception e) {
	// 			// e.printStackTrace();
	// 			flash.error("Server Unreachable");
	// 			login_page();
	// 		} finally {
	// 			flash.clear();
	// 		    if (context != null)
	// 		    {
	// 		        context.close();
	// 		    }
	// 		}
	// 	}
	// }
}
