package controllers;


import java.util.List;

import javax.persistence.Query;

import models.MKT_Configuration;
import models.UserDetails;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.enterprise.Role;
import org.jclouds.abiquo.domain.enterprise.User;
import org.jclouds.abiquo.features.services.AdministrationService;
import org.jclouds.rest.AuthorizationException;

import play.Logger;
import play.cache.Cache;
import play.data.validation.Required;
import play.db.jpa.JPA;
import play.mvc.Before;
import play.mvc.Controller;
import portal.util.Context;

public class Login extends Controller{

	public static void login_page()
	{
		render();
	}
	
	public static void logout()
	{
		Logger.info("-----INSIDE LOGOUT()-----");
		session.remove("username");
		session.remove("password");
		Cache.delete(session.getId());
		login_page();
		Logger.info("-----EXITING LOGOUT()-----");
				
	}
	@Before(only = {"connect"}) 
	static void createMKTConfi()
	{
		Query query = JPA.em().createNamedQuery("getMktConfi");
		query.setParameter(1, 1);
		List<MKT_Configuration> result= query.getResultList();
		if (result.size() == 0)
		{
		MKT_Configuration mkt1 = new MKT_Configuration();
		mkt1.setMkt_deploy_enterprise("PORTAL DEPLOY");
		mkt1.setMkt_deploy_pw( "demo");
		mkt1.setMkt_deploy_user("demo-deploy");
		mkt1.save();
		}
	}
	public static void connect(@Required String username, @Required String password)
	{
		if(validation.hasErrors()) {
	        flash.error(" Both Name and password are required!");
	        login_page();
	    }
		else 
		{
			Logger.info("-------------- INSIDE LOGIN.CONNECT()--------------");
			
				session.put("username", username);
				session.put("password", password);
		
		AbiquoContext context = Context.getContext(username,password);
		try{
			Logger.info("context: " + context );
			if (context !=null)
			{
				AdministrationService adminService = context.getAdministrationService();
				if (adminService != null)
				{
					User currentUser = adminService.getCurrentUserInfo();
					String useremail = currentUser.getEmail();
					session.put("email", useremail);
					if (currentUser!=null)
					{
					Role role=	currentUser.getRole();
					Logger.info("Role of user" + role);
				
						if (role.getName().contentEquals("CLOUD_ADMIN") )
									Producer.subscribedOffers();
									
				else 
						Consumer.ServiceCatalog();
			}}
				
				}
			
			else 
			{
				flash.error("Unable to get the context");
				login_page();
			}
		}
		catch(AuthorizationException ae)
		{
			ae.printStackTrace();
			flash.error(" Unauthorized User");
			login_page();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			flash.error("Server Unreachable");
			login_page();
		}
		}
	}
}
