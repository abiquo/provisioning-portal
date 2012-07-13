package controllers;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.enterprise.Role;
import org.jclouds.abiquo.domain.enterprise.User;
import org.jclouds.abiquo.features.services.AdministrationService;
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
			flash.error(" Both Name and password are required!");
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
				flash.error(" Unauthorized User");
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
}
