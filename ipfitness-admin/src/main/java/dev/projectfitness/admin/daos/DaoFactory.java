package dev.projectfitness.admin.daos;

import dev.projectfitness.admin.daos.impls.AdminDao;
import dev.projectfitness.admin.daos.impls.AdvisorDao;
import dev.projectfitness.admin.daos.impls.CategoryAttributeDao;
import dev.projectfitness.admin.daos.impls.CategoryDao;
import dev.projectfitness.admin.daos.impls.StatisticsDao;
import dev.projectfitness.admin.daos.impls.UserDao;

public class DaoFactory {
	private static DaoFactory instance;
	private final AdminDao adminDao;
	private final UserDao userDao;
	private final AdvisorDao advisorDao;
	private final CategoryDao categoryDao;
	private final CategoryAttributeDao categoryAttributeDao;
	private final StatisticsDao statisticsDao;

	public static DaoFactory getInstance() {
		if (instance == null)
			instance = new DaoFactory();
		return instance;
	}

	private DaoFactory() {
		adminDao = new AdminDao();
		userDao = new UserDao();
		advisorDao = new AdvisorDao();
		categoryAttributeDao = new CategoryAttributeDao();
		categoryDao = new CategoryDao(categoryAttributeDao);
		statisticsDao = new StatisticsDao();
	}

	public AdminDao getAdminDao() {
		return adminDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public CategoryDao getCategoryDao() {
		return categoryDao;
	}

	public CategoryAttributeDao getCategoryAttributeDao() {
		return categoryAttributeDao;
	}

	public AdvisorDao getAdvisorDao() {
		return advisorDao;
	}

	public StatisticsDao getStatisticsDao() {
		return statisticsDao;
	}

}
