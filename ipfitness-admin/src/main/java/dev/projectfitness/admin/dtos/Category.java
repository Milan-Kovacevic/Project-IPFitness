package dev.projectfitness.admin.dtos;

import java.util.List;

public class Category {
	private Integer categoryId;
	private String name;
	private List<CategoryAttribute> attributes;

	public Category() {
		super();
	}

	public Category(Integer categoryId, String name, List<CategoryAttribute> attributes) {
		super();
		this.categoryId = categoryId;
		this.name = name;
		this.attributes = attributes;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CategoryAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<CategoryAttribute> attributes) {
		this.attributes = attributes;
	}

}
