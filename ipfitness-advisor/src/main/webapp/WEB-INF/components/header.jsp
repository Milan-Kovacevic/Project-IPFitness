<%@page import="dev.projectfitness.advisor.util.Constants.Pages"%>
<%@page import="dev.projectfitness.advisor.beans.AdvisorBean"%>
<%@page import="dev.projectfitness.advisor.util.Constants.MenuItem"%>
<%@page import="dev.projectfitness.advisor.util.Constants.SessionParams"%>

<%
AdvisorBean advisorBean = (AdvisorBean) session.getAttribute("authAdvisor");
boolean isAuthenticated = advisorBean != null && advisorBean.isAuthenticated();

String activeLink = "";
if (session.getAttribute(SessionParams.MENU_ACTIVE_LINK) != null)
	activeLink = session.getAttribute(SessionParams.MENU_ACTIVE_LINK).toString();
%>
<header
	class="navbar p-0 navbar-expand-lg navbar-dark bg-main border-bottom border-1 border-secondary">
	<div class="container-fluid">
		<a class="navbar-brand" href="./"> <%@ include
				file="/WEB-INF/components/logo.jsp"%></a>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#mynavbar">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="mynavbar">
			<ul class="navbar-nav me-auto">
				<li class="nav-item"><a
					class="nav-link ms-1 me-2 <%=isAuthenticated ? "enabled" : "disabled"%><%=activeLink == MenuItem.MESSAGES_ITEM ? " active" : ""%>"
					href="<%= Pages.MESSAGES_PAGE %>">Messages</a></li>
				<%
				if (!isAuthenticated) {
				%>
				<li class="nav-item"><a
					class="nav-link ms-1 me-2<%=activeLink == MenuItem.LOGIN_ITEM ? " active" : ""%>"
					href="<%= Pages.LOGIN_PAGE %>">Login</a></li>
				<%
				}
				%>

				<%
				if (isAuthenticated) {
				%>
				<li class="nav-item"><a class="nav-link ms-1 me-2"
					href="<%= Pages.LOGOUT_PAGE %>">Logout</a></li>
				<%
				}
				%>
			</ul>
			
			<%
			if (isAuthenticated && advisorBean.getAdvisor() != null) {
			%>
			<div class="account-card">
				<i class="material-symbols-outlined">account_circle</i>
				<p><%=advisorBean.getAdvisor().getDisplayName()%></p>
			</div>
			<%
			}
			%>
		</div>
	</div>
</header>