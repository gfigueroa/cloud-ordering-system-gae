<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="util.DateManager" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.SurveyManager" %>
<%@ page import="datastore.Survey" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="datastore.RestaurantOpinionPollManager" %>
<%@ page import="datastore.RestaurantOpinionPoll" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>

<%
  User sessionUser = (User)session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("/login.jsp");
  else {
  	if (sessionUser.getUserType() != User.Type.ADMINISTRATOR && sessionUser.getUserType() != User.Type.RESTAURANT) {
  		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  	}
  }
    
  Key restaurantKey = sessionUser.getUserType() == User.Type.RESTAURANT ?
  		sessionUser.getKey().getParent() : KeyFactory.stringToKey(request.getParameter("r_key"));

  Restaurant restaurant = RestaurantManager.getRestaurant(restaurantKey);
  
  char type;
  if (request.getParameter("type") == null) {
  	type = 'A';
  }
  else {
  	type = request.getParameter("type").charAt(0);
  }
%>

<% 
Printer printer = (Printer)session.getAttribute("printer");
%>
<jsp:include page="../header/language-header.jsp" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />
<link type="text/css" href="../stylesheets/colorbox.css" rel="stylesheet" />
<script src="../js/jquery.min.js"></script>
<script src="../js/jquery.colorbox-min.js"></script>
<script src="../js/popup.js"></script>
<script src="../js/confirmAction.js"></script>

<%@  include file="../header/page-title.html" %>
</head>

<body>
<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<%if (sessionUser.getUserType()!= User.Type.RESTAURANT) {%>
	<a href="../admin/listRestaurant.jsp" class="back-link-RS"><%= printer.print("Back to Retail Store") %></a> 
<%}%>

<h1>
<%= restaurant.getRestaurantName() + " - " %>
<%
if (type == 'A') {
%>
	<%= printer.print("Active Surveys") %>
<%
}
else if (type == 'I') {
%>
	<%= printer.print("Inactive Surveys") %>
<%
}
else {
%>
	<%= printer.print("Survey History") %>
<%
}
%>
</h1>

<div>
	<a href="listSurvey.jsp?r_key=<%= KeyFactory.keyToString(restaurantKey) %>&type=A"><%= printer.print("Active Surveys") %></a> | <a href="listSurvey.jsp?r_key=<%= KeyFactory.keyToString(restaurantKey) %>&type=I"><%= printer.print("Inactive Surveys") %></a> | <a href="listSurvey.jsp?r_key=<%= KeyFactory.keyToString(restaurantKey) %>&type=H"><%= printer.print("Survey History") %></a>
</div>

<%
	List<Survey> SurveyList;
	if (type == 'A')
		SurveyList = SurveyManager.getActiveSurveysFromStore(restaurant.getKey());
	else if (type == 'I')
		SurveyList = SurveyManager.getInactiveSurveysFromStore(restaurant.getKey());
	else 
		SurveyList = SurveyManager.getExpiredSurveysFromStore(restaurant.getKey());
%>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">

<%
if (sessionUser.getUserType() == User.Type.RESTAURANT) {
%>
  <tr>
  	<td width="20%"><a href="addSurvey.jsp">+ <%= printer.print("Add Survey") %></a></td>
  	<td width="20%"></td>
    <td width="30%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
  </tr>
  
  <tr>
  	<td width="20%"></td>
  	<td width="20%"></td>
    <td width="30%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
  </tr>
<%
}
%>

  <tr>
  	<td width="20%"><%= printer.print("Starting Date") %></td>
  	<td width="20%"><%= printer.print("Ending Date") %></td>
    <td width="30%"><%= printer.print("Survey Title") %></td>
    <td width="10%"><%= printer.print("Survey Type") %></td>
    <td width="10%"><%= printer.print("Status") %></td>
    <td width="10%"><%= printer.print("Actions") %></td>
  </tr>
  
  <tr>
  	<td width="20%"></td>
  	<td width="20%"></td>
  	<td width="30%"></td>
  	<td width="10%"></td>
  	<td width="10%"></td>
    <td width="10%"></td>
  </tr>

  <% 
  for (Survey survey : SurveyList) {
  %>
	  <tr>
	  	<td width="20%"><%= DateManager.printDateAsString(survey.getSurveyStartingDate()) %></td>
	    <td width="20%"><%= DateManager.printDateAsString(survey.getSurveyEndingDate()) %></td>
	    <td width="30%"><%= survey.getSurveyTitle() %></td>
	    <td width="10%"><%= printer.print(survey.getSurveyTypeString()) %></td>
	    <td width="10%"><%= printer.print(survey.getCurrentStatus().toString()) %></td>
	    <td width="10%">
	    <%
	    if (type == 'A' || type == 'I') {
	    %>
	    	<a id="<%= survey.getKey() %>" href="editSurvey.jsp?k=<%= KeyFactory.keyToString(survey.getKey()) + "&readonly=true" %>"><%= printer.print("View") %></a>
	    	<br/>
	    	<a href="editSurvey.jsp?k=<%= KeyFactory.keyToString(survey.getKey()) %>"><%= printer.print("Edit") %></a>
	    <%
	    }
	    else {
	    %>
	    	<a id="<%= survey.getKey() %>" href="editSurvey.jsp?k=<%= KeyFactory.keyToString(survey.getKey()) + "&readonly=true" %>"><%= printer.print("View") %></a>
	    <%
	    }
	    %>
	    <br/>
	    <a href="javascript:void(0);" onclick="confirmDelete('/manageRestaurant?action=delete&type=S&k=<%= KeyFactory.keyToString(survey.getKey()) %>', '<%= printer.print("Are you sure you want to delete this Survey")%>');"><%= printer.print("Delete") %></a>
	    <%
	    if (sessionUser.getUserType() == User.Type.RESTAURANT) {
	    %>
	    	<hr/>
	    	<a id="<%= survey.getKey() %>" href="listOpinionPoll.jsp?sk=<%= KeyFactory.keyToString(survey.getKey())%>"><%= printer.print("View Polls") %> (<%=survey.getOpinionPolls().size()%>) </a>
	    <%
	    }
	    %>
	    </td>
	  </tr>
  <%  
  }
  %>
</table>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
