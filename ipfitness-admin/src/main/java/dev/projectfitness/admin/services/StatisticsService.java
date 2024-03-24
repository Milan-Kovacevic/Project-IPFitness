package dev.projectfitness.admin.services;

import java.util.stream.Collectors;

import dev.projectfitness.admin.beans.ActionItemBean;
import dev.projectfitness.admin.daos.DaoFactory;
import dev.projectfitness.admin.daos.impls.StatisticsDao;
import dev.projectfitness.admin.daos.paging.Page;
import dev.projectfitness.admin.daos.paging.Pageable;
import dev.projectfitness.admin.dtos.ActionItem;
import dev.projectfitness.admin.util.Constants;

public class StatisticsService {
	private final StatisticsDao statisticsDao;

	public StatisticsService() {
		super();
		this.statisticsDao = DaoFactory.getInstance().getStatisticsDao();
	}

	public Pageable<ActionItemBean> getAllStatistics(int pageNumber, String searchQuery) {
		int pageNum = 1;
		if (pageNumber > 1)
			pageNum = pageNumber;

		Page page = new Page(pageNum, Constants.PAGE_SIZE, searchQuery);
		Pageable<ActionItem> actions = statisticsDao.getAll(page);
		return new Pageable<ActionItemBean>(actions.getTotalPages(), actions.getTotalItems(), actions.getPage(),
				actions.getPageItems(), actions.getPageSize(),
				actions.getItems().stream().map(a -> new ActionItemBean(a)).collect(Collectors.toList()));
	}
}
