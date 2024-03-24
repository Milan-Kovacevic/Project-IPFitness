<%@page import="dev.projectfitness.advisor.util.Constants.Pages"%>
<%@page import="dev.projectfitness.advisor.util.Constants"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<jsp:useBean id="authAdvisor"
	class="dev.projectfitness.advisor.beans.AdvisorBean" scope="session"></jsp:useBean>
<jsp:useBean id="advisorService"
	class="dev.projectfitness.advisor.services.AdvisorService"
	scope="application"></jsp:useBean>

<%
String errorMessage = null;
// Checks if login form is submitted by user
if ("1".equals(request.getParameter("loginAction"))) {
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	if (username != null && password != null) {
		AdvisorBean bean = advisorService.login(username, password);
		if (bean != null && bean.isAuthenticated()) {
			authAdvisor.setAdvisor(bean.getAdvisor());
			authAdvisor.setAuthenticated(bean.isAuthenticated());
		}
		else {
			errorMessage = "Invalid username or password. Please try again.";
		}
	}
}

//Checks if advisor is authenticated
if (authAdvisor != null && authAdvisor.isAuthenticated()) {
	session.setAttribute(SessionParams.MENU_ACTIVE_LINK, MenuItem.MESSAGES_ITEM);
	response.sendRedirect(Pages.MESSAGES_PAGE);
} else {
	session.setAttribute(SessionParams.MENU_ACTIVE_LINK, MenuItem.LOGIN_ITEM);
}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" href="./css/styles.css">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<title><%=Constants.APPLICATION_TITLE%></title>
</head>
<body>
	<div class="background-div"></div>
	<div class="main-div">
		<%@ include file="/WEB-INF/components/header.jsp"%>
		<div class="content">
			<div class="login-div floating-card container-sm">
				<div>
					<h1 class="m-0">Welcome back</h1>
					<h5
						class="display-6 login-subtitle<%=(errorMessage != null ? "" : " pb-3")%>">Please
						login to your account</h5>
					<%
					if (errorMessage != null) {
					%>
					<div class="alert alert-danger mt-3">
						<strong>Error!</strong>&nbsp;<%=errorMessage%>
					</div>
					<%
					}
					%>
					<form method="post" action="<%=Pages.LOGIN_PAGE%>" class="mt-5">
						<div class="input-group mb-3 mt-5">
							<span class="material-symbols-outlined p-2 input-group-text">person</span>
							<input type="text" class="form-control p-2" id="username"
								placeholder="Enter your username..." name="username" required>
						</div>
						<div class="input-group mb-4 mt-2 pb-2">
							<span class="material-symbols-outlined p-2 input-group-text">key</span>
							<input type="password" class="form-control p-2" id="password"
								placeholder="Enter your password..." name="password" required>
						</div>
						<button type="submit" name="loginAction" value="1"
							class="btn mt-2 bg-main border-main text-white">Login</button>
					</form>
				</div>

			</div>
		</div>
	</div>
</body>
</html>