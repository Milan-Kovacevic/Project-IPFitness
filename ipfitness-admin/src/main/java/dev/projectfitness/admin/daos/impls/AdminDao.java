package dev.projectfitness.admin.daos.impls;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import dev.projectfitness.admin.daos.Dao;
import dev.projectfitness.admin.daos.pooling.ConnectionPool;
import dev.projectfitness.admin.dtos.Admin;
import dev.projectfitness.admin.util.Utility;

public class AdminDao implements Dao<Admin, Integer> {
	private final ConnectionPool connectionPool;
	private static final String SQL_GET_BY_USERNAME_QUERY = "SELECT * FROM administrator where Username=?";

	public AdminDao() {
		super();
		connectionPool = ConnectionPool.getInstance();
	}

	public Optional<Admin> getByUsername(String username) {
		Admin admin = null;
		Connection connection = null;
		ResultSet rs = null;
		Object[] values = { username };

		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_GET_BY_USERNAME_QUERY, false,
					values);
			rs = statement.executeQuery();
			if (rs.next()) {
				admin = new Admin(rs.getInt(1), rs.getString("Username"), rs.getString("Password"),
						rs.getString("FirstName"), rs.getString("LastName"));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}

		return Optional.ofNullable(admin);
	}

}
