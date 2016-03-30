<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Collection" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="datastore.MenuItem" %>
<%@ page import="datastore.MenuItemManager" %>
<%@ page import="datastore.MenuItemType" %>
<%@ page import="datastore.MenuItemTypeManager" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>

<%
  User sessionUser = (User)session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("/login.jsp");
  else {
  	if (sessionUser.getUserType() != User.Type.RESTAURANT) {
  		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  	}
  }
%>

<% 
Printer printer = (Printer)session.getAttribute("printer");
%>
<jsp:include page="../header/language-header.jsp" />

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
<script src="../js/treeview.js"></script>

<%@  include file="../header/page-title.html" %>
</head>

<body>
<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<h1><%= printer.print("Product Items") %></h1>

<%
	Restaurant restaurant = RestaurantManager.getRestaurant(sessionUser);
    
    HashMap<Key, ArrayList<MenuItem>> menuItemMap = 
    		MenuItemManager.getAllMenuItemsGroupedByType(restaurant);
    
    Set<Key> menuItemTypeKeySet = menuItemMap.keySet();
%>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">

  <tr>
    <td width="70%"><a href="/restaurant/addMenuItem.jsp">+ <%= printer.print("Add New Product Item") %></a></td>
    <td width="5%"></td>
    <td width="15%"></td>
    <td width="10%"></td>
  </tr>
  
  <tr>
  	<td width="70%"></td>
    <td width="5%"></td>
    <td width="15%"></td>
    <td width="10%"></td>
  </tr>
</table>

<% 
  for (Key menuItemTypeKey : menuItemTypeKeySet) {
  	MenuItemType menuItemType = MenuItemTypeManager.getMenuItemType(menuItemTypeKey);
  	ArrayList<MenuItem> menuItemArrayList = menuItemMap.get(menuItemTypeKey);
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">
  <tr>
  	<td width="70%"><a href="JavaScript:doMenu('<%= menuItemType != null ? menuItemType.getMenuItemTypeName() : KeyFactory.keyToString(menuItemTypeKey) %>');" id='x<%= menuItemType != null ? menuItemType.getMenuItemTypeName() : KeyFactory.keyToString(menuItemTypeKey) %>'>[+]</a><%= menuItemType != null ? " " + menuItemType.getMenuItemTypeName() + " (" + menuItemArrayList.size() + ")": printer.print("No Menu Item Type") %></td>
  	<td width="5%"></td>
  	<td width="15%"></td>
    <td width="10%"></td>
  </tr>
</table>

<div  class="treeview" id='<%= menuItemType != null ? menuItemType.getMenuItemTypeName() : KeyFactory.keyToString(menuItemTypeKey) %>' >
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">	
	
	<%
	  	for (MenuItem menuItem : menuItemArrayList) {
	%>
	  <tr>
	    <td width="70%"><a id="<%= KeyFactory.keyToString(menuItem.getKey()) %>" href="/restaurant/editMenuItem.jsp?k=<%= KeyFactory.keyToString(menuItem.getKey()) + "&readonly=true" %>"><%= menuItem.getMenuItemName() %></a></td>
	    <td width="5%"><img src="../images/edit1.jpg" width="23" height="23" /><a href="/restaurant/editMenuItem.jsp?k=<%= KeyFactory.keyToString(menuItem.getKey()) %>"><%= printer.print("Edit") %></a></td>
	    <td width="15%"><img src="../images/edit1.jpg" width="23" height="23" /><a href="/restaurant/editProductItemProperty.jsp?readonly=true&mi_k=<%= KeyFactory.keyToString(menuItem.getKey()) %>"><%= printer.print("Additional Properties") %></a></td>
	    <td width="10%"><img src="../images/delete.jpg" width="23" height="23" /><a href="javascript:void(0);" onclick="confirmDelete('/manageRestaurant?action=delete&type=I&k=<%= KeyFactory.keyToString(menuItem.getKey()) %>', '<%= printer.print("Are you sure you want to delete this Product Item")%>');"><%= printer.print("Delete") %></a></td>
	  </tr>
	<%  
		}
	%>
	  <tr>
	  	<td width="70%"></td>
	    <td width="5%"></td>
	    <td width="15%"></td>
	    <td width="10%"></td>
	  </tr>
	</table>
</div>
<%
  }
%>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
