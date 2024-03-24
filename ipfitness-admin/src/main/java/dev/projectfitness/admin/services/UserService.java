package dev.projectfitness.admin.services;

import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dev.projectfitness.admin.beans.UserBean;
import dev.projectfitness.admin.daos.DaoFactory;
import dev.projectfitness.admin.daos.impls.UserDao;
import dev.projectfitness.admin.daos.paging.Page;
import dev.projectfitness.admin.daos.paging.Pageable;
import dev.projectfitness.admin.dtos.User;
import dev.projectfitness.admin.util.Constants;

public class UserService {
	private final UserDao userDao;

	public UserService() {
		super();
		this.userDao = DaoFactory.getInstance().getUserDao();
	}

	public Pageable<UserBean> getAllUsers(int pageNumber, String searchQuery) {
		int pageNum = 1;
		if (pageNumber > 1)
			pageNum = pageNumber;

		Page page = new Page(pageNum, Constants.PAGE_SIZE, searchQuery);
		Pageable<User> users = userDao.getAll(page);
		return new Pageable<UserBean>(users.getTotalPages(), users.getTotalItems(), users.getPage(),
				users.getPageItems(), users.getPageSize(),
				users.getItems().stream().map(u -> new UserBean(u)).collect(Collectors.toList()));
	}

	public boolean existsByUsername(String username) {
		return userDao.getByUsername(username).isPresent();
	}

	public void insertUser(User user) {
		if (user == null)
			return;
		String password = encryptPassword(user.getPassword());
		user.setPassword(password);
		userDao.save(user);
	}

	public boolean updateUser(User user) {
		if (user == null)
			return false;
		return userDao.update(user);
	}

	public boolean deleteUser(Integer userId) {
		return userDao.delete(userId);
	}

	private String encryptPassword(String password) {
		String result = password;
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(Constants.ENCRYPTION_SERVICE_ENDPOINT);
		Response response = target.request(MediaType.TEXT_PLAIN).accept(MediaType.TEXT_PLAIN).post(Entity.text(password));
		result = response.readEntity(String.class);
		return result;
	}
}
