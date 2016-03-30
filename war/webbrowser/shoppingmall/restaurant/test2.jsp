<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.RestaurantType" %>
<%@ page import="datastore.RestaurantTypeManager" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.MenuItemManager" %>
<%@ page import="datastore.MenuItemTypeManager" %>
<%@ page import="datastore.MenuItemType" %>
<%@ page import="datastore.MenuItem" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link type="text/css" rel="stylesheet" href="../css/font.css" />
<link type="text/css" rel="stylesheet" href="../css/restaurant_list.css" />
<link type="text/css" rel="stylesheet" href="../css/deafult.css" />
<link rel="stylesheet" type="text/css" href="../css/header_footer.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>智慧人科技-點餐即時享-店家資訊</title>
</head>

<%
	Key menuItemTypeKey;
	String restaurantKeyString = request.getParameter("r_key");
	String menuitemtypeKeyString = request.getParameter("mit_key");
	
	
	Restaurant restaurant = RestaurantManager.getRestaurant(KeyFactory.stringToKey(restaurantKeyString));
	List<MenuItemType> menuItemTypes = restaurant.getMenuItemTypes();
	
	
	if (menuitemtypeKeyString == null)
	{
		menuItemTypeKey = menuItemTypes.get(0).getKey();
	}
	else {
		menuItemTypeKey = KeyFactory.stringToKey(menuitemtypeKeyString);
	}
	
	
	
	
	List<MenuItem> menuItems = MenuItemManager.getAllMenuItemsByType(menuItemTypeKey);
	
	
%>

<body class="body">
<div class="body_contant">
  <div style="height:130px;"><jsp:include page="../head$er/header.html" flush="true" /></div>  
  <div style="padding-left:39px;"><img src="../restaurant/img/index_contant.gif" width="865px"/></div>
	<div>
	<% for(MenuItemType menuItemType : menuItemTypes) {
	%><br/>
		<a href="test2.jsp?r_key=<%= restaurantKeyString %>&mit_key=<%= KeyFactory.keyToString(menuItemType.getKey()) %>">
		<%= menuItemType.getMenuItemTypeName() %>
		</a>
	<%
	}
	%>
	</div>
  <div>
  	<% for(MenuItem menuitems : menuItems ){ %>
  	<br/>
  		<%= menuitems.getMenuItemName() %>
  	<%} %>
  </div>	

    
<div style="width:940px; height:100px;">  	
	<jsp:include page="../header/footer.html"/> 
</div>

</div>
</body>
</html>