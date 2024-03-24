<%@page import="dev.projectfitness.admin.util.Constants.ActionQuery"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<title>404 Page</title>
</head>
<body>
	<div class="d-flex align-items-center justify-content-center vh-100">
		<div class="text-center">
			<h1 class="display-1 fw-bold">404</h1>
			<p class="fs-3">
				<span class="text-danger"><strong>Opps!</strong></span> Page not found.
			</p>
			<p class="lead">The page you are looking for does not exist.</p>
			<a href="/ipfitness-admin/<%= ActionQuery.HOME_QUERY %>" class="btn btn-secondary">Go Home</a>
		</div>
	</div>
</body>
</html>