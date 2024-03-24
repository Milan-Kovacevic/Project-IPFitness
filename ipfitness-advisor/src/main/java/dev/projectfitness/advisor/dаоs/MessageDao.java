package dev.projectfitness.advisor.dаоs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.projectfitness.advisor.daos.pooling.ConnectionPool;
import dev.projectfitness.advisor.dtos.Message;
import dev.projectfitness.advisor.util.Utility;

public class MessageDao {
	private final ConnectionPool connectionPool;
	private static final String REQUIRED_MESSAGE_FIELDS = "q.QuestionId, q.Content, CONCAT(u.FirstName, ' ' , u.LastName) AS SenderName, u.Mail, q.SendDate";
	private static final String SQL_GET_BY_ID = "SELECT " + REQUIRED_MESSAGE_FIELDS
			+ " FROM question q INNER JOIN fitness_user u USING(UserId) WHERE q.QuestionId=? AND q.IsRead=0";
	private static final String SQL_GET_ALL_UNREAD_MESSAGES = "SELECT " + REQUIRED_MESSAGE_FIELDS
			+ " FROM question q INNER JOIN fitness_user u USING(UserId) WHERE q.IsRead=0 ORDER BY SendDate DESC";
	private static final String SQL_SEARCH_UNREAD_MESSAGES = "SELECT " + REQUIRED_MESSAGE_FIELDS
			+ " FROM question q INNER JOIN fitness_user u USING(UserId) WHERE q.IsRead=0 AND "
			+ "(CONCAT(u.FirstName, ' ' , u.LastName) LIKE ? OR q.Content LIKE ?) ORDER BY SendDate DESC";
	private static final String SQL_UPDATE_MESSAGE_READ = "UPDATE question q SET q.IsRead=1 WHERE q.QuestionId=?";
	private static final String SQL_INSERT_MESSAGE_RECORD = "INSERT INTO question_record (QuestionId, AdvisorId) VALUES (?,?)";

	public MessageDao() {
		super();
		connectionPool = ConnectionPool.getInstance();
	}

	public Optional<Message> getById(Integer id) {
		Message message = null;
		Connection connection = null;
		ResultSet rs = null;
		Object[] values = { id };

		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_GET_BY_ID, false, values);
			rs = statement.executeQuery();
			if (rs.next()) {
				Instant instant = rs.getTimestamp("SendDate").toInstant();
				message = new Message(rs.getInt(1), rs.getString("Content"), rs.getString("SenderName"),
						rs.getString("Mail"), Utility.formatDate(instant));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}

		return Optional.ofNullable(message);
	}

	public List<Message> getAllUnread() {
		List<Message> messages = new ArrayList<>();
		Connection connection = null;
		ResultSet rs = null;
		Object[] values = {};
		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_GET_ALL_UNREAD_MESSAGES, false,
					values);
			rs = statement.executeQuery();
			while (rs.next()) {
				Instant instant = rs.getTimestamp("SendDate").toInstant();
				Message message = new Message(rs.getInt(1), rs.getString("Content"), rs.getString("SenderName"),
						rs.getString("Mail"), Utility.formatDate(instant));
				messages.add(message);
			}
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}

		return messages;
	}

	public List<Message> searchUnreadMessages(String query) {
		List<Message> messages = new ArrayList<>();
		Connection connection = null;
		ResultSet rs = null;
		String searchParam = "%" + query + "%";
		Object[] values = { searchParam, searchParam };
		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_SEARCH_UNREAD_MESSAGES, false,
					values);
			rs = statement.executeQuery();
			while (rs.next()) {
				Instant instant = rs.getTimestamp("SendDate").toInstant();
				Message message = new Message(rs.getInt(1), rs.getString("Content"), rs.getString("SenderName"),
						rs.getString("Mail"), Utility.formatDate(instant));
				messages.add(message);
			}
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}

		return messages;
	}

	public boolean updateMessageRead(Integer id) {
		Connection connection = null;
		boolean success = false;
		Object[] values = { id };

		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_UPDATE_MESSAGE_READ, false, values);
			success = statement.executeUpdate() > 0;
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		return success;
	}

	public void saveMessageRecord(Integer questionId, Integer advisorId) {
		Connection connection = null;
		Object[] values = { questionId, advisorId };

		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_INSERT_MESSAGE_RECORD, true, values);
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
	}

}
