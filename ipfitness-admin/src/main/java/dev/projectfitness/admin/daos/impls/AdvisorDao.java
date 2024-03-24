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
import dev.projectfitness.admin.dtos.Advisor;
import dev.projectfitness.admin.util.Utility;

public class AdvisorDao implements PageableDao<Advisor, Integer> {
	private final ConnectionPool connectionPool;
	private static final String SQL_INSERT = "INSERT INTO advisor (DisplayName, Username, Password, Email, AdminId) VALUES (?,?,?,?,?)";
	private static final String SQL_UPDATE = "UPDATE advisor SET DisplayName=?, Username=?, Password=?, Email=? WHERE AdvisorId=?";
	private static final String SQL_UPDATE_NO_PASSWORD = "UPDATE advisor SET DisplayName=?, Username=?, Email=? WHERE AdvisorId=?";
	private static final String SQL_DELETE = "DELETE FROM advisor WHERE AdvisorId=?";
	private static final String SQL_GET_BY_ID = "SELECT * FROM advisor where AdvisorId=?";
	private static final String SQL_GET_BY_USERNAME = "SELECT * FROM advisor where Username=?";
	private static final String SQL_GET_COUNT = "SELECT count(*) FROM advisor";
	private static final String SQL_GET_ALL_PAGINATED = "SELECT * FROM advisor WHERE DisplayName like ? LIMIT ? OFFSET ? ";

	public AdvisorDao() {
		super();
		connectionPool = ConnectionPool.getInstance();
	}

	@Override
	public Pageable<Advisor> getAll(Page page) {
		List<Advisor> advisors = new ArrayList<>();
		Connection connection = null;
		ResultSet rs = null;
		int totalCount = 0;
		String searchQuery = "%" + page.getSearchQuery() + "%";
		Object[] values = { searchQuery, page.getPageSize(), (page.getPageNumber() - 1) * page.getPageSize() };
		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_GET_ALL_PAGINATED, false, values);
			rs = statement.executeQuery();
			while (rs.next()) {
				Advisor advisor = new Advisor(rs.getInt(1), rs.getString("DisplayName"), rs.getString("Username"),
						rs.getString("Password"), rs.getString("Email"), rs.getInt("AdminId"));
				advisors.add(advisor);
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
		Pageable<Advisor> result = new Pageable<>(totalPages, totalCount, page.getPageNumber(), advisors.size(),
				page.getPageSize(), advisors);
		return result;
	}

	@Override
	public Optional<Advisor> getById(Integer id) {
		Advisor advisor = null;
		Connection connection = null;
		ResultSet rs = null;
		Object[] values = { id };

		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_GET_BY_ID, false, values);
			rs = statement.executeQuery();
			if (rs.next()) {
				advisor = new Advisor(rs.getInt(1), rs.getString("DisplayName"), rs.getString("Username"),
						rs.getString("Password"), rs.getString("Email"), rs.getInt("AdminId"));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}

		return Optional.ofNullable(advisor);
	}

	public Optional<Advisor> getByUsername(String username) {
		Advisor advisor = null;
		Connection connection = null;
		ResultSet rs = null;
		Object[] values = { username };

		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_GET_BY_USERNAME, false, values);
			rs = statement.executeQuery();
			if (rs.next()) {
				advisor = new Advisor(rs.getInt(1), rs.getString("DisplayName"), rs.getString("Username"),
						rs.getString("Password"), rs.getString("Email"), rs.getInt("AdminId"));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}

		return Optional.ofNullable(advisor);
	}

	@Override
	public void save(Advisor advisor) {
		Connection connection = null;
		ResultSet generatedKeys = null;
		Object[] values = { advisor.getDisplayName(), advisor.getUsername(), advisor.getPassword(), advisor.getEmail(),
				advisor.getAdminId() };

		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_INSERT, true, values);
			statement.executeUpdate();
			generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next())
				advisor.setAdvisorId(generatedKeys.getInt(1));
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}

	}

	@Override
	public boolean update(Advisor advisor) {
		if (advisor.getPassword() == null || "".equals(advisor.getPassword()))
			return updateWithoutPassword(advisor);

		Connection connection = null;
		boolean success = false;
		Object[] values = { advisor.getDisplayName(), advisor.getUsername(), advisor.getPassword(), advisor.getEmail(),
				advisor.getAdvisorId() };

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

	private boolean updateWithoutPassword(Advisor advisor) {
		Connection connection = null;
		boolean success = false;
		Object[] values = { advisor.getDisplayName(), advisor.getUsername(), advisor.getEmail(),
				advisor.getAdvisorId() };

		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_UPDATE_NO_PASSWORD, false, values);
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

}
