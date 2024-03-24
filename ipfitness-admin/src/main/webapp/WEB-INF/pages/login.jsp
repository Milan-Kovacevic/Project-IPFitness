<%@page import="dev.projectfitness.admin.util.Constants"%>
<%@page import="dev.projectfitness.admin.util.Constants.SessionParams"%>
<%@page import="dev.projectfitness.admin.util.Constants.ActionQuery"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
<script src="./js/login.js"></script>
<script src="./js/global.js"></script>
<title><%=Constants.APPLICATION_TITLE%></title>
</head>
<body onload="onLoginPageLoad()">
	<div class="background-div"></div>
	<div class="main-div">
		<%@ include file="/WEB-INF/components/header.jsp"%>
		<div class="content">
			<div class="login-div floating-card container-sm">
				<div>
					<h1 class="m-0">Welcome back</h1>
					<h5
						class="display-6 login-subtitle<%=(session.getAttribute(SessionParams.ERROR_MESSAGE) != null ? "" : " pb-2")%>">Please
						login to your account</h5>
					<%
					if (session.getAttribute(SessionParams.ERROR_MESSAGE) != null) {
					%>
					<div class="alert alert-danger mt-3">
						<strong>Error!</strong>&nbsp;<%= session.getAttribute(SessionParams.ERROR_MESSAGE) %>
					</div>
					<%
					}
					%>
					<form onsubmit="javascript:onFormSubmit()" method="post"
						action="<%=ActionQuery.LOGIN_QUERY%>" class="mt-5">
						<div class="input-group mb-3 mt-5">
							<span class="material-symbols-outlined p-2 input-group-text">person</span>
							<input type="text" class="form-control p-2" id="username"
								placeholder="Enter your username..." name="username" required>
						</div>
						<div class="input-group mb-3 mt-2">
							<span class="material-symbols-outlined p-2 input-group-text">key</span>
							<input type="password" class="form-control p-2" id="password"
								placeholder="Enter your password..." name="password" required>
						</div>

						<div class="form-check mb-4">
							<label class="form-check-label"> <input
								class="form-check-input" onchange="onRememberMeChanged()"
								type="checkbox" name=remember id="remember"> Remember
								username
							</label>
						</div>
						<button type="submit" class="btn btn-secondary">Login</button>
					</form>
				</div>

			</div>
		</div>
	</div>
</body>
</html>