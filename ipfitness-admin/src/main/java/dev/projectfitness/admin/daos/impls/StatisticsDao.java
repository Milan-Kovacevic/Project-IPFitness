package dev.projectfitness.admin.daos.impls;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import dev.projectfitness.admin.daos.Dao;
import dev.projectfitness.admin.daos.paging.Page;
import dev.projectfitness.admin.daos.paging.Pageable;
import dev.projectfitness.admin.daos.pooling.ConnectionPool;
import dev.projectfitness.admin.dtos.ActionItem;
import dev.projectfitness.admin.util.Utility;

public class StatisticsDao implements Dao<ActionItem, Integer> {
	private final ConnectionPool connectionPool;
	private static final String SQL_GET_COUNT = "SELECT count(*) FROM user_action";
	private static final String SQL_GET_ALL_PAGINATED = "SELECT * FROM user_action ORDER BY ActionTime DESC, ActionId ASC LIMIT ? OFFSET ?";

	public StatisticsDao() {
		super();
		connectionPool = ConnectionPool.getInstance();
	}

	public Pageable<ActionItem> getAll(Page page) {
		List<ActionItem> statistics = new ArrayList<>();
		Connection connection = null;
		ResultSet rs = null;
		int totalCount = 0;
		Object[] values = { page.getPageSize(), (page.getPageNumber() - 1) * page.getPageSize() };
		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_GET_ALL_PAGINATED, false, values);
			rs = statement.executeQuery();
			while (rs.next()) {
				Instant instant = rs.getTimestamp("ActionTime").toInstant();
				ActionItem item = new ActionItem(rs.getInt(1), rs.getString("IpAddress"), rs.getString("Endpoint"),
						rs.getString("Message"), rs.getInt("Severity"), Utility.formatDate(instant));
				statistics.add(item);
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
		Pageable<ActionItem> result = new Pageable<>(totalPages, totalCount, page.getPageNumber(), statistics.size(),
				page.getPageSize(), statistics);
		return result;
	}

}
