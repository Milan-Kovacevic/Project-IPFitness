package dev.projectfitness.admin.daos.impls;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.projectfitness.admin.daos.CrudDao;
import dev.projectfitness.admin.daos.pooling.ConnectionPool;
import dev.projectfitness.admin.dtos.CategoryAttribute;
import dev.projectfitness.admin.util.Utility;

public class CategoryAttributeDao implements CrudDao<CategoryAttribute, Integer> {
	private final ConnectionPool connectionPool;
	private static final String SQL_INSERT = "INSERT INTO category_attribute (Name, CategoryId) VALUES (?,?)";
	private static final String SQL_UPDATE = "UPDATE category_attribute SET Name=? , CategoryId=? WHERE AttributeId=?";
	private static final String SQL_DELETE = "DELETE FROM category_attribute WHERE AttributeId=?";
	private static final String SQL_GET_BY_ID = "SELECT * FROM category_attribute where AttributeId=?";
	private static final String SQL_GET_ALL = "SELECT * FROM category_attribute";
	private static final String SQL_GET_BY_CATEGORY = "SELECT * FROM category_attribute WHERE CategoryId=?";

	public CategoryAttributeDao() {
		super();
		connectionPool = ConnectionPool.getInstance();
	}

	public List<CategoryAttribute> getByCategory(int id) {
		List<CategoryAttribute> attributes = new ArrayList<>();
		Connection connection = null;
		ResultSet rs = null;
		Object[] values = { id };
		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_GET_BY_CATEGORY, false, values);
			rs = statement.executeQuery();
			while (rs.next()) {
				CategoryAttribute attribute = new CategoryAttribute(rs.getInt(1), rs.getString("Name"),
						Integer.parseInt(rs.getString("CategoryId")));
				attributes.add(attribute);
			}
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}

		return attributes;
	}

	@Override
	public List<CategoryAttribute> getAll() {
		List<CategoryAttribute> attributes = new ArrayList<>();
		Connection connection = null;
		ResultSet rs = null;
		Object[] values = {};
		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_GET_ALL, false, values);
			rs = statement.executeQuery();
			while (rs.next()) {
				CategoryAttribute attribute = new CategoryAttribute(rs.getInt(1), rs.getString("Name"),
						Integer.parseInt(rs.getString("CategoryId")));
				attributes.add(attribute);
			}
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}

		return attributes;
	}

	@Override
	public Optional<CategoryAttribute> getById(Integer id) {
		CategoryAttribute attribute = null;
		Connection connection = null;
		ResultSet rs = null;
		Object[] values = { id };

		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_GET_BY_ID, false, values);
			rs = statement.executeQuery();
			if (rs.next()) {
				attribute = new CategoryAttribute(rs.getInt(1), rs.getString("Name"),
						Integer.parseInt(rs.getString("CategoryId")));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}

		return Optional.ofNullable(attribute);
	}

	@Override
	public void save(CategoryAttribute attribute) {
		Connection connection = null;
		ResultSet generatedKeys = null;
		Object[] values = { attribute.getName(), attribute.getCategoryId() };

		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_INSERT, true, values);
			statement.executeUpdate();
			generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next())
				attribute.setAttributeId(generatedKeys.getInt(1));
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
	}

	@Override
	public boolean update(CategoryAttribute attribute) {
		Connection connection = null;
		boolean success = false;
		Object[] values = { attribute.getName(), attribute.getCategoryId(), attribute.getAttributeId() };

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

}
