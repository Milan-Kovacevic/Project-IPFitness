package dev.projectfitness.advisor.util;

public class Constants {
	public static final String APPLICATION_TITLE = "IP Fitness - Advisor Application";
	public static final String EMAIL_SERVICE_ENDPOINT = "http://localhost:9001/api/v1/email";
	
	public static class SessionParams {
		public static final String AUTHENTICATED_ADVISOR = "authUser";
		public static final String MENU_ACTIVE_LINK = "menuActive";
		public static final String SEARCH_MESSAGES_QUERY = "searchQuery";
	}
	
	public static class RequestParams {
		public static final String TABLE_PAGE_NUMBER = "currentPage";
		public static final String TABLE_NEXT_PAGE = "nextPage";
		public static final String TABLE_PREVIOUS_PAGE = "previousPage";
		public static final String SEARCH_QUERY = "searchQuery";
	}

	public static class Pages {
		public static final String LOGIN_PAGE = "login.jsp";
		public static final String LOGOUT_PAGE = "logout.jsp";
		public static final String MESSAGES_PAGE = "messages.jsp";
		public static final String FILE_UPLOAD_PAGE = "file-upload.jsp";
	}

	public static class MenuItem {
		public static final String LOGIN_ITEM = "login";
		public static final String MESSAGES_ITEM = "messages";
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
}
