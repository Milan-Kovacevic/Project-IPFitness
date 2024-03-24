package dev.projectfitness.admin.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import dev.projectfitness.admin.beans.CategoryBean;
import dev.projectfitness.admin.daos.paging.Pageable;
import dev.projectfitness.admin.dtos.Category;
import dev.projectfitness.admin.dtos.CategoryAttribute;
import dev.projectfitness.admin.dtos.EditCategory;
import dev.projectfitness.admin.services.CategoryService;
import dev.projectfitness.admin.util.Constants.RequestParams;
import dev.projectfitness.admin.util.Constants.SessionParams;
import dev.projectfitness.admin.util.Utility;

public class CategoriesHandler {
	private final NavigationHandler navigationHandler;
	private final CategoryService categoryService;

	public CategoriesHandler(NavigationHandler navigationHandler, CategoryService categoryService) {
		super();
		this.navigationHandler = navigationHandler;
		this.categoryService = categoryService;
	}

	public void handleCategoriesRedirect(HttpServletRequest request, HttpServletResponse response) {
		if (!Utility.isAdminAuthenticated(request, response)) {
			navigationHandler.navigateToLogin(request, response);
			return;
		}

		Utility.clearMessagesFromSession(request.getSession());
		storeCategoriesInSession(request);
		navigationHandler.navigateToCategories(request, response);
	}

	public void handleCategoryInsert(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		if (!Utility.isAdminAuthenticated(request, response)) {
			navigationHandler.navigateToLogin(request, response);
			return;
		}
		Utility.clearMessagesFromSession(session);

		Category category = parseCategoryFromRequestData(request);
		if (category == null) {
			session.setAttribute(SessionParams.ERROR_MESSAGE, "All category parameters must be filled.");
			navigationHandler.navigateToCategories(request, response);
			return;
		}

		if (categoryService.existsByName(category.getName())) {
			session.setAttribute(SessionParams.ERROR_MESSAGE, "Category name is already taken.");
			navigationHandler.navigateToCategories(request, response);
			return;
		}

		categoryService.insertCategory(category);

		storeCategoriesInSession(request);

		navigationHandler.navigateToCategories(request, response);
	}

	public void handleCategoryUpdate(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		if (!Utility.isAdminAuthenticated(request, response)) {
			navigationHandler.navigateToLogin(request, response);
			return;
		}
		Utility.clearMessagesFromSession(session);

		EditCategory category = parseCategoryForEditFromRequestData(request);
		if (category == null) {
			session.setAttribute(SessionParams.ERROR_MESSAGE, "All category parameters must be filled.");
			navigationHandler.navigateToCategories(request, response);
			return;
		}

		boolean success = categoryService.updateCategory(category);
		if (!success) {
			session.setAttribute(SessionParams.ERROR_MESSAGE,
					"Unable to update category details. Check if name of category is already taken.");
		} else {
			session.setAttribute(SessionParams.SUCCESS_MESSAGE, "Category details updated successfully.");
		}

		storeCategoriesInSession(request);
		navigationHandler.navigateToCategories(request, response);
	}

	public void handleCategoryDelete(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		if (!Utility.isAdminAuthenticated(request, response)) {
			navigationHandler.navigateToLogin(request, response);
			return;
		}
		Utility.clearMessagesFromSession(session);

		if (request.getParameter("categoryId") == null) {
			session.setAttribute(SessionParams.ERROR_MESSAGE, "All category related parameters must be filled.");
			navigationHandler.navigateToCategories(request, response);
			return;
		}

		Integer categoryId = Integer.parseInt(request.getParameter("categoryId"));
		boolean success = categoryService.deleteCategory(categoryId);
		if (!success) {
			session.setAttribute(SessionParams.ERROR_MESSAGE, "Selected category is not found.");
		} else {
			session.setAttribute(SessionParams.SUCCESS_MESSAGE, "Category deleted successfully.");
		}

		storeCategoriesInSession(request);
		navigationHandler.navigateToCategories(request, response);
	}

	public void handleCategoryGet(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		if (!Utility.isAdminAuthenticated(request, response)) {
			navigationHandler.navigateToLogin(request, response);
			return;
		}
		Utility.clearMessagesFromSession(session);

		if (request.getParameter("categoryId") != null) {
			int categoryId = Integer.parseInt(request.getParameter("categoryId"));
			CategoryBean bean = categoryService.getById(categoryId);
			session.setAttribute(SessionParams.SHOW_DETAILS_ITEM, bean);
		}

		storeCategoriesInSession(request);
		navigationHandler.navigateToCategories(request, response);
	}

	private Category parseCategoryFromRequestData(HttpServletRequest request) {
		String name = request.getParameter("name");
		String attributesJson = request.getParameter("attributes");

		if (name == null || "".equals(name) || attributesJson == null || "".equals(attributesJson))
			return null;

		Gson gson = new Gson();
		CategoryAttribute[] attributes = gson.fromJson(attributesJson, CategoryAttribute[].class);
		List<CategoryAttribute> list = new ArrayList<CategoryAttribute>();
		for (var attr : attributes)
			list.add(attr);
		return new Category(null, name, list);
	}

	private EditCategory parseCategoryForEditFromRequestData(HttpServletRequest request) {
		String id = request.getParameter("categoryId");
		String name = request.getParameter("name");
		String attributesJson = request.getParameter("attributes");
		String deletedJson = request.getParameter("deleted");

		if (id == null || name == null || "".equals(name) || attributesJson == null || "".equals(attributesJson)
				|| deletedJson == null || "".equals(deletedJson))
			return null;

		Gson gson = new Gson();
		Integer categoryId = Integer.parseInt(id);
		CategoryAttribute[] attributes = gson.fromJson(attributesJson, CategoryAttribute[].class);
		CategoryAttribute[] deleted = gson.fromJson(deletedJson, CategoryAttribute[].class);
		List<CategoryAttribute> lAttributes = new ArrayList<CategoryAttribute>();
		for (var attr : attributes)
			lAttributes.add(attr);
		List<CategoryAttribute> lDeleted = new ArrayList<CategoryAttribute>();
		for (var attr : deleted)
			lDeleted.add(attr);
		return new EditCategory(categoryId, name, lAttributes, lDeleted);
	}

	private void storeCategoriesInSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		int pageNumber = 1;
		String searchQuery = "";
		if (request.getParameter(RequestParams.SEARCH_QUERY) != null) {
			searchQuery = request.getParameter(RequestParams.SEARCH_QUERY);
			session.setAttribute(SessionParams.SEARCH_CATEGORIES_QUERY, searchQuery);
		}
		else if (session.getAttribute(SessionParams.SEARCH_CATEGORIES_QUERY) != null) {
			searchQuery = (String) session.getAttribute(SessionParams.SEARCH_CATEGORIES_QUERY);
		}
		if (session.getAttribute(SessionParams.CATEGORIES_SESSION_ATTRIBUTE_NAME) != null) {
			@SuppressWarnings("unchecked")
			Pageable<CategoryBean> sessionCategories = (Pageable<CategoryBean>) session
					.getAttribute(SessionParams.CATEGORIES_SESSION_ATTRIBUTE_NAME);
			if (request.getParameter(RequestParams.TABLE_PAGE_NUMBER) != null) {
				pageNumber = Integer.valueOf(request.getParameter(RequestParams.TABLE_PAGE_NUMBER));
			} else if (request.getParameter(RequestParams.TABLE_NEXT_PAGE) != null) {
				pageNumber = sessionCategories.getPage() + 1;
			} else if (request.getParameter(RequestParams.TABLE_PREVIOUS_PAGE) != null) {
				pageNumber = sessionCategories.getPage() - 1;
			} else {
				pageNumber = sessionCategories.getPage();
			}
		}
		Pageable<CategoryBean> categories = categoryService.getAllCategories(pageNumber, searchQuery);
		session.setAttribute(SessionParams.CATEGORIES_SESSION_ATTRIBUTE_NAME, categories);
	}

}
