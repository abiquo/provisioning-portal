package controllers;

import org.hamcrest.core.IsNull;
import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.enterprise.Role;
import org.jclouds.abiquo.domain.enterprise.User;
import org.jclouds.abiquo.domain.exception.AbiquoException;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.features.services.AdministrationService;
import org.jclouds.abiquo.predicates.enterprise.RolePredicates;
import org.jclouds.rest.AuthorizationException;

import play.Logger;
import play.cache.Cache;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.mvc.Controller;
import portal.util.Context;
import portal.util.CurrentUserContext;

public class Login extends Controller {

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

			AbiquoContext context = Context.getContext(username, password);
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
						User currentUser = adminService.getCurrentUserInfo();
						flash.put("currentUserInfo", currentUser);
						Integer enterpriseID = currentUser.getEnterprise()
								.getId();

						String useremail = currentUser.getEmail();
						session.put("email", useremail);
						if (currentUser != null) {
							Role role = currentUser.getRole();
							Logger.info("Role of user" + role);

							if (role.getName().contentEquals("CLOUD_ADMIN")) {
								ProducerLocal.admin();
							} else {
								Consumer.ServiceCatalog(enterpriseID);
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
				if (context != null) {
					context.close();
				}
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
		///////////
			// First of all create the Abiquo context pointing to the Abiquo API
			/*AbiquoContext context = ContextBuilder.newBuilder(new AbiquoApiMetadata())
			    .endpoint("http://10.60.21.33/api")
			    .credentials("user", "password")
			    .modules(ImmutableSet.<Module> of(new SLF4JLoggingModule()))
			    .build(AbiquoContext.class);*/

			AbiquoContext context = Context.getContext("admin", "xabiquo");
				  
			try
			{
			    // Create a new enterprise with a given set of limits
			    Enterprise enterprise = Enterprise.builder(context)
			        .name(username)
			        .cpuCountLimits(5, 10)      // Number of CPUs: Maximum 10, warn when 5 are in use
			        .ramLimits(2048, 4096)      // Ram in MB: 4GB total, warn when 2GB are in use
			        .publicIpsLimits(5, 10)     // Available public IPs: 10, warn when 5 are in use
			        //.storageLimits(5120 * 1204 * 1024, 10240 * 1204 * 1024) // External storage capacity: 10GB, warn when 5GB are in use
			        .build();

			    enterprise.save();

			    // Allow the enterprise to use a Datacenter
			   /* Datacenter datacenter =
			        context.getAdministrationService().findDatacenter(DatacenterPredicates.name("San Francisco"));*/
			    
			    /*Datacenter datacenter =
				        context.getAdministrationService().getDatacenter(0);

			    enterprise.allowDatacenter(datacenter);*/

			    // Create an Enterprise administrator, so it can begin using the cloud infrastructure
			    // and can start creating the users of the enterprise
			    Role role =
			        context.getAdministrationService().findRole(RolePredicates.name("USER"));

			    // Create the user with the selected role in the just created enterprise
			    User enterpriseUser = User.builder(context, enterprise, role) 
			        .name(username, username)       // The name and surname
			        .email(email) // The email address
			        .nick(username)              // The login username
			        .password(password)       // The password
			        .build();

			    enterpriseUser.save();
			    
			    connect(username, password);

			    // At this point, the new Enterprise is created and ready to begin consuming the resources
			    // of the cloud
			} catch (AbiquoException ae) {
				// ae.printStackTrace();				
				if (ae.getMessage().trim().equals("ENTERPRISE-4 - Duplicate name for an enterprise")) {
					flash.error("User already registered");
				} else {
					flash.error("Error creating new account");
				}
				login_page();
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
			    if (context != null)
			    {
			        context.close();
			    }
			}
		}
	}
}
