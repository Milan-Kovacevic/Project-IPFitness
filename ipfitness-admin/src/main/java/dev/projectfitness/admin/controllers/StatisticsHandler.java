package dev.projectfitness.admin.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dev.projectfitness.admin.beans.ActionItemBean;
import dev.projectfitness.admin.daos.paging.Pageable;
import dev.projectfitness.admin.services.StatisticsService;
import dev.projectfitness.admin.util.Utility;
import dev.projectfitness.admin.util.Constants.RequestParams;
import dev.projectfitness.admin.util.Constants.SessionParams;

public class StatisticsHandler {
	private final NavigationHandler navigationHandler;
	private final StatisticsService statisticsService;

	public StatisticsHandler(NavigationHandler navigationHandler, StatisticsService statisticsService) {
		super();
		this.navigationHandler = navigationHandler;
		this.statisticsService = statisticsService;
	}

	public void handleStatisticsRedirect(HttpServletRequest request, HttpServletResponse response) {
		if (!Utility.isAdminAuthenticated(request, response)) {
			navigationHandler.navigateToLogin(request, response);
			return;
		}

		Utility.clearMessagesFromSession(request.getSession());
		storeStatisticsInSession(request);
		navigationHandler.navigateToStatistics(request, response);
	}

	private void storeStatisticsInSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		int pageNumber = 1;
		String searchQuery = "";

		if (session.getAttribute(SessionParams.STATISTICS_SESSION_ATTRIBUTE_NAME) != null) {
			@SuppressWarnings("unchecked")
			Pageable<ActionItemBean> sessionStatistics = (Pageable<ActionItemBean>) session
					.getAttribute(SessionParams.STATISTICS_SESSION_ATTRIBUTE_NAME);
			if (request.getParameter(RequestParams.TABLE_PAGE_NUMBER) != null) {
				pageNumber = Integer.valueOf(request.getParameter(RequestParams.TABLE_PAGE_NUMBER));
			} else if (request.getParameter(RequestParams.TABLE_NEXT_PAGE) != null) {
				pageNumber = sessionStatistics.getPage() + 1;
			} else if (request.getParameter(RequestParams.TABLE_PREVIOUS_PAGE) != null) {
				pageNumber = sessionStatistics.getPage() - 1;
			} else {
				pageNumber = sessionStatistics.getPage();
			}
		}
		Pageable<ActionItemBean> statistics = statisticsService.getAllStatistics(pageNumber, searchQuery);
		session.setAttribute(SessionParams.STATISTICS_SESSION_ATTRIBUTE_NAME, statistics);
	}

}
