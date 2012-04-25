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



    }
    
    

}

