package dev.projectfitness.admin.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dev.projectfitness.admin.services.AdminService;
import dev.projectfitness.admin.services.AdvisorService;
import dev.projectfitness.admin.services.CategoryService;
import dev.projectfitness.admin.services.StatisticsService;
import dev.projectfitness.admin.services.UserService;
import dev.projectfitness.admin.util.Constants;

@WebServlet("/MainController")
public class MainController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final NavigationHandler navigationHandler;
	private final LoginHandler loginHandler;
	private final LogoutHandler logoutHandler;
	private final HomeHandler homeHandler;
	private final UsersHandler usersHandler;
	private final CategoriesHandler categoriesHandler;
	private final AdvisorsHandler advisorsHandler;
	private final StatisticsHandler statisticsHandler;

	private final AdminService adminService;
	private final UserService userService;
	private final CategoryService categoryService;
	private final AdvisorService advisorService;
	private final StatisticsService statisticsService;

	public MainController() {
		super();
		adminService = new AdminService();
		userService = new UserService();
		categoryService = new CategoryService();
		advisorService = new AdvisorService();
		statisticsService = new StatisticsService();
		
		navigationHandler = new NavigationHandler();
		loginHandler = new LoginHandler(navigationHandler, adminService);
		logoutHandler = new LogoutHandler(navigationHandler, adminService);
		homeHandler = new HomeHandler(navigationHandler);
		usersHandler = new UsersHandler(navigationHandler, userService);
		categoriesHandler = new CategoriesHandler(navigationHandler, categoryService);
		advisorsHandler = new AdvisorsHandler(navigationHandler, advisorService);
		statisticsHandler = new StatisticsHandler(navigationHandler, statisticsService);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding(Constants.REQUEST_CHARACTER_ENCODING);
		String clientAction = request.getParameter(Constants.ACTION_URL_ATTRIBUTE);

		if (clientAction == null || "".equals(clientAction)) {
			homeHandler.handleHomeRedirect(request, response);
		} else if (Constants.Actions.LOGIN_ACTION.equalsIgnoreCase(clientAction)) {
			loginHandler.handleLogin(request, response);
		} else if (Constants.Actions.LOGOUT_ACTION.equalsIgnoreCase(clientAction)) {
			logoutHandler.handleLogout(request, response);
		} else if (Constants.Actions.HOME_ACTION.equalsIgnoreCase(clientAction)) {
			homeHandler.handleHomeRedirect(request, response);
		} else if (Constants.Actions.USERS_ACTION.equalsIgnoreCase(clientAction)) {
			usersHandler.handleUsersRedirect(request, response);
		} else if (Constants.Actions.USER_ADD_ACTION.equalsIgnoreCase(clientAction)) {
			usersHandler.handleUserInsert(request, response);
		} else if (Constants.Actions.USER_EDIT_ACTION.equalsIgnoreCase(clientAction)) {
			usersHandler.handleUserUpdate(request, response);
		} else if (Constants.Actions.USER_DELETE_ACTION.equalsIgnoreCase(clientAction)) {
			usersHandler.handleUserDelete(request, response);
		} else if (Constants.Actions.CATEGORIES_ACTION.equalsIgnoreCase(clientAction)) {
			categoriesHandler.handleCategoriesRedirect(request, response);
		} else if (Constants.Actions.CATEGORY_ADD_ACTION.equalsIgnoreCase(clientAction)) {
			categoriesHandler.handleCategoryInsert(request, response);
		} else if (Constants.Actions.CATEGORY_GET_ACTION.equalsIgnoreCase(clientAction)) {
			categoriesHandler.handleCategoryGet(request, response);
		} else if (Constants.Actions.CATEGORY_EDIT_ACTION.equalsIgnoreCase(clientAction)) {
			categoriesHandler.handleCategoryUpdate(request, response);
		} else if (Constants.Actions.CATEGORY_DELETE_ACTION.equalsIgnoreCase(clientAction)) {
			categoriesHandler.handleCategoryDelete(request, response);
		} else if (Constants.Actions.ADVISORS_ACTION.equalsIgnoreCase(clientAction)) {
			advisorsHandler.handleAdvisorsRedirect(request, response);
		} else if (Constants.Actions.ADVISOR_ADD_ACTION.equalsIgnoreCase(clientAction)) {
			advisorsHandler.handleAdvisorInsert(request, response);
		} else if (Constants.Actions.ADVISOR_EDIT_ACTION.equalsIgnoreCase(clientAction)) {
			advisorsHandler.handleAdvisorUpdate(request, response);
		} else if (Constants.Actions.ADVISOR_DELETE_ACTION.equalsIgnoreCase(clientAction)) {
			advisorsHandler.handleAdvisorDelete(request, response);
		} else if (Constants.Actions.STATISTICS_ACTION.equalsIgnoreCase(clientAction)) {
			statisticsHandler.handleStatisticsRedirect(request, response);
		} else {
			navigationHandler.navigateToNotFound(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
