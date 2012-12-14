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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.jclouds.abiquo.AbiquoApi;
import org.jclouds.abiquo.AbiquoAsyncApi;
import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.enterprise.Role;
import org.jclouds.abiquo.domain.enterprise.User;
import org.jclouds.abiquo.domain.exception.AbiquoException;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.features.services.AdministrationService;
import org.jclouds.abiquo.predicates.enterprise.RolePredicates;
import org.jclouds.abiquo.predicates.infrastructure.DatacenterPredicates;
import org.jclouds.rest.AuthorizationException;
import org.jclouds.rest.RestContext;

import play.Logger;
import play.Play;
import play.cache.Cache;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.mvc.Controller;
import play.mvc.Http;
import portal.util.Context;
import portal.util.CurrentUserContext;
import portal.util.UAgentInfo;

public class Login extends Controller {
	
	public static void login_agent() {
		Http.Header uaHeader = request.headers.get("user-agent");	
		UAgentInfo uagentInfo = new UAgentInfo(uaHeader.toString(),null);
		if(uagentInfo.detectTierTablet() || uagentInfo.detectTierIphone() || uagentInfo.detectWindowsMobile()) Mobile.login();
		else login_page();
	}

	public static void login_page() {
		session.remove("username");
		session.remove("password");
		Cache.delete(session.getId());			
		render();
	}

	public static void logout() {
		Logger.info("-----INSIDE LOGOUT()-----");
		session.remove("username");
		session.remove("password");
		session.clear();
		Context.closeContext();
		Cache.delete(session.getId());
		login_page();
		Logger.info("-----EXITING LOGOUT()-----");

	}

	public static void connect(@Required final String username,
			@Required final String password) {
		if (Validation.hasErrors()) {
			flash.error("Username and password required");
			login_page();
		} else {
			Logger.info("-------------- INSIDE LOGIN.CONNECT()--------------");

			session.put("username", username);
			session.put("password", password);
			AbiquoContext context = Context.getApiClient(username, password);

			// Cache.set(session.getId() + "-context", context, "30mn");
			// AbiquoUtils.setContext(context);
			// PortalContext userContext = new PortalContext();
			// userContext.setContext(context);
			CurrentUserContext.setContext(context);
			CurrentUserContext.setUser(username);

			try {
				Logger.info("context: " + context);

				if (context != null) {

					AdministrationService adminService = context
							.getAdministrationService();
					if (adminService != null) {
						User currentUser = adminService.getCurrentUser();
						flash.put("currentUserInfo", currentUser);

						String useremail = currentUser.getEmail();
						session.put("email", useremail);
						if (currentUser != null) {
							Role role = currentUser.getRole();
							Logger.info("Role of user" + role);

							if (role.getName().contentEquals("CLOUD_ADMIN")) {
								ProducerLocal.admin();
							} else {
								Consumer.ServiceCatalog();
							}
						}
					}

				}

				else {
					flash.error("Unable to get the context");
					login_page();
				}
			} catch (AuthorizationException ae) {
				// ae.printStackTrace();
				flash.error("Unauthorized User");
				login_page();
			} catch (Exception e) {
				// e.printStackTrace();
				flash.error("Server Unreachable");
				login_page();
			} finally {
				flash.clear();
				/*
				 * if (context != null) { context.close(); }
				 */
			}
		}
	}

	public static void register(@Required final String username,
			@Required final String password, @Required final String email) {
		if (Validation.hasErrors()) {
			flash.error("Username and password required");
			login_page();
		} else {
			Logger.info("-------------- INSIDE REGISTER.CONNECT()--------------");
			// /////////
			// First of all create the Abiquo context pointing to the Abiquo API
			/*
			 * AbiquoContext context = ContextBuilder.newBuilder(new
			 * AbiquoApiMetadata()) .endpoint("http://10.60.21.33/api")
			 * .credentials("user", "password") .modules(ImmutableSet.<Module>
			 * of(new SLF4JLoggingModule())) .build(AbiquoContext.class);
			 */

			AbiquoContext context = null;
			try {
				Properties props = new Properties();
				props.load(new FileInputStream(Play
						.getFile("conf/config.properties")));

				final String admin = props.getProperty("admin");
				final String passwordAdmin = props.getProperty("password");
				final String datacenterName = props.getProperty("datacenter");
				final String rolePortal = props.getProperty("role");

				context = Context.getApiClient(admin, passwordAdmin);
				// Create a new enterprise with a given set of limits
				Enterprise enterprise = Enterprise
						.builder(context.getApiContext()).name(username)
						.cpuCountLimits(5, 10) // Number of CPUs: Maximum 10,
												// warn when 5 are in use
						.ramLimits(2048, 4096) // Ram in MB: 4GB total, warn
												// when 2GB are in use
						.publicIpsLimits(5, 10) // Available public IPs: 10,
												// warn when 5 are in use
						// .storageLimits(5120 * 1204 * 1024, 10240 * 1204 *
						// 1024) // External storage capacity: 10GB, warn when
						// 5GB are in use
						.build();

				enterprise.save();

				// Allow the enterprise to use a Datacenter
				Datacenter datacenter = context.getAdministrationService()
						.findDatacenter(
								DatacenterPredicates.name(datacenterName));

				enterprise.allowDatacenter(datacenter);

				/*
				 * Datacenter datacenter =
				 * context.getAdministrationService().getDatacenter(0);
				 * 
				 * enterprise.allowDatacenter(datacenter);
				 */

				// Create an Enterprise administrator, so it can begin using the
				// cloud infrastructure
				// and can start creating the users of the enterprise
				Role role = context.getAdministrationService().findRole(
						RolePredicates.name(rolePortal));

				// Create the user with the selected role in the just created
				// enterprise
				User enterpriseUser = User
						.builder(context.getApiContext(), enterprise, role)
						.name(username, username) // The name and surname
						.email(email) // The email address
						.nick(username) // The login username
						.password(password) // The password
						.build();

				enterpriseUser.save();

				connect(username, password);

				// At this point, the new Enterprise is created and ready to
				// begin consuming the resources
				// of the cloud
			} catch (AbiquoException ae) {
				// ae.printStackTrace();
				if (ae.getMessage()
						.trim()
						.equals("ENTERPRISE-4 - Duplicate name for an enterprise")) {
					flash.error("User already registered");
				} else {
					flash.error("Error creating new account");
				}
				login_page();
			} catch (AuthorizationException ae) {
				// ae.printStackTrace();
				flash.error("Unauthorized User");
				login_page();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// e.printStackTrace();
				flash.error("Server Unreachable");
				login_page();
			} finally {
				flash.clear();
				if (context != null) {
					context.close();
				}
			}
		}
	}
}
