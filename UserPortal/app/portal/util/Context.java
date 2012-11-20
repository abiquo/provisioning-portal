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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.jclouds.ContextBuilder;
import org.jclouds.abiquo.AbiquoApiMetadata;
import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;

import play.Play;

public class Context {

	/** Context */
	private static AbiquoContext context;
	private static String userInContext;

	// ----------------------------------------------------------------------------
	// --- JCLOUDS API PROPER METHODS TO IVOKE API!
	// ----------------------------------------------------------------------------
	public static AbiquoContext getApiClient(final String username,
			final String password) {
		if (username != null && password != null) {
			Properties props = new Properties();
			// load a properties file
			try {
				props.load(new FileInputStream(Play
						.getFile("conf/config.properties")));
				props.put("abiquo.endpoint", props.getProperty("api"));
				// String token = generateToken(userSession);

				// Properties props = new Properties();
				// We will use token based authentication
				// props.setProperty(AbiquoProperties.CREDENTIAL_IS_TOKEN,
				// "true");
				// Do not retry methods that fail with 5xx error codes
				props.put("jclouds.max-retries", "0");
				// Custom timeouts in ms
				// External storage operations take a while in some storage
				// devices
				props.put("jclouds.timeouts.CloudClient.createVolume", "90000");
				props.put("jclouds.timeouts.CloudClient.updateVolume", "90000");
				props.put("jclouds.timeouts.CloudClient.replaceVolumes",
						"90000");
				props.put("jclouds.timeouts.CloudClient.deleteVolume", "90000");
				props.put(
						"jclouds.timeouts.CloudClient.makePersistentVirtualMachine",
						"300000");

				context = ContextBuilder.newBuilder(new AbiquoApiMetadata()) //
						.endpoint(props.getProperty("api")) //
						.credentials(username, password) //
						// .modules(ImmutableSet.<Module> of(new
						// SLF4JLoggingModule())) //
						.overrides(props) //
						.build(AbiquoContext.class);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return context;
	}

	public static void closeContext() {
		if (context != null) {
			context.close();
			context = null;
		}
	}

	// public static final AbiquoContext getContext(String username, String
	// password)
	// {
	// AbiquoContext context = null;
	// if ( username != null && password != null)
	// {
	// Properties props = new Properties();
	// //load a properties file
	// try {
	// props.load(new FileInputStream(Play.getFile("conf/config.properties")));
	//
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// props.put("abiquo.endpoint", props.getProperty("api"));
	//
	// //props.put("abiquo.endpoint", "http://67.111.53.253/api");
	// context = new AbiquoContextFactory().createContext(username ,password
	// ,props);
	// }
	// return context;
	// }
}
