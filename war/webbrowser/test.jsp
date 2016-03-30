<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.MenuItemType" %>
<%@ page import="datastore.RestaurantTypeManager" %>
<%@ page import="datastore.RestaurantType" %>
<%@ page import="datastore.MenuItemManager" %>
<%@ page import="datastore.MenuItem" %>
<%@ page import="datastore.MenuItemTypeManager" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="webbrowser.OrderItemList" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link type="text/css" rel="stylesheet" href="../css/menu_list.css" />
<link type="text/css" rel="stylesheet" href="../css/deafult.css" />
<link rel="stylesheet" type="text/css" href="../css/header_footer.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>智慧人科技-點餐即時享-西洋美食</title>
</head>
<script type="text/JavaScript">
<!--

function Add(count) {

	var a = document.getElementById(count).value;
	var total = parseInt(a);
	if(total >= 0)
	{
		total = total+1;
	}else{
		total=0;
	}
	document.getElementById(count).value = total.toString();

}

function SUB(count) {

	var a = document.getElementById(count).value;
	var total = parseInt(a);
	if(total > 0)
	{
		total = total-1;
	}else{
		total=0;
	}
	document.getElementById(count).value = total.toString();

}


</script>
<%
	//OrderItemList.ClearSession(session);
	//session.setAttribute("CarSet", null);
	
	String restaurantKeyString = request.getParameter("r_key");
	String menuItemTypeKeyString = request.getParameter("mit_key"); 
	String[] menuItemServiceTime ={"All day","Breakfast (07:00 - 12:00)","Lunch (12:00 - 16:30)","Dinner (16:30 - 23:30)","Breakfast + Lunch","Breakfast + Dinner","Lunch + Dinner"};
	
	Key menuItemTypeKey;
	MenuItemType getmenuItemType;
	List<MenuItem> menuItems;
		
	Restaurant restaurant = RestaurantManager.getRestaurant(KeyFactory.stringToKey(restaurantKeyString));
	RestaurantType restaurantType = RestaurantTypeManager.getRestaurantType(restaurant.getRestaurantType());
	List<MenuItemType> menuItemTypes = MenuItemTypeManager.getRestaurantMenuItemTypes(restaurant.getKey());
	
	if(!menuItemTypes.isEmpty()){
		if(menuItemTypeKeyString == null)
		{
			menuItemTypeKey = menuItemTypes.get(0).getKey();
		}else{
			menuItemTypeKey = KeyFactory.stringToKey(menuItemTypeKeyString);
		}
		getmenuItemType = MenuItemTypeManager.getMenuItemType(menuItemTypeKey);
		menuItems = MenuItemManager.getAllMenuItemsByType(menuItemTypeKey);
	}
	
	//Key menuItemTypeKey = KeyFactory.stringToKey(menuItemTypeKeyString);
	

	
	//if(restaurantKeyString == null)
	//{
	//	response.sendRedirect("../restaurant/restaurant_list.jsp");
	//}
	
	//RestaurantType restaurantType = RestaurantTypeManager.getRestaurantType(restaurant.getRestaurantType());
	
	//if(menuItemTypeKeyString == null)
	//{
	//	menuItemTypeKey = menuItemTypes.get(0).getKey();
	//}else{
	//	menuItemTypeKey = KeyFactory.stringToKey(menuItemTypeKeyString);
	//}
	

	//MenuItemType getmenuItemType = MenuItemTypeManager.getMenuItemType(menuItemTypeKey);
	//List<MenuItem> menuItems = MenuItemManager.getAllMenuItemsByType(menuItemTypeKey);
	
%>
<body class="body">
<div><%= restaurant.getRestaurantLogo().getKeyString() %></div>

</body>
</html>


