<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="datastore.MenuItem" %>
<%@ page import="datastore.MenuItemManager" %>
<%@ page import="datastore.MenuItemType" %>
<%@ page import="datastore.MenuItemTypeManager" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.google.appengine.api.datastore.Email" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>

<%
  User sessionUser = (User)session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("/login.jsp");
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
<meta name="viewport" content="target-densitydpi=device-dpi, width=device-width" />
</head>

<body>
<%@  include file="../header/logout-bar.html" %>
<%@  include file="../header/page-banner.html" %>
<%@  include file="../menu/customer-menu.html" %>

<h1>Search Menu Items</h1>

<div id="search-container">

<div id="search-toolbox">
	<form method="get" id="form1" name="form1" class="form-style" action="">
	  	<fieldset>
		<legend>Search menu items</legend>
		
	    <div class="i">
	    	<input type="input" name="search_box" 
	        		<% if (request.getParameter("search_box") != null) { %>
	                	value="<%= request.getParameter("search_box") %>"
	                <% } %>/>
	    </div>
	    
	    <div class="j">
	      	<select name="search_by">
		    	<option value="T"
		    	<% if (request.getParameter("search_by") != null && request.getParameter("search_by").charAt(0) == 'T') { %>
		        	selected="true"
		        <% } %>>
		        	Menu Item Type
		    	</option>
		        <option value="R"
		        <% if (request.getParameter("search_by") != null && request.getParameter("search_by").charAt(0) == 'R') { %>
		        	selected="true"
		        <% } %>>
		        	Restaurant Email
		        </option>
	      	</select>    
	    </div>
	    
	    <div class="j">
	    	<input type="submit" name="Search" value="Search" />
	    </div>
	    
	    </fieldset>
	
		<br />

    </form>
    
    <hr />
    
</div>

<div id="search-results">
	<h1>Results</h1>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">
	
	<tr>
    	<td width="60%">Menu Item</td>
    	<td width="20%">Restaurant</td>
    	<td width="10%">Type</td>
    	<td width="10%">Price</td>
  	</tr>
  	
  	<tr>
  		<td width="60%"></td>
    	<td width="20%"></td>
    	<td width="10%"></td>
    	<td width="10%"></td>
  	</tr>
	
	<% 
	  List<MenuItem> menuItems = new ArrayList<MenuItem>();
	  String numString;
	  String searchFilter = request.getParameter("search_box");
	  if (searchFilter != null && !searchFilter.isEmpty()) {
	      switch (request.getParameter("search_by").charAt(0)) {
	          case 'R':
	              Restaurant restaurant = RestaurantManager.getRestaurant(new Email(searchFilter));
	              if (restaurant != null) {
	               	  menuItems = MenuItemManager.getAllMenuItemsByRestaurant(restaurant.getKey());
	              }
	              break;
	          case 'T':
	              try {
	              	  Key menuItemType = KeyFactory.stringToKey(searchFilter);
	                  menuItems = MenuItemManager.getAllMenuItemsByType(menuItemType);
	              } 
	              catch (NumberFormatException nfe) {
	                  break; // not much to do just display nothing
	              }
	              break;
	          default:
	              assert(false); // no such type
	      }
	  }
	  
	  Restaurant thisRestaurant = null;
	  for (MenuItem menuItem : menuItems) {
	      if (thisRestaurant != null) {
	          if (!thisRestaurant.getKey().equals(menuItem.getKey().getParent())) {
	              thisRestaurant = RestaurantManager.getRestaurant(menuItem.getKey().getParent());
	          }
	      }
	      else {
	  	  	  thisRestaurant = RestaurantManager.getRestaurant(menuItem.getKey().getParent());
	  	  }
	%>
		  <tr>
		    <td width="60%"><a id="listdetail_<%= KeyFactory.keyToString(menuItem.getKey()) %>" 
		                       href="customerMenuItemDetail.jsp?k=<%= KeyFactory.keyToString(menuItem.getKey()) %>">
		                       <%= menuItem.getMenuItemName() %>
		                    </a>
		    </td>
		    <td width="20%"><%= thisRestaurant.getRestaurantName() %></td>
		    <td width="10%"><%= MenuItemTypeManager.getMenuItemType(menuItem.getMenuItemType()).getMenuItemTypeName() %></td>
		    <td width="10%"><%= menuItem.getMenuItemPrice() %></td>
		  </tr>
	<%  
	  }
	%>
	</table>
</div>

</div>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
