package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.persistence.Query;

import models.DateParts;
import models.MKT_Configuration;
import models.Nodes;
import models.Nodes_Resources;
import models.mkt_enterprise_view;
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
import org.jclouds.abiquo.predicates.enterprise.EnterprisePredicates;
import org.jclouds.rest.AuthorizationException;

import com.abiquo.model.enumerator.HypervisorType;

import play.Logger;
import play.data.validation.Error;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.JPA;
import play.db.jpa.JPABase;
import play.libs.MimeTypes;
import play.mvc.Controller;
import portal.util.Context;


public class ProducerLocal extends Controller{

		
	
	public static void subscribedOffers(){
			
			Logger.info(" -----INSIDE PRODUCER SUBSCRIBEDOFFERS()------");
			String user =session.get("username");
			List<sc_offers_subscriptions> resultSet = null;
			try{
					resultSet = ProducerDAO.getSubscribedOffersGroupByServiceLevels();
					Logger.info(" -----EXITING PRODUCER SUBSCRIBEDOFFERS()------");
					render(resultSet,user);
			}
			catch(Exception e)
			{
				Logger.warn(e, "Exception occured retrieving offers. No of offers retrieved : ", resultSet.size());
			}
			
		
	}
	
	public static void displayOffer(String service_level){
		  Logger.info(" -----INSIDE PRODUCER DISPLAYOFFER()------");
		  Logger.info(" Service_level " + service_level);
		  String user =session.get("username");
		 
		  List<sc_offers_subscriptions> resultSet = ProducerDAO.getSubscribedOffersGroupByServiceLevels();
		  List<sc_offers_subscriptions> resultSet1 = ProducerDAO.getSubscribedOffers(service_level);
		  Logger.info(" -----INSIDE PRODUCER DISPLAYOFFER()------");
		  render("/ProducerLocal/subscribedOffers.html",resultSet,resultSet1,user );
	}
	
	/* disable the selected offer */
	public static void disableOffer(Long scOfferId)
	{
		Logger.info(" -----INSIDE PRODUCER DISABLEOFFER()------");
		Logger.info(" Offer Id to delete : " + scOfferId );
				
		sc_offers_subscriptions offerSub = JPA.em().find(sc_offers_subscriptions.class, scOfferId);
		sc_offer sc_offer = offerSub.getSc_offer();
		Integer sc_offer_id=  sc_offer.getSc_offer_id();
		
		Query query = JPA.em().createNativeQuery("delete from mkt_enterprise_view where sc_offer_id = ?1");
		query.setParameter(1, sc_offer_id);
		query.executeUpdate();
		System.out.println(" deleted ");
		offerSub.delete();
		Logger.info(" -----EXITING PRODUCER DISABLEOFFER()------");
		Producer.poe();
		//render("/Producer/saveConfigure.html",user);
		
	}

	/**
	 * configure offer existing in portal database
	 * @param sc_offer_id
	 */
	public static void configureExistingOffer (Integer sc_offer_id)
	{
		
		Logger.info("------ INSIDE CONFIGURE EXISTING OFFER -------");
		String user =session.get("username");
		Logger.info(" sc_offer_Id to configure : " + sc_offer_id );
		 List<sc_offer> scOffer = ProducerDAO.getOfferDetails(sc_offer_id);
		 
		 Query query1 =JPA.em().createNativeQuery("select * from sc_offers_subscriptions where sc_offer_sc_offer_id = ?1",sc_offers_subscriptions.class);
		 query1.setParameter(1,sc_offer_id);
		 List<sc_offers_subscriptions> subscribedOffers = query1.getResultList();
		 Iterator<sc_offers_subscriptions> subscribedOffers_it = subscribedOffers.iterator();
		 Date expireDate = null;
		 Date startDate = null;
		 String lease_period = null;
		 while (subscribedOffers_it.hasNext())
		 {
			 sc_offers_subscriptions subscribedOffer = subscribedOffers_it.next();
			  expireDate = subscribedOffer.getExpiration_date();
			  startDate = subscribedOffer.getStart_date();
			  lease_period = subscribedOffer.getLease_period();
		 }
		 //String expire_date = expireDate.toString();
		 //String start_date = startDate.toString();
			
		Logger.info("------ EXITING CONFIGURE EXISTING OFFER -------");
		 render(scOffer,user,expireDate,startDate,lease_period );
	}

	/**
	 * Save the configuration for selected offer
	 * @param sc_offers
	 * @param icon
	 * @param image
	 */
	public static void saveConfigure (sc_offer sc_offers , File icon, File image  ) 
	{
		String user =session.get("username");
		Logger.info("-----------INSIDE SAVECONFIGURE()------------");
		Logger.info("-----------INSIDE SAVECONFIGURE()------------");
		Logger.info("------ saveConfigure() id------- " + sc_offers.getSc_offer_id());
		Logger.info("------ saveConfigure() Offer name ------- " + sc_offers.getSc_offer_name());
		Logger.info("------ saveConfigure() short description------- " + sc_offers.getShort_description());
		Logger.info("------ saveConfigure() description------- " + sc_offers.getDescription());
		
		Logger.info("------ saveConfigure() icon ----" + icon);
		Logger.info("------ saveConfigure() image ----" + image );
		try{
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
		catch( FileNotFoundException  e ){
			
		}
	}
	
		public static void admin() {
		String user = session.get("username");
		render(user);
	}

}
