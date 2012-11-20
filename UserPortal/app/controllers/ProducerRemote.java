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
/**
 * @author David Lopez
 *  Producer component of provisioning portal .
 *  Includes methods that talks to Abiquo server and retrieve required information about vdc, vapp and vm
 */

package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.Nodes;
import models.Nodes_Resources;
import models.Offer;
import models.OfferPurchased;

import org.jclouds.abiquo.AbiquoApi;
import org.jclouds.abiquo.AbiquoAsyncApi;
import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.HardDisk;
import org.jclouds.abiquo.domain.cloud.VirtualAppliance;
import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.cloud.VirtualMachineTemplate;
import org.jclouds.abiquo.domain.network.PrivateNetwork;
import org.jclouds.rest.RestContext;

import play.Logger;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.db.jpa.Blob;
import play.libs.MimeTypes;
import play.mvc.Controller;
import portal.util.AbiquoUtils;
import portal.util.Context;

import com.abiquo.model.enumerator.HypervisorType;

public class ProducerRemote extends Controller {

	/**
	 * 
	 * @return list of virtualdatacenters available for current user's ( logged
	 *         in as CLOUD_ADMIN) enterprise
	 */

	public static Iterable<VirtualDatacenter> listVirtualDatacenters() {
		Logger.info("-----INSIDE PRODUCER LISTVDC()------ ");

		String user = session.get("username");
		String password = session.get("password");

		/*
		 * String user = CurrentUserContext.getUser();
		 * Logger.info("Session user in listVDC(): "+ user); AbiquoContext
		 * context = CurrentUserContext.getContext(); AbiquoContext context =
		 * contextProp.getContext(); Logger.info("Created context :" + context
		 * );
		 */

		AbiquoContext context = Context.getApiClient(user, password);
		AbiquoUtils.setAbiquoUtilsContext(context);
		Iterable<VirtualDatacenter> vdc_list = AbiquoUtils.getAllVDC();

		for (VirtualDatacenter virtualDatacenter : vdc_list) {
			virtualDatacenter.listVirtualAppliances();
		}
		return vdc_list;

	}

	/**
	 * Lists virtual machines for selected virtual appliance
	 * 
	 * @param id_vdc_param
	 *            The virtual datacentre id
	 * @param id_va_param
	 *            The virtual appliance id
	 */
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
							offers, price);
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
	public static void vmDetails(final Integer vdc, final Integer va,
			final Integer vm) {
		Logger.info(" -----INSIDE PRODUCER VMDETAILS()------");

		String user = session.get("username");
		String password = session.get("password");
		Logger.info("Session user in vmDetails(): " + user);
		if (user != null) {
			AbiquoContext context = Context.getApiClient(user, password);

			try {
				VirtualMachine virtualMachine = AbiquoUtils.getVMDetails(vdc,
						va, vm);
				// context.getCloudService().getVirtualDatacenter(vdc).getVirtualAppliance(va).getVirtualMachine(vm);
				final Integer cpu = virtualMachine.getCpu();
				final Integer ram = virtualMachine.getRam();

				// hd in MBytes
				final Integer hd = (int) (virtualMachine.getHdInBytes() / (1024 * 1024));
				List<HardDisk> harddisk = virtualMachine
						.listAttachedHardDisks();

				VirtualMachineTemplate template = virtualMachine.getTemplate();
				// String template_path = template.getIconUrl();
				String template_name = template.getName();
				String template_path = template.getPath();
				render(vdc, va, vm, cpu, ram, hd, harddisk, template_name,
						template_path, user);
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

	/*
	 * static String setDate(DateParts date) { String year = date.getYear();
	 * String month = date.getMonth(); String day = date.getDay(); String
	 * gendate = year+"-"+month+"-"+day; Logger.info(gendate); return gendate; }
	 */

	/**
	 * Create service catalog entry ( saving to portal database)
	 * 
	 * @param sc_offers
	 * @param icon
	 * @param image
	 * @param offerSubscription
	 * @param date
	 */
	public static void addToServiceCatalog(@Valid final Offer offer,
			@Required final File icon, final File image,
			final OfferPurchased offerSubscription, final VirtualAppliance vapp) {

		if (Validation.hasErrors()) {
			flash.error("Please enter valid data. See errors inline. Icon is required. Max characters for : Short Description - 30 and Long Description - 255 ");
			params.flash();
			Validation.keep();
			/*
			 * configure(sc_offers.getIdVirtualDataCenter_ref(),
			 * sc_offers.getSc_offer_id());
			 */
			render();

		} else {
			extracted();
			String user = session.get("username");
			String password = session.get("password");
			if (user != null) {
				Logger.info("Session user in addToServiceCatalog(): " + user);
				Logger.info(" and  vdc " + offer.getVirtualDatacenter()
						+ "  & va_id : " + offer.getId());
				Logger.info(" lease period "
						+ offerSubscription.getLeasePeriod());
				/*
				 * Logger.info(" start date" +
				 * offerSubscription.getStart_date());
				 * Logger.info(" expiry date" +
				 * offerSubscription.getExpiration_date());
				 * 
				 * Logger.info(" Year : " + date.getYear());
				 * Logger.info(" Year : " + date.getMonth());
				 * Logger.info(" Year : " + date.getDay());
				 * 
				 * String start_date = setDate(date); DateFormat formatter ;
				 * Date datee ;
				 */
				Integer vdc_id_param = offer.getVirtualDatacenter();
				Integer id_va_param = offer.getVirtualAppliance();
				String lease_period = offerSubscription.getLeasePeriod();

				AbiquoContext context = Context.getApiClient(user, password);
				try {
					/*
					 * formatter = new SimpleDateFormat("yyyy-M-d"); datee =
					 * (Date)formatter.parse(start_date);
					 */
					AbiquoUtils.setAbiquoUtilsContext(context);
					VirtualDatacenter virtualDC = AbiquoUtils
							.getVDCDetails(vdc_id_param);
					VirtualAppliance va = AbiquoUtils.getVADetails(
							vdc_id_param, id_va_param);

					Integer id_datacenter = virtualDC.getDatacenter().getId();
					HypervisorType hypervisor = virtualDC.getHypervisorType();

					PrivateNetwork network = PrivateNetwork
							.builder(context.getApiContext()).name("10.80.0.0")
							.gateway("10.80.0.1").address("10.80.0.0").mask(22)
							.build();

					Offer scOffer = new Offer();
					scOffer.setVirtualAppliance(va.getId());
					scOffer.setName(va.getName());
					scOffer.setPrice(AbiquoUtils.getVAPrice(vdc_id_param,
							id_va_param));
					if (icon != null) {
						scOffer.setIconName(offer.getIconName());
						scOffer.setIconName(icon.getName());
						scOffer.setIcon(new Blob());
						scOffer.getIcon().set(new FileInputStream(icon),
								MimeTypes.getContentType(icon.getName()));
					}
					if (image != null) {
						scOffer.setImage(new Blob());
						scOffer.getImage().set(new FileInputStream(image),
								MimeTypes.getContentType(image.getName()));
					}
					scOffer.setShortDescription(offer.getShortDescription());
					scOffer.setDatacenter(id_datacenter);
					scOffer.setHypervisorType(hypervisor.toString());
					scOffer.setDefaultNetworkType(network.getAddress());
					scOffer.setVirtualDatacenter(va.getVirtualDatacenter()
							.getId());
					scOffer.setVirtualAppliance(va.getId());

					if (offerSubscription.getLeasePeriod().equals("100 years"))
						scOffer.setServiceType("Infinite");
					else
						scOffer.setServiceType("Expire");
					scOffer.setVirtualDatacenter(virtualDC.getId());

					Set<Nodes> node_set = new HashSet<Nodes>();
					Nodes node = null;
					List<VirtualMachine> vmlist_todeploy = va
							.listVirtualMachines();
					for (VirtualMachine aVM : vmlist_todeploy) {
						VirtualMachineTemplate vm_template_todeploy = aVM
								.getTemplate();
						// String template_path =
						// vm_template_todeploy.getPath();
						// String vmName = aVM.getName();
						int cpu = aVM.getCpu();
						int ram = aVM.getRam();
						// HD in GBytes
						int hd = (int) (aVM.getHdInBytes() / (1024 * 1024));
						// Long hd = aVM.getHdInBytes();
						String description = aVM.getDescription();
						node = new Nodes();
						node.setId_node(aVM.getId());
						node.setCpu(cpu);
						node.setRam(ram);
						node.setHd(hd);
						node.setIdImage(vm_template_todeploy.getId());
						node.setIcon("icon");
						node.setDescription(description);
						node.setNode_name(vm_template_todeploy.getName());
						Logger.info(" description : "
								+ offer.getLongDescription());
						scOffer.setLongDescription(offer.getLongDescription());
						node_set.add(node);
						Set<Nodes_Resources> node_resource_set = new HashSet<Nodes_Resources>();

						List<HardDisk> attached_disks = aVM
								.listAttachedHardDisks();
						for (HardDisk hdisk : attached_disks) {
							// HardDisk hdisk = attacheddisks_it.next();
							Long size = hdisk.getSizeInMb();
							Integer sequence = hdisk.getSequence();
							Logger.info(" hard disk sequence :" + sequence);
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
					scOffer.setState("PUBLISHED");
					// OfferPurchased offerSub = new OfferPurchased();
					// offerSub.setOffer(scOffer);
					// offerSub.setStart_date(datee);
					scOffer.setDefaultServiceLevel(virtualDC.getName());
					scOffer.setDefaultLeasePeriod(lease_period);
					scOffer.setId(offer.getId());
					scOffer.save();
					Logger.info("-----------EXITING ADDTOSERVICECATALOG()------------");
					render(user);
					// listVDC();
				} catch (Exception e) {

					Logger.warn(e,
							"EXCEPTION OCCURED IN addToServiceCAtalog()",
							vdc_id_param);
					flash.error("Entry already exists");
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
	}

	private static void extracted() {
		Logger.info(" -----INSIDE PRODUCER ADDTOSERVICECATALOG()------");
	}

	public static void configure(final Integer vdc_id_param,
			final Integer id_va_param) {
		Logger.info(" -----INSIDE PRODUCER CONFIGURE()------");

		String user = session.get("username");
		String password = session.get("password");
		if (user != null) {
			VirtualDatacenter virtualDC = null;
			VirtualAppliance va = null;
			List<VirtualMachine> vmList = null;

			AbiquoContext context = Context.getApiClient(user, password);
			try {
				AbiquoUtils.setAbiquoUtilsContext(context);
				virtualDC = AbiquoUtils.getVDCDetails(vdc_id_param);

				if (virtualDC != null) {
					Logger.info(" virtualDC  : " + virtualDC.getName());
					va = AbiquoUtils.getVADetails(vdc_id_param, id_va_param);
					if (va != null) {
						Logger.info(" va  : " + virtualDC.getName());
						vmList = va.listVirtualMachines();
					}

					// Price
					final String price = AbiquoUtils.getVAPrice(vdc_id_param,
							id_va_param);
					Logger.info(" -----EXITING PRODUCER CONFIGURE()------");
					render(va, virtualDC, vmList, user, price);
				} else {
					flash.error("Unable to retrieve virtual datacenter");
					Producer.poe();
				}

			} catch (Exception e) {
				flash.error("Oops !!!.........Unable to retrieve virtual datacenter");
				Producer.poe();

			} finally {
				flash.clear();
				if (context != null) {
					context.close();
				}
			}
		} else {

			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
	}

}
