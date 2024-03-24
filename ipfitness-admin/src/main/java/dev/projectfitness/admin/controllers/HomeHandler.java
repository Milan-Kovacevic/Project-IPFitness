package dev.projectfitness.admin.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dev.projectfitness.admin.beans.AdminBean;
import dev.projectfitness.admin.util.Constants.SessionParams;

public class HomeHandler {
	private final NavigationHandler navigationHandler;

	public HomeHandler(NavigationHandler navigationHandler) {
		super();
		this.navigationHandler = navigationHandler;
	}

	public void handleHomeRedirect(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Object result = session.getAttribute(SessionParams.AUTHENTICATED_ADMIN);

		if (result == null) {
			navigationHandler.navigateToLogin(request, response);
			return;
		}
		AdminBean admin = (AdminBean) result;
		if (!admin.isAuthenticated()) {
			navigationHandler.navigateToLogin(request, response);
			return;
		}

		navigationHandler.navigateToMain(request, response);
	}

}
