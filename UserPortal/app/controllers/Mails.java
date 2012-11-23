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

import models.Deploy_Bundle;
import models.Deploy_Bundle_Nodes;
import models.MKT_Configuration;
import models.OfferPurchased;
import models.Offer;
import models.OfferPurchased;

import org.apache.commons.mail.EmailAttachment;
//import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.VirtualAppliance;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.enterprise.User;
import play.mvc.Controller;

public class Mails extends Mailer {

	/**
	 * Confirmation Email sent to user after deployment success.
	 * 
	 * @param vncPort
	 * @param vncAddress
	 * @param password
	 * @param name
	 * @param offerName
	 * @param useremail
	 * @param exp_date
	 */
	public static void sendEmail(OfferPurchased op, VirtualAppliance vapp) {

		Logger.info("INSIDE MAILS SENDEMAIL()....");
		setSubject("Abiquo Confirmation");
		addRecipient(op.getUser().getEmail());
		setFrom("Admin <provisioning-portal@abiquo.com>");
		Logger.info("SENDING EMAIL TO  ...." + op.getUser().getEmail());
		List<VirtualMachine> vms = vapp.listVirtualMachines();
		send(op, vms);
	}

	/**
	 * Confirmation Email sent to user after deployment success.
	 * 
	 * @param vncPort
	 * @param vncAddress
	 * @param password
	 * @param name
	 * @param offerName
	 * @param useremail
	 * @param exp_date
	 */
	public static void sendExtendEmail(OfferPurchased op) {

		Logger.info("INSIDE MAILS SENDEMAIL()....");
		setSubject("Abiquo Confirmation");
		addRecipient(op.getUser().getEmail()); 
		setFrom("Admin <provisioning-portal@abiquo.com>");
		Logger.info("SENDING EMAIL TO  ...." + op.getUser().getEmail());

		@SuppressWarnings("deprecation")
		final String urlserver = Http.Request.current().getBase()
				+ "/consumer/extendOffer?purchasedOfferId="
				+ op.getEntityId().toString();
		send(op, urlserver);
	}

	public static void sendExpiredEmail(OfferPurchased op) {

		Logger.info("INSIDE MAILS SENDEMAIL()....");
		setSubject("Abiquo Confirmation");
		addRecipient(op.getUser().getEmail()); 
		setFrom("Admin <provisioning-portal@abiquo.com>");
		Logger.info("SENDING EMAIL TO  ...." + op.getUser().getEmail());
		send(op);
	}

	/**
	 * Email sent if deployment fails.
	 * 
	 * @param offerName
	 * @param name
	 * @param useremail
	 */
	public static void sendFailureEmail(String offerName, String name,
			String useremail) {

		Logger.info("INSIDE Mails.sendFailureEmail()....");
		setSubject("Abiquo Confirmation");
		addRecipient(useremail);
		setFrom("Admin <provisioning-portal@abiquo.com>");
		Logger.info("SENDING EMAIL TO  ...." + useremail);
		send(name, offerName, useremail);
	}

	/**
	 * Email sent with message
	 * 
	 * @param offerName
	 * @param name
	 * @param useremail
	 */
	public static void sendEmailMessage(String message, String offerName, String name,
			String useremail, String expDate) {

		Logger.info("INSIDE Mails.sendFailureEmail()....");
		setSubject("Abiquo Confirmation");
		addRecipient(useremail);
		setFrom("Admin <provisioning-portal@abiquo.com>");
		Logger.info("SENDING EMAIL TO  ...." + useremail);
		send(message, name, offerName, useremail, expDate);
	}

	/**
	 * Updation made after deployment.
	 * 
	 * @param vdc_id
	 *            Deployed virtual machine's Virtual datacenter
	 * @param vapp
	 *            Deployed virtual machine's virtual appliance
	 * @param vm_id
	 *            Deployed virtual machine id
	 * @param vmName
	 *            Deployed virtual machine name
	 */
	public static void updateUserConsumption_onSuccess(Integer vdc_id,
			VirtualAppliance vapp, Integer vm_id, VirtualMachine vmName) {
		Logger.info(" Inside Consumer.updateDeployBundleNode() ......");
		Logger.info(" CREATED VM : " + vmName);
		try {
			/* After deployment , JPA em() and session variables are lost */
			if (JPA.local.get() == null) {
				JPA.local.set(new JPA());
				JPA.local.get().entityManager = JPA.newEntityManager();
			}

			String offerName = vapp.getName();
			Integer vapp_id = vapp.getId();
			Enterprise enterprise = vapp.getEnterprise();
			Logger.info(" vapp enterprise :" + enterprise);
			VirtualMachine vm = Helper.getVMDetails(vdc_id, vapp_id, vm_id,
					enterprise);

			/* get virtual machine detials : password, port, address */
			String vmpassword = vm.getPassword();
			int port = vm.getVncPort();
			String ip = vm.getVncAddress();
			String name = vm.getNameLabel();
			Logger.info(" PASSWORD : " + vmpassword + "  IP :" + ip
					+ "  PORT :" + port);

			/* update portal database */
			EntityManager em = JPA.em();
			Query query = em
					.createQuery(" select p from Deploy_Bundle_Nodes as p where p.node_id = ?1");
			query.setParameter(1, vm_id);
			Integer id = null;
			List<Deploy_Bundle_Nodes> bundleNodes = query.getResultList();
			for (Deploy_Bundle_Nodes node : bundleNodes) {
				id = node.getIdbundle_nodes();

			}
			Logger.info("id :" + id);

			Deploy_Bundle_Nodes nodes = Deploy_Bundle_Nodes.findById(id);
			Logger.info("nodes ::; " + nodes);
			em.getTransaction().begin();
			Logger.info(vmpassword);
			nodes.setVdrp_password(vmpassword);
			Logger.info(ip);
			nodes.setVdrpPort(port);
			nodes.setVdrpIP(ip);
			nodes.save();
			em.getTransaction().commit();
			/* get user email id and expiration date */
			Query query1 = em
					.createQuery(" select p from User_Consumption as p where p.vdc_id = ?1");
			query1.setParameter(1, vdc_id);
			String emailID = null;
			Date exp_date = null;
			List<OfferPurchased> consumption = query1.getResultList();
			for (OfferPurchased userCon : consumption) {
				emailID = userCon.getUser().getEmail();
				exp_date = userCon.getExpiration();

			}
			Logger.info(" preparaing to send mail.....");
			//Mails.sendEmail(port, ip, vmpassword, name, offerName, emailID,exp_date);
		} finally {
			JPA.local.get().entityManager.close();
			JPA.local.remove();
		}

	}

	/**
	 * Update( i.e delete) database entries if deployment fails. Sends failure
	 * email to user.
	 * 
	 * @param vdc_id
	 * @param vapp
	 * @param vm
	 */
	public static void updateUserConsumption_onFailure(Integer vdc_id,
			VirtualAppliance vapp, VirtualMachine vm) {
		Logger.info(" Inside Consumer.updateDeployBundleNode() ......");
		Logger.info(" CREATED VM : " + vm);
		try {
			if (JPA.local.get() == null) {

				Logger.info("NULL JAP");
				JPA.local.set(new JPA());
				JPA.local.get().entityManager = JPA.newEntityManager();
			}

			String offerName = vapp.getName();
			String name = vm.getNameLabel();
			/* get user email id */
			EntityManager em = JPA.em();
			Query query1 = em
					.createQuery(" select p from User_Consumption as p where p.vdc_id = ?1");
			query1.setParameter(1, vdc_id);

			String emailID = null;
			String user_consumption_id = null;
			List<OfferPurchased> consumption = query1.getResultList();
			for (OfferPurchased userCon : consumption) {
				emailID = userCon.getUser().getEmail();
				user_consumption_id = userCon.getUser().getIdAbiquo()
						.toString();

			}
			/*
			 * delete that entry from database and send email to user about
			 * deployment failure
			 */
			em.getTransaction().begin();
			OfferPurchased userConsumption = JPA.em().find(
					OfferPurchased.class, user_consumption_id);
			userConsumption.delete();
			em.getTransaction().commit();
			Logger.info(" preparaing to send mail.....");
			Mails.sendFailureEmail(offerName, name, emailID);
		} finally {
			JPA.local.get().entityManager.close();
			JPA.local.remove();
		}

	}

}
