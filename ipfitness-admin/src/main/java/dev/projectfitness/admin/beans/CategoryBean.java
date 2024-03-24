package dev.projectfitness.admin.beans;

import dev.projectfitness.admin.dtos.Category;

public class CategoryBean {
	private Category category;

	public CategoryBean() {
		super();
	}

	public CategoryBean(Category category) {
		super();
		this.category = category;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}
