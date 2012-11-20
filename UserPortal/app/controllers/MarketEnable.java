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
import java.util.List;

import models.MKT_Configuration;
import models.Offer;
import models.OfferPurchased;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.enterprise.Enterprise;

import play.Logger;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.mvc.Before;
import play.mvc.Controller;
import portal.util.AbiquoUtils;
import portal.util.Context;

/**
 * 
 * @author David Lopez Producer creates service cataolog entries. MarketEnable
 *         defines the service catalog offers for each enterprise. Example - 10
 *         offers available in Servivce catalog. After MarketEnablement :
 *         enterprise 1 user's can see 5 offers whereas enterprise 2 user's can
 *         see only 2 offers.
 */
public class MarketEnable extends Controller {

	@Before
	static void checkAuthentification() {
		Logger.info(session.get("username"));
	}

	/**
	 * Lists all enterprises for market enablement
	 */
	public static void marketEnable() {
		String user = session.get("username");
		String password = session.get("password");
		AbiquoContext context = Context.getApiClient(user, password);
		if (context != null) {
			AbiquoUtils.setAbiquoUtilsContext(context);
			try {
				Iterable<Enterprise> enterpriseList = AbiquoUtils
						.getAllEnterprises();

				ArrayList<EnterpriseEnabled> enterpriseEnabledList = new ArrayList<EnterpriseEnabled>();

				for (Enterprise enterprise : enterpriseList) {
					List<MKT_Configuration> resultSet = MarketDAO
							.getMKTConfiguration(enterprise.getId());

					boolean enabled = true;
					if (resultSet.size() == 0) {
						enabled = false;
					}
					EnterpriseEnabled enterpriseEnabled = new EnterpriseEnabled(
							enterprise, enabled);
					enterpriseEnabledList.add(enterpriseEnabled);
				}
				render(enterpriseEnabledList, user);
			} catch (Exception e) {
				flash.error("Unable to create context");
				render();
				// e.printStackTrace();

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

	/**
	 * Displays configure form to enable market
	 * 
	 * @param enterprise_id
	 * @param enterprise_name
	 */
	public static void enable(final Integer enterprise_id,
			final String enterprise_name) {
		Logger.info("------------------------- INSIDE Enable()-----------------");
		String user = session.get("username");
		String password = session.get("password");
		AbiquoContext context = Context.getApiClient(user, password);
		if (context != null) {
			try {
				List<MKT_Configuration> resultSet1 = MarketDAO
						.getMKTConfiguration(enterprise_id);

				if (resultSet1.size() == 0) {
					AbiquoUtils.setAbiquoUtilsContext(context);
					Iterable<Enterprise> enterpriseList = AbiquoUtils
							.getAllEnterprises();
					render(enterprise_id, enterprise_name, enterpriseList, user);
				} else {
					flash.error(" Market alredy configured. ");
					marketEnable();
				}

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

	/**
	 * Saves the market configurations : deploy enterprise, username and
	 * password
	 * 
	 * @param mkt
	 */
	public static void enableMKTConfiguration(final Integer enterprise_id,
			final String enterprise_name) {
		if (Validation.hasErrors()) {
			flash.error("Please fill in required fields");
			params.flash();
			Validation.keep();
			enable(enterprise_id, enterprise_name);
		}

		Logger.info("------------------------- INSIDE SAVEMKT()-----------------");
		Logger.info("Enterprise id :  " + enterprise_id
				+ " Enterprise Name  :  " + enterprise_name);

		String user = session.get("username");
		String password = session.get("password");

		MKT_Configuration market = new MKT_Configuration();
		market.setEnterprise_id(enterprise_id);
		market.setEnterprise_name(enterprise_name);
		// market.setMkt_deploy_enterprise(mkt.getMkt_deploy_enterprise());
		market.setMkt_deploy_pw(password);
		market.setMkt_deploy_user(user);
		market.setDeploy_enterprise_id(enterprise_id);
		market.save();
		marketEnable();

		Logger.info("------------------------- EXITING  SAVEMKT()-----------------");
	}

	/**
	 * Saves the market configurations : deploy enterprise, username and
	 * password
	 * 
	 * @param mkt
	 */
	public static void saveMKTConfiguration(@Valid final MKT_Configuration mkt) {
		if (Validation.hasErrors()) {
			flash.error("Please fill in required fields");
			params.flash();
			Validation.keep();
			enable(mkt.getEnterprise_id(), mkt.getEnterprise_name());
		}

		Logger.info("------------------------- INSIDE SAVEMKT()-----------------");
		Logger.info("Enterprise id :  " + mkt.getEnterprise_id()
				+ " Enterprise Name  :  " + mkt.getEnterprise_name()
				+ " Deploy username  :  " + mkt.getMkt_deploy_user()
				+ " Deploy username  :  " + mkt.getMkt_deploy_user()
				+ " Deploy Enterprise  :  " + mkt.getDeploy_enterprise_id());

		MKT_Configuration market = new MKT_Configuration();
		market.setEnterprise_id(mkt.getEnterprise_id());
		market.setEnterprise_name(mkt.getEnterprise_name());
		// market.setMkt_deploy_enterprise(mkt.getMkt_deploy_enterprise());
		market.setMkt_deploy_pw(mkt.getMkt_deploy_pw());
		market.setMkt_deploy_user(mkt.getMkt_deploy_user());
		market.setDeploy_enterprise_id(mkt.getDeploy_enterprise_id());
		market.save();
		marketEnable();

		Logger.info("------------------------- EXITING  SAVEMKT()-----------------");
	}

	/**
	 * Publish offers per enterprise.
	 * 
	 * @param enterprise_id
	 * @param enterprise_name
	 */
	public static void publishMarket(final Integer enterprise_id,
			final String enterprise_name) {
		Logger.info("------------------------- INSIDE publishMarket()-----------------");
		Logger.info("Enterprise_id " + enterprise_id + "Enterprise_name "
				+ enterprise_name);
		String user = session.get("username");
		if (user != null) {
			List<MKT_Configuration> resultSet1 = MarketDAO
					.getMKTConfiguration(enterprise_id);

			/*
			 * check if market is enabled. Publish offers only if market is
			 * enabled
			 */
			if (resultSet1.size() > 0) {
				List<OfferPurchased> resultSet = ProducerDAO
						.getSubscribedOffersGroupByServiceLevels();
				Logger.info("ResultSet " + resultSet.size());
				Logger.info("------------------------- EXITING publishMarket()-----------------");
				render(enterprise_id, enterprise_name, resultSet, user);
			} else {
				flash.error(" To continue configure the Market ");
				enable(enterprise_id, enterprise_name);
			}
		} else {
			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
	}

	/**
	 * Displays available Service level and offers .
	 * 
	 * @param service_level
	 * @param enterprise_id
	 * @param enterprise_name
	 */
	public static void publishOffersPerMarket(final String service_level,
			final Integer enterprise_id, final String enterprise_name) {
		Logger.info(" -----------------INSIDE PRODUCER publishOffersPerMarket()------");
		String user = session.get("username");
		Logger.info("Enterprise id " + enterprise_id + " Service_level "
				+ service_level + " Enterprise_name " + enterprise_name);
		if (user != null) {
			List<OfferPurchased> resultSet = ProducerDAO
					.getSubscribedOffersGroupByServiceLevels();
			List<Offer> resultSet1 = ProducerDAO
					.getSubscribedOffers(service_level);
			List<Offer> resultSet4 = ProducerDAO
					.getSubscribedOffers1(service_level);

			Logger.info("Resultset for service levels : " + resultSet);
			Logger.info("Resultset1 for VDC offers  : " + resultSet1);
			Logger.info("Resultset4 offers published : " + resultSet4);
			Logger.info(" ---------------------EXITING PRODUCER publishOffersPerMarket()------");
			render("/MarketEnable/publishOfferBack.html", resultSet,
					resultSet1, resultSet4, user, enterprise_id,
					enterprise_name);
		} else {
			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
	}

	/**
	 * Save Service offers enabled for enterprise.
	 * 
	 * @param enterprise_id
	 * @param scOffer
	 * @param service_level
	 * @param enterprise_name
	 */
	public static void saveMarketView(final Integer enterprise_id,
			final List<Integer> scOffer, final List<Boolean> scOfferState,
			final String service_level, final String enterprise_name) {
		try {
			Logger.info("------------------------- INSIDE saveMarketView()-----------------");
			if (scOffer == null) {
				flash.error("No offers selected ");
				publishMarket(enterprise_id, enterprise_name);
			} else {
				Logger.info(scOffer.size()
						+ " offer(s) to be enabled for enterprise with id: "
						+ enterprise_id + " and name :" + enterprise_name
						+ " and  Service level :" + service_level);

				for (int i = 0; i < scOffer.size(); i++) {
					final Integer sc_offer_id = scOffer.get(i);
					final Boolean hasToChangeState = scOfferState.get(i);

					if (hasToChangeState) {
						Offer offer = Offer.findById(sc_offer_id);
						if (offer.getState() == null)
							offer.setState("PUBLISHED");
						else
							offer.setState(null);
						offer.save();
					}
					/*
					 * mkt_enterprise_view mktView = new mkt_enterprise_view();
					 * mktView.setEnterprise_id(enterprise_id);
					 * mktView.setSc_offer_id(sc_offer_id);
					 * mktView.setService_level(service_level); mktView.save();
					 */
				}
				Logger.info("Market View Updated ");
				Logger.info("------------------------- EXITING saveMarketView()-----------------");
				publishMarket(enterprise_id, enterprise_name);
			}
		} finally {
			flash.clear();
		}

	}
}
