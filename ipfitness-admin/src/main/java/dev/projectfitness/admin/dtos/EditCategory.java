package dev.projectfitness.admin.dtos;

import java.util.List;

public class EditCategory {
	private Integer categoryId;
	private String name;
	private List<CategoryAttribute> attributes;
	private List<CategoryAttribute> deleted;

	public EditCategory() {
		super();
	}

	public EditCategory(Integer categoryId, String name, List<CategoryAttribute> attributes,
			List<CategoryAttribute> deleted) {
		super();
		this.categoryId = categoryId;
		this.name = name;
		this.attributes = attributes;
		this.deleted = deleted;
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

	public List<CategoryAttribute> getDeleted() {
		return deleted;
	}

	public void setDeleted(List<CategoryAttribute> deleted) {
		this.deleted = deleted;
	}

}
