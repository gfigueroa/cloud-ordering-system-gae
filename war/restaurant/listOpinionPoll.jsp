<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="util.DateManager" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.RestaurantOpinionPollManager" %>
<%@ page import="datastore.RestaurantOpinionPoll" %>
<%@ page import="datastore.SurveyOpinionPoll" %>
<%@ page import="datastore.SurveyOpinionPollManager" %>
<%@ page import="datastore.Survey" %>
<%@ page import="datastore.SurveyManager" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.RestaurantManager" %>
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
  
  Survey survey=null;
  String surveyType=null;
  if (request.getParameter("sk") != null) {
 	survey = SurveyManager.getSurvey(KeyFactory.stringToKey(request.getParameter("sk")));
  	surveyType= survey.getSurveyTitle();
  	type = 'S';
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
<% if (type == 'S'){%> 
	<a href="listSurvey.jsp" class="back-link"><%= printer.print("Back to Surveys") %></a> 
<%}%>


<h1>
<%= type == 'S' ? printer.print("Survey") + " - " + survey.getSurveyTitle() : restaurant.getRestaurantName() %>
<%
if (type == 'A') {
%>
	- <%= printer.print("Active Opinion Polls") %>
<%
}
else if (type == 'I') {
%>
	- <%= printer.print("Inactive Opinion Polls") %>
<%
}
else if (type == 'H'){
%>
	- <%= printer.print("Opinion Poll History") %>
<%
}
%>
</h1>


<div>
<% 
if(type != 'S'){
%>
	<a href="listOpinionPoll.jsp?r_key=<%= KeyFactory.keyToString(restaurantKey) %>&type=A"><%= printer.print("Active Opinion Polls") %></a> | <a href="listOpinionPoll.jsp?r_key=<%= KeyFactory.keyToString(restaurantKey) %>&type=I"><%= printer.print("Inactive Opinion Polls") %></a> | <a href="listOpinionPoll.jsp?r_key=<%= KeyFactory.keyToString(restaurantKey) %>&type=H"><%= printer.print("Opinion Poll History") %></a>
<%
}
%>
</div>

<%
	List<RestaurantOpinionPoll> restaurantOpinionPollList=null;
	List<SurveyOpinionPoll> surveyOpinionPollList=null;
	if (type == 'A')
		restaurantOpinionPollList = RestaurantOpinionPollManager.getActiveRestaurantOpinionPollsFromRestaurant(restaurant.getKey());
	else if (type == 'I')
		restaurantOpinionPollList = RestaurantOpinionPollManager.getInactiveRestaurantOpinionPollsFromRestaurant(restaurant.getKey());
	else if (type == 'H')
		restaurantOpinionPollList = RestaurantOpinionPollManager.getExpiredRestaurantOpinionPollsFromRestaurant(restaurant.getKey());
	else 
		surveyOpinionPollList = survey.getOpinionPolls();
%>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">

<%
if (sessionUser.getUserType() == User.Type.RESTAURANT) {
%>
  <tr>
   	<% 
  	if (type!='S') {
  	%> 	
  		<td width="20%"><a href="addOpinionPoll.jsp">+ <%= printer.print("Add Opinion Poll") %></a></td>
  	<%
  	}
  	else {
  	%>
  		<td width="20%"><a href="addOpinionPoll.jsp?sk=<%= request.getParameter("sk") %>">+ <%= printer.print("Add Opinion Poll") %></a></td>
  	<%
  	}
  	%>
  	<td width="15%"></td>
    <td width="30%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
  </tr>
  
  <tr>
  	<td width="20%"></td>
  	<td width="15%"></td>
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
  	<td width="15%"><%= printer.print("Ending Date") %></td>
    <td width="30%"><%= printer.print("Opinion Poll Title") %></td>
    <td width="10%"><%= printer.print("Opinion Poll Type") %></td>
    <td width="10%"><%= printer.print("Status") %></td>
    <td width="10%"><%= printer.print("Actions") %></td>
  </tr>
  
  <tr>
  	<td width="20%"></td>
  	<td width="15%"></td>
  	<td width="30%"></td>
  	<td width="10%"></td>
  	<td width="10%"></td>
    <td width="10%"></td>
  </tr>

  <% 
  if(type!='S'){
	  for (RestaurantOpinionPoll restaurantOpinionPoll : restaurantOpinionPollList) {
	  %>
		  <tr>
		  	<td width="20%"><%= DateManager.printDateAsString(restaurantOpinionPoll.getRestaurantOpinionPollStartingDate()) %></td>
		    <td width="15%"><%= DateManager.printDateAsString(restaurantOpinionPoll.getRestaurantOpinionPollEndingDate()) %></td>
		    <td width="30%"><%= restaurantOpinionPoll.getRestaurantOpinionPollTitle() %></td>
		    <td width="10%"><%= printer.print(restaurantOpinionPoll.getRestaurantOpinionPollTypeString()) %></td>
		    <td width="10%"><%= printer.print(restaurantOpinionPoll.getCurrentStatus().toString()) %></td>
		    <td width="10%">
		    <%
		    if (type == 'A' || type == 'I') {
		    %>
		    	<a id="<%= restaurantOpinionPoll.getKey() %>" href="editOpinionPoll.jsp?k=<%= KeyFactory.keyToString(restaurantOpinionPoll.getKey()) + "&readonly=true" %>"><%= printer.print("View") %></a>
		    	<br/>
		    	<a href="editOpinionPoll.jsp?k=<%= KeyFactory.keyToString(restaurantOpinionPoll.getKey()) %>"><%= printer.print("Edit") %></a>
		    <%
		    }
		    else if(type=='H') {
		    %>
		    	<a id="<%= restaurantOpinionPoll.getKey() %>" href="editOpinionPoll.jsp?k=<%= KeyFactory.keyToString(restaurantOpinionPoll.getKey()) + "&readonly=true" %>"><%= printer.print("View") %></a>
		    <%
		   	}
		    %>
		    <br/>
		    <a href="javascript:void(0);" onclick="confirmDelete('/manageRestaurant?action=delete&type=L&k=<%= KeyFactory.keyToString(restaurantOpinionPoll.getKey()) %>', '<%= printer.print("Are you sure you want to delete this Opinion Poll")%>');"><%= printer.print("Delete") %></a>
		    </td>
		  </tr>
	  <%  
	  }
  }else{
	  for (SurveyOpinionPoll surveyOpinionPoll : surveyOpinionPollList) {
	  %>
		  <tr>
		  	<td width="20%"><%= DateManager.printDateAsString(survey.getSurveyStartingDate()) %></td>
		    <td width="15%"><%= DateManager.printDateAsString(survey.getSurveyEndingDate()) %></td>
		    <td width="30%"><%= surveyOpinionPoll.getSurveyOpinionPollTitle() %></td>
		    <td width="10%"><%= printer.print(surveyOpinionPoll.getSurveyOpinionPollTypeString()) %></td>
		    <td width="10%"><%= printer.print(survey.getCurrentStatus().toString()) %></td>
		    <td width="10%">

			<a id="<%= surveyOpinionPoll.getKey() %>" href="editOpinionPoll.jsp?k=<%= KeyFactory.keyToString(surveyOpinionPoll.getKey()) + "&sk=" + request.getParameter("sk") + "&readonly=true" %>"><%= printer.print("View") %></a>
		    <br/>
		    <a href="editOpinionPoll.jsp?k=<%= KeyFactory.keyToString(surveyOpinionPoll.getKey()) + "&sk=" + request.getParameter("sk") %>"><%= printer.print("Edit") %></a>	    

		    <br/>
		    <a href="javascript:void(0);" onclick="confirmDelete('/manageRestaurant?action=delete&type=L&k=<%= KeyFactory.keyToString(surveyOpinionPoll.getKey()) %>', '<%= printer.print("Are you sure you want to delete this Opinion Poll")%>');"><%= printer.print("Delete") %></a>
		    </td>
		  </tr>
  <%  
 	  }
  }
  %>
</table>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
