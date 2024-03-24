<%@page import="dev.projectfitness.admin.util.Constants.SessionParams"%>
<%@page import="dev.projectfitness.admin.beans.AdvisorBean"%>
<%@page import="dev.projectfitness.admin.daos.paging.Pageable"%>
<%@page import="dev.projectfitness.admin.util.Constants"%>
<%@page import="dev.projectfitness.admin.util.Constants.ActionQuery"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>

<%
Pageable<AdvisorBean> advisors = null;
if (session.getAttribute(SessionParams.ADVISORS_SESSION_ATTRIBUTE_NAME) != null
		&& session.getAttribute(SessionParams.ADVISORS_SESSION_ATTRIBUTE_NAME) instanceof Pageable<?>)
	advisors = (Pageable<AdvisorBean>) session.getAttribute(SessionParams.ADVISORS_SESSION_ATTRIBUTE_NAME);

int pageStart = advisors != null && advisors.getItems().size() > 0
		? ((advisors.getPage() - 1) * advisors.getPageSize()) + 1
		: 0;
int pageEnd = advisors != null ? (advisors.getPage() - 1) * (advisors.getPageSize()) + advisors.getPageItems() : 0;
int totalItems = advisors != null ? advisors.getTotalItems() : 0;

boolean hasPreviousPages = advisors != null && advisors.getPage() > 1;
boolean hasNextPages = advisors != null && advisors.getTotalPages() <= advisors.getPage();

String searchText = session.getAttribute(SessionParams.SEARCH_ADVISORS_QUERY) != null
		? session.getAttribute(SessionParams.SEARCH_ADVISORS_QUERY).toString()
		: "";
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
<script src="./js/advisors.js"></script>
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
									advisors
								</h2>
							</div>
						</div>
						<div class="d-flex justify-content-start mt-3">
							<form id="search-form" action="javascript:searchData()"
								class="search-box">
								<i class="material-symbols-outlined">search</i> <input
									id="search-query" type="text" class="form-control"
									placeholder="Search&hellip;" value="<%=searchText%>">
							</form>
							<div class="ms-auto">
								<button data-bs-target="#addAdvisorModal" data-bs-toggle="modal"
									type="button" class="img-button btn btn-outline-secondary">
									<i class="material-symbols-outlined"> add_circle </i> Add New
								</button>
							</div>
						</div>
					</div>
					<%
					if (session.getAttribute(SessionParams.ERROR_MESSAGE) != null) {
					%>
					<div class="alert alert-danger mb-0 mt-3">
						<strong>Error!</strong>&nbsp;<%=session.getAttribute(SessionParams.ERROR_MESSAGE)%>
					</div>
					<%
					} else if (session.getAttribute(SessionParams.SUCCESS_MESSAGE) != null) {
					%>
					<div class="alert alert-success mb-0 mt-3">
						<strong>Success!</strong>&nbsp;<%=session.getAttribute(SessionParams.SUCCESS_MESSAGE)%>
					</div>
					<%
					}
					%>
					<table class="table align-middle table-hover table-responsive">
						<thead class="table-secondary">
							<%
							if (advisors != null && advisors.getItems().size() > 0) {
							%>
							<tr>
								<th>#</th>
								<th>Display Name</th>
								<th>Username</th>
								<th>Email</th>
								<th>Actions</th>
							</tr>
							<%
							} else {
							%>
							<p class="mt-3">
								<i class="fs-5">There are no advisors to show now...</i>
							</p>
							<%
							}
							%>
						</thead>
						<tbody>
							<%
							if (advisors != null) {
								for (int i = 0; i < advisors.getItems().size(); i++) {
									AdvisorBean bean = advisors.getItems().get(i);
							%>
							<tr id="tr-<%=i%>">
								<td><%=bean.getAdvisor().getAdvisorId()%></td>
								<td><%=bean.getAdvisor().getDisplayName()%></td>
								<td><%=bean.getAdvisor().getUsername()%></td>
								<td><%=bean.getAdvisor().getEmail()%></td>
								<td><a href="#editAdvisorModal" data-bs-toggle="modal"
									class="text-muted edit edit-id" title="Edit"><i
										id="ubtn-<%=i%>" class="material-symbols-outlined">edit</i></a> <a
									href="#deleteAdvisorModal" data-bs-toggle="modal"
									class="text-muted delete delete-id" title="Delete"><i
										id="ubtn-<%=i%>" class="material-symbols-outlined">delete</i></a>
							</tr>
							<%
							}
							}
							%>
						</tbody>
					</table>


					<div class="pagination-container">
						<div class="hint-text">
							Showing <b><%=pageStart%> - <%=pageEnd%></b> out of <b><%=totalItems%></b>
							total entries
						</div>
						<form id="page-form" method="post" class="pagination"
							action="<%=ActionQuery.ADVISORS_QUERY%>">
							<div class="page-item<%=hasPreviousPages ? "" : " disabled"%>">
								<a href="javascript:goToPreviousPage()" title="Previous"
									class="page-link"> <i class="material-symbols-outlined">navigate_before</i>
								</a>
							</div>
							<%
							if (advisors != null) {
								for (int i = 0; i < advisors.getTotalPages(); i++) {
							%>
							<div
								class="page-item<%=(advisors.getPage() == (i + 1) ? " active" : "")%>">
								<a href="javascript:setPage(<%=(i + 1)%>)" class="page-link"><%=(i + 1)%></a>
							</div>
							<%
							}
							}
							%>
							<div class="page-item<%=hasNextPages ? " disabled" : ""%>">
								<a href="javascript:goToNextPage()" title="Next"
									class="page-link"><i class="material-symbols-outlined">navigate_next</i></a>
							</div>
						</form>
					</div>

				</div>
			</div>
		</div>
		<!-- Delete Modal -->
		<div id="deleteAdvisorModal" class="modal fade">
			<div class="modal-dialog">
				<div class="modal-content modal-container">
					<form onsubmit="submitDelete()" id="delete-form" method="post"
						action="<%=ActionQuery.ADVISOR_DELETE_QUERY%>">
						<div class="modal-header">
							<h4 class="modal-title">Delete Advisor</h4>
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
		<div id="editAdvisorModal" class="modal fade">
			<div class="modal-dialog">
				<div class="modal-content modal-container">
					<form id="edit-form" method="post"
						action="<%=ActionQuery.ADVISOR_EDIT_QUERY%>">
						<div class="modal-header">
							<h4 class="modal-title">Update Advisor Details</h4>
						</div>
						<div class="modal-body">
							<input type="hidden" style="display: none" name="advisorId">
							<div class="form-group mb-2">
								<label>Display Name:</label> <input type="text"
									placeholder="Enter display name..." name="displayName"
									class="form-control" required>
							</div>
							<div class="form-group mb-2">
								<label>Username:</label> <input type="text"
									placeholder="Enter username..." name="username"
									class="form-control" required>
							</div>
							<div class="form-group mb-2">
								<label>Email:</label> <input type="email"
									placeholder="Enter email..." name="email" class="form-control"
									required>
							</div>
							<div class="form-group mb-2">
								<label>Password:</label> <input type="password"
									placeholder="Enter password (not required)..." name="password"
									class="form-control">
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
		<div id="addAdvisorModal" class="modal fade">
			<div class="modal-dialog">
				<div class="modal-content modal-container">
					<form method="post" action="<%=ActionQuery.ADVISOR_ADD_QUERY%> ">
						<div class="modal-header">
							<h4 class="modal-title">Add New Advisor</h4>
						</div>
						<div class="modal-body">
							<div class="form-group mb-2">
								<label>Display Name:</label> <input type="text"
									placeholder="Enter display name..." name="displayName"
									class="form-control" required>
							</div>
							<div class="form-group mb-2">
								<label>Username:</label> <input type="text"
									placeholder="Enter username..." name="username"
									class="form-control" required>
							</div>
							<div class="form-group mb-2">
								<label>Email:</label> <input type="email"
									placeholder="Enter email..." name="email" class="form-control"
									required>
							</div>
							<div class="form-group mb-2">
								<label>Password:</label> <input type="password"
									placeholder="Enter password..." name="password"
									class="form-control" required>
							</div>
						</div>
						<div class="modal-footer">
							<input type="button" class="btn btn-default"
								data-bs-dismiss="modal" value="Cancel"> <input
								type="submit" class="btn btn-secondary" value="Add Advisor">
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>