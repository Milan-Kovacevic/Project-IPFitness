<%@page import="dev.projectfitness.admin.dtos.CategoryAttribute"%>
<%@page import="dev.projectfitness.admin.beans.CategoryBean"%>
<%@page import="dev.projectfitness.admin.daos.paging.Pageable"%>
<%@page import="dev.projectfitness.admin.util.Constants"%>
<%@page import="dev.projectfitness.admin.util.Constants.SessionParams"%>
<%@page import="dev.projectfitness.admin.util.Constants.ActionQuery"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%
Pageable<CategoryBean> categories = null;
if (session.getAttribute(SessionParams.CATEGORIES_SESSION_ATTRIBUTE_NAME) != null
		&& session.getAttribute(SessionParams.CATEGORIES_SESSION_ATTRIBUTE_NAME) instanceof Pageable<?>)
	categories = (Pageable<CategoryBean>) session.getAttribute(SessionParams.CATEGORIES_SESSION_ATTRIBUTE_NAME);

CategoryBean beanDetails = null;
if (session.getAttribute(SessionParams.SHOW_DETAILS_ITEM) != null)
	beanDetails = (CategoryBean) session.getAttribute(SessionParams.SHOW_DETAILS_ITEM);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" href="./css/styles.css">
<link rel="stylesheet" href="./css/table.css">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="./js/categories.js"></script>
<script src="./js/global.js"></script>
<title><%=Constants.APPLICATION_TITLE%></title>
</head>
<body onload="onLoad(<%=beanDetails != null ? "true" : "false"%>)">
	<div class="background-div"></div>
	<div class="main-div">
		<%@ include file="/WEB-INF/components/header.jsp"%>

		<div class="floating-card">
			<div class="container-sm table-container table-responsive">
				<div class="table-wrapper">
					<div class="table-title">
						<div class="row">
							<div class="col-sm-8">
								<h2 class="table-title">
									<strong class="text-secondary">Manage</strong> IP Fitness
									categories
								</h2>
							</div>
						</div>
						<div class="d-flex justify-content-start mt-3">
							<form id="search-form" action="javascript:searchData()"
								class="search-box">
								<i class="material-symbols-outlined">search</i> <input
									id="search-query" type="text" class="form-control"
									placeholder="Search&hellip;"
									value="<%=(session.getAttribute(SessionParams.SEARCH_CATEGORIES_QUERY) != null
		? session.getAttribute(SessionParams.SEARCH_CATEGORIES_QUERY)
		: "")%>">
							</form>
							<div class="ms-auto">
								<button data-bs-target="#addCategoryModal"
									data-bs-toggle="modal" type="button"
									class="img-button btn btn-outline-secondary">
									<i class="material-symbols-outlined"> add_circle </i> Add New
								</button>
							</div>
						</div>
					</div>
					<%
					if (session.getAttribute(SessionParams.ERROR_MESSAGE) != null) {
						out.print("<div class=\"alert alert-danger mb-0 mt-3\">");
						out.print("<strong>Error!</strong>&nbsp;" + session.getAttribute(SessionParams.ERROR_MESSAGE));
						out.print("</div>");
					} else if (session.getAttribute(SessionParams.SUCCESS_MESSAGE) != null) {
						out.print("<div class=\"alert alert-success mb-0 mt-3\">");
						out.print("<strong>Success!</strong>&nbsp;" + session.getAttribute(SessionParams.SUCCESS_MESSAGE));
						out.print("</div>");
					}
					%>
					<table class="table align-middle table-hover table-responsive">
						<thead class="table-secondary">
							<%
							if (categories != null && categories.getItems().size() > 0) {
								out.print("<tr>");
								out.print("<th>#</th>");
								out.print("<th>Category Name</th>");
								out.print("<th>Total attributes added</th>");
								out.print("<th>Actions</th>");
								out.print("</tr>");
							} else {
								out.print("<p class=\"mt-3\"><i class=\"fs-5\">There are no categories to show now...</i></p>");
							}
							%>
						</thead>
						<tbody>
							<%
							if (categories != null) {
								for (int i = 0; i < categories.getItems().size(); i++) {
									CategoryBean bean = categories.getItems().get(i);
									out.print("<tr id=tr-" + i + ">");
									out.print("<td>" + bean.getCategory().getCategoryId() + "</td>");
									out.print("<td>" + bean.getCategory().getName() + "</td>");
									out.print("<td>" + bean.getCategory().getAttributes().size() + "</td>");
									out.print("<td>" + "<a href=\"javascript:showCategoryDetails(" + bean.getCategory().getCategoryId()
									+ ")\" class=\"text-muted edit edit-id\" title=\"Edit\"><i id=\"ubtn-" + i
									+ "\"  class=\"material-symbols-outlined\">edit</i></a>");
									out.print(
									"<a href=\"#deleteCategoryModal\" class=\"text-muted delete delete-id\" data-bs-toggle=\"modal\" title=\"Delete\"><i id=\"dbtn-"
											+ i + "\" class=\"material-symbols-outlined\">delete</i></a>");
									out.print("</td>");
									out.print("</tr>");
								}
							}
							%>
						</tbody>
					</table>


					<div class="pagination-container">
						<div class="hint-text">
							Showing <b><%=((categories != null && categories.getItems().size() > 0)
		? ((categories.getPage() - 1) * categories.getPageSize()) + 1
		: 0)%> - <%=categories != null ? (categories.getPage() - 1) * (categories.getPageSize()) + categories.getPageItems() : 0%>
							</b>out of <b> <%=categories != null ? categories.getTotalItems() : 0%>
							</b> total entries
						</div>
						<form id="page-form" method="post" class="pagination"
							action="<%=ActionQuery.CATEGORIES_QUERY%>">
							<div
								class="page-item<%=categories != null && categories.getPage() > 1 ? "" : " disabled"%>">
								<a href="javascript:goToPreviousPage()" title="Previous"
									class="page-link"> <i class="material-symbols-outlined">navigate_before</i>
								</a>
							</div>
							<%
							if (categories != null) {
								for (int i = 0; i < categories.getTotalPages(); i++) {
									out.print("<div class=\"page-item" + (categories.getPage() == (i + 1) ? " active" : "") + "\">"
									+ "<a href=\"javascript:setPage(" + (i + 1) + ")\" class=\"page-link\">" + (i + 1) + "</a>" + "</li>");

									out.print("</div>");
								}
							}
							%>
							<div
								class="page-item<%=(categories == null || categories.getTotalPages() <= categories.getPage() ? " disabled" : "")%>">
								<a href="javascript:goToNextPage()" title="Next"
									class="page-link"><i class="material-symbols-outlined">navigate_next</i></a>
							</div>
						</form>
					</div>

				</div>
			</div>
		</div>
		<!-- Delete Modal -->
		<div id="deleteCategoryModal" class="modal fade">
			<div class="modal-dialog">
				<div class="modal-content modal-container">
					<form onsubmit="submitDelete()" id="delete-form" method="post"
						action="<%=ActionQuery.CATEGORY_DELETE_QUERY%>">
						<div class="modal-header">
							<h4 class="modal-title">Delete Category</h4>
						</div>
						<div class="modal-body">
							<p class="fs-5">Are you sure you want to delete this record?</p>
							<p class="text-warning">This action cannot be undone.</p>
						</div>
						<div class="modal-footer">
							<input type="button" class="btn btn-default"
								data-bs-dismiss="modal" value="Cancel"> <input
								type="submit" class="btn btn-danger" value="Delete">
						</div>
					</form>
				</div>
			</div>
		</div>
		<!-- Add Modal -->
		<div id="addCategoryModal" class="modal fade">
			<div class="modal-dialog">
				<div class="modal-content modal-container">
					<div class="modal-header">
						<h4 class="modal-title">Add New Category</h4>
					</div>
					<div class="modal-body">
						<form onsubmit="addCategory()" id="insertCategoryForm"
							method="post" action="<%=ActionQuery.CATEGORY_ADD_QUERY%> ">
							<div class="form-group">
								<label class="mb-1">Category Name:</label> <input type="text"
									placeholder="Enter category name..." name="name"
									class="form-control" required>
							</div>
							<div class="form-group">
								<label class="mb-0 mt-3">Category Attributes:</label>
								<p class="note-p mb-2 mt-0">
									<b>Note: </b><span>drag n drop attributes to change
										order...</span>
								</p>
								<div id="attributesListOnInsert"
									class="list-group list-group-numbered draggable-container">
									<!-- Items are injected here with JS -->
								</div>
							</div>
						</form>
						<form class="mt-3" id="attributeFormOnInsert"
							action="javascript:addAttribute(false)">
							<p class="mb-1">Add new attribute here...</p>
							<input name="attributeName" id="attributeName" type="text"
								required placeholder="Enter attribute name..."
								class="form-control mb-1"> <input type="submit"
								class="btn btn-outline-light text-dark" value="Add Attribute">
						</form>
					</div>
					<div class="modal-footer">
						<input type="button" class="btn btn-default"
							data-bs-dismiss="modal" value="Cancel"> <input
							onclick="addCategory()" type="submit" class="btn btn-secondary"
							value="Add Category">
					</div>
				</div>
			</div>
		</div>
		<!-- Edit Modal -->
		<div id="editCategoryModal" class="modal fade">
			<div class="modal-dialog">
				<div class="modal-content modal-container">
					<div class="modal-header">
						<h4 class="modal-title">Update Category Details</h4>
					</div>
					<div class="modal-body">
						<form onsubmit="updateCategory()" id="editCategoryForm"
							method="post" action="<%=ActionQuery.CATEGORY_EDIT_QUERY%> ">
							<div class="form-group">
								<label class="mb-1">Category Name:</label> <input type="text"
									placeholder="Enter category name..." name="name"
									class="form-control" required
									<%=beanDetails != null ? " value=\"" + beanDetails.getCategory().getName() + "\"" : ""%>>
								<%
								if (beanDetails != null) {
									out.print(
									"<input type=\"hidden\" value=\"" + beanDetails.getCategory().getCategoryId() + "\" name=\"categoryId\">");
								}
								%>
							</div>
							<div class="form-group">
								<label class="mb-0 mt-3">Category Attributes:</label>
								<p class="note-p mb-2 mt-0">
									<b>Note: </b><span>drag n drop attributes to change
										order...</span>
								</p>
								<div id="attributesListOnEdit"
									class="list-group list-group-numbered draggable-container">
									<%
									if (beanDetails != null) {
										for (CategoryAttribute attr : beanDetails.getCategory().getAttributes()) {
											out.print(
											"<div class=\"list-group-item list-group-item-action draggable-item d-flex justify-content-start gap-1 align-items-center\""
													+ " draggable=\"true\">");
											out.print("<p class=\"m-0 flex-grow-1\">" + attr.getName() + "</p>");
											out.print("<i class=\"text-secondary cursor-pointer material-symbols-outlined\">close_small</i>");
											out.print("<span style=\"display: none\">" + attr.getAttributeId() + "</span>");
											out.print("</div>");
										}
									}
									%>
								</div>
							</div>
						</form>
						<form class="mt-3" id="attributeFormOnEdit"
							action="javascript:addAttribute(true)">
							<p class="mb-1">Add new attribute here...</p>
							<input name="attributeName" id="attributeName" type="text"
								required placeholder="Enter attribute name..."
								class="form-control mb-1"> <input type="submit"
								class="btn btn-outline-light text-dark" value="Add Attribute">
						</form>
					</div>
					<div class="modal-footer">
						<input type="button" class="btn btn-default"
							data-bs-dismiss="modal" value="Cancel"> <input
							onclick="updateCategory()" type="submit"
							class="btn btn-secondary" value="Update">
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>