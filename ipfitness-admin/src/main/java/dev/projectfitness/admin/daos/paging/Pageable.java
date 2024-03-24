package dev.projectfitness.admin.daos.paging;

import java.util.List;

public class Pageable<T> {
	private int totalPages;
	private int totalItems;
	private int page;
	private int pageItems;
	private int pageSize;
	private List<T> items;

	public Pageable() {
		super();
	}

	public Pageable(int totalPages, int totalItems, int page, int pageItems, int pageSize, List<T> items) {
		super();
		this.totalPages = totalPages;
		this.totalItems = totalItems;
		this.page = page;
		this.pageItems = pageItems;
		this.pageSize = pageSize;
		this.items = items;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getPageItems() {
		return pageItems;
	}

	public void setPageItems(int pageItems) {
		this.pageItems = pageItems;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

}
