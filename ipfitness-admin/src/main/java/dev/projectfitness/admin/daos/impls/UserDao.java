package dev.projectfitness.admin.daos.impls;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.projectfitness.admin.daos.PageableDao;
import dev.projectfitness.admin.daos.paging.Page;
import dev.projectfitness.admin.daos.paging.Pageable;
import dev.projectfitness.admin.daos.pooling.ConnectionPool;
import dev.projectfitness.admin.dtos.User;
import dev.projectfitness.admin.util.Utility;

public class UserDao implements PageableDao<User, Integer> {
	private final ConnectionPool connectionPool;
	private static final String SQL_INSERT = "INSERT INTO fitness_user (Username, Password, FirstName, LastName, Active, Mail, City, Avatar, Enabled) VALUES (?,?,?,?,?,?,?,?,1)";
	private static final String SQL_UPDATE = "UPDATE fitness_user SET Username=? , FirstName=? , LastName=? , Active=? , Mail=? , City=? WHERE UserId=? AND Enabled=1";
	private static final String SQL_DELETE = "UPDATE fitness_user SET Enabled=0 WHERE UserId=?";
	private static final String SQL_GET_BY_ID = "SELECT * FROM fitness_user where UserId=? AND Enabled=1";
	private static final String SQL_GET_BY_USERNAME = "SELECT * FROM fitness_user where Username=? AND Enabled=1";
	private static final String SQL_GET_COUNT = "SELECT count(*) FROM fitness_user WHERE Enabled=1";
	private static final String SQL_GET_ALL_PAGINATED = "SELECT * FROM fitness_user WHERE Enabled=1 AND (CONCAT(FirstName, \" \", LastName) like ? or Username like ?) LIMIT ? OFFSET ? ";

	public UserDao() {
		super();
		connectionPool = ConnectionPool.getInstance();
	}

	@Override
	public Optional<User> getById(Integer id) {
		User user = null;
		Connection connection = null;
		ResultSet rs = null;
		Object[] values = { id };

		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_GET_BY_ID, false, values);
			rs = statement.executeQuery();
			if (rs.next()) {
				user = new User(rs.getInt(1), rs.getString("Username"), rs.getString("Password"),
						rs.getString("FirstName"), rs.getString("LastName"), rs.getBoolean("Active"),
						rs.getString("Mail"), rs.getString("City"), rs.getString("Avatar"));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}

		return Optional.ofNullable(user);
	}

	public Optional<User> getByUsername(String username) {
		User user = null;
		Connection connection = null;
		ResultSet rs = null;
		Object[] values = { username };

		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_GET_BY_USERNAME, false, values);
			rs = statement.executeQuery();
			if (rs.next()) {
				user = new User(rs.getInt(1), rs.getString("Username"), rs.getString("Password"),
						rs.getString("FirstName"), rs.getString("LastName"), rs.getBoolean("Active"),
						rs.getString("Mail"), rs.getString("City"), rs.getString("Avatar"));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}

		return Optional.ofNullable(user);
	}

	@Override
	public void save(User user) {
		Connection connection = null;
		ResultSet generatedKeys = null;
		Object[] values = { user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(),
				user.getActive(), user.getMail(), user.getCity(), user.getAvatar() };

		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_INSERT, true, values);
			statement.executeUpdate();
			generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next())
				user.setUserId(generatedKeys.getInt(1));
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
	}

	@Override
	public boolean update(User user) {
		Connection connection = null;
		boolean success = false;
		Object[] values = { user.getUsername(), user.getFirstName(), user.getLastName(), user.getActive(),
				user.getMail(), user.getCity(), user.getUserId() };

		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_UPDATE, false, values);
			success = statement.executeUpdate() > 0;
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		return success;
	}

	@Override
	public boolean delete(Integer id) {
		Connection connection = null;
		boolean success = false;
		Object[] values = { id };

		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_DELETE, false, values);
			success = statement.executeUpdate() > 0;
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
		return success;
	}

	@Override
	public Pageable<User> getAll(Page page) {
		List<User> users = new ArrayList<>();
		Connection connection = null;
		ResultSet rs = null;
		int totalCount = 0;
		String searchQuery = "%" + page.getSearchQuery() + "%";
		Object[] values = { searchQuery , searchQuery, page.getPageSize(), (page.getPageNumber() - 1) * page.getPageSize() };
		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_GET_ALL_PAGINATED, false, values);
			rs = statement.executeQuery();
			while (rs.next()) {
				User user = new User(rs.getInt(1), rs.getString("Username"), rs.getString("Password"),
						rs.getString("FirstName"), rs.getString("LastName"), rs.getBoolean("Active"),
						rs.getString("Mail"), rs.getString("City"), rs.getString("Avatar"));
				users.add(user);
			}
			statement.close();

			statement = Utility.prepareStatement(connection, SQL_GET_COUNT, false, new Object[] {});
			rs = statement.executeQuery();
			if (rs.next())
				totalCount = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}

		int totalPages = (int) Math.ceil((double) totalCount / page.getPageSize());
		Pageable<User> result = new Pageable<>(totalPages, totalCount, page.getPageNumber(), users.size(),
				page.getPageSize(), users);
		return result;
	}

}
