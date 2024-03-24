<%@page import="dev.projectfitness.admin.util.Constants"%>
<%@page import="dev.projectfitness.admin.util.Constants.SessionParams"%>
<%@page import="dev.projectfitness.admin.util.Constants.ActionQuery"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1" >
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
		<div class="card rounded-0 border-2 bg-light text-center mb-3 mt-auto">
			<p class="card-text fs-3 text-dark">
				<span class="text-secondary fs-1">IP<strong>Fitness</strong></span>
				- Fitness related activities
			</p>
			<p class="card-text fs-5 mb-2">
				Administrator Application - <a class="card-link fs-5"
					href="https://etf.unibl.org/" target="_blank">ETF@2023</a>
			</p>
		</div>
		<p>
	</div>
</body>
</html>