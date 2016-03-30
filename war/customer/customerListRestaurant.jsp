<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>

<%
  User sessionUser = (User)session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("../login.jsp");
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

<%@  include file="../header/page-title.html" %>
</head>

<body>
<%@  include file="../header/logout-bar.html" %>
<%@  include file="../header/page-banner.html" %>
<%@  include file="../menu/customer-menu.html" %>

<h1>Restaurant List</h1>

<%
	List<Restaurant> restaurantList = RestaurantManager.getAllRestaurants();
%>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">
  
  <tr>
  	<td width="15%">Restaurant</td>
  	<td width="20%">Restaurant name</td>
  	<td width="40%">Description</td>
  	<td width="15%">Restaurant Information</td>
  	<td width="15%">Branches</td>
  </tr>
  
  <tr>
  	<td width="15%"></td>
  	<td width="20%"></td>
  	<td width="40%"></td>
  	<td width="15%"></td>
  	<td width="15%"></td>
  </tr>

<% 
  for (Restaurant restaurant : restaurantList) {
%>
  <tr>
    <td width="15%">
    <%
    if (restaurant.getRestaurantLogo() != null) {
    %>
    	<a href="customerListMenuItem.jsp?r_key=<%= KeyFactory.keyToString(restaurant.getKey()) %>"><img src="/img?blobkey=<%= restaurant.getRestaurantLogo().getKeyString() %>&s=150"></a>
    <%
    }
    %>
    </td>
    <td width="20%"><a href="customerListMenuItem.jsp?r_key=<%= KeyFactory.keyToString(restaurant.getKey()) %>"><%= restaurant.getRestaurantName() %></a></td>
    <td width="40%"><%= restaurant.getRestaurantDescription() %></td>
    <td width="15%"><a id="listdetail_<%= KeyFactory.keyToString(restaurant.getKey()) %>" href="customerRestaurantDetail.jsp?k=<%= KeyFactory.keyToString(restaurant.getKey()) %>">View Restaurant Info</a></td>
    <td width="15%"><a href="customerListBranch.jsp?r_key=<%= KeyFactory.keyToString(restaurant.getKey()) %>">View Branches</a></td>
  </tr>
<%  
  }
%>
</table>

<jsp:include page="../header/page-footer.jsp" />
</body>
</html>
