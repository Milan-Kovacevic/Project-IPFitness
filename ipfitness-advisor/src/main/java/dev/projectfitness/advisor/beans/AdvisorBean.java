package dev.projectfitness.advisor.beans;

import dev.projectfitness.advisor.dtos.Advisor;

public class AdvisorBean {
	private Advisor advisor;
	private boolean isAuthenticated;

	public AdvisorBean() {
		super();
	}

	public AdvisorBean(Advisor advisor, boolean isAuthenticated) {
		super();
		this.advisor = advisor;
		this.isAuthenticated = isAuthenticated;
	}

	public Advisor getAdvisor() {
		return advisor;
	}

	public void setAdvisor(Advisor advisor) {
		this.advisor = advisor;
	}

	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

}
