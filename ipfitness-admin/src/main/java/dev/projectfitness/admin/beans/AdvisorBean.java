package dev.projectfitness.admin.beans;

import dev.projectfitness.admin.dtos.Advisor;

public class AdvisorBean {
	private Advisor advisor;

	public AdvisorBean() {
		super();
	}

	public AdvisorBean(Advisor advisor) {
		super();
		this.advisor = advisor;
	}

	public Advisor getAdvisor() {
		return advisor;
	}

	public void setAdvisor(Advisor advisor) {
		this.advisor = advisor;
	}

}
