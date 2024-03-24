<%@page import="dev.projectfitness.admin.util.Constants.MenuItem"%>
<%@page import="dev.projectfitness.admin.util.Constants"%>
<%@page import="dev.projectfitness.admin.util.Constants.SessionParams"%>
<%@page import="dev.projectfitness.admin.beans.AdminBean"%>
<%@page import="dev.projectfitness.admin.util.Constants.ActionQuery"%>
<%
boolean isAuthenticated = session.getAttribute(SessionParams.AUTHENTICATED_ADMIN) != null
		&& ((AdminBean) session.getAttribute(SessionParams.AUTHENTICATED_ADMIN)).isAuthenticated();
Object activeMenuLink = session.getAttribute(SessionParams.MENU_ACTIVE_LINK);
%>

<header class="navbar p-0 navbar-expand-lg navbar-dark bg-secondary">
	<div class="container-fluid">
		<a class="navbar-brand" href="<%=ActionQuery.HOME_QUERY%>"> <%@ include
				file="/WEB-INF/components/logo.jsp"%></a>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#mynavbar">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="mynavbar">
			<ul class="navbar-nav me-auto">
				<li class="nav-item"><a
					class="nav-link ms-1 me-2 <%=isAuthenticated ? "enabled" : "disabled"%> <%=MenuItem.HOME_ITEM.equals(activeMenuLink.toString()) ? "active" : ""%>"
					href="<%=ActionQuery.HOME_QUERY%>">Home</a></li>

				<%
				if (isAuthenticated == true) {
				%>
				<li class="nav-item"><a
					class="nav-link ms-1 me-2<%=((MenuItem.CATEGORIES_ITEM.equals(activeMenuLink.toString())) ? " active" : "")%>"
					href="<%=ActionQuery.CATEGORIES_QUERY%>">Categories</a></li>

				<li class="nav-item dropdown"><a
					class="nav-link ms-1 me-2 dropdown-toggle<%=((MenuItem.USERS_ITEM.equals(activeMenuLink.toString())) ? " active" : "")%>"
					href="" role="button" data-bs-toggle="dropdown">Users</a>
					<ul class="dropdown-menu">
						<li><a class="dropdown-item"
							href="<%=ActionQuery.USERS_QUERY%>">Fitness users</a></li>
						<li><a class="dropdown-item"
							href="<%=ActionQuery.ADVISORS_QUERY%>">Advisors</a></li>
					</ul></li>

				<li class="nav-item"><a
					class="nav-link ms-1 me-2<%=((MenuItem.STATISTICS_ITEM.equals(activeMenuLink.toString())) ? " active" : "")%>"
					href="<%=ActionQuery.STATISTICS_QUERY%>">Statistics</a></li>

				<%
				}
				%>
				<%
				if (!isAuthenticated) {
				%>
				<li class="nav-item"><a
					class="nav-link ms-1 me-2<%=(MenuItem.LOGIN_ITEM.equals(activeMenuLink.toString()) ? " active" : "")%>"
					href="./">Login</a></li>
				<%
				}
				%>
				<%
				if (isAuthenticated) {
				%>
				<li class="nav-item"><a class="nav-link ms-1 me-2"
					href="<%=ActionQuery.LOGOUT_QUERY%>">Logout</a></li>
				<%
				}
				%>
			</ul>
			<%
			if (isAuthenticated) {
				AdminBean bean = (AdminBean) session.getAttribute(SessionParams.AUTHENTICATED_ADMIN);
				if (bean.getAdmin() != null) {
			%>
			<div class="account-card">
				<i class="material-symbols-outlined">shield_person</i>
				<p><%=bean.getAdmin().getFirstName() + " " + bean.getAdmin().getLastName()%></p>
			</div>
			<%
			}
			}
			%>

		</div>
	</div>
</header>