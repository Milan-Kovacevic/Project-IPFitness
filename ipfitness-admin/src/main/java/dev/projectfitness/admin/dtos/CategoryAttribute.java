package dev.projectfitness.admin.dtos;

public class CategoryAttribute {
	private Integer attributeId;
	private String name;
	private Integer categoryId;

	public CategoryAttribute() {
		super();
	}

	public CategoryAttribute(Integer attributeId, String name, Integer categoryId) {
		super();
		this.attributeId = attributeId;
		this.name = name;
		this.categoryId = categoryId;
	}

	public Integer getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(Integer attributeId) {
		this.attributeId = attributeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

}
