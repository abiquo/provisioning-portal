package controllers;

import java.util.List;

import javax.persistence.Query;

import models.MKT_Configuration;
import models.sc_offer;
import play.db.jpa.JPA;

public class ConsumerDAO {
	public static List<sc_offer> getPublishedOffers ()
	{
			Query query1 = JPA.em().createNamedQuery("getAllOffersByState");
			query1.setParameter(1, "PUBLISHED");
			List<sc_offer> listOffers = query1.getResultList();
			return listOffers;
	}	
	public static List<sc_offer> getPublishedOffersByEnt( final Integer enterprise_id)
	{
			Query query1 = JPA.em().createNamedQuery("getAvailableOffersByState");
			query1.setParameter(1, enterprise_id);
			query1.setParameter(2, "PUBLISHED");
			List<sc_offer> listOffers = query1.getResultList();
			return listOffers;
	}		
	
}
