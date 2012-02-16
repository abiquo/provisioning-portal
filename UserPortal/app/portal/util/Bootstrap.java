package portal.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.AbiquoContextFactory;

import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.mvc.Before;

@OnApplicationStart

public class Bootstrap extends Job {

	//private static UserInfo ui;

    public void doJob() {

        // Load all properties in rest.properties using the appropriate ClassLoader

        Properties props = new Properties();

        try {
			props.load(Play.classloader.getResourceAsStream("rest.properties"));
			Enumeration e = props.propertyNames();

	        while (e.hasMoreElements()) {

	            String key = (String) e.nextElement();
	            System.setProperty(key, props.getProperty(key));

	        }
	      
			
		} catch (IOException e1) {
						e1.printStackTrace();
		}




        // Set all loaded properties as system properties

        

    }
    
    /*public static final AbiquoContext getContext() 
    {
		Properties props = new Properties();
		props.put("abiquo.endpoint", "http://67.111.53.253/api");
		AbiquoContext context = new AbiquoContextFactory().createContext(ui.getUsername(),ui.getPassword(),props);
    	return context;
    }
    
	 @Before 
		static void setUserInfo(String username, String password)
		{
			UserInfo ui = new UserInfo (username,password);
			System.out.println("USERBAME " + username + "PASWORD"+  password);
		}*/

}

