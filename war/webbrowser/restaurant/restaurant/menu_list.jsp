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
<%@ page import="com.google.appengine.api.blobstore.BlobKey" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
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
	
	Key menuItemTypeKey = null;
	MenuItemType getmenuItemType = null;
	List<MenuItem> menuItems = null;
		
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
%>
<body class="body">

<div class="body_contant">
  	<div style="height:150px;"><jsp:include page="../header/header.jsp" flush="true" /></div>  
 	<div class="menu_title"><span><h2><h8><%= restaurantType.getRestaurantTypeName() %></h8><%= restaurant.getRestaurantName() %></h2></span></div>
	
<div style="width:940px; overflow:hidden; padding-top:20px;">
    <div style="float:left; width:194px; padding:0px 14px 0 42px; text-align:center;">
    <%if(restaurant.getRestaurantLogo()!= null){ %>
    &emsp;<img src="/img?blobkey=<%= restaurant.getRestaurantLogo().getKeyString() %>&s=100">
    <%} %>
    </div>
     
    <div style="float:left; overflow:hidden;">
    <div style="width:338px; font-size:28px;" class="menu_tyle_title"><%= menuItemTypeKey != null ? getmenuItemType.getMenuItemTypeName():""%></div>
    <div style="width:298px; font-size:18px; line-height:35px;" class="menu_tyle_title">預定套數&ensp;<select><option>請選擇</option></select>&ensp;<a href="car_check.jsp?r_key=<%= restaurantKeyString %>">前往購物車</a></div>
    <div style="margin-top:40px;"><hr class="menu_tyle"/></div>
    </div>
    
</div>
	
    <div style="width:940px; overflow:hidden;">
    	<div class="type_list">
        	<div style="height:20px;"></div>
        	<% if (menuItemTypeKey!=null){ %>
			<% for(MenuItemType menuItemType : menuItemTypes){ 
			%>
        	<div class="menutypelist"><a href="menu_list.jsp?r_key=<%= restaurantKeyString %>&mit_key=<%= KeyFactory.keyToString(menuItemType.getKey()) %>"><%= menuItemType.getMenuItemTypeName() %></a></div>
			<%
			}
			
			} %>
        </div>
    <form action="/OrderItemList" method="post">
        <div style="float:left; width:690px;">
        	<% if (menuItemTypeKey!=null){ %>
        	<table cellpadding="0" cellspacing="0" class="menu_list_table" width="648px">
            	<tr bgcolor="#F1E6D2" height="20px"><td valign="middle" colspan="4"><h1>　<%= getmenuItemType.getMenuItemTypeName() %></h1></td></tr>
                
                <% 
                	int row = 1;int column = 1;
                	for(MenuItem menuItem : menuItems){ 
                		if(((row-1) %4 == 0) || (row == 1)){
                %>
                			<tr align="center">
                <% 
                		}
                		if(menuItem.isAvailable()){
                			if(row %4 == 0){
                %>
                	        	<td class="menu_list"  style="border-right-style:none;" valign="top">
				<%
	                		}else{
                %>
    	            			<td class="menu_list" valign="top">
                <% 		
    	            		}
                %>
                	
                			<div style="height:100px; padding-top:10px;">
                    		<img src="/img?blobkey=<%= menuItem.getMenuItemImage().getKeyString() %>&s=100">
                    		</div>
                			<div style="height:45px;"><h6><%= menuItem.getMenuItemName() %></h6></div>
                			<div><h5>價格 <%= menuItem.getMenuItemPrice() %></h5></div>
                			<div><h5>供應時間 <br /><%= menuItemServiceTime[menuItem.getMenuItemServiceTime()] %></h5></div>
                    		<div style=" vertical-align:middle;">
                    		<a href="/addToCar_test?v=cart&action=append&k=<%= KeyFactory.keyToString(menuItem.getKey()) %>&mit_key=<%= KeyFactory.keyToString(menuItemTypeKey) %>">
                    		<h5><br /><img border="0" src="img/addToCart.png"></img></h5></a>
                    		</div>
                			</td>
                
                <%
                			if( row%4 == 0){
                				column = column + 1;
                %>
                				</tr>
                <%
                			}
                			row = row +1;
                		}
                	}
                	if(row%4 > 0){
                		int temp;
                		for(temp= (row%4) ; temp<5;temp++ ){
                			if(temp == 4){
                %>				<td class="menu_list" style="border-right-style:none;"></td>                				             				
                				</tr>
                <%
                			}else{
                %>
                				<td class="menu_list"></td>
                <%			
                			}
                		}
                	}
                %>
                </table>
           <%}else{ %>  	   
        	   <table cellpadding="0" cellspacing="0" class="menu_list_table" width="648px">
        	   <tr>
        	   		<td class="menu_list">尚無產品中。</td>
        	   </tr>
        	    	   
        	   </table>
           <%}%>
                
        </div>
        
    </form>

    </div>

    
    
    
	<div style="width:940px; height:100px;">  	
    	<jsp:include page="../header/footer.jsp"/> 
 	</div>
</div>

</body>
</html>


