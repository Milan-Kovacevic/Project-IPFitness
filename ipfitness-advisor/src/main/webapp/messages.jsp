<%@page import="java.io.DataInputStream"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="dev.projectfitness.advisor.dtos.Message"%>
<%@page import="dev.projectfitness.advisor.beans.MessageBean"%>
<%@page import="java.util.List"%>
<%@page import="dev.projectfitness.advisor.util.Constants.Pages"%>
<%@page import="dev.projectfitness.advisor.beans.AdvisorBean"%>
<%@page import="dev.projectfitness.advisor.util.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<jsp:useBean id="authAdvisor"
	class="dev.projectfitness.advisor.beans.AdvisorBean" scope="session"></jsp:useBean>
<jsp:useBean id="messageService"
	class="dev.projectfitness.advisor.services.MessageService"
	scope="application"></jsp:useBean>
<%
// Authentication
if (!authAdvisor.isAuthenticated()) {
	session.setAttribute(SessionParams.MENU_ACTIVE_LINK, MenuItem.LOGIN_ITEM);
	response.sendRedirect(Pages.LOGIN_PAGE);
} else {
	session.setAttribute(SessionParams.MENU_ACTIVE_LINK, MenuItem.MESSAGES_ITEM);
}

// Check if session contains selected message
Message selectedMessage = null;
if (session.getAttribute("messageBean") != null) {
	MessageBean bean = (MessageBean) session.getAttribute("messageBean");
	selectedMessage = bean.getMessage();
}

// Control attributes
String errorMessage = (String) session.getAttribute("errorMessage");
String successMessage = (String) session.getAttribute("successMessage");
List<MessageBean> messages;

// Sets the selectedMessage 
if (request.getParameter("action") != null && "details".equals(request.getParameter("action"))) {
	String messageId = request.getParameter("messageId");
	if (messageId != null) {
		int id = 0;
		try {
	id = Integer.parseInt(messageId);
	MessageBean bean = messageService.getMessage(id);
	if (bean != null && bean.getMessage() != null) {
		selectedMessage = bean.getMessage();
		session.setAttribute("messageBean", bean);
	} else {
		errorMessage = "Unable to open selected message.";
		session.removeAttribute("messageBean");
		selectedMessage = null;
	}
		} finally {

		}
	}
	messages = messageService.getAllUnreadMessages();
} else if (request.getParameter("action") != null && "search".equals(request.getParameter("action"))) {
	// Search for messages
	String query = request.getParameter("query");
	messages = messageService.searchUnreadMessages(query);
	session.setAttribute("searchQuery", query);
	session.removeAttribute("messageBean");
	selectedMessage = null;
} else {
	session.removeAttribute("messageBean");
	selectedMessage = null;
	messages = messageService.getAllUnreadMessages();
}

String searchText = session.getAttribute("searchQuery") == null ? "" : session.getAttribute("searchQuery").toString();
boolean hasMessages = messages != null && messages.size() > 0;
session.removeAttribute("successMessage");
session.removeAttribute("errorMessage");
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
<title><%=Constants.APPLICATION_TITLE%></title>
<script>
setTimeout(() => {
	var alerts = document.getElementsByClassName("alert");
	for (var alert of alerts) {
		alert.style.display = "none";
	}
}, 2500);
<%if (selectedMessage != null) {%>
	function onLoad() {
		var button = document.createElement("button");
		button.setAttribute("data-bs-target", "#messageDetailsModal");
		button.setAttribute("data-bs-toggle", "modal");
		button.style = "display: none";
		var parent = document.getElementById("temp");
		parent.appendChild(button);
		button.click();
		
		document.getElementById("submitBtn").addEventListener("click", ()=>{
			let form = document.getElementById("form");
			if(form.reportValidity()){
				let loader = document.getElementById("loader");
				loader.classList.remove("d-none");
			}
		});
	}
<%}%>
</script>
</head>
<body <%=selectedMessage != null ? "onload=\"onLoad()\"" : ""%>>
	<div class="background-div"></div>
	<div class="main-div">
		<%@ include file="/WEB-INF/components/header.jsp"%>
		<div id="temp" style="display: none"></div>
		<div class="floating-card">
			<div class="container-sm table-container d-flex flex-column">
				<div class="table-title mb-3">
					<div class="d-flex justify-content-start align-items-center">
						<i class="material-symbols-outlined">contact_support</i>
						<div class="">
							<h2 class="table-title">
								<strong class="text-main"> Messages</strong> from fitness
								application users
							</h2>
						</div>
					</div>
					<div class="d-flex justify-content-start mt-3">
						<form method="get" action="<%=Pages.MESSAGES_PAGE%>"
							class="search-box">
							<input type="hidden" style="display: none" name="action"
								value="search"> <i class="material-symbols-outlined">search</i>
							<input type="text" class="form-control"
								placeholder="Search&hellip;" name="query"
								value="<%=searchText%>">
						</form>
					</div>
					<%
					if (errorMessage != null) {
					%>
					<div class="alert alert-danger mb-0 mt-3">
						<strong>Error!</strong>&nbsp;<%=errorMessage%>
						Click <a class="alert-link" href="<%=Pages.MESSAGES_PAGE%>">here</a>
						to refresh.
					</div>
					<%
					} else if (successMessage != null) {
					%>
					<div class="alert alert-success mb-0 mt-3">
						<strong>Success!</strong>&nbsp;<%=successMessage%>
					</div>
					<%
					}
					%>
				</div>
				<div class="table-wrapper table-responsive">
					<table class="table align-middle table-hover">
						<thead class="table-main table-secondary">
							<%
							if (hasMessages) {
							%>
							<tr>
								<th>Sender Name</th>
								<th>Send Time</th>
								<th>Message</th>
								<th>Details</th>
							</tr>
							<%
							} else {
							%>
							<p class="mt-3">
								<i class="fs-5">There are no messages to show now...</i>
							</p>
							<%
							}
							%>
						</thead>
						<tbody>
							<%
							if (hasMessages) {
								for (MessageBean bean : messages) {
									Message msg = bean.getMessage();
							%>
							<tr>
								<td><%=msg.getSenderName()%></td>
								<td><%=msg.getSendDate()%></td>
								<td><%=msg.getContent()%></td>
								<td><a
									href="<%=Pages.MESSAGES_PAGE%>?action=details&messageId=<%=msg.getQuestionId()%>"
									class="text-main" title="View Details"><span
										class="material-symbols-outlined">visibility</span> </a></td>
							</tr>
							<%
							}
							}
							%>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<%
		if (selectedMessage != null) {
		%>
		<div id="messageDetailsModal" class="modal fade">
			<div class="modal-dialog modal-dialog-scrollable">
				<div class="modal-content modal-container">
					<form id="form" method="post"
						action="<%=Constants.Pages.FILE_UPLOAD_PAGE%>"
						enctype="multipart/form-data">
						<div class="modal-header">
							<h4 class="modal-title">Reply to message</h4>
						</div>
						<div class="modal-body">
							<input type="hidden" style="display: none" name="messageId"
								value="<%=selectedMessage.getQuestionId()%>"> <input
								type="hidden" style="display: none" name="action" value="send">
							<input type="hidden" style="display: none" name="recipient"
								value="<%=selectedMessage.getSenderMail()%>">
							<div class="d-flex align-items-center mb-1">
								<span class="material-symbols-outlined me-1">schedule</span> <label
									class="mb-1 mt-1"><%=selectedMessage.getSendDate()%></label>
							</div>
							<div class="form-group mb-0">
								<label>Sender Name: <span class="modal-text ms-1"><%=selectedMessage.getSenderName()%></span></label>
							</div>
							<div class="form-group mb-2">
								<label>Sender Email: <span class="modal-text ms-1"><%=selectedMessage.getSenderMail()%></span></label>
							</div>
							<div class="form-group mb-2">
								<label class="mb-1">Message:</label>
								<textarea rows=3 readonly="readonly" class="form-control"><%=selectedMessage.getContent()%></textarea>
							</div>
							<div class="form-group mb-1">
								<label class="mb-1">Reply:</label>
								<textarea rows=4 placeholder="Enter your reply here..."
									name="message" class="form-control" required="required"></textarea>
							</div>
							<div class="form-group mb-4">
								<label class="mb-1">Attach files:</label> <input type="file"
									name="attachment" multiple="multiple" maxlength="3"
									class="form-control">
							</div>
							<div class="modal-footer">
								<input type="button" class="btn btn-default"
									data-bs-dismiss="modal" value="Cancel">
								<button id="submitBtn" type="submit"
									class="btn bg-main border-main text-white">
									<span id="loader"
										class="d-none spinner-border spinner-border-sm me-1"
										role="status" aria-hidden="true"></span> Send Mail
								</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<%
		}
		%>
	</div>
</body>
</html>