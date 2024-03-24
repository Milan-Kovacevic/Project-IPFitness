package dev.projectfitness.admin.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dev.projectfitness.admin.beans.UserBean;
import dev.projectfitness.admin.daos.paging.Pageable;
import dev.projectfitness.admin.dtos.User;
import dev.projectfitness.admin.services.UserService;
import dev.projectfitness.admin.util.Constants.RequestParams;
import dev.projectfitness.admin.util.Constants.SessionParams;
import dev.projectfitness.admin.util.Utility;

public class UsersHandler {
	private final NavigationHandler navigationHandler;
	private final UserService userService;

	public UsersHandler(NavigationHandler navigationHandler, UserService userService) {
		super();
		this.navigationHandler = navigationHandler;
		this.userService = userService;
	}

	public void handleUsersRedirect(HttpServletRequest request, HttpServletResponse response) {
		if (!Utility.isAdminAuthenticated(request, response)) {
			navigationHandler.navigateToLogin(request, response);
			return;
		}

		Utility.clearMessagesFromSession(request.getSession());
		storeUsersInSession(request);
		navigationHandler.navigateToUsers(request, response);
	}

	public void handleUserInsert(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		if (!Utility.isAdminAuthenticated(request, response)) {
			navigationHandler.navigateToLogin(request, response);
			return;
		}
		Utility.clearMessagesFromSession(session);

		User user = parseUserFromRequestData(request, false);
		if (user == null) {
			session.setAttribute(SessionParams.ERROR_MESSAGE, "All user parameters must be filled.");
			navigationHandler.navigateToUsers(request, response);
			return;
		}

		if (userService.existsByUsername(user.getUsername())) {
			session.setAttribute(SessionParams.ERROR_MESSAGE, "Username is already taken.");
			navigationHandler.navigateToUsers(request, response);
			return;
		}

		userService.insertUser(user);

		storeUsersInSession(request);

		navigationHandler.navigateToUsers(request, response);
	}

	public void handleUserUpdate(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		if (!Utility.isAdminAuthenticated(request, response)) {
			navigationHandler.navigateToLogin(request, response);
			return;
		}
		Utility.clearMessagesFromSession(session);

		User user = parseUserFromRequestData(request, true);
		if (user == null || request.getParameter("userId") == null) {
			session.setAttribute(SessionParams.ERROR_MESSAGE, "All user parameters must be filled.");
			navigationHandler.navigateToUsers(request, response);
			return;
		}
		user.setUserId(Integer.parseInt(request.getParameter("userId")));
		boolean success = userService.updateUser(user);

		if (!success) {
			session.setAttribute(SessionParams.ERROR_MESSAGE,
					"Unable to update user details. Check if username is already taken.");
		} else {
			session.setAttribute(SessionParams.SUCCESS_MESSAGE, "User details are updated successfully.");
		}

		storeUsersInSession(request);
		navigationHandler.navigateToUsers(request, response);
	}

	public void handleUserDelete(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		if (!Utility.isAdminAuthenticated(request, response)) {
			navigationHandler.navigateToLogin(request, response);
			return;
		}
		Utility.clearMessagesFromSession(session);

		if (request.getParameter("userId") == null) {
			session.setAttribute(SessionParams.ERROR_MESSAGE, "All user related parameters must be filled.");
			navigationHandler.navigateToUsers(request, response);
			return;
		}

		Integer userId = Integer.parseInt(request.getParameter("userId"));
		boolean success = userService.deleteUser(userId);
		if (!success) {
			session.setAttribute(SessionParams.ERROR_MESSAGE, "Selected user is not found.");
		} else {
			session.setAttribute(SessionParams.SUCCESS_MESSAGE, "User deleted successfully.");
		}

		storeUsersInSession(request);
		navigationHandler.navigateToUsers(request, response);
	}

	private User parseUserFromRequestData(HttpServletRequest request, boolean isUpdate) {
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String city = request.getParameter("city");
		String mail = request.getParameter("mail");
		Boolean status = "on".equals(request.getParameter("active"));

		if (firstName == null || "".equals(firstName) || lastName == null || "".equals(lastName) || username == null
				|| "".equals(username) || (!isUpdate && (password == null || "".equals(password))) || city == null
				|| "".equals(city) || mail == null || "".equals(mail) || status == null) {
			return null;
		}
		return new User(null, username, password, firstName, lastName, status, mail, city, null);
	}

	private void storeUsersInSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		int pageNumber = 1;
		String searchQuery = "";
		if (request.getParameter(RequestParams.SEARCH_QUERY) != null) {
			searchQuery = request.getParameter(RequestParams.SEARCH_QUERY);
			session.setAttribute(SessionParams.SEARCH_USERS_QUERY, searchQuery);
		} else if (session.getAttribute(SessionParams.SEARCH_USERS_QUERY) != null) {
			searchQuery = (String) session.getAttribute(SessionParams.SEARCH_USERS_QUERY);
		}
		if (session.getAttribute(SessionParams.USERS_SESSION_ATTRIBUTE_NAME) != null) {
			@SuppressWarnings("unchecked")
			Pageable<UserBean> sessionUsers = (Pageable<UserBean>) session
					.getAttribute(SessionParams.USERS_SESSION_ATTRIBUTE_NAME);
			if (request.getParameter(RequestParams.TABLE_PAGE_NUMBER) != null) {
				pageNumber = Integer.valueOf(request.getParameter(RequestParams.TABLE_PAGE_NUMBER));
			} else if (request.getParameter(RequestParams.TABLE_NEXT_PAGE) != null) {
				pageNumber = sessionUsers.getPage() + 1;
			} else if (request.getParameter(RequestParams.TABLE_PREVIOUS_PAGE) != null) {
				pageNumber = sessionUsers.getPage() - 1;
			} else {
				pageNumber = sessionUsers.getPage();
			}
		}
		Pageable<UserBean> users = userService.getAllUsers(pageNumber, searchQuery);
		session.setAttribute(SessionParams.USERS_SESSION_ATTRIBUTE_NAME, users);
	}

}
