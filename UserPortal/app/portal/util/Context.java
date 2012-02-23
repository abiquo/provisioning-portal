package portal.util;

import play.*;
import play.jobs.Job;
import play.mvc.*;

import java.util.*;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.AbiquoContextFactory;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.enterprise.User;
import org.jclouds.abiquo.features.services.AdministrationService;
import org.jclouds.abiquo.features.services.CloudService;
import org.jclouds.abiquo.handlers.AbiquoErrorHandler;
import org.jclouds.abiquo.predicates.enterprise.UserPredicates;
import org.jclouds.http.HttpResponseException;



public class Context {
	
	/*private  AbiquoContext context;
	private String username;
	private String password;
	public Context(String username, String password)
	{
		this.username=username;
		this.password=password;
		Properties props = new Properties();
		props.put("abiquo.endpoint", "http://67.111.53.253/api");
		context = new AbiquoContextFactory().createContext(username ,password ,props);
	}
	private void setContext(AbiquoContext context)
	{
		this.context=context;
	}*/
	
	public static final AbiquoContext getContext(String username, String password) 
    {
		Properties props = new Properties();
		props.put("abiquo.endpoint", "http://67.111.53.253/api");
		AbiquoContext context = new AbiquoContextFactory().createContext(username ,password ,props);
    	return context;
    }
	
		
	
}