package dev.projectfitness.admin.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dev.projectfitness.admin.beans.AdminBean;
import dev.projectfitness.admin.services.AdminService;
import dev.projectfitness.admin.util.Constants.SessionParams;

public class LoginHandler {
	private final NavigationHandler navigationHandler;
	private final AdminService adminService;

	public LoginHandler(NavigationHandler navigationHandler, AdminService adminService) {
		super();
		this.navigationHandler = navigationHandler;
		this.adminService = adminService;
	}

	public void handleLogin(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if (username == null || password == null) {
			navigationHandler.navigateToLogin(request, response);
			return;
		}
		HttpSession session = request.getSession();
		session.setAttribute(SessionParams.ERROR_MESSAGE, null);

		AdminBean admin = adminService.login(username, password);
		if (admin == null || !admin.isAuthenticated()) {
			session.setAttribute(SessionParams.ERROR_MESSAGE, "Invalid username or password. Please, try again.");
			navigationHandler.navigateToLogin(request, response);
		} else {
			session.setAttribute(SessionParams.AUTHENTICATED_ADMIN, admin);
			navigationHandler.navigateToMain(request, response);
		}

	}
}
