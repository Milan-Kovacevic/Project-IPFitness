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
import dev.projectfitness.admin.dtos.Category;
import dev.projectfitness.admin.dtos.CategoryAttribute;
import dev.projectfitness.admin.util.Utility;

public class CategoryDao implements PageableDao<Category, Integer> {
	private final ConnectionPool connectionPool;
	private static final String SQL_INSERT = "INSERT INTO category (Name) VALUES (?)";
	private static final String SQL_UPDATE = "UPDATE category SET Name=? WHERE CategoryId=?";
	private static final String SQL_DELETE = "DELETE FROM category WHERE CategoryId=?";
	private static final String SQL_GET_BY_ID = "SELECT * FROM category where CategoryId=?";
	private static final String SQL_GET_COUNT = "SELECT count(*) FROM category";
	private static final String SQL_GET_ALL_PAGINATED = "SELECT * FROM category WHERE Name like ? LIMIT ? OFFSET ?";
	private static final String SQL_GET_BY_NAME = "SELECT * FROM category WHERE Name=?";

	private final CategoryAttributeDao categoryAttributeDao;

	public CategoryDao(CategoryAttributeDao categoryAttributeDao) {
		super();
		connectionPool = ConnectionPool.getInstance();
		this.categoryAttributeDao = categoryAttributeDao;
	}

	@Override
	public Pageable<Category> getAll(Page page) {
		List<Category> categories = new ArrayList<>();
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
				List<CategoryAttribute> attributes = categoryAttributeDao.getByCategory(rs.getInt(1));
				Category category = new Category(rs.getInt(1), rs.getString("Name"), attributes);
				categories.add(category);
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
		Pageable<Category> result = new Pageable<>(totalPages, totalCount, page.getPageNumber(), categories.size(),
				page.getPageSize(), categories);
		return result;
	}

	@Override
	public Optional<Category> getById(Integer id) {
		Category category = null;
		Connection connection = null;
		ResultSet rs = null;
		Object[] values = { id };

		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_GET_BY_ID, false, values);
			rs = statement.executeQuery();
			if (rs.next()) {
				List<CategoryAttribute> attributes = categoryAttributeDao.getByCategory(rs.getInt(1));
				category = new Category(rs.getInt(1), rs.getString("Name"), attributes);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}

		return Optional.ofNullable(category);
	}

	@Override
	public void save(Category category) {
		Connection connection = null;
		ResultSet generatedKeys = null;
		Object[] values = { category.getName() };

		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_INSERT, true, values);
			statement.executeUpdate();
			generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				category.setCategoryId(generatedKeys.getInt(1));
				for (CategoryAttribute atr : category.getAttributes()) {
					atr.setCategoryId(category.getCategoryId());
					categoryAttributeDao.save(atr);
				}
			}

			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
	}

	@Override
	public boolean update(Category category) {
		Connection connection = null;
		boolean success = false;
		Object[] values = { category.getName(), category.getCategoryId() };

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

	public Optional<Category> getByName(String name) {
		Category category = null;
		Connection connection = null;
		ResultSet rs = null;
		Object[] values = { name };

		try {
			connection = connectionPool.checkOut();
			PreparedStatement statement = Utility.prepareStatement(connection, SQL_GET_BY_NAME, false, values);
			rs = statement.executeQuery();
			if (rs.next()) {
				List<CategoryAttribute> attributes = categoryAttributeDao.getByCategory(rs.getInt(1));
				category = new Category(rs.getInt(1), rs.getString("Name"), attributes);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}

		return Optional.ofNullable(category);
	}

}
