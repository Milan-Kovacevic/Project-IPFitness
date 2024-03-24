package dev.projectfitness.admin.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dev.projectfitness.admin.beans.AdminBean;
import dev.projectfitness.admin.services.AdminService;
import dev.projectfitness.admin.util.Constants.SessionParams;

public class LogoutHandler {
	private final NavigationHandler navigationHandler;
	private final AdminService adminService;

	public LogoutHandler(NavigationHandler navigationHandler, AdminService adminService) {
		super();
		this.navigationHandler = navigationHandler;
		this.adminService = adminService;
	}

	public void handleLogout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Object result = session.getAttribute(SessionParams.AUTHENTICATED_ADMIN);

		if (result == null) {
			navigationHandler.navigateToLogin(request, response);
			return;
		}

		AdminBean admin = (AdminBean) result;
		adminService.logout(admin);
		session.invalidate();
		navigationHandler.navigateToLogout(request, response);
	}
}
