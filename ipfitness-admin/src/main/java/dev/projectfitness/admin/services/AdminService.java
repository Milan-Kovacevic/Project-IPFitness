package dev.projectfitness.admin.services;

import java.util.Optional;

import dev.projectfitness.admin.beans.AdminBean;
import dev.projectfitness.admin.daos.DaoFactory;
import dev.projectfitness.admin.daos.impls.AdminDao;
import dev.projectfitness.admin.dtos.Admin;

public class AdminService {
	private final AdminDao adminDao;

	public AdminService() {
		super();
		adminDao = DaoFactory.getInstance().getAdminDao();
	}

	public AdminBean login(String username, String password) {
		Optional<Admin> optAdmin = adminDao.getByUsername(username);
		if (optAdmin.isEmpty())
			return null;
		Admin admin = optAdmin.get();
		if (!password.equals(admin.getPassword()))
			return null;

		AdminBean bean = new AdminBean(admin);
		bean.setAuthenticated(true);
		return bean;
	}

	public void logout(AdminBean bean) {
		bean.setAdmin(null);
		bean.setAuthenticated(false);
	}
}
