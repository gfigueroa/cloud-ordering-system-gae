<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.MenuItem" %>
<%@ page import="datastore.MenuItemManager" %>
<%@ page import="datastore.MenuItemType" %>
<%@ page import="datastore.MenuItemTypeManager" %>
<%@ page import="datastore.Branch" %>
<%@ page import="datastore.BranchManager" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="com.google.appengine.api.blobstore.BlobKey" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>

<%
  User sessionUser = (User)session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("/login.jsp");
  
  String error = request.getParameter("etype");
  
  Key menuItemKey = KeyFactory.stringToKey(request.getParameter("k"));
  MenuItem menuItem = MenuItemManager.getMenuItem(menuItemKey);
  MenuItemType menuItemType = MenuItemTypeManager.getMenuItemType(menuItem.getMenuItemType());
  
  Restaurant restaurant = RestaurantManager.getRestaurant(menuItemKey.getParent());
  
  BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />
<%@  include file="../header/page-title.html" %>
</head>

<body>

<form method="post" id="form1" name="form1"
      class="form-style">

    <fieldset>
	<legend>Menu Item</legend>
	
	<div>
		<h2>Menu Item Information</h2>
	</div>
	
	<div>
       	<label  for="r_name"><span>Restaurant</span></label>
		<input type="text" name="r_name" class="input_extra_large" value="<%= restaurant.getRestaurantName() %>" title="" disabled="true" /><br />
		<div id="r_name"></div>
	</div>
    
    <div>
       	<label  for="mit_id"><span>Menu Item Type</span></label>
		<input type="text" name="mit_id" class="input_extra_large" value="<%= menuItemType != null ? menuItemType.getMenuItemTypeName() : "No menu item type" %>" title="" disabled="true" /><br />
		<div id="mit_id"></div>
	</div>
	
    <div>
       	<label  for="mi_name"><span>Menu Item Name</span></label>
		<input type="text" name="mi_name" class="input_extra_large" value="<%= menuItem.getMenuItemName() %>" title="" disabled="true" /><br />
		<div id="mi_name"></div>
	</div>
	
	<div>
		<label for="mi_price"><span>Price</span></label>
		<input type="text" name="mi_price" class="input_large" value="<%= menuItem.getMenuItemPrice() %>" disabled="true" /><br />
		<div id="mi_price"></div>
	</div>
	
	<div>
		<label for="mi_discount"><span>Discount</span></label>
		<input type="text" name="mi_discount" class="input_large" value="<%= menuItem.getMenuItemDiscount() == null ? "" : menuItem.getMenuItemDiscount() %>" disabled="true" /><br />
		<div id="mi_discount"></div>
	</div>
	
 	<div>
       	<label for="mi_description"><span>Description</span></label>
		<textarea  name="mi_description" class="input_extra_large" value="" disabled="true"><%= menuItem.getMenuItemDescription() %></textarea>
		<br />
		<div id="mi_description"></div>
	</div>
    
    <%
	if (menuItem.getMenuItemImage() != null) {
	%>
	    <div>
	       	<label for="mi_image"><span>Image</span></label>
				<a target="_new" href="/img?blobkey=<%= menuItem.getMenuItemImage().getKeyString() %>">
				<img src="/img?blobkey=<%= menuItem.getMenuItemImage().getKeyString() %>&s=300">
				</a>
				<br />
			
			<div id="mi_image"></div>
		</div>
	<%
	}
	%>
	
	<div>
		<label for="mi_serving_time"><span>Serving time (minutes)</span></label>
		<input type="text" name="mi_serving_time" class="input_large" value="<%= menuItem.getMenuItemServingTime() == null ? "" : menuItem.getMenuItemServingTime() %>" disabled="true" /><br />
		<div id="mi_serving_time"></div>
	</div>
	
	<div>
       	<label for="mi_isavailable"><span>Available</span></label>
		<input type="radio" <%= menuItem.isAvailable() ? "checked=\"true\"" : "" %> name="mi_isavailable" value="Y" disabled="true" /> Yes
		<input type="radio" <%= !menuItem.isAvailable() ? "checked=\"true\"" : "" %> name="mi_isavailable" value="N" disabled="true" /> No
		<br />
		<div id="mi_isavailable"></div>
	</div>
	
	<div>
		<label for="mi_service_time"><span>Service time</span></label>
		<select name="mi_service_time" disabled="true">
        <option value="0" <%= menuItem.getMenuItemServiceTime() == 0 ? "selected=\"true\"" : "" %>>All day</option>
        <option value="1" <%= menuItem.getMenuItemServiceTime() == 1 ? "selected=\"true\"" : "" %>>Breakfast (07:00 - 12:00)</option>
        <option value="2" <%= menuItem.getMenuItemServiceTime() == 2 ? "selected=\"true\"" : "" %>>Lunch (12:00 - 16:30)</option>
        <option value="3" <%= menuItem.getMenuItemServiceTime() == 3 ? "selected=\"true\"" : "" %>>Dinner (16:30 - 23:30)</option>
        <option value="4" <%= menuItem.getMenuItemServiceTime() == 4 ? "selected=\"true\"" : "" %>>Breakfast + Lunch</option>
        <option value="5" <%= menuItem.getMenuItemServiceTime() == 5 ? "selected=\"true\"" : "" %>>Breakfast + Dinner</option>
        <option value="6" <%= menuItem.getMenuItemServiceTime() == 6 ? "selected=\"true\"" : "" %>>Lunch + Dinner</option>
        </select>
		<div id="mi_service_time"></div>
	</div>
	
	<hr/>
	
	<div id="product-checkout">
	<% 
	if (menuItem.isAvailable()) {
		List<Branch> branches = BranchManager.getRestaurantBranches(restaurant.getKey());
		if (!branches.isEmpty()) {
	%>
			<a href="/addToCart?v=cart&action=append&k=<%= request.getParameter("k") %>"><img src="../images/addToCart.png" /></a>
	<% 
		}
		else {
	%>
			<div class="error-div">This restaurant has no available branches.</div>
	<%
		}
	} 
	%>
	</div>

	</fieldset>
	
	<br class="clearfloat" />

</form>

</body>
</html>