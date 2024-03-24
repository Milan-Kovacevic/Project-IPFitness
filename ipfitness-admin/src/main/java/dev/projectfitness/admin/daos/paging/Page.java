package dev.projectfitness.admin.daos.paging;

public class Page {
	private int pageNumber;
	private int pageSize;
	private String searchQuery;

	public Page() {
		super();
	}

	public Page(int pageNumber, int pageSize, String searchQuery) {
		super();
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.searchQuery = searchQuery;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

}
