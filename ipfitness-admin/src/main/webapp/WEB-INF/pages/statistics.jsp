<%@page import="dev.projectfitness.admin.beans.ActionItemBean"%>
<%@page import="dev.projectfitness.admin.daos.paging.Pageable"%>
<%@page import="dev.projectfitness.admin.util.Constants"%>
<%@page import="dev.projectfitness.admin.util.Constants.SessionParams"%>
<%@page import="dev.projectfitness.admin.util.Constants.ActionQuery"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%
Pageable<ActionItemBean> statistics = null;
if (session.getAttribute(SessionParams.STATISTICS_SESSION_ATTRIBUTE_NAME) != null
		&& session.getAttribute(SessionParams.STATISTICS_SESSION_ATTRIBUTE_NAME) instanceof Pageable<?>)
	statistics = (Pageable<ActionItemBean>) session.getAttribute(SessionParams.STATISTICS_SESSION_ATTRIBUTE_NAME);
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" href="./css/styles.css">
<link rel="stylesheet" href="./css/table.css">
<script src="./js/global.js"></script>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="./js/global.js"></script>
<script src="./js/statistics.js"></script>
<title><%=Constants.APPLICATION_TITLE%></title>
</head>
<body onload="onLoad()">
	<div class="background-div"></div>
	<div class="main-div">
		<%@ include file="/WEB-INF/components/header.jsp"%>
		<div class="floating-card">
			<div class="container-sm table-container table-responsive">
				<div class="table-wrapper">
					<div class="table-title mb-2">
						<div class="row">
							<div class="col-sm-8">
								<h2 class="table-title">
									<strong class="text-secondary">Monitor </strong> IP Fitness
									backend server
								</h2>
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
							if (statistics != null && statistics.getItems().size() > 0) {
								out.print("<tr>");
								out.print("<th>#</th>");
								out.print("<th>IP Address</th>");
								out.print("<th>Request Endpoint</th>");
								out.print("<th>Action Severity</th>");
								out.print("<th>Time of Request</th>");
								out.print("<th>Actions</th>");
								out.print("</tr>");
							}
							%>
						</thead>
						<tbody>
							<%
							if (statistics != null) {
								for (int i = 0; i < statistics.getItems().size(); i++) {
									ActionItemBean bean = statistics.getItems().get(i);
									out.print("<tr id=tr-" + i + ">");
									out.print("<td>" + bean.getAction().getActionId() + "</td>");
									out.print("<td>" + bean.getAction().getIpAddress() + "</td>");
									out.print("<td>" + bean.getAction().getEndpoint() + "</td>");
									out.print("<td>" + "<span class=\"badge " + (bean.getAction().getSeverity() == 0 ? "bg-info text-dark\">Info"
									: (bean.getAction().getSeverity() == 1) ? "bg-danger\">Exception" : "bg-warning text-dark\">Sensitive")
									+ "</span></td>");
									out.print("<td>" + bean.getAction().getActionTime() + "</td>");
									out.print("<td>");
									out.print(
									"<a href=\"#detailsModal\" data-bs-toggle=\"modal\" class=\"text-muted details-id\" title=\"View message\"><i id=\"ibtn-"
											+ i + "\"  class=\"material-symbols-outlined\">contact_support</i></a>");
									out.print("</td>");
									out.print("<td class=\"d-none\">" + bean.getAction().getMessage() + "</td>");
									out.print("</tr>");
								}
							}
							%>
						</tbody>
					</table>


					<div class="pagination-container">
						<div class="hint-text">
							Showing <b><%=statistics != null && statistics.getItems().size() > 0
		? ((statistics.getPage() - 1) * statistics.getPageSize()) + 1
		: 0%> - <%=statistics != null ? (statistics.getPage() - 1) * (statistics.getPageSize()) + statistics.getPageItems() : 0%></b>
							out of <b><%=statistics != null ? statistics.getTotalItems() : 0%></b>
							total entries
						</div>
						<form id="page-form" method="post" class="pagination"
							action="<%=ActionQuery.STATISTICS_QUERY%>">
							<div
								class="page-item<%=statistics != null && statistics.getPage() > 1 ? "" : " disabled"%>">
								<a href="javascript:goToPreviousPage()" title="Previous"
									class="page-link"> <i class="material-symbols-outlined">navigate_before</i>
								</a>
							</div>
							<%
							if (statistics != null) {
								for (int i = 0; i < statistics.getTotalPages(); i++) {
									out.print("<div class=\"page-item" + (statistics.getPage() == (i + 1) ? " active" : "") + "\">"
									+ "<a href=\"javascript:setPage(" + (i + 1) + ")\" class=\"page-link\">" + (i + 1) + "</a>" + "</li>");

									out.print("</div>");
								}
							}
							%>
							<div
								class="page-item<%=(statistics == null || statistics.getTotalPages() <= statistics.getPage() ? " disabled" : "")%>">
								<a href="javascript:goToNextPage()" title="Next"
									class="page-link"><i class="material-symbols-outlined">navigate_next</i></a>
							</div>
						</form>
					</div>

				</div>
			</div>
		</div>
		<!-- Action item message details -->
		<div id=detailsModal class="modal fade">
			<div class="modal-dialog modal-lg modal-dialog-scrollable">
				<div class="modal-content modal-container">
					<div class="modal-header">
						<h4 class="modal-title">Action message details</h4>
					</div>
					<div class="modal-body">
						<div class="d-flex align-items-center mb-1">
							<span class="material-symbols-outlined me-1">schedule</span> <label
								class="mb-1 mt-1" id="time">23/01/2024 22:23:14</label>
						</div>
						<div class="form-group mb-2">
							<label class="mb-1">Message:</label>
							<p id="message"></p>
						</div>
					</div>
					<div class="modal-footer">
						<input type="button" class="btn btn-default"
							data-bs-dismiss="modal" value="Cancel">
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>