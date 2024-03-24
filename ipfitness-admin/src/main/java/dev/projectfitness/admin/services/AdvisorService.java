package dev.projectfitness.admin.services;

import java.util.stream.Collectors;

import dev.projectfitness.admin.beans.AdvisorBean;
import dev.projectfitness.admin.daos.DaoFactory;
import dev.projectfitness.admin.daos.impls.AdvisorDao;
import dev.projectfitness.admin.daos.paging.Page;
import dev.projectfitness.admin.daos.paging.Pageable;
import dev.projectfitness.admin.dtos.Advisor;
import dev.projectfitness.admin.util.Constants;

public class AdvisorService {
	private final AdvisorDao advisorDao;

	public AdvisorService() {
		super();
		advisorDao = DaoFactory.getInstance().getAdvisorDao();
	}

	public Pageable<AdvisorBean> getAllAdvisors(int pageNumber, String searchQuery) {
		int pageNum = 1;
		if (pageNumber > 1)
			pageNum = pageNumber;

		Page page = new Page(pageNum, Constants.PAGE_SIZE, searchQuery);
		Pageable<Advisor> advisors = advisorDao.getAll(page);
		return new Pageable<AdvisorBean>(advisors.getTotalPages(), advisors.getTotalItems(), advisors.getPage(),
				advisors.getPageItems(), advisors.getPageSize(),
				advisors.getItems().stream().map(a -> new AdvisorBean(a)).collect(Collectors.toList()));
	}

	public boolean existsByUsername(String username) {
		return advisorDao.getByUsername(username).isPresent();
	}

	public void insertAdvisor(Advisor advisor) {
		if (advisor == null)
			return;
		advisorDao.save(advisor);
	}

	public boolean updateAdvisor(Advisor advisor) {
		if (advisor == null)
			return false;
		return advisorDao.update(advisor);
	}

	public boolean deleteAdvisor(Integer advisorId) {
		return advisorDao.delete(advisorId);
	}

}
