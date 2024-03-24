package dev.projectfitness.admin.beans;

import dev.projectfitness.admin.dtos.User;

public class UserBean {
	private User user;

	public UserBean() {
		super();
	}

	public UserBean(User user) {
		super();
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
