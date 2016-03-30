<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="datastore.RestaurantType" %>
<%@ page import="datastore.RestaurantTypeManager" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>

<% 
Printer printer = (Printer)session.getAttribute("printer");
%>
<jsp:include page="../header/language-header.jsp" />

<%
  User sessionUser = (User)session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("../login.jsp");
  else {
  	if (sessionUser.getUserType() != User.Type.ADMINISTRATOR) {
  		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  	}
  }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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

<h1><%= printer.print("Retail Store List") %></h1>

<%
	List<Restaurant> restaurantList = RestaurantManager.getAllRestaurants();
%>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">

  <tr>
    <td width="30%"><a href="/admin/addRestaurant.jsp">+ <%= printer.print("Add New Retail Store") %></a></td>
    <td width="10%"></td>
    <td width="15%"></td>
    <td width="15%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
  </tr>
  
  <tr>
  	<td width="30%"></td>
  	<td width="10%"></td>
  	<td width="15%"></td>
  	<td width="15%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
  </tr>
  
  <tr>
  	<td width="30%"><%= printer.print("Retail Store Name") %></td>
  	<td width="10%"><%= printer.print("Service Type") %></td>
  	<td width="15%"><%= printer.print("Retail Store type") %></td>
  	<td width="15%"><%= printer.print("E-mail") %></td>
    <td width="10%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
  </tr>

<% 
  for (Restaurant restaurant : restaurantList) {
  	RestaurantType type = RestaurantTypeManager.getRestaurantType(restaurant.getRestaurantType());
%>
  <tr>
    <td width="30%"><a href="editRestaurant.jsp?k=<%= KeyFactory.keyToString(restaurant.getKey()) + "&readonly=true" %>"><%= restaurant.getRestaurantName() %></a></td>
    <td width="10%"><%= type != null ? printer.print(type.getStoreSuperTypeString()) : printer.print("Not assigned yet") %></td>
    <td width="15%"><%= type != null ? type.getRestaurantTypeName() : printer.print("Not assigned yet") %></td>
    <td width="15%"><%= restaurant.getUser().getUserEmail().getEmail() %></td>
    <td width="10%">
    <img src="../images/edit1.jpg" width="23" height="23" /><a href="../restaurant/listNews.jsp?r_key=<%= KeyFactory.keyToString(restaurant.getKey()) %>"><%= printer.print("News") %></a><br/>
    <img src="../images/edit1.jpg" width="23" height="23" /><a href="../restaurant/listOpinionPoll.jsp?r_key=<%= KeyFactory.keyToString(restaurant.getKey()) %>"><%= printer.print("Opinion Polls") %></a><br/>
    <img src="../images/edit1.jpg" width="23" height="23" /><a href="../restaurant/listSurvey.jsp?r_key=<%= KeyFactory.keyToString(restaurant.getKey()) %>"><%= printer.print("Surveys") %></a>
    </td>
    <td width="10%">
    	<img src="../images/edit1.jpg" width="23" height="23" /><a href="../restaurant/listBranch.jsp?r_key=<%= KeyFactory.keyToString(restaurant.getKey()) %>"><%= printer.print("Branches") %></a></br>
		<img src="../images/edit1.jpg" width="23" height="23" /><a href="editRestaurant.jsp?k=<%= KeyFactory.keyToString(restaurant.getKey()) %>"><%= printer.print("Edit Profile") %></a>
	</td>
    <td width="10%"><img src="../images/delete.jpg" width="23" height="23" /><a href="javascript:void(0);" onclick="confirmDelete('/manageUser?action=delete&u_type=R&k=<%= KeyFactory.keyToString(restaurant.getKey()) %>', '<%= printer.print("Are you sure you want to delete this Retail Store")%>');"><%= printer.print("Delete") %></a>
    </td>
  </tr>
<%  
  }
%>
</table>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>