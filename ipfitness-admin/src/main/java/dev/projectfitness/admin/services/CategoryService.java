package dev.projectfitness.admin.services;

import java.util.Optional;
import java.util.stream.Collectors;

import dev.projectfitness.admin.beans.CategoryBean;
import dev.projectfitness.admin.daos.DaoFactory;
import dev.projectfitness.admin.daos.impls.CategoryAttributeDao;
import dev.projectfitness.admin.daos.impls.CategoryDao;
import dev.projectfitness.admin.daos.paging.Page;
import dev.projectfitness.admin.daos.paging.Pageable;
import dev.projectfitness.admin.dtos.Category;
import dev.projectfitness.admin.dtos.CategoryAttribute;
import dev.projectfitness.admin.dtos.EditCategory;
import dev.projectfitness.admin.util.Constants;

public class CategoryService {
	private final CategoryDao categoryDao;
	private final CategoryAttributeDao categoryAttributeDao;

	public CategoryService() {
		super();
		this.categoryDao = DaoFactory.getInstance().getCategoryDao();
		this.categoryAttributeDao = DaoFactory.getInstance().getCategoryAttributeDao();
	}

	public Pageable<CategoryBean> getAllCategories(int pageNumber, String searchQuery) {
		int pageNum = 1;
		if (pageNumber > 1)
			pageNum = pageNumber;

		Page page = new Page(pageNum, Constants.PAGE_SIZE, searchQuery);
		Pageable<Category> categories = categoryDao.getAll(page);
		return new Pageable<CategoryBean>(categories.getTotalPages(), categories.getTotalItems(), categories.getPage(),
				categories.getPageItems(), categories.getPageSize(),
				categories.getItems().stream().map(c -> new CategoryBean(c)).collect(Collectors.toList()));
	}

	public void insertCategory(Category category) {
		if (category != null)
			categoryDao.save(category);
	}

	public boolean updateCategory(EditCategory dto) {
		if (dto == null)
			return false;
		Category category = new Category(dto.getCategoryId(), dto.getName(), dto.getAttributes());
		boolean success = categoryDao.update(category);

		if (success) {
			for (CategoryAttribute atr : dto.getDeleted()) {
				categoryAttributeDao.delete(atr.getAttributeId());
			}
			for (CategoryAttribute atr : dto.getAttributes()) {
				atr.setCategoryId(dto.getCategoryId());
				if (atr.getAttributeId() != null)
					categoryAttributeDao.update(atr);
				else
					categoryAttributeDao.save(atr);
			}
		}

		return success;
	}

	public boolean deleteCategory(Integer categoryId) {
		return categoryDao.delete(categoryId);
	}

	public CategoryBean getById(int categoryId) {
		Optional<Category> result = categoryDao.getById(categoryId);
		if (result.isEmpty())
			return null;
		return new CategoryBean(result.get());
	}

	public boolean existsByName(String name) {
		return categoryDao.getByName(name).isPresent();
	}
}
