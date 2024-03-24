<%@page import="java.net.HttpURLConnection"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.stream.Collectors"%>
<%@page import="java.util.List"%>
<%@page import="dev.projectfitness.advisor.util.Constants.MenuItem"%>
<%@page import="dev.projectfitness.advisor.util.Constants.Pages"%>
<%@page import="dev.projectfitness.advisor.util.Constants.SessionParams"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<jsp:useBean id="authAdvisor"
	class="dev.projectfitness.advisor.beans.AdvisorBean" scope="session"></jsp:useBean>
<jsp:useBean id="messageService"
	class="dev.projectfitness.advisor.services.MessageService"
	scope="application"></jsp:useBean>
<jsp:useBean id="messageBean"
	class="dev.projectfitness.advisor.beans.MessageBean" scope="session"></jsp:useBean>
<%
// Authentication
if (!authAdvisor.isAuthenticated()) {
	session.setAttribute(SessionParams.MENU_ACTIVE_LINK, MenuItem.LOGIN_ITEM);
	response.sendRedirect(Pages.LOGIN_PAGE);
} else {
	session.setAttribute(SessionParams.MENU_ACTIVE_LINK, MenuItem.MESSAGES_ITEM);
}

if (messageBean != null && messageBean.getMessage() != null && request.getContentType() != null
		&& request.getContentType().startsWith("multipart/form-data;") && request.getParameter("message") != null
		&& request.getPart("attachment") != null) {
	String message = request.getParameter("message");
	List<Part> attachment = request.getParts().stream()
	.filter(part -> "attachment".equals(part.getName()) && part.getSize() > 0).collect(Collectors.toList());
	String postDate = messageBean.getMessage().getSendDate();
	String recipient = messageBean.getMessage().getSenderName();
	String subject = recipient + " answered your question posted on " + postDate;

	int statusCode = messageService.sendMessageResponse(messageBean.getMessage(), subject, message, attachment);
	if (statusCode == HttpURLConnection.HTTP_OK) {
		session.setAttribute("successMessage", "Email is sent successfully to " + recipient + ".");
	} else if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
		session.setAttribute("errorMessage", "Unable to send email to " + recipient + ". Please, try again later");
	} else if (statusCode == HttpURLConnection.HTTP_BAD_REQUEST) {
		session.setAttribute("errorMessage",
		"Mail message is not formed correctly. Check if number of filex exceed the limit of 5MB.");
	} else if (statusCode == HttpURLConnection.HTTP_UNAVAILABLE) {
		session.setAttribute("errorMessage", "Server is currently not available. Please, try again later.");
	}
	else {
		session.setAttribute("errorMessage", "Unexpected error ocurred. Please, try again later");
	}
	session.removeAttribute("messageBean");
}
response.sendRedirect(Pages.MESSAGES_PAGE);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>IPFitness - Upload file</title>
</head>
<body>

</body>
</html>