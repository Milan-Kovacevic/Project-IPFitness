package dev.projectfitness.admin.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dev.projectfitness.admin.util.Constants;
import dev.projectfitness.admin.util.Constants.MenuItem;
import dev.projectfitness.admin.util.Constants.SessionParams;
import dev.projectfitness.admin.util.Utility;

public class NavigationHandler {

	public NavigationHandler() {
		super();
	}

	public void navigateToLogin(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getSession().setAttribute(SessionParams.MENU_ACTIVE_LINK, MenuItem.LOGIN_ITEM);
			Utility.forwardResponseTo(Constants.Pages.LOGIN_PAGE, request, response);
		} catch (Exception ex) {
			// TODO: Log
		}
	}

	public void navigateToMain(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getSession().setAttribute(SessionParams.MENU_ACTIVE_LINK, MenuItem.HOME_ITEM);
			Utility.forwardResponseTo(Constants.Pages.MAIN_PAGE, request, response);
		} catch (Exception ex) {
			// TODO: Log
		}
	}

	public void navigateToUsers(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getSession().setAttribute(SessionParams.MENU_ACTIVE_LINK, MenuItem.USERS_ITEM);
			Utility.forwardResponseTo(Constants.Pages.USERS_PAGE, request, response);
		} catch (Exception ex) {
			// TODO: Log
		}
	}

	public void navigateToCategories(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getSession().setAttribute(SessionParams.MENU_ACTIVE_LINK, MenuItem.CATEGORIES_ITEM);
			Utility.forwardResponseTo(Constants.Pages.CATEGORIES_PAGE, request, response);
		} catch (Exception ex) {
			// TODO: Log
		}
	}

	public void navigateToStatistics(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getSession().setAttribute(SessionParams.MENU_ACTIVE_LINK, MenuItem.STATISTICS_ITEM);
			Utility.forwardResponseTo(Constants.Pages.STATISTICS_PAGE, request, response);
		} catch (Exception ex) {
			// TODO: Log
		}
	}

	public void navigateToNotFound(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getSession().setAttribute(SessionParams.MENU_ACTIVE_LINK, null);
			Utility.forwardResponseTo(Constants.Pages.$404_PAGE, request, response);
		} catch (Exception ex) {
			// TODO: Log
		}
	}

	public void navigateToLogout(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getSession().setAttribute(SessionParams.MENU_ACTIVE_LINK, MenuItem.LOGIN_ITEM);
			Utility.forwardResponseTo(Constants.Pages.LOGOUT_PAGE, request, response);
		} catch (Exception ex) {
			// TODO: Log
		}
	}

	public void navigateToAdvisors(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getSession().setAttribute(SessionParams.MENU_ACTIVE_LINK, MenuItem.USERS_ITEM);
			Utility.forwardResponseTo(Constants.Pages.ADVISORS_PAGE, request, response);
		} catch (Exception ex) {
			// TODO: Log
		}
	}

}
