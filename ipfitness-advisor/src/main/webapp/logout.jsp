<%@page import="dev.projectfitness.advisor.util.Constants"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<jsp:useBean id="authAdvisor"
	class="dev.projectfitness.advisor.beans.AdvisorBean" scope="session"></jsp:useBean>
<jsp:useBean id="advisorService"
	class="dev.projectfitness.advisor.services.AdvisorService"
	scope="application"></jsp:useBean>
<%
advisorService.logout(authAdvisor);
session.invalidate();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" href="./css/styles.css">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
<title><%=Constants.APPLICATION_TITLE%></title>
</head>
<body>
	<div class="d-flex align-items-center justify-content-center vh-100">
		<div class="text-center mb-4">
			<p class="fs-2">
				<span class="text-main"><strong>Goodbye!</strong></span> Logout
				was successfull
			</p>
			<p class="lead fs-4">Your session ends here...</p>
			<a href="./" class="fs-5">Go Home</a>
		</div>
	</div>
</body>
</html>