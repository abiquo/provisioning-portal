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
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import models.Offer;
import models.OfferPurchased;
import play.Logger;
import play.db.jpa.Blob;
import play.db.jpa.JPA;
import play.libs.MimeTypes;
import play.mvc.Controller;
import play.mvc.Finally;

public class ProducerLocal extends Controller {

	public static void subscribedOffers() {

		Logger.info(" -----INSIDE PRODUCER SUBSCRIBEDOFFERS()------");
		String user = session.get("username");
		if (user != null) {
			List<OfferPurchased> resultSet = null;
			try {
				resultSet = ProducerDAO
						.getSubscribedOffersGroupByServiceLevels();
				Logger.info(" -----EXITING PRODUCER SUBSCRIBEDOFFERS()------");
				render(resultSet, user);
			} catch (Exception e) {
				Logger.warn(
						e,
						"Exception occured retrieving offers . No of offers retrieved : ",
						resultSet.size());
			}

		} else {

			flash.error("You are not connected.Please Login");
			Login.login_page();
		}

	}

	public static void displayOffer(final String service_level) {
		Logger.info(" -----INSIDE PRODUCER DISPLAYOFFER()------");
		Logger.info(" Service_level " + service_level);
		String user = session.get("username");
		if (user != null) {
			List<OfferPurchased> resultSet = ProducerDAO
					.getSubscribedOffersGroupByServiceLevels();
			List<Offer> resultSet1 = ProducerDAO
					.getSubscribedOffers(service_level);
			Logger.info(" -----INSIDE PRODUCER DISPLAYOFFER()------");
			render("/ProducerLocal/subscribedOffers.html", resultSet,
					resultSet1, user);
		} else {

			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
	}

	/* disable the selected offer */
	public static void disableOffer(final Integer scOfferId) {
		Logger.info(" -----INSIDE PRODUCER DISABLEOFFER()------");
		Logger.info(" Offer Id to delete : " + scOfferId);

		Offer offerToDelete = Offer.findById(scOfferId);
		offerToDelete.delete();

		Logger.info(" Offer deleted ");
		Logger.info(" -----EXITING PRODUCER DISABLEOFFER()------");
		// Producer.poe();
		render("/Producer/saveConfigure.html");
	}

	/**
	 * configure offer existing in portal database
	 * 
	 * @param sc_offer_id
	 */
	public static void configureExistingOffer(final Integer sc_offer_id) {
		Logger.info("------ INSIDE CONFIGURE EXISTING OFFER -------");
		String user = session.get("username");
		if (user != null) {

			if (sc_offer_id == null) {
				flash.error("Oops!! Unable to get Offer . Please try again ");
				Producer.poe();

			} else {
				Logger.info(" sc_offer_Id to configure existing offer : "
						+ sc_offer_id);
				try {
					List<Offer> scOffer = ProducerDAO
							.getOfferDetails(sc_offer_id);

					/*
					 * Query query1 =JPA.em().createNativeQuery(
					 * "select * from sc_offers_subscriptions where sc_offer_sc_offer_id = ?1"
					 * ,sc_offers_subscriptions.class);
					 * query1.setParameter(1,sc_offer_id);
					 * List<sc_offers_subscriptions> subscribedOffers =
					 * query1.getResultList();
					 */

					/*
					 * List<OfferPurchased> subscribedOffers = ProducerDAO
					 * .getSubscribedOfferGivenOfferId(sc_offer_id); Date
					 * expireDate = null; Date startDate = null; String
					 * lease_period = null; for (OfferPurchased subscribedOffer
					 * : subscribedOffers) { expireDate =
					 * subscribedOffer.getExpiration(); startDate =
					 * subscribedOffer.getStart(); lease_period =
					 * subscribedOffer.getLeasePeriod();
					 * Logger.info(" expire date for selected  offer " +
					 * expireDate);
					 * Logger.info(" start date for selected  offer " +
					 * startDate);
					 * Logger.info(" Lease Period for selected  offer " +
					 * lease_period);
					 * 
					 * }
					 */
					Logger.info("------ EXITING CONFIGURE EXISTING OFFER -------");
					render(scOffer, user);
				}

				catch (Exception e) {
					Logger.warn(e,
							"EXCEPTION OCCURED IN configureExistingOffer ",
							sc_offer_id);
				}
			}
		} else {

			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
	}

	/**
	 * Save the configuration for selected offer
	 * 
	 * @param sc_offers
	 * @param icon
	 * @param image
	 */
	public static void saveConfigure(final Offer offer, final File icon,
			final File image) {
		String user = session.get("username");
		if (user != null) {
			Logger.info("-----------INSIDE SAVECONFIGURE()------------");
			Logger.info("------ saveConfigure() id------- " + offer.getId());
			Logger.info("------ saveConfigure() Offer name ------- "
					+ offer.getName());
			Logger.info("------ saveConfigure() short description------- "
					+ offer.getShortDescription());
			Logger.info("------ saveConfigure() description------- "
					+ offer.getLongDescription());

			Logger.info("------ saveConfigure() icon ----" + icon);
			Logger.info("------ saveConfigure() image ----" + image);
			try {
				Offer scOffer = Offer.findById(offer.getId());

				scOffer.setId(offer.getId());
				scOffer.setName(offer.getName());
				if (icon != null) {
					scOffer.setIcon(new Blob());
					scOffer.getIcon().set(new FileInputStream(icon),
							MimeTypes.getContentType(icon.getName()));
					scOffer.setIconName(icon.getName());
				}
				if (image != null) {
					scOffer.setImage(new Blob());
					scOffer.getImage().set(new FileInputStream(image),
							MimeTypes.getContentType(image.getName()));
				}
				scOffer.setShortDescription(offer.getShortDescription());
				scOffer.setLongDescription(offer.getLongDescription());
				scOffer.setDefaultLeasePeriod(offer.getDefaultLeasePeriod());
				scOffer.save();
				scOffer.refresh();
				// Helper.displayIcon(scOffer.getSc_offer_id());

				/*
				 * List<OfferPurchased> subscribedOffers = ProducerDAO
				 * .getSubscribedOfferGivenOfferId(offer.getId()); for
				 * (OfferPurchased subOffer : subscribedOffers) { /*
				 * subOffer.setExpiration_date(subscription.getExpiration_date
				 * ()); subOffer.setStart_date(subscription.getStart_date());s
				 * subOffer.setLeasePeriod(subscription.getLeasePeriod());
				 * subOffer.save(); }
				 */

				Logger.info("-----------EXITING SAVECONFIGURE()------------");
				render(user);
			} catch (FileNotFoundException e) {
				Logger.info("Icon /Image File not found");
			}
		} else {

			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
	}

	public static void admin() {

		String user = session.get("username");
		if (user != null) {
			render(user);
		} else {

			flash.error("You are not connected.Please Login");
			Login.login_page();
		}
	}

	@Finally
	static void clearFlash() {
		flash.clear();
	}

}
