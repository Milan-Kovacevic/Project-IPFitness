package dev.projectfitness.advisor.dаоs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import dev.projectfitness.advisor.dtos.Advisor;
import dev.projectfitness.advisor.util.Utility;
import dev.projectfitness.advisor.daos.pooling.ConnectionPool;

public class AdvisorDao {
	private final ConnectionPool connectionPool;
	private static final String SQL_GET_BY_USERNAME = "SELECT * FROM advisor where Username=?";

	public AdvisorDao() {
		super();
		connectionPool = ConnectionPool.getInstance();
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
				advisor = new Advisor(rs.getInt(1), rs.getString("Username"), rs.getString("Password"),
						rs.getString("DisplayName"));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}

		return Optional.ofNullable(advisor);
	}

}
