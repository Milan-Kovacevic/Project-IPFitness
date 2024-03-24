package dev.projectfitness.admin.daos.pooling;

import java.sql.*;
import java.util.*;

import dev.projectfitness.admin.util.Constants.DataSourceProperties;

public class ConnectionPool {
	private static ConnectionPool instance;
	private String jdbcURL;
	private String username;
	private String password;
	private int preconnectCount;
	private int connectCount;
	private int maxIdleConnections;
	private int maxConnections;
	private Vector<Connection> usedConnections;
	private Vector<Connection> freeConnections;

	public static ConnectionPool getInstance() {
		if (instance == null)
			instance = new ConnectionPool();
		return instance;
	}

	private ConnectionPool() {
		super();
		ResourceBundle bundle = PropertyResourceBundle
				.getBundle("dev.projectfitness.admin.daos.pooling.connectionPool");
		jdbcURL = bundle.getString(DataSourceProperties.JDBC_URL);
		username = bundle.getString(DataSourceProperties.USERNAME);
		password = bundle.getString(DataSourceProperties.PASSWORD);
		String driver = bundle.getString(DataSourceProperties.DRIVER);
		// Default connection pool properties
		preconnectCount = 0;
		maxIdleConnections = 10;
		maxConnections = 10;
		try {
			Class.forName(driver);
			preconnectCount = Integer.parseInt(bundle.getString(DataSourceProperties.PRECONNECT_COUNT));
			maxIdleConnections = Integer.parseInt(bundle.getString(DataSourceProperties.MAX_IDLE_CONNECTIONS));
			maxConnections = Integer.parseInt(bundle.getString(DataSourceProperties.MAX_CONNECTIONS));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		freeConnections = new Vector<Connection>();
		usedConnections = new Vector<Connection>();

		try {
			for (int i = 0; i < preconnectCount; i++) {
				Connection conn;
				conn = DriverManager.getConnection(jdbcURL, username, password);
				conn.setAutoCommit(true);
				freeConnections.addElement(conn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connectCount = preconnectCount;
	}

	public synchronized Connection checkOut() throws SQLException {
		Connection conn = null;
		if (freeConnections.size() > 0) {
			conn = (Connection) freeConnections.elementAt(0);
			freeConnections.removeElementAt(0);
			usedConnections.addElement(conn);
		} else {
			if (connectCount < maxConnections) {
				conn = DriverManager.getConnection(jdbcURL, username, password);
				usedConnections.addElement(conn);
				connectCount++;
			} else {
				try {
					wait();
					conn = (Connection) freeConnections.elementAt(0);
					freeConnections.removeElementAt(0);
					usedConnections.addElement(conn);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
		return conn;
	}

	public synchronized void checkIn(Connection aConn) {
		if (aConn == null)
			return;
		if (usedConnections.removeElement(aConn)) {
			freeConnections.addElement(aConn);
			while (freeConnections.size() > maxIdleConnections) {
				int lastOne = freeConnections.size() - 1;
				Connection conn = (Connection) freeConnections.elementAt(lastOne);
				try {
					conn.close();
				} catch (SQLException ex) {
				}
				freeConnections.removeElementAt(lastOne);
			}
			notify();
		}
	}

}
