<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>

<%
  User sessionUser = (User) session.getAttribute("user");
%>

<%
switch (sessionUser.getUserType()) {
	case ADMINISTRATOR:
%>
	<jsp:include page="../menu/admin-menu.jsp" />
		
<%
		break;
	case RESTAURANT:
%>
		<jsp:include page="../menu/restaurant-menu.jsp" />
<%
		break;
	case CUSTOMER:
%>
		<%@  include file="../menu/customer-menu.html" %>
<%
		break;
}
%>