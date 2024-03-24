package dev.projectfitness.admin.beans;

import dev.projectfitness.admin.dtos.Admin;

public class AdminBean {
	private Admin admin;
	private boolean isAuthenticated;

	public AdminBean() {
		super();
	}

	public AdminBean(Admin admin) {
		super();
		isAuthenticated = false;
		this.admin = admin;
	}

	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

}
