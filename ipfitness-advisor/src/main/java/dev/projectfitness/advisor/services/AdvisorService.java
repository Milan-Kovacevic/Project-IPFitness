package dev.projectfitness.advisor.services;

import java.util.Optional;

import dev.projectfitness.advisor.beans.AdvisorBean;
import dev.projectfitness.advisor.dtos.Advisor;
import dev.projectfitness.advisor.dаоs.AdvisorDao;
import dev.projectfitness.advisor.dаоs.DaoFactory;

public class AdvisorService {
	private final AdvisorDao advisorDao;

	public AdvisorService() {
		super();
		advisorDao = DaoFactory.getInstance().getAdvisorDao();
	}

	public AdvisorBean login(String username, String password) {
		Optional<Advisor> optAdvisor = advisorDao.getByUsername(username);
		if (optAdvisor.isEmpty())
			return null;
		Advisor advisor = optAdvisor.get();
		if (!password.equals(advisor.getPassword()))
			return null;

		AdvisorBean bean = new AdvisorBean(advisor, true);
		return bean;
	}

	public void logout(AdvisorBean bean) {
		if (bean == null)
			return;
		bean.setAdvisor(null);
		bean.setAuthenticated(false);
	}

}
