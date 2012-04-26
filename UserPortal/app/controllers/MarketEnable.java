package controllers;

import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import models.MKT_Configuration;
import models.mkt_enterprise_view;
import models.sc_offer;
import models.sc_offers_subscriptions;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.features.services.AdministrationService;

import play.Logger;
import play.data.validation.Valid;
import play.db.jpa.JPA;
import play.mvc.Before;
import play.mvc.Controller;
import portal.util.AbiquoUtils;
import portal.util.Context;

/**
 * 
 * @author Harpreet Kaur
 * Producer creates service cataolog entries. MarketEnable defines the service catalog offers for each enterprise.
 * Example - 10 offers available in Servivce catalog. After MarketEnablement : enterprise 1 user's can see 5 offers 
 * whereas enterprise 2 user's can see  only 2 offers.
 */
public class MarketEnable extends Controller {

	@Before
    static void checkAuthentification() {
        Logger.info(session.get("username") );
    }
	
	/**
	 * Lists all enterprises for market enablement
	 */
	public static void marketEnable()
	{
				String user =session.get("username");
				String password =session.get("password");
				AbiquoContext context = Context.getContext(user,password);
				if ( context  !=null)
				{
						AbiquoUtils.setAbiquoUtilsContext(context);
						 try
						 {
							 	   Iterable<Enterprise> enterpriseList = AbiquoUtils.getAllEnterprises();
							 	   render(enterpriseList,user);
						 }
						 catch(Exception e)
						 {
								flash.error("Unable to create context");
								render();
								e.printStackTrace();
								
						}
						 finally
						 {
							 flash.clear();
							 if ( context != null)
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
	 * Displays configure form to enalble market 
	 * @param enterprise_id
	 * @param enterprise_name
	 */
	public static void enable( Integer enterprise_id , String enterprise_name)
	{
		Logger.info("------------------------- INSIDE Enable()-----------------");
		String user =session.get("username");
		String password =session.get("password");
		AbiquoContext context = Context.getContext(user,password);
		if ( context  !=null)
		{
		try {
					List<MKT_Configuration> resultSet1 = MarketDAO.getMKTConfiguration(enterprise_id);
					
				  	if ( resultSet1.size() == 0 )
					  {
						  	AbiquoUtils.setAbiquoUtilsContext(context);
							Iterable<Enterprise> enterpriseList = AbiquoUtils.getAllEnterprises();
							render(enterprise_id, enterprise_name, enterpriseList,user);
					  }
					  else 
					  {
						  flash.error(" Market alredy configured. ");
						  marketEnable();
					  }
					
		}
		finally 
		{
			 flash.clear();
			 if ( context != null)
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
	 * Saves the market configurations : deploy enterprise, username and password
	 * @param mkt
	 */
	public static void saveMKTConfiguration( @Valid MKT_Configuration mkt)
	{
		 if(validation.hasErrors())
		 {
			   flash.error("Please fill in required fields");
		       params.flash(); 
		       validation.keep(); 
		       enable(mkt.getEnterprise_id(),mkt.getEnterprise_name());
		  }
		
		 Logger.info("------------------------- INSIDE SAVEMKT()-----------------");
		 Logger.info("Enterprise id :  " + mkt.getEnterprise_id() + " Enterprise Name  :  " + mkt.getEnterprise_name() + " Deploy username  :  " + mkt.getMkt_deploy_user() + " Deploy username  :  " 
					+ mkt.getMkt_deploy_user() + " Deploy Enterprise  :  " + mkt.getDeploy_enterprise_id());
		
		 MKT_Configuration market = new MKT_Configuration();
					market.setEnterprise_id(mkt.getEnterprise_id());
					market.setEnterprise_name(mkt.getEnterprise_name());
					//market.setMkt_deploy_enterprise(mkt.getMkt_deploy_enterprise());
					market.setMkt_deploy_pw(mkt.getMkt_deploy_pw());
					market.setMkt_deploy_user(mkt.getMkt_deploy_user());
					market.setDeploy_enterprise_id(mkt.getDeploy_enterprise_id());
		market.save();
		marketEnable();
		
		Logger.info("------------------------- EXITING  SAVEMKT()-----------------");
	}
	
	/**
	 * Publish offers per enterprise.
	 * @param enterprise_id
	 * @param enterprise_name
	 */
	public static void publishMarket( Integer enterprise_id , String enterprise_name)
	{
		Logger.info("------------------------- INSIDE publishMarket()-----------------");
		Logger.info("Enterprise_id " + enterprise_id + "Enterprise_name " + enterprise_name);
		String user =session.get("username");
		if ( user  !=null)
		{
		List<MKT_Configuration> resultSet1 = MarketDAO.getMKTConfiguration(enterprise_id);
		
		/* check if market is enabled. Publish offers only if market is enabled */
	  	if ( resultSet1.size() > 0 )
		  {
			  	List<sc_offers_subscriptions> resultSet = ProducerDAO.getSubscribedOffersGroupByServiceLevels();
			  	Logger.info("ResultSet " + resultSet.size());
			  	Logger.info("------------------------- EXITING publishMarket()-----------------");
			  	render(enterprise_id, enterprise_name, resultSet, user);
		  }
		  else 
		  {
			  flash.error(" To continue configure the Market ");
			  enable(enterprise_id, enterprise_name);
		  }
		}
		else 
			{
					flash.error("You are not connected.Please Login");
					Login.login_page();
			}
	}
	/**
	 * Displays available Service level and offers .
	 * @param service_level
	 * @param enterprise_id
	 * @param enterprise_name
	 */
	public static void publishOffersPerMarket(String service_level, Integer enterprise_id , String enterprise_name){
		  Logger.info(" -----------------INSIDE PRODUCER publishOffersPerMarket()------");
		  String user =session.get("username");
		  Logger.info("Enterprise id " + enterprise_id + " Service_level " + service_level +" Enterprise_name " + enterprise_name);
		 if ( user != null )  {
		  List<sc_offers_subscriptions> resultSet = ProducerDAO.getSubscribedOffersGroupByServiceLevels();
		  List<sc_offers_subscriptions> resultSet1 = ProducerDAO.getSubscribedOffers(service_level);
		  List<sc_offer> resultSet4 = ProducerDAO.getSubscribedOffers1(service_level);
		  List<mkt_enterprise_view> resultSet3 = MarketDAO.getOffersForEnterprise(enterprise_id, service_level);
		 
		  Logger.info("Resultset for service levels : " + resultSet);
		  Logger.info("Resultset1 for VDC offers  : " + resultSet1);
		  Logger.info("Resultset3 offers published : " + resultSet3);
		  Logger.info("Resultset4 offers published : " + resultSet4);
		  Logger.info(" ---------------------EXITING PRODUCER publishOffersPerMarket()------");
		  render("/MarketEnable/publishMarket.html",resultSet,resultSet1,resultSet3 ,resultSet4, user ,enterprise_id, enterprise_name);
	}
	else 
		{
				flash.error("You are not connected.Please Login");
				Login.login_page();
		}
	}
	
	
	/**
	 * Save Service offers enabled for enterprise.
	 * @param enterprise_id
	 * @param scOffer
	 * @param service_level
	 * @param enterprise_name
	 */
	public static void saveMarketView(Integer enterprise_id, List<Integer> scOffer, String service_level,String enterprise_name)
	{
		try {
					Logger.info("------------------------- INSIDE saveMarketView()-----------------");
					if ( scOffer == null )
					{
						flash.error("No offers selected ");
						publishMarket( enterprise_id ,  enterprise_name);
					}
					else 
					{
						Logger.info(scOffer.size() + " offer(s) to be enabled for enterprise with id: " + enterprise_id + " and name :" + enterprise_name + " and  Service level :" + service_level);
						Iterator<Integer> scOffer_it = scOffer.iterator();
							for ( int i= 0 ;i< scOffer.size() ; i++)
							{
								
								while(scOffer_it.hasNext())
								{
										Integer sc_offer_id = scOffer_it.next();
										mkt_enterprise_view mktView = new mkt_enterprise_view();
										mktView.setEnterprise_id(enterprise_id);
										mktView.setSc_offer_id(sc_offer_id);
										mktView.setService_level(service_level);
										mktView.save();
								}
								Logger.info("Market View Updated ");
							}
						Logger.info("------------------------- EXITING saveMarketView()-----------------");
						publishMarket( enterprise_id ,  enterprise_name);
					}
					
	 	}
	
	finally {
		flash.clear();
	}
	
	}
}
