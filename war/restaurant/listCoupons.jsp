<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="util.DateManager" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.RestaurantNewsManager" %>
<%@ page import="datastore.RestaurantNews" %>
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

<h1>
<%= restaurant.getRestaurantName() + " - " %>
<%
if (type == 'A') {
%>
	<%= printer.print("Active Coupons") %>
<%
}
else if (type == 'I') {
%>
	<%= printer.print("Inactive Coupons") %>
<%
}
else {
%>
	<%= printer.print("Coupons History") %>
<%
}
%>
</h1>

<div>
	<a href="listCoupons.jsp?r_key=<%= KeyFactory.keyToString(restaurantKey) %>&type=A"><%= printer.print("Active Coupons") %></a> | <a href="listCoupons.jsp?r_key=<%= KeyFactory.keyToString(restaurantKey) %>&type=I"><%= printer.print("Inactive Coupons") %></a> | <a href="listCoupons.jsp?r_key=<%= KeyFactory.keyToString(restaurantKey) %>&type=H"><%= printer.print("Coupons History") %></a>
</div>

<%
	List<RestaurantNews> restaurantNewsList;
	if (type == 'A')
		restaurantNewsList = RestaurantNewsManager.getActiveRestaurantNewsFromRestaurant(restaurant.getKey());
	else if (type == 'I')
		restaurantNewsList = RestaurantNewsManager.getInactiveRestaurantNewsFromRestaurant(restaurant.getKey());
	else 
		restaurantNewsList = RestaurantNewsManager.getExpiredRestaurantNewsFromRestaurant(restaurant.getKey());
%>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">

<%
if (sessionUser.getUserType() == User.Type.RESTAURANT) {
%>
  <tr>
  	<td width="20%"><a href="addCoupons.jsp">+ <%= printer.print("Add Coupons") %></a></td>
  	<td width="20%"></td>
    <td width="20%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
  </tr>
  
  <tr>
  	<td width="20%"></td>
  	<td width="20%"></td>
    <td width="20%"></td>
    <td width="10%"></td>
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
    <td width="20%"><%= printer.print("Title") %></td>
    <td width="10%"><%= printer.print("Type") %></td>
    <td width="10%"><%= printer.print("Price") %></td>
    <td width="10%"><%= printer.print("Cash Included") %></td>
    <td width="10%"><%= printer.print("Actions") %></td>
  </tr>
  
  <tr>
  	<td width="20%"></td>
  	<td width="20%"></td>
  	<td width="20%"></td>
  	<td width="10%"></td>
  	<td width="10%"></td>
  	<td width="10%"></td>
    <td width="10%"></td>
  </tr>

  <% 
  for (RestaurantNews restaurantNews : restaurantNewsList) {
  %>
	  <tr>
	  	<td width="20%"><%= DateManager.printDateAsString(restaurantNews.getRestaurantNewsStartingDate()) %></td>
	    <td width="20%"><%= DateManager.printDateAsString(restaurantNews.getRestaurantNewsEndingDate()) %></td>
	    <td width="20%">Title</td>
	    <td width="10%">Type</td>
	    <td width="10%">Price</td>
	    <td width="10%">Cash</td>
	    <td width="10%">
	    <%
	    if (type == 'A' || type == 'I') {
	    %>
	    	<a id="<%= KeyFactory.keyToString(restaurantNews.getKey()) %>" href="editCoupons.jsp?k=<%= KeyFactory.keyToString(restaurantNews.getKey()) + "&readonly=true" %>"><%= printer.print("View") %></a>
	    	<br/>
	    	<a href="editCoupons.jsp?k=<%= KeyFactory.keyToString(restaurantNews.getKey()) %>"><%= printer.print("Edit") %></a>
	    <%
	    }
	    else {
	    %>
	    	<a id="<%= KeyFactory.keyToString(restaurantNews.getKey()) %>" href="editCoupons.jsp?k=<%= KeyFactory.keyToString(restaurantNews.getKey()) + "&readonly=true" %>"><%= printer.print("View") %></a>
	    <%
	    }
	    %>
	    <br/>
	    <a href="javascript:void(0);" onclick="confirmDelete('/manageRestaurant?action=delete&type=C&k=<%= KeyFactory.keyToString(restaurantNews.getKey()) %>', '<%= printer.print("Are you sure you want to delete this Coupon")%>');"><%= printer.print("Delete") %></a>
	    </td>
	  </tr>
  <%  
  }
  %>
</table>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
