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

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import org.jclouds.abiquo.AbiquoContext;

import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.mvc.Before;

@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() {

		// Load all properties in rest.properties using the appropriate
		// ClassLoader

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
