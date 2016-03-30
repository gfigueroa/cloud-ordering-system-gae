<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="datastore.MenuItem" %>
<%@ page import="datastore.MenuItemManager" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>

<%
  User sessionUser = (User)session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("/login.jsp");

  Key restaurantKey = KeyFactory.stringToKey(request.getParameter("r_key"));
  Restaurant restaurant = RestaurantManager.getRestaurant(restaurantKey);
  List<MenuItem> menuItemList = MenuItemManager.getAllMenuItemsByRestaurant(restaurant.getKey());
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

<h1><%
    if (restaurant.getRestaurantLogo() != null) {
    %>
    	<img src="/img?blobkey=<%= restaurant.getRestaurantLogo().getKeyString() %>&s=100">
    <%
    }
    %>
    <%= restaurant.getRestaurantName() %> - Menu
</h1>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">

  <tr>
  	<td width="20%"></td>
    <td width="40%">Name</td>
    <td width="40%">Price</td>
  </tr>
  
  <tr>
  	<td width="20%"></td>
  	<td width="40%"></td>
    <td width="40%"></td>
  </tr>

<% 
  for (MenuItem menuItem : menuItemList) {
%>
  <tr>
  	<td width="20%">
  	<%
  	if (menuItem.getMenuItemImage() != null) {
  	%>
  		<a id="listdetail_<%= KeyFactory.keyToString(menuItem.getKey()) %>" href="customerMenuItemDetail.jsp?k=<%= KeyFactory.keyToString(menuItem.getKey()) %>"><img src="/img?blobkey=<%= menuItem.getMenuItemImage().getKeyString() %>&s=100"></a>
  	<%
  	}
  	%>
  	</td>
    <td width="40%"><a id="listdetail_<%= KeyFactory.keyToString(menuItem.getKey()) %>" href="customerMenuItemDetail.jsp?k=<%= KeyFactory.keyToString(menuItem.getKey()) %>"><%= menuItem.getMenuItemName() %></a></td>
    <td width="40%"><%= menuItem.getMenuItemPrice() %></td>
  </tr>
<%  
  }
%>
</table>

<jsp:include page="../header/page-footer.jsp" />
</body>
</html>
