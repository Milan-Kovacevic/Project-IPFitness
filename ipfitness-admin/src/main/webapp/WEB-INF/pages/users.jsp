<%@page import="dev.projectfitness.admin.daos.paging.Pageable"%>
<%@page import="dev.projectfitness.admin.dtos.User"%>
<%@page import="java.util.ArrayList"%>
<%@page import="dev.projectfitness.admin.beans.UserBean"%>
<%@page import="java.util.List"%>
<%@page import="dev.projectfitness.admin.util.Constants"%>
<%@page import="dev.projectfitness.admin.util.Constants.SessionParams"%>
<%@page import="dev.projectfitness.admin.util.Constants.ActionQuery"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%
Pageable<UserBean> users = null;
if (session.getAttribute(SessionParams.USERS_SESSION_ATTRIBUTE_NAME) != null
		&& session.getAttribute(SessionParams.USERS_SESSION_ATTRIBUTE_NAME) instanceof Pageable<?>)
	users = (Pageable<UserBean>) session.getAttribute(SessionParams.USERS_SESSION_ATTRIBUTE_NAME);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" href="./css/styles.css">
<link rel="stylesheet" href="./css/table.css">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="./js/users.js"></script>
<script src="./js/global.js"></script>
<title><%=Constants.APPLICATION_TITLE%></title>
</head>
<body onload="onLoad()">
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
									application users
								</h2>
							</div>
						</div>
						<div class="d-flex justify-content-start mt-3">
							<form id="search-form" action="javascript:searchData()"
								class="search-box">
								<i class="material-symbols-outlined">search</i> <input
									id="search-query" type="text" class="form-control"
									placeholder="Search&hellip;"
									value="<%=(session.getAttribute(SessionParams.SEARCH_USERS_QUERY) != null
		? session.getAttribute(SessionParams.SEARCH_USERS_QUERY)
		: "")%>">
							</form>
							<div class="ms-auto">
								<button data-bs-target="#addUserModal" data-bs-toggle="modal"
									type="button" class="img-button btn btn-outline-secondary">
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
							if (users != null && users.getItems().size() > 0) {
								out.print("<tr>");
								out.print("<th>#</th>");
								out.print("<th>First Name</th>");
								out.print("<th>Last Name</th>");
								out.print("<th>Username</th>");
								out.print("<th>E-Mail</th>");
								out.print("<th>Status</th>");
								out.print("<th>City</th>");
								out.print("<th>Actions</th>");
								out.print("</tr>");
							} else {
								out.print("<p class=\"mt-3\"><i class=\"fs-5\">There are no users to show now...</i></p>");
							}
							%>
						</thead>
						<tbody>
							<%
							if (users != null) {
								for (int i = 0; i < users.getItems().size(); i++) {
									UserBean bean = users.getItems().get(i);
									out.print("<tr id=tr-" + i + ">");
									out.print("<td>" + bean.getUser().getUserId() + "</td>");
									out.print("<td>" + bean.getUser().getFirstName() + "</td>");
									out.print("<td>" + bean.getUser().getLastName() + "</td>");
									out.print("<td>" + bean.getUser().getUsername() + "</td>");
									out.print("<td>" + bean.getUser().getMail() + "</td>");
									out.print("<td>" + "<span class=\"badge "
									+ (bean.getUser().getActive() ? "bg-success\">Active" : "bg-warning\">Inactive") + "</span></td>");
									out.print("<td>" + bean.getUser().getCity() + "</td>");
									out.print("<td>");
									out.print(
									"<a href=\"#editUserModal\" data-bs-toggle=\"modal\" class=\"text-muted edit edit-id\" title=\"Edit\"><i id=\"ubtn-"
											+ i + "\"  class=\"material-symbols-outlined\">edit</i></a>");
									out.print(
									"<a href=\"#deleteUserModal\" class=\"text-muted delete delete-id\" data-bs-toggle=\"modal\" title=\"Delete\"><i id=\"dbtn-"
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
							Showing <b><%=users != null && users.getItems().size() > 0 ? ((users.getPage() - 1) * users.getPageSize()) + 1 : 0%>
								- <%=users != null ? (users.getPage() - 1) * (users.getPageSize()) + users.getPageItems() : 0%></b>
							out of <b><%=users != null ? users.getTotalItems() : 0%></b>
							total entries
						</div>
						<form id="page-form" method="post" class="pagination"
							action="<%=ActionQuery.USERS_QUERY%>">
							<div
								class="page-item<%=users != null && users.getPage() > 1 ? "" : " disabled"%>">
								<a href="javascript:goToPreviousPage()" title="Previous"
									class="page-link"> <i class="material-symbols-outlined">navigate_before</i>
								</a>
							</div>
							<%
							if (users != null) {
								for (int i = 0; i < users.getTotalPages(); i++) {
									out.print("<div class=\"page-item" + (users.getPage() == (i + 1) ? " active" : "") + "\">"
									+ "<a href=\"javascript:setPage(" + (i + 1) + ")\" class=\"page-link\">" + (i + 1) + "</a>" + "</li>");

									out.print("</div>");
								}
							}
							%>
							<div
								class="page-item<%=(users == null || users.getTotalPages() <= users.getPage() ? " disabled" : "")%>">
								<a href="javascript:goToNextPage()" title="Next"
									class="page-link"><i class="material-symbols-outlined">navigate_next</i></a>
							</div>
						</form>
					</div>

				</div>
			</div>
		</div>
		<!-- Delete Modal -->
		<div id="deleteUserModal" class="modal fade">
			<div class="modal-dialog">
				<div class="modal-content modal-container">
					<form onsubmit="submitDelete()" id="delete-form" method="post"
						action="<%=ActionQuery.USER_DELETE_QUERY%>">
						<div class="modal-header">
							<h4 class="modal-title">Delete User</h4>
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
		<!-- Edit Modal -->
		<div id="editUserModal" class="modal fade">
			<div class="modal-dialog">
				<div class="modal-content modal-container">
					<form id="edit-form" method="post"
						action="<%=ActionQuery.USER_EDIT_QUERY%>">
						<div class="modal-header">
							<h4 class="modal-title">Update User Details</h4>
						</div>
						<div class="modal-body">
							<input type="hidden" style="display: none" name="userId">
							<div class="form-group mb-2">
								<label>First Name:</label> <input type="text"
									placeholder="Enter first name..." name="firstName"
									class="form-control" required>
							</div>
							<div class="form-group mb-2">
								<label>Last Name:</label> <input type="text"
									placeholder="Enter last name..." name="lastName"
									class="form-control" required>
							</div>
							<div class="form-group mb-2">
								<label>Username:</label> <input type="text"
									placeholder="Enter username..." name="username"
									class="form-control" required>
							</div>
							<div class="form-group mb-2">
								<label>City:</label> <input type="text"
									placeholder="Enter city name..." name="city"
									class="form-control" required>
							</div>
							<div class="form-group mb-3">
								<label>E-Mail:</label> <input type="email"
									placeholder="Enter e-mail..." name="mail" class="form-control"
									required>
							</div>
							<div class="form-check mb-0">
								<label class="form-check-label"> <input
									class="form-check-input" type="checkbox" name="active">
									Activate
								</label>
							</div>
						</div>
						<div class="modal-footer">
							<input type="button" class="btn btn-default"
								data-bs-dismiss="modal" value="Cancel"> <input
								type="submit" class="btn btn-secondary" value="Update">
						</div>
					</form>
				</div>
			</div>
		</div>
		<!-- Add Modal -->
		<div id="addUserModal" class="modal fade">
			<div class="modal-dialog">
				<div class="modal-content modal-container">
					<form method="post" action="<%=ActionQuery.USER_ADD_QUERY%> ">
						<div class="modal-header">
							<h4 class="modal-title">Add New User</h4>
						</div>
						<div class="modal-body">
							<div class="form-group mb-2">
								<label>First Name:</label> <input type="text"
									placeholder="Enter first name..." name="firstName"
									class="form-control" required>
							</div>
							<div class="form-group mb-2">
								<label>Last Name:</label> <input type="text"
									placeholder="Enter last name..." name="lastName"
									class="form-control" required>
							</div>
							<div class="form-group mb-2">
								<label>Username:</label> <input type="text"
									placeholder="Enter username..." name="username"
									class="form-control" required>
							</div>
							<div class="form-group mb-2">
								<label>Password:</label> <input type="password"
									placeholder="Enter password..." name="password"
									class="form-control" required>
							</div>
							<div class="form-group mb-2">
								<label>City:</label> <input type="text"
									placeholder="Enter city name..." name="city"
									class="form-control" required>
							</div>
							<div class="form-group mb-3">
								<label>E-Mail:</label> <input type="email"
									placeholder="Enter e-mail..." name="mail" class="form-control"
									required>
							</div>
							<div class="form-check mb-0">
								<label class="form-check-label"> <input
									class="form-check-input" type="checkbox" name="active">
									Activate
								</label>
							</div>
						</div>
						<div class="modal-footer">
							<input type="button" class="btn btn-default"
								data-bs-dismiss="modal" value="Cancel"> <input
								type="submit" class="btn btn-secondary" value="Add User">
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>