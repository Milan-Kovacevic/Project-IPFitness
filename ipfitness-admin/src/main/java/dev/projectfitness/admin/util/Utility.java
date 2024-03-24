package dev.projectfitness.admin.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dev.projectfitness.admin.beans.AdminBean;
import dev.projectfitness.admin.dtos.Admin;
import dev.projectfitness.admin.util.Constants.SessionParams;

public class Utility {

	public static void forwardResponseTo(String page, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispacher = request.getRequestDispatcher(page);
		if (dispacher != null)
			dispacher.forward(request, response);
	}

	public static PreparedStatement prepareStatement(Connection connection, String sql, boolean returnGeneratedKeys,
			Object... values) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(sql,
				returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);

		for (int i = 0; i < values.length; i++) {
			preparedStatement.setObject(i + 1, values[i]);
		}
		return preparedStatement;
	}

	private Utility() {
		super();
	}

	public static boolean isAdminAuthenticated(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Object result = session.getAttribute(SessionParams.AUTHENTICATED_ADMIN);

		if (result == null) {
			return false;
		}
		AdminBean admin = (AdminBean) result;
		if (!admin.isAuthenticated()) {
			return false;
		}

		return true;
	}

	public static Admin getAuthenticatedAdmin(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object result = session.getAttribute(SessionParams.AUTHENTICATED_ADMIN);

		if (result == null) {
			return null;
		}
		AdminBean bean = (AdminBean) result;
		return bean.getAdmin();
	}

	public static void clearMessagesFromSession(HttpSession session) {
		session.setAttribute(SessionParams.ERROR_MESSAGE, null);
		session.setAttribute(SessionParams.SUCCESS_MESSAGE, null);
		session.setAttribute(SessionParams.SHOW_DETAILS_ITEM, null);
	}
	
	public static String formatDate(Instant instant) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                .withZone(ZoneId.of("UTC"));

        return formatter.format(instant);
	}
	
}
