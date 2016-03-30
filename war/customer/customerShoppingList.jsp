<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="datastore.MenuItem" %>
<%@ page import="session.MenuItemContainer" %>
<%@ page import="session.CartManager" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>

<%
  User sessionUser = (User)session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("../login.jsp");
    
  String message = request.getParameter("msg");
  String action = request.getParameter("action");
  
  HashMap<Key, ArrayList<MenuItemContainer>> menuItemMap = CartManager.getCartGroupedByRestaurant(session);
  Set<Key> restaurantKeys = menuItemMap.keySet();
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

<h1>Shopping List</h1>

<%
if (message != null && action != null && message.equals("success") && action.equals("buy")) {
%>
	<div class="success-div">
	Order sent successfully to restaurant!
	</div>
<%
}
%>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">
  
  <tr>
  	<td width="15%">Restaurant</td>
  	<td width="20%">Restaurant name</td>
  	<td width="40%">Order detail</td>
  	<td width="15%">Total</td>
  	<td width="15%">Actions</td>
  </tr>
  
  <tr>
  	<td width="15%"></td>
  	<td width="20%"></td>
  	<td width="40%"></td>
  	<td width="15%"></td>
  	<td width="15%"></td>
  </tr>

<% 
  for (Key restaurantKey : restaurantKeys) {
  	Restaurant restaurant = RestaurantManager.getRestaurant(restaurantKey);
  	ArrayList<MenuItemContainer> menuItems = menuItemMap.get(restaurantKey);
  	double total = 0;
%>
  <tr>
    <td width="15%">
    <%
    if (restaurant.getRestaurantLogo() != null) {
    %>
    	<img src="/img?blobkey=<%= restaurant.getRestaurantLogo().getKeyString() %>&s=150">
    <%
    }
    %>
    </td>
    <td width="20%"><%= restaurant.getRestaurantName() %></td>
    <td width="40%">
    <%
    for (MenuItemContainer menuItemContainer : menuItems) {
    	
    	MenuItem menuItem = menuItemContainer.menuItem;
    	double discount = menuItem.getMenuItemDiscount() != null ?
        			menuItem.getMenuItemDiscount() * menuItem.getMenuItemPrice() :
        			0;
        double price = (menuItem.getMenuItemPrice() - discount) * menuItemContainer.qty;
        total += price;
    %>
    <p>
    <%= menuItem.getMenuItemName() %>
    </p>
    <%
    }
    %>
    </td>
    <td width="15%"><%= total %></td>
    <td width="15%"><a href="customerShoppingCart.jsp?r_key=<%= KeyFactory.keyToString(restaurantKey) %>">Checkout</a></td>
  </tr>
<%  
  }
%>
</table>

<jsp:include page="../header/page-footer.jsp" />
</body>
</html>
