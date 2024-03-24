package dev.projectfitness.admin.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dev.projectfitness.admin.beans.AdvisorBean;
import dev.projectfitness.admin.daos.paging.Pageable;
import dev.projectfitness.admin.dtos.Advisor;
import dev.projectfitness.admin.services.AdvisorService;
import dev.projectfitness.admin.util.Constants.RequestParams;
import dev.projectfitness.admin.util.Constants.SessionParams;
import dev.projectfitness.admin.util.Utility;

public class AdvisorsHandler {
	private final NavigationHandler navigationHandler;
	private final AdvisorService advisorService;

	public AdvisorsHandler(NavigationHandler navigationHandler, AdvisorService advisorService) {
		super();
		this.navigationHandler = navigationHandler;
		this.advisorService = advisorService;
	}

	public void handleAdvisorsRedirect(HttpServletRequest request, HttpServletResponse response) {
		if (!Utility.isAdminAuthenticated(request, response)) {
			navigationHandler.navigateToLogin(request, response);
			return;
		}

		Utility.clearMessagesFromSession(request.getSession());
		storeAdvisorsInSession(request);
		navigationHandler.navigateToAdvisors(request, response);
	}

	public void handleAdvisorInsert(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		if (!Utility.isAdminAuthenticated(request, response)) {
			navigationHandler.navigateToLogin(request, response);
			return;
		}
		Utility.clearMessagesFromSession(session);

		Advisor advisor = parseAdvisorFromRequestData(request, false);
		if (advisor == null) {
			session.setAttribute(SessionParams.ERROR_MESSAGE, "All advisor parameters must be filled.");
			navigationHandler.navigateToAdvisors(request, response);
			return;
		}

		if (advisorService.existsByUsername(advisor.getUsername())) {
			session.setAttribute(SessionParams.ERROR_MESSAGE, "Username is already taken.");
			navigationHandler.navigateToAdvisors(request, response);
			return;
		}

		advisorService.insertAdvisor(advisor);
		storeAdvisorsInSession(request);
		navigationHandler.navigateToAdvisors(request, response);
	}

	public void handleAdvisorUpdate(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		if (!Utility.isAdminAuthenticated(request, response)) {
			navigationHandler.navigateToLogin(request, response);
			return;
		}
		Utility.clearMessagesFromSession(session);

		Advisor advisor = parseAdvisorFromRequestData(request, true);
		if (advisor == null || request.getParameter("advisorId") == null) {
			session.setAttribute(SessionParams.ERROR_MESSAGE, "All advisor parameters must be filled.");
			navigationHandler.navigateToAdvisors(request, response);
			return;
		}
		advisor.setAdvisorId(Integer.parseInt(request.getParameter("advisorId")));
		boolean success = advisorService.updateAdvisor(advisor);

		if (!success) {
			session.setAttribute(SessionParams.ERROR_MESSAGE,
					"Unable to update advisor details. Check if username is already taken.");
		} else {
			session.setAttribute(SessionParams.SUCCESS_MESSAGE, "Advisor details are updated successfully.");
		}

		storeAdvisorsInSession(request);
		navigationHandler.navigateToAdvisors(request, response);
	}

	public void handleAdvisorDelete(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		if (!Utility.isAdminAuthenticated(request, response)) {
			navigationHandler.navigateToLogin(request, response);
			return;
		}
		Utility.clearMessagesFromSession(session);

		if (request.getParameter("advisorId") == null) {
			session.setAttribute(SessionParams.ERROR_MESSAGE, "All advisor related parameters must be filled.");
			navigationHandler.navigateToAdvisors(request, response);
			return;
		}

		Integer advisorId = Integer.parseInt(request.getParameter("advisorId"));
		boolean success = advisorService.deleteAdvisor(advisorId);
		if (!success) {
			session.setAttribute(SessionParams.ERROR_MESSAGE, "Selected advisor is not found.");
		} else {
			session.setAttribute(SessionParams.SUCCESS_MESSAGE, "Advisor deleted successfully.");
		}

		storeAdvisorsInSession(request);
		navigationHandler.navigateToAdvisors(request, response);
	}

	private Advisor parseAdvisorFromRequestData(HttpServletRequest request, boolean isUpdate) {
		String displayName = request.getParameter("displayName");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");

		if (displayName == null || "".equals(displayName) || username == null || "".equals(username)
				|| (!isUpdate && (password == null || "".equals(password))) || email == null || "".equals(email)) {
			return null;
		}
		return new Advisor(null, displayName, username, password, email,
				Utility.getAuthenticatedAdmin(request).getAdminId());
	}

	private void storeAdvisorsInSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		int pageNumber = 1;
		String searchQuery = "";
		if (request.getParameter(RequestParams.SEARCH_QUERY) != null) {
			searchQuery = request.getParameter(RequestParams.SEARCH_QUERY);
			session.setAttribute(SessionParams.SEARCH_ADVISORS_QUERY, searchQuery);
		} else if (session.getAttribute(SessionParams.SEARCH_ADVISORS_QUERY) != null) {
			searchQuery = (String) session.getAttribute(SessionParams.SEARCH_ADVISORS_QUERY);
		}
		if (session.getAttribute(SessionParams.ADVISORS_SESSION_ATTRIBUTE_NAME) != null) {
			@SuppressWarnings("unchecked")
			Pageable<AdvisorBean> sessionAdvisors = (Pageable<AdvisorBean>) session
					.getAttribute(SessionParams.ADVISORS_SESSION_ATTRIBUTE_NAME);
			if (request.getParameter(RequestParams.TABLE_PAGE_NUMBER) != null) {
				pageNumber = Integer.valueOf(request.getParameter(RequestParams.TABLE_PAGE_NUMBER));
			} else if (request.getParameter(RequestParams.TABLE_NEXT_PAGE) != null) {
				pageNumber = sessionAdvisors.getPage() + 1;
			} else if (request.getParameter(RequestParams.TABLE_PREVIOUS_PAGE) != null) {
				pageNumber = sessionAdvisors.getPage() - 1;
			} else {
				pageNumber = sessionAdvisors.getPage();
			}
		}
		Pageable<AdvisorBean> users = advisorService.getAllAdvisors(pageNumber, searchQuery);
		session.setAttribute(SessionParams.ADVISORS_SESSION_ATTRIBUTE_NAME, users);
	}

}
