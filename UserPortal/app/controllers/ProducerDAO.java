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

import java.util.List;

import javax.persistence.Query;

import models.Offer;
import models.OfferPurchased;
import play.Logger;
import play.db.jpa.JPA;

public class ProducerDAO {

	static List<OfferPurchased> getOffersPurchasedFromUserId(final String userId) {
		Logger.info(" -----INSIDE PRODUCERDAO getOffersPurchasedFromUserId()------");
		
		Query query = JPA.em().createNamedQuery("getOffersPurchasedFromUserId");
		query.setParameter(1,userId);
		List<OfferPurchased> resultSet = query.getResultList();
		Logger.info(" -----EXITING PRODUCERDAO getOffersPurchasedFromUserId()------");
		
		return resultSet;
		
	}
	static List<OfferPurchased>  getSubscribedOffersGroupByServiceLevels(){
		Logger.info(" -----INSIDE PRODUCERDAO getPublishedServiceLevels()------");
		
		Query query = JPA.em().createNamedQuery("getSubscribedOffersGroupByServiceLevels");
		List<OfferPurchased> resultSet = query.getResultList();
		Logger.info(" -----EXITING PRODUCERDAO getPublishedServiceLevels()------");
		
		return resultSet;
	 }
	 
	 static List<Offer>  getSubscribedOffers(String service_level){
			Logger.info(" -----INSIDE PRODUCERDAO getPublishedServiceLevelOffers()------");
			
			Query query = JPA.em().createNamedQuery("getSubscribedOffers");
			query.setParameter(1,service_level);
			List<Offer> resultSet1 = query.getResultList();
			Logger.info("resultSet1 size :" + resultSet1.size());
			Logger.info(" -----EXITING PRODUCERDAO getPublishedServiceLevelOffers()------");
			
			return resultSet1;
	}
	 
	 static  List<Offer>  getOfferDetails(Integer sc_offer_id){
			Logger.info(" -----INSIDE PRODUCERDAO getPublishedOfferDetails()------");
			
			 Query query = JPA.em().createNamedQuery("getOfferDetails");
			 query.setParameter(1,sc_offer_id);
			 List<Offer> scOffer = query.getResultList();
			 Logger.info(" -----EXITING PRODUCERDAO getPublishedOfferDetails()------");
			
			return scOffer;
	}
	 static List<Offer>  getSubscribedOffers1(String service_level){
			Logger.info(" -----INSIDE PRODUCERDAO getPublishedServiceLevelOffers()------");
			
			Query query = JPA.em().createNamedQuery("getAllOffers");
			//query.setParameter(1,service_level);
			List<Offer> resultSet1 = query.getResultList();
			Logger.info("resultSet1 size :" + resultSet1.size());
			Logger.info(" -----EXITING PRODUCERDAO getPublishedServiceLevelOffers()------");
			
			return resultSet1;
	}
	 
	 static List<Offer> groupByVDC_EnterpriseView (Integer enterpriseID)
		{
				Query query = JPA.em().createNamedQuery("groupByVDC_EnterpriseView");
				//Query query = JPA.em().createQuery("select p from sc_offer as p where p.sc_offer_id in ( select s.sc_offer_id from mkt_enterprise_view as s where s.enterprise_id = ?1 ) GROUP BY p.virtualDataCenter_name");
				
				//query.setParameter(1, enterpriseID);
			    List<Offer> result1 = query.getResultList();
			    return result1;
		}
	static List<Offer> getVappListForVDC_EnterpriseView (Integer enterpriseID , String vdc_name_param)
		{
				Query query2 = JPA.em().createNamedQuery("getVappListForVDC_EnterpriseView");
			    query2.setParameter(1,enterpriseID);
			    query2.setParameter(2,vdc_name_param);
				List<Offer> result2= query2.getResultList();
				return result2;
		}
		
	static List<OfferPurchased>  getSubscribedOfferGivenOfferId(Integer sc_offer_id )
	{
		 Query query1 =JPA.em().createNativeQuery("select * from sc_offers_subscriptions where sc_offer_sc_offer_id = ?1",OfferPurchased.class);
		 query1.setParameter(1,sc_offer_id);
		 List<OfferPurchased> subscribedOffers = query1.getResultList();
		 return subscribedOffers;
	}
}
