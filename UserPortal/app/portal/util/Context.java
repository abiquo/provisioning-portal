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
package portal.util;

import play.*;
import play.jobs.Job;
import play.mvc.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

	
	public static final AbiquoContext getContext(String username, String password) 
    {
		AbiquoContext context = null;		
		if ( username != null && password != null)
		{
		Properties props = new Properties();
		 //load a properties file
		try {
			props.load(new FileInputStream(Play.getFile("conf/config.properties")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}           
		props.put("abiquo.endpoint", props.getProperty("api"));
		
		//props.put("abiquo.endpoint", "http://67.111.53.253/api");
		context = new AbiquoContextFactory().createContext(username ,password ,props);
		}
    	return context;
    }
}
