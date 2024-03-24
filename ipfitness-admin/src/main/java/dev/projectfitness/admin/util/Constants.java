package dev.projectfitness.admin.util;

public class Constants {
	public static final String REQUEST_CHARACTER_ENCODING = "UTF-8";
	public static final String ACTION_URL_ATTRIBUTE = "action";
	public static final String APPLICATION_TITLE = "IP Fitness - Administrator Application";
	public static final String ENCRYPTION_SERVICE_ENDPOINT = "http://localhost:9001/utility/encrypt";
	public static final int PAGE_SIZE = 8;
	
	public static class SessionParams {
		public static final String AUTHENTICATED_ADMIN = "authUser";
		public static final String MENU_ACTIVE_LINK = "menuActive";
		public static final String USERS_SESSION_ATTRIBUTE_NAME = "users";
		public static final String CATEGORIES_SESSION_ATTRIBUTE_NAME = "categories";
		public static final String ADVISORS_SESSION_ATTRIBUTE_NAME = "advisors";
		public static final String STATISTICS_SESSION_ATTRIBUTE_NAME = "statistics";
		public static final String ERROR_MESSAGE = "errorMessage";
		public static final String SUCCESS_MESSAGE = "successMessage";
		public static final String SHOW_DETAILS_ITEM = "showDetailsItem";
		public static final String SEARCH_USERS_QUERY = "searchUsersQuery";
		public static final String SEARCH_CATEGORIES_QUERY = "searchCategoriesQuery";
		public static final String SEARCH_ADVISORS_QUERY = "searchAdvisorsQuery";
	}

	public static class RequestParams {
		public static final String TABLE_PAGE_NUMBER = "currentPage";
		public static final String TABLE_NEXT_PAGE = "nextPage";
		public static final String TABLE_PREVIOUS_PAGE = "previousPage";
		public static final String SEARCH_QUERY = "searchQuery";
	}

	public static class Actions {
		public static final String LOGIN_ACTION = "login";
		public static final String LOGOUT_ACTION = "logout";
		public static final String HOME_ACTION = "home";
		public static final String USERS_ACTION = "users";
		public static final String CATEGORIES_ACTION = "categories";
		public static final String ADVISORS_ACTION = "advisors";
		public static final String STATISTICS_ACTION = "statistics";
		public static final String USER_ADD_ACTION = "addUser";
		public static final String USER_EDIT_ACTION = "editUser";
		public static final String USER_DELETE_ACTION = "deleteUser";
		public static final String ADVISOR_ADD_ACTION = "addAdvisor";
		public static final String ADVISOR_EDIT_ACTION = "editAdvisor";
		public static final String ADVISOR_DELETE_ACTION = "deleteAdvisor";
		public static final String CATEGORY_ADD_ACTION = "addCategory";
		public static final String CATEGORY_EDIT_ACTION = "editcategory";
		public static final String CATEGORY_GET_ACTION = "getCategory";
		public static final String CATEGORY_DELETE_ACTION = "deleteCategory";
	}

	public static class ActionQuery {
		public static final String LOGIN_QUERY = "?" + ACTION_URL_ATTRIBUTE + "=" + Actions.LOGIN_ACTION;
		public static final String LOGOUT_QUERY = "?" + ACTION_URL_ATTRIBUTE + "=" + Actions.LOGOUT_ACTION;
		public static final String HOME_QUERY = "?" + ACTION_URL_ATTRIBUTE + "=" + Actions.HOME_ACTION;
		public static final String USERS_QUERY = "?" + ACTION_URL_ATTRIBUTE + "=" + Actions.USERS_ACTION;
		public static final String CATEGORIES_QUERY = "?" + ACTION_URL_ATTRIBUTE + "=" + Actions.CATEGORIES_ACTION;
		public static final String ADVISORS_QUERY = "?" + ACTION_URL_ATTRIBUTE + "=" + Actions.ADVISORS_ACTION;
		public static final String STATISTICS_QUERY = "?" + ACTION_URL_ATTRIBUTE + "=" + Actions.STATISTICS_ACTION;
		public static final String USER_ADD_QUERY = "?" + ACTION_URL_ATTRIBUTE + "=" + Actions.USER_ADD_ACTION;
		public static final String USER_EDIT_QUERY = "?" + ACTION_URL_ATTRIBUTE + "=" + Actions.USER_EDIT_ACTION;
		public static final String USER_DELETE_QUERY = "?" + ACTION_URL_ATTRIBUTE + "=" + Actions.USER_DELETE_ACTION;
		public static final String ADVISOR_ADD_QUERY = "?" + ACTION_URL_ATTRIBUTE + "=" + Actions.ADVISOR_ADD_ACTION;
		public static final String ADVISOR_EDIT_QUERY = "?" + ACTION_URL_ATTRIBUTE + "=" + Actions.ADVISOR_EDIT_ACTION;
		public static final String ADVISOR_DELETE_QUERY = "?" + ACTION_URL_ATTRIBUTE + "="
				+ Actions.ADVISOR_DELETE_ACTION;
		public static final String CATEGORY_ADD_QUERY = "?" + ACTION_URL_ATTRIBUTE + "=" + Actions.CATEGORY_ADD_ACTION;
		public static final String CATEGORY_EDIT_QUERY = "?" + ACTION_URL_ATTRIBUTE + "="
				+ Actions.CATEGORY_EDIT_ACTION;
		public static final String CATEGORY_GET_QUERY = "?" + ACTION_URL_ATTRIBUTE + "=" + Actions.CATEGORY_GET_ACTION;
		public static final String CATEGORY_DELETE_QUERY = "?" + ACTION_URL_ATTRIBUTE + "="
				+ Actions.CATEGORY_DELETE_ACTION;
	}

	public static class Pages {
		public static final String LOGIN_PAGE = "/WEB-INF/pages/login.jsp";
		public static final String MAIN_PAGE = "/WEB-INF/pages/main.jsp";
		public static final String $404_PAGE = "/WEB-INF/pages/404.jsp";
		public static final String LOGOUT_PAGE = "/WEB-INF/pages/logout.jsp";
		public static final String USERS_PAGE = "/WEB-INF/pages/users.jsp";
		public static final String CATEGORIES_PAGE = "/WEB-INF/pages/categories.jsp";
		public static final String ADVISORS_PAGE = "/WEB-INF/pages/advisors.jsp";
		public static final String STATISTICS_PAGE = "/WEB-INF/pages/statistics.jsp";
	}

	public static class DataSourceProperties {
		public static final String JDBC_URL = "datasource.url";
		public static final String USERNAME = "datasource.username";
		public static final String PASSWORD = "datasource.password";
		public static final String DRIVER = "datasource.driver-class-name";
		public static final String PRECONNECT_COUNT = "datasource.pool.preconnect-count";
		public static final String MAX_IDLE_CONNECTIONS = "datasource.pool.max-idle-connections";
		public static final String MAX_CONNECTIONS = "datasource.pool.max-connections";
	}

	public static class MenuItem {
		public static final String HOME_ITEM = "home";
		public static final String LOGIN_ITEM = "login";
		public static final String USERS_ITEM = "users";
		public static final String CATEGORIES_ITEM = "categories";
		public static final String STATISTICS_ITEM = "statistics";
	}
}
