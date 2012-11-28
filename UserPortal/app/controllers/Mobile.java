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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.persistence.Query;

import models.Deploy_Bundle;
import models.Deploy_Bundle_Nodes;
import models.Deploy_Nodes_Resources;
import models.Nodes;
import models.Nodes_Resources;
import models.Offer;
import models.OfferPurchased;
import models.UserPortal;
import models.VirtualApplianceFull;
import models.VirtualDatacenterFull;
import models.VirtualMachineFull;
import monitor.VmEventHandler;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.HardDisk;
import org.jclouds.abiquo.domain.cloud.VirtualAppliance;
import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.cloud.VirtualMachineTemplate;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.enterprise.Role;
import org.jclouds.abiquo.domain.enterprise.User;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.domain.network.PrivateNetwork;
import org.jclouds.abiquo.domain.task.AsyncTask;
import org.jclouds.abiquo.features.services.AdministrationService;
import org.jclouds.abiquo.features.services.CloudService;
import org.jclouds.abiquo.monitor.VirtualApplianceMonitor;
import org.jclouds.abiquo.monitor.VirtualMachineMonitor;
import org.jclouds.rest.AuthorizationException;

import play.Logger;
import play.Play;
import play.cache.Cache;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.db.jpa.JPA;
import play.mvc.Controller;
import portal.util.AbiquoUtils;
import portal.util.Context;
import portal.util.CurrentUserContext;

import com.abiquo.model.enumerator.HypervisorType;
import com.abiquo.server.core.cloud.VirtualApplianceState;
import com.abiquo.server.core.cloud.VirtualMachineState;

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
		// login_page();
		Logger.info("-----EXITING LOGOUT()-----");
		render();
	}

	public static void index() {
		Logger.info("-----INSIDE Index()-----");
		String user = session.get("username");
		if (user != null) {
			render(user);
		} else {
			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
		Logger.info("-----EXITING Index()-----");
	}

	public static void admin() {
		Logger.info("-----INSIDE Index()-----");
		String user = session.get("username");
		if (user != null) {
			render(user);
		} else {
			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
		Logger.info("-----EXITING Index()-----");
	}

	public static void listActiveOffers() {
		Logger.info("-----INSIDE Index()-----");
		String user = session.get("username");
		if (user != null) {
			List<Offer> offers = ConsumerDAO.getPublishedOffers();
			render(offers, user);
		} else {
			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
		Logger.info("-----EXITING Index()-----");
	}

	public static void listInactiveOffers() {
		Logger.info("-----INSIDE Index()-----");
		String user = session.get("username");
		if (user != null) {
			ArrayList<VirtualDatacenterFull> vdcList = new ArrayList<VirtualDatacenterFull>();

			Iterable<VirtualDatacenter> vdc_list = ProducerRemote
					.listVirtualDatacenters();
			for (VirtualDatacenter virtualDatacenter : vdc_list) {
				final Iterable<VirtualAppliance> listVirtualAppliances = virtualDatacenter
						.listVirtualAppliances();
				ArrayList<VirtualAppliance> listVappWithVM = new ArrayList<VirtualAppliance>();
				for (VirtualAppliance virtualAppliance : listVirtualAppliances) {
					Integer va_id = virtualAppliance.getId();
					Query query = JPA.em().createNamedQuery("getOfferDetails");
					query.setParameter(1, va_id);
					List<Offer> scOffer = query.getResultList();
					if (scOffer.size() == 0) {
						List<VirtualMachine> vmList = virtualAppliance
								.listVirtualMachines();
						if (vmList.size() > 0) {
							listVappWithVM.add(virtualAppliance);
						}
					}
				}
				VirtualDatacenterFull vdFull = new VirtualDatacenterFull(
						virtualDatacenter, listVappWithVM);
				vdcList.add(vdFull);
			}
			render(vdcList, user);
		} else {
			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
		Logger.info("-----EXITING Index()-----");
	}

	public static void listOffersToPurchase() {
		// Logger.info("---------INSIDE CONSUMER listOffersToPurchase()---------------");
		// Logger.info("Enterprie ID for current User " + enterpriseID);
		String user = session.get("username");
		if (user != null) {
			Logger.info("CURRENT USER EMAIL ID: " + user);
			List<Offer> offers = ConsumerDAO.getPublishedOffers();
			Logger.info("------------EXITING CONSUMER AVAILABLEOFFERS()--------------");
			render(offers, user);
		} else {

			flash.error("You are not connected.Please Login");
			Mobile.login();
		}
	}

	public static void listOffersPurchased() {
		Logger.info("---------INSIDE CONSUMER PURCHASEDOFFERS()---------------");
		Logger.info("Enterprie ID for current User ");
		String user = session.get("username");
		String password = session.get("password");

		if (user != null) {
			Logger.info("CURRENT USER EMAIL ID: " + user);
			AbiquoContext contextt = Context.getApiClient(user, password);
			if (contextt != null) {
				AbiquoUtils.setAbiquoUtilsContext(contextt);
				final User userAbiquo = contextt.getAdministrationService()
						.getCurrentUser();
				final CloudService cloudService = contextt.getCloudService();
				final Integer idEnterprise = userAbiquo.getEnterprise().getId();
				List<OfferPurchased> offersPurchased = ProducerDAO
						.getOffersPurchasedFromEnterpriseId(idEnterprise);
				for (OfferPurchased offerPurchased : offersPurchased) {
					VirtualDatacenter vdc = cloudService
							.getVirtualDatacenter(offerPurchased
									.getIdVirtualDatacenterUser());
					VirtualAppliance vapp = vdc
							.getVirtualAppliance(offerPurchased
									.getIdVirtualApplianceUser());
					offerPurchased.setVirtualApplianceState(vapp != null ? vapp
							.getState() : VirtualApplianceState.UNKNOWN);
					offerPurchased.save();
				}

				Logger.info("------------EXITING CONSUMER PURCHASEDOFFERS()--------------");
				render(offersPurchased, user);
			}

		} else {

			flash.error("You are not connected.Please Login");
			Login.login_page();
		}

	}

	public static void manageOffer(final Integer offerPurchasedId) {
		Logger.info("---------INSIDE CONSUMER PURCHASEDOFFERS()---------------");
		Logger.info("Enterprie ID for current User ");
		String user = session.get("username");
		String password = session.get("password");

		AbiquoContext context = Context.getApiClient(user, password);
		AbiquoUtils.setAbiquoUtilsContext(context);

		final User userAbiquo = context.getAdministrationService()
				.getCurrentUser();
		// final CloudService cloudService = contextt.getCloudService();
		final Enterprise userEnterprise = userAbiquo.getEnterprise();
		final Integer idEnterprise = userEnterprise.getId();

		final OfferPurchased offerPurchased = OfferPurchased
				.findById(offerPurchasedId);

		if (offerPurchased.getUser().getIdEnterprise() == idEnterprise) {

			VirtualDatacenter virtualDatacenter = context.getCloudService()
					.getVirtualDatacenter(
							offerPurchased.getIdVirtualDatacenterUser());
			VirtualAppliance vapp = virtualDatacenter
					.getVirtualAppliance(offerPurchased
							.getIdVirtualApplianceUser());

			ArrayList<VirtualMachineFull> listVM = new ArrayList<VirtualMachineFull>();
			for (VirtualMachine virtualMachine : vapp.listVirtualMachines()) {
				VirtualMachineFull vmfull = new VirtualMachineFull(
						virtualMachine);

				vmfull.setCpu(virtualMachine.getCpu());
				vmfull.setRam(virtualMachine.getRam());
				vmfull.setHd((int) (virtualMachine.getHdInBytes() / (1024 * 1024)));

				//VirtualMachineTemplate vtemplate = virtualMachine.getNameLabel();
				vmfull.setTemplate_name(virtualMachine.getNameLabel());
				//vmfull.setTemplate_path(vtemplate.getPath());

				listVM.add(vmfull);
			}

			VirtualApplianceFull vappFull = new VirtualApplianceFull(vapp,
					listVM);

			/*
			 * List<HardDisk> harddisk = virtualMachine
			 * .listAttachedHardDisks();
			 */

			// VirtualMachineTemplate template = virtualMachine.getTemplate();
			// String template_path = template.getIconUrl();
			// String template_name = template.getName();
			// String template_path = template.getPath();
			render(vappFull, user, offerPurchased, offerPurchasedId);

		} else {

			flash.error("You are not connected.Please Login");
			Login.login_page();
		}

	}

	public static void listAllOffers(Integer id_vdc, String service_level) {
		Iterable<VirtualDatacenter> vdc_list = null;
		VirtualDatacenter virtualDatacenter = null;
		List<VirtualAppliance> vaList = null;
		List<VirtualAppliance> vaWithVm = new LinkedList<VirtualAppliance>();
		String user = session.get("username");
		String password = session.get("password");
		if (user != null) {
			AbiquoContext context = Context.getApiClient(user, password);
			AbiquoUtils.setAbiquoUtilsContext(context);
			try {
				if (id_vdc != null) {
					vdc_list = AbiquoUtils.getAllVDC();
					virtualDatacenter = AbiquoUtils.getVDCDetails(id_vdc);
					if (virtualDatacenter != null) {
						vaList = virtualDatacenter.listVirtualAppliances();
						for (VirtualAppliance virtualAppliance : vaList) {
							Integer va_id = virtualAppliance.getId();
							Query query = JPA.em().createNamedQuery(
									"getOfferDetails");
							query.setParameter(1, va_id);
							List<Offer> scOffer = query.getResultList();
							if (scOffer.size() == 0) {
								List<VirtualMachine> vmList = virtualAppliance
										.listVirtualMachines();
								if (vmList.size() > 0) {
									vaWithVm.add(virtualAppliance);

								}
							}
						}
					}

				}
				// List<OfferPurchased> resultSet =
				// ProducerDAO.getSubscribedOffersGroupByServiceLevels();
				List<Offer> resultSet1 = ProducerDAO
						.getSubscribedOffers(service_level);
				Logger.info(" resultSet1 size " + resultSet1);
				Logger.info(" -----INSIDE PRODUCER DISPLAYOFFER()------");
				render(resultSet1, user, vaWithVm, virtualDatacenter, vdc_list);
			} catch (Exception e) {
				flash.error("Unable to create context");
				// e.printStackTrace();
				// Logger.info(" -----EXITING PRODUCER VMDETAILS()------" +
				// e.printStackTrace(), "");
				render("/ProducerRemote/listVDC.html");

			} finally {
				flash.clear();
				/*
				 * if (context!= null) context.close();
				 */
			}

		} else {

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
			AbiquoContext context = Context.getApiClient(user, password);
			AbiquoUtils.setAbiquoUtilsContext(context);
			try {

				VirtualDatacenter virtualDatacenter = context.getCloudService()
						.getVirtualDatacenter(vdcId);
				VirtualAppliance vapp = virtualDatacenter
						.getVirtualAppliance(vaId);

				ArrayList<VirtualMachineFull> listVM = new ArrayList<VirtualMachineFull>();
				for (VirtualMachine virtualMachine : vapp.listVirtualMachines()) {
					VirtualMachineFull vmfull = new VirtualMachineFull(
							virtualMachine);

					vmfull.setCpu(virtualMachine.getCpu());
					vmfull.setRam(virtualMachine.getRam());
					vmfull.setHd((int) (virtualMachine.getHdInBytes() / (1024 * 1024)));

					VirtualMachineTemplate vtemplate = virtualMachine
							.getTemplate();
					vmfull.setTemplate_name(vtemplate.getName());
					vmfull.setTemplate_path(vtemplate.getPath());

					listVM.add(vmfull);
				}

				VirtualApplianceFull vappFull = new VirtualApplianceFull(vapp,
						listVM);

				/*
				 * List<HardDisk> harddisk = virtualMachine
				 * .listAttachedHardDisks();
				 */

				// VirtualMachineTemplate template =
				// virtualMachine.getTemplate();
				// String template_path = template.getIconUrl();
				// String template_name = template.getName();
				// String template_path = template.getPath();
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
			AbiquoContext context = Context.getApiClient(user, password);
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

					List<Offer> offers = ProducerDAO
							.getOfferDetails(id_va_param);

					// Price
					final String price = AbiquoUtils.getVAPrice(id_vdc_param,
							id_va_param);
					render(vmList, virtualAppliance, virtualDatacenter, user,
							offers, price, id_vdc_param, id_va_param);
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

			AbiquoContext context = Context.getApiClient(username, password);
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
						User currentUser = adminService.getCurrentUser();
						flash.put("currentUserInfo", currentUser);
						Integer enterpriseID = currentUser.getEnterprise()
								.getId();

						String useremail = currentUser.getEmail();
						session.put("email", useremail);
						if (currentUser != null) {
							Role role = currentUser.getRole();
							Logger.info("Role of user" + role);

							if (role.getName().contentEquals("CLOUD_ADMIN")) {
								// ProducerLocal.admin();
								admin();
							} else {
								// Consumer.ServiceCatalog(enterpriseID);
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

	public static void confirmation(final Integer purchasedOfferId,
			final String action) {
		Logger.info("-----INSIDE Index()-----");
		String user = session.get("username");
		if (user != null) {
			/*
			 * final Offer offer = Offer.findById(offerId); render(user, offer);
			 */
			if (action.equals("reset"))
				resetConfirmation(purchasedOfferId);
			else if (action.equals("delete"))
				deleteConfirmation(purchasedOfferId);
			else
				purchaseConfirmation(purchasedOfferId);
		} else {
			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
		Logger.info("-----EXITING Index()-----");
	}

	public static void purchaseConfirmation(final Integer offerId) {
		Logger.info("-----INSIDE Index()-----");
		String user = session.get("username");
		if (user != null) {
			final Offer offer = Offer.findById(offerId);
			render(user, offer);
		} else {
			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
		Logger.info("-----EXITING Index()-----");
	}

	public static void resetConfirmation(final Integer purchasedOfferId) {
		Logger.info("-----INSIDE Index()-----");
		String user = session.get("username");
		if (user != null) {
			render(user, purchasedOfferId);
		} else {
			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
		Logger.info("-----EXITING Index()-----");
	}

	public static void deleteConfirmation(final Integer purchasedOfferId) {
		Logger.info("-----INSIDE Index()-----");
		String user = session.get("username");
		if (user != null) {
			render(user, purchasedOfferId);
		} else {
			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
		Logger.info("-----EXITING Index()-----");
	}

	public static void upgradeConfirmation(final Integer purchasedOfferId) {
		Logger.info("-----INSIDE Index()-----");
		String user = session.get("username");
		if (user != null) {
			render(user, purchasedOfferId);
		} else {
			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
		Logger.info("-----EXITING Index()-----");
	}

	/**
	 * 1. Customer buy offer as a User. Deployment needs CLOUD_ADMIN privilege.
	 * Hence,require deploy user setup for the enterprise that consumer belongs
	 * to.2 users - session user and deploy user . 2. Save the deployment
	 * details such as user, vdc created, SC offer id , lease etc into database.
	 * 3. Destroy date needs to be updated with the date when offer is
	 * undeployed after lease has expired (in future releases). For now, its
	 * null. 4. Refer portal-schema if needed.
	 * 
	 * @param id_datacenter
	 *            The datacenter id to be used for deployment.
	 * @param vdc_id_param
	 *            The id of virtual datacenter to be created.
	 * @param sc_offer_id
	 *            The id of virtual appliance to be deployed.
	 * @param va_param
	 *            The virtual appliance name.
	 * @param lease_period
	 */
	@SuppressWarnings("null")
	public static void deploy(final Integer id_datacenter,
			final Integer vdc_id_param, final Integer sc_offer_id,
			final String va_param, final String lease_period) {
		Logger.info("---------INSIDE CONSUMER DEPLOY()---------------");
		Logger.info(" DEPLOY( INTEGER ID_DATACENTER:: " + id_datacenter
				+ ", INTEGER VDC_ID_PARAM :: " + vdc_id_param
				+ ", INTEGER SC_OFFER_ID :: " + sc_offer_id
				+ " , String va_param:: " + va_param + ")");

		String deploy_username = null;
		String deploy_password = null;
		Integer deploy_enterprise_id = null;

		String user = session.get("username");
		String password = session.get("password");

		AbiquoContext contextt = Context.getApiClient(user, password);
		if (contextt != null) {
			AbiquoUtils.setAbiquoUtilsContext(contextt);

			/* ---------------------------- */
			/*
			 * Retrieve the deploy username and password for current user's
			 * Enterprise.
			 */
			Enterprise current_enterprise = AbiquoUtils
					.getCurrentUserEnterprise();
			Integer enterprise_id = current_enterprise.getId();

			/*
			 * for (MKT_Configuration mkt : mkt_conf) { deploy_username =
			 * mkt.getMkt_deploy_user(); deploy_password =
			 * mkt.getMkt_deploy_pw(); deploy_enterprise_id =
			 * mkt.getDeploy_enterprise_id(); }
			 */

			deploy_username = user;
			deploy_password = password;
			deploy_enterprise_id = current_enterprise.getId();

			Logger.info(" DEPLOY ENTERPRISE ID  + USERNAME + PASSWORD :"
					+ deploy_enterprise_id + "  " + deploy_username + "  "
					+ deploy_password);
			/* ---------------------------- */

			/* Create context with deploy username and password for deployments */
			AbiquoContext context = Context.getApiClient(deploy_username,
					deploy_password);

			VirtualDatacenter vdc_toDeploy = null;
			VirtualAppliance virtualapp_todeploy = null;
			VirtualMachine vm_todeploy = null;
			VirtualDatacenter virtualDC = null;
			String vdc_name = null;
			try {
				// AbiquoUtils.setAbiquoUtilsContext(context);
				Enterprise enterprise = AbiquoUtils
						.getEnterprise(deploy_enterprise_id);
				String useremail = session.get("email");
				String vdc_user = session.get("username");
				String vdcname = Helper.vdcNameGen(vdc_user);
				Logger.info("CURRENT USER EMAIL ID: " + useremail);
				Logger.info(" vdcname : " + vdcname);

				virtualDC = AbiquoUtils.getMarketplaceDetails(vdc_id_param);
				Logger.info(" VDC to deploy: ", virtualDC);
				vdc_name = virtualDC.getName();
				HypervisorType hypervisor = virtualDC.getHypervisorType();
				Logger.info(" Hypervisor to deploy: ", hypervisor);

				// get first datacenter allowed. For developement only will be
				// one.
				Datacenter datacenter = enterprise.listAllowedDatacenters()
						.get(0);
				Logger.info(" Datacenter to deploy: ", datacenter);

				PrivateNetwork network = PrivateNetwork
						.builder(context.getApiContext()).name("10.80.0.0")
						.gateway("10.80.0.1").address("10.80.0.0").mask(22)
						.build();
				Logger.info(" Network Built");

				vdc_toDeploy = VirtualDatacenter
						.builder(context.getApiContext(), datacenter,
								enterprise).name(vdcname).cpuCountLimits(0, 0)
						.hdLimitsInMb(0, 0).publicIpsLimits(0, 0)
						.ramLimits(0, 0).storageLimits(0, 0).vlansLimits(0, 0)
						.hypervisorType(hypervisor).network(network).build();

				Logger.info("VDC built  ");
				vdc_toDeploy.save();
				Logger.info(" 1. VDC CREATED ");
				virtualapp_todeploy = VirtualAppliance
						.builder(context.getApiContext(), vdc_toDeploy)
						.name(va_param).build();
				virtualapp_todeploy.save();

				Logger.info(" 2. VAPP CREATED ");

				/* Save the deploy info to the portal database : user, vdc etc */
				final User userAbiquo = contextt.getAdministrationService()
						.getCurrentUser();
				final Integer idUser = userAbiquo.getId();
				final OfferPurchased offerPurchased = new OfferPurchased();
				UserPortal userToSave = UserPortal.findById(idUser);

				if (userToSave == null) {
					// Try to recover from jClouds
					final String nickUser = userAbiquo.getNick();
					final String emailUser = userAbiquo.getEmail();
					final Integer idEnterprise = userAbiquo.getEnterprise()
							.getId();
					userToSave = new UserPortal(idUser, nickUser, emailUser,
							idEnterprise);
					userToSave.save();
				}

				offerPurchased.setUser(userToSave);

				Date current = new Date();
				Calendar cal = Calendar.getInstance();
				if (lease_period.contentEquals("30 days")) {
					Logger.info("case1 : 30 days lease ");
					cal.add(Calendar.DATE, 30);
				} else if (lease_period.contentEquals("60 days")) {
					Logger.info("case2 : 60 days lease");
					cal.add(Calendar.DATE, 60);
				} else if (lease_period.contentEquals("90 days")) {
					Logger.info("case3 : 90 days lease ");
					cal.add(Calendar.DATE, 90);

				}
				Logger.info("--------------------");
				offerPurchased.setStart(current);
				offerPurchased.setExpiration(cal.getTime());
				// user_consumption.setVdc_name(vdc_toDeploy.getName());
				offerPurchased.setLeasePeriod(lease_period);
				offerPurchased.setIdVirtualDatacenterUser(vdc_toDeploy.getId());
				offerPurchased.setIdVirtualApplianceUser(virtualapp_todeploy
						.getId());

				final Offer offer = Offer.findById(sc_offer_id);
				// offer.setVirtualDatacenter(vdc_toDeploy.getId());
				offerPurchased.setOffer(offer);

				Set<Deploy_Bundle> deploy_bundle_set = new HashSet<Deploy_Bundle>();
				Deploy_Bundle deploy_Bundle = new Deploy_Bundle();
				deploy_Bundle.setDeploy_datacenter(datacenter.getId());
				deploy_Bundle.setDeploy_hypervisorType(hypervisor.toString());
				deploy_Bundle.setDeploy_network("");
				deploy_Bundle.setVapp_name(virtualapp_todeploy.getName());
				deploy_Bundle.setVdc_name(vdc_toDeploy.getId());
				deploy_Bundle.setOfferPurchased(offerPurchased);
				deploy_Bundle.setVapp_id(virtualapp_todeploy.getId());
				deploy_bundle_set.add(deploy_Bundle);
				/*
				 * String query =
				 * "select p from sc_offer as p where p.sc_offer_id = ?1";
				 * JPAQuery result = sc_offer.find(query, sc_offer_id);
				 */List<Offer> nodes = ProducerDAO.getOfferDetails(sc_offer_id);
				for (Offer node : nodes) {
					// ///Set<Deploy_Bundle_Nodes> deploy_Bundle_Nodes_list =
					// new HashSet<Deploy_Bundle_Nodes>();

					// / Retrieve nodes from jClouds
					Set<Nodes> vmlist_todeploy = node.getNodes();

					Set<Deploy_Bundle_Nodes> deploy_Bundle_Nodes_list = new HashSet<Deploy_Bundle_Nodes>();
					for (Nodes aVM : vmlist_todeploy) {
						String vmName = aVM.getNode_name();
						VirtualMachineTemplate vm_template_todeploy = enterprise
								.getTemplateInRepository(datacenter,
										aVM.getIdImage());
						int cpu = aVM.getCpu();
						int ram = aVM.getRam();
						// String description = aVM.getDescription();

						vm_todeploy = VirtualMachine
								.builder(context.getApiContext(),
										virtualapp_todeploy,
										vm_template_todeploy).nameLabel(vmName)
								.cpu(cpu).ram(ram).password("vmpassword")
								.build();
						vm_todeploy.save();
						Logger.info(" 3. VM CREATED");
						Deploy_Bundle_Nodes deploy_Bundle_Nodes = new Deploy_Bundle_Nodes();
						deploy_Bundle_Nodes.setCpu(cpu);
						deploy_Bundle_Nodes.setNode_name(vmName);
						deploy_Bundle_Nodes.setNode_name(vm_todeploy
								.getNameLabel());
						deploy_Bundle_Nodes.setNode_id(vm_todeploy.getId());
						deploy_Bundle_Nodes.setRam(ram);
						deploy_Bundle_Nodes.setVdrp_password("");
						deploy_Bundle_Nodes.setVdrpIP("");
						deploy_Bundle_Nodes.setVdrpPort(0);
						deploy_Bundle_Nodes_list.add(deploy_Bundle_Nodes);
						// deploy_Bundle_Nodes.setResources(resources);

						List<HardDisk> hardDisk_toattach = new ArrayList<HardDisk>();
						Set<Deploy_Nodes_Resources> deploy_Nodes_Resources_list = new HashSet<Deploy_Nodes_Resources>();
						Set<Nodes_Resources> resources = aVM.getResources();
						for (Nodes_Resources resource : resources) {
							Long size = resource.getValue();
							HardDisk disk = HardDisk
									.builder(context.getApiContext(),
											vdc_toDeploy).sizeInMb(size)
									.build();
							disk.save();
							hardDisk_toattach.add(disk);
							Deploy_Nodes_Resources deploy_Nodes_Resources = new Deploy_Nodes_Resources();
							deploy_Nodes_Resources.setResourceType(resource
									.getResourceType());
							deploy_Nodes_Resources.setResourceType(resource
									.getSequence());
							deploy_Nodes_Resources
									.setValue(resource.getValue());
							deploy_Nodes_Resources_list
									.add(deploy_Nodes_Resources);
						}
						deploy_Bundle_Nodes
								.setResources(deploy_Nodes_Resources_list);
						HardDisk[] disks = new HardDisk[hardDisk_toattach
								.size()];
						for (int i = 0; i < hardDisk_toattach.size(); i++) {
							disks[i] = hardDisk_toattach.get(i);
						}
						vm_todeploy.attachHardDisks(disks);
						Logger.info(" 4. HARDDISKS ATTACHED ");
						VmEventHandler handler = new VmEventHandler(context,
								vm_todeploy);
						Logger.info(" Handler created :");
						VirtualMachineMonitor monitor = context
								.getMonitoringService()
								.getVirtualMachineMonitor();
						monitor.register(handler);
						vm_todeploy.deploy();
						Logger.info("STARTING MONITORING ......");
						monitor.monitorDeploy(vm_todeploy);

					}
					Logger.info("SAVING DEPLOY INFORMATION ......");
					deploy_Bundle.setNodes(deploy_Bundle_Nodes_list);

					offerPurchased.setNodes(deploy_bundle_set);
					offerPurchased.setServiceLevel(offer
							.getDefaultServiceLevel());
					offerPurchased.save();
					Logger.info("DEPLOY INFO SAVED ......");
					Logger.info("------------EXITING CONSUMER DEPLOY()--------------");
					final String action = "deployed";
					render("/Mobile/action.html", vdc_name, enterprise_id,
							action);
				}

			} catch (AuthorizationException ae) {

				Logger.warn(ae, "EXCEPTION OCCURED IN deploy()");
				String message = "Deployment cannot proceed further. Please Check deploy user and password for your enterprise.";
				render("/errors/error.html", message);
			} catch (Exception ae) {

				Logger.warn(ae, "EXCEPTION OCCURED  IN deploy()");
				String message = "Deployment cannot proceed further. Please contact your System Administrator.";
				render("/errors/error.html", message);
				if (context != null) {
					context.close();
				}
			}

		} else {

			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
	}

	public static void deleteOffer(final Integer purchasedOfferId) {
		/*
		 * Logger.info("---------INSIDE CONSUMER DEPLOY()---------------");
		 * Logger.info(" DEPLOY( INTEGER ID_DATACENTER:: " +
		 * ", INTEGER SC_OFFER_ID :: " + sc_offer_id + " , String va_param:: " +
		 * vappId + ")");
		 */

		String deploy_username = null;
		String deploy_password = null;
		Integer deploy_enterprise_id = null;

		String user = session.get("username");
		String password = session.get("password");

		AbiquoContext contextt = Context.getApiClient(user, password);
		if (contextt != null) {
			AbiquoUtils.setAbiquoUtilsContext(contextt);

			/* ---------------------------- */
			/*
			 * Retrieve the deploy username and password for current user's
			 * Enterprise.
			 */
			Enterprise current_enterprise = AbiquoUtils
					.getCurrentUserEnterprise();
			Integer enterprise_id = current_enterprise.getId();
			/*
			 * List<MKT_Configuration> mkt_conf = MarketDAO
			 * .getMKTConfiguration(enterprise_id);
			 * 
			 * for (MKT_Configuration mkt : mkt_conf) { deploy_username =
			 * mkt.getMkt_deploy_user(); deploy_password =
			 * mkt.getMkt_deploy_pw(); deploy_enterprise_id =
			 * mkt.getDeploy_enterprise_id(); }
			 */

			deploy_username = user;
			deploy_password = password;
			deploy_enterprise_id = current_enterprise.getId();

			Logger.info(" UNDEPLOY ENTERPRISE ID  + USERNAME + PASSWORD :"
					+ deploy_enterprise_id + "  " + deploy_username + "  "
					+ deploy_password);
			/* ---------------------------- */

			/* Create context with deploy username and password for deployments */
			AbiquoContext context = Context.getApiClient(deploy_username,
					deploy_password);

			VirtualDatacenter vdc_toDeploy = null;
			VirtualAppliance virtualapp_todeploy = null;
			VirtualMachine vm_todeploy = null;
			VirtualDatacenter virtualDC = null;
			String vdc_name = null;
			try {
				Enterprise enterprise = AbiquoUtils
						.getEnterprise(deploy_enterprise_id);
				String useremail = session.get("email");
				String vdc_user = session.get("username");
				String vdcname = Helper.vdcNameGen(vdc_user);
				Logger.info("CURRENT USER EMAIL ID: " + useremail);
				Logger.info(" vdcname : " + vdcname);

				final OfferPurchased offerPurchased = OfferPurchased
						.findById(purchasedOfferId);
				VirtualDatacenter vdc = context.getCloudService()
						.getVirtualDatacenter(
								offerPurchased.getIdVirtualDatacenterUser());
				VirtualAppliance vapp = vdc.getVirtualAppliance(offerPurchased
						.getIdVirtualApplianceUser());
				// List<VirtualMachine> lvm = vapp.listVirtualMachines();
				//
				// VirtualMachineMonitor monitor =
				// context.getMonitoringService().getVirtualMachineMonitor();
				// for (VirtualMachine virtualMachine : lvm) {
				// virtualMachine.undeploy();
				// }
				//
				// VirtualMachine[] arr = new VirtualMachine[lvm.size()];
				// monitor.awaitCompletionUndeploy(lvm.toArray(arr));
				//
				// for (VirtualMachine virtualMachine : lvm) {
				// virtualMachine.delete();
				//
				// }

				VirtualApplianceMonitor monitorVapp = context
						.getMonitoringService().getVirtualApplianceMonitor();
				AsyncTask[] undeployTasks = vapp.undeploy();
				monitorVapp.awaitCompletionUndeploy(vapp);

				if (vapp.getState() == VirtualApplianceState.NOT_DEPLOYED) {
					vapp.delete();
					vdc.delete();
					offerPurchased.delete();
				} else {

					AbiquoUtils.checkErrorsInTasks(undeployTasks);
					Logger.info("Tasks Checked");

				}

				Logger.info("OFFER DELETED ......");
				Logger.info("------------EXITING CONSUMER DEPLOY()--------------");
				final String action = "deleted";
				render("/Mobile/action.html", vdc_name, enterprise_id, action);

			} catch (AuthorizationException ae) {

				Logger.warn(ae, "EXCEPTION OCCURED IN deploy()");
				String message = "Deployment cannot proceed further. Please Check deploy user and password for your enterprise.";
				render("/errors/error.html", message);
			} catch (Exception ae) {

				Logger.warn(ae, "EXCEPTION OCCURED  IN deploy()");
				String message = "Deployment cannot proceed further. Please contact your System Administrator.";
				render("/errors/error.html", message);
				if (context != null) {
					context.close();
				}

			}

		} else {

			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
	}

	public static void resetOffer(final Integer purchasedOfferId) {
		Logger.info("---------INSIDE CONSUMER DEPLOY()---------------");
		/*
		 * Logger.info(" DEPLOY( INTEGER ID_DATACENTER:: " +
		 * ", INTEGER SC_OFFER_ID :: " + sc_offer_id + " , String va_param:: " +
		 * vappId + ")");
		 */

		String deploy_username = null;
		String deploy_password = null;
		Integer deploy_enterprise_id = null;

		String user = session.get("username");
		String password = session.get("password");

		AbiquoContext contextt = Context.getApiClient(user, password);
		if (contextt != null) {
			AbiquoUtils.setAbiquoUtilsContext(contextt);

			/* ---------------------------- */
			/*
			 * Retrieve the deploy username and password for current user's
			 * Enterprise.
			 */
			Enterprise current_enterprise = AbiquoUtils
					.getCurrentUserEnterprise();
			Integer enterprise_id = current_enterprise.getId();
			/*
			 * List<MKT_Configuration> mkt_conf = MarketDAO
			 * .getMKTConfiguration(enterprise_id);
			 * 
			 * for (MKT_Configuration mkt : mkt_conf) { deploy_username =
			 * mkt.getMkt_deploy_user(); deploy_password =
			 * mkt.getMkt_deploy_pw(); deploy_enterprise_id =
			 * mkt.getDeploy_enterprise_id(); }
			 */

			deploy_username = user;
			deploy_password = password;
			deploy_enterprise_id = current_enterprise.getId();

			Logger.info(" UNDEPLOY ENTERPRISE ID  + USERNAME + PASSWORD :"
					+ deploy_enterprise_id + "  " + deploy_username + "  "
					+ deploy_password);
			/* ---------------------------- */

			/* Create context with deploy username and password for deployments */
			AbiquoContext context = Context.getApiClient(deploy_username,
					deploy_password);

			VirtualDatacenter vdc_toDeploy = null;
			VirtualAppliance virtualapp_todeploy = null;
			VirtualMachine vm_todeploy = null;
			VirtualDatacenter virtualDC = null;
			String vdc_name = null;
			try {
				Enterprise enterprise = AbiquoUtils
						.getEnterprise(deploy_enterprise_id);
				String useremail = session.get("email");
				String vdc_user = session.get("username");
				String vdcname = Helper.vdcNameGen(vdc_user);
				Logger.info("CURRENT USER EMAIL ID: " + useremail);
				Logger.info(" vdcname : " + vdcname);

				final OfferPurchased offerPurchased = OfferPurchased
						.findById(purchasedOfferId);
				VirtualDatacenter vdc = context.getCloudService()
						.getVirtualDatacenter(
								offerPurchased.getIdVirtualDatacenterUser());
				VirtualAppliance vapp = vdc.getVirtualAppliance(offerPurchased
						.getIdVirtualApplianceUser());

				List<VirtualMachine> lvm = vapp.listVirtualMachines();
				VirtualMachine[] arr = new VirtualMachine[lvm.size()];
				VirtualMachineMonitor monitor = context.getMonitoringService()
						.getVirtualMachineMonitor();

				for (VirtualMachine virtualMachine : lvm) {
					virtualMachine.changeState(VirtualMachineState.OFF);
				}
				monitor.awaitState(VirtualMachineState.OFF, lvm.toArray(arr));

				for (VirtualMachine virtualMachine : lvm) {
					virtualMachine.changeState(VirtualMachineState.ON);
				}
				monitor.awaitState(VirtualMachineState.ON, lvm.toArray(arr));

				if (vapp.getState() == VirtualApplianceState.DEPLOYED) {
					Logger.info("OFFER RESET SUCCESSFULLY");
				} else {
					// AbiquoUtils.checkErrorsInTasks(deployTasks);
					Logger.info("Tasks Checked");
				}

				Logger.info("OFFER RESUMED ......");
				Logger.info("------------EXITING CONSUMER DEPLOY()--------------");
				final String action = "reseted";
				render("/Mobile/action.html", vdc_name, enterprise_id, action);

			} catch (AuthorizationException ae) {

				Logger.warn(ae, "EXCEPTION OCCURED IN deploy()");
				String message = "Deployment cannot proceed further. Please Check deploy user and password for your enterprise.";
				render("/errors/error.html", message);
			} catch (Exception ae) {

				Logger.warn(ae, "EXCEPTION OCCURED  IN deploy()");
				String message = "Deployment cannot proceed further. Please contact your System Administrator.";
				render("/errors/error.html", message);
				if (context != null) {
					context.close();
				}

			}

		} else {

			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
	}

	public static void vncConnection(final String vncAddress,
			final String vncPort, final String vncPassword) {
		Logger.info("---------INSIDE CONSUMER OFFERDETAILS()---------------");

		String user = session.get("username");
		if (user != null) {
			Logger.info("------------EXITING CONSUMER OFFERDETAILS()--------------");
			render(vncAddress, vncPort, vncPassword, user);

		} else {

			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
	}

	public static void vncLayer(final String vncAddress, final String vncPort,
			final String vncPassword) {
		Logger.info("---------INSIDE CONSUMER OFFERDETAILS()---------------");

		String user = session.get("username");
		if (user != null) {
			Logger.info("------------EXITING CONSUMER OFFERDETAILS()--------------");

			try {
				// TODO: connect ssh to retrieve data from each vm
				Properties props = new Properties();
				// load a properties file
				props.load(new FileInputStream(Play
						.getFile("conf/config.properties")));

				final String noVNCPath = props.getProperty("noVNC");
				final String noVNCPort = props.getProperty("noVNCPort");
				final String noVNCServer = props.getProperty("noVNCServer");
				// final String sp = noVNCPath + "utils/websockify --web " +
				// noVNCPath + " " + noVNCPort + " " + vncAddress + ":" +
				// vncPort;
				ProcessBuilder pb = new ProcessBuilder(
						"public/noVNC/utils/websockify", "--web",
						"public/noVNC/", noVNCPort, vncAddress + ":" + vncPort);
				pb.redirectErrorStream(); // redirect stderr to stdout
				Process process = pb.start();
				play.mvc.Http.Request current = play.mvc.Http.Request.current();
				String url = current.url;
				String domain = current.domain;
				Integer newPortRaw = Integer.parseInt(noVNCPort);
				Integer newPort = newPortRaw == 6900 ? 6080 : newPortRaw + 1;

				props.setProperty("noVNCPort", newPort.toString());
				props.save(new FileOutputStream(new File(
						"conf/config.properties")), "");
				render(vncAddress, vncPort, vncPassword, noVNCServer,
						noVNCPort, url, user, domain);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
	}

	// public static void register(@Required final String username,
	// @Required final String password, @Required final String email) {
	// if (Validation.hasErrors()) {
	// flash.error("Username and password required");
	// login_page();
	// } else {
	// Logger.info("-------------- INSIDE REGISTER.CONNECT()--------------");
	// ///////////
	// // First of all create the Abiquo context pointing to the Abiquo API
	// /*AbiquoContext context = ContextBuilder.newBuilder(new
	// AbiquoApiMetadata())
	// .endpoint("http://10.60.21.33/api")
	// .credentials("user", "password")
	// .modules(ImmutableSet.<Module> of(new SLF4JLoggingModule()))
	// .build(AbiquoContext.class);*/

	// AbiquoContext context = null;
	// try
	// {
	// Properties props = new Properties();
	// props.load(new FileInputStream(Play.getFile("conf/config.properties")));

	// final String admin = props.getProperty("admin");
	// final String passwordAdmin = props.getProperty("password");
	// final String datacenterName = props.getProperty("datacenter");
	// final String rolePortal = props.getProperty("role");

	// context = Context.getContext (admin, passwordAdmin);
	// // Create a new enterprise with a given set of limits
	// Enterprise enterprise = Enterprise.builder(context)
	// .name(username)
	// .cpuCountLimits(5, 10) // Number of CPUs: Maximum 10, warn when 5 are in
	// use
	// .ramLimits(2048, 4096) // Ram in MB: 4GB total, warn when 2GB are in use
	// .publicIpsLimits(5, 10) // Available public IPs: 10, warn when 5 are in
	// use
	// //.storageLimits(5120 * 1204 * 1024, 10240 * 1204 * 1024) // External
	// storage capacity: 10GB, warn when 5GB are in use
	// .build();

	// enterprise.save();

	// // Allow the enterprise to use a Datacenter
	// Datacenter datacenter =
	// context.getAdministrationService().findDatacenter(DatacenterPredicates.name(datacenterName));

	// enterprise.allowDatacenter(datacenter);

	// /*Datacenter datacenter =
	// context.getAdministrationService().getDatacenter(0);

	// enterprise.allowDatacenter(datacenter);*/

	// // Create an Enterprise administrator, so it can begin using the cloud
	// infrastructure
	// // and can start creating the users of the enterprise
	// Role role =
	// context.getAdministrationService().findRole(RolePredicates.name(rolePortal));

	// // Create the user with the selected role in the just created enterprise
	// User enterpriseUser = User.builder(context, enterprise, role)
	// .name(username, username) // The name and surname
	// .email(email) // The email address
	// .nick(username) // The login username
	// .password(password) // The password
	// .build();

	// enterpriseUser.save();

	// connect(username, password);

	// // At this point, the new Enterprise is created and ready to begin
	// consuming the resources
	// // of the cloud
	// } catch (AbiquoException ae) {
	// // ae.printStackTrace();
	// if
	// (ae.getMessage().trim().equals("ENTERPRISE-4 - Duplicate name for an enterprise"))
	// {
	// flash.error("User already registered");
	// } else {
	// flash.error("Error creating new account");
	// }
	// login_page();
	// } catch (AuthorizationException ae) {
	// // ae.printStackTrace();
	// flash.error("Unauthorized User");
	// login_page();
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (Exception e) {
	// // e.printStackTrace();
	// flash.error("Server Unreachable");
	// login_page();
	// } finally {
	// flash.clear();
	// if (context != null)
	// {
	// context.close();
	// }
	// }
	// }
	// }
}
