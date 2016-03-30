<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.RestaurantType" %>
<%@ page import="datastore.RestaurantTypeManager" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory;" %>
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
	String typePageString = request.getParameter("t_page");
	int typePage = 0;
	if (typePageString != null) {
		typePage = Integer.parseInt(typePageString);
	}
	
	List<RestaurantType> restaurantTypes = RestaurantTypeManager.getAllRestaurantTypes();
	String thisTypeString = request.getParameter("r_type");
	RestaurantType thisType = null;
	if (thisTypeString != null) {
		thisType = RestaurantTypeManager.getRestaurantType(Long.parseLong(thisTypeString));
	}
	else {
		response.sendRedirect("../index/index.jsp");
	}
	
	List<Restaurant> restaurants = RestaurantManager.getAllRestaurantsByType(thisType.getKey());
	
%>

<body class="body">
<div class="body_contant">
  <div style="height:130px;"><jsp:include page="../header/header.html" flush="true" /></div>  
  <div style="padding-left:39px;"><img src="../restaurant/img/index_contant.gif" width="865px"/></div>
  <div style="red">
  	
  	<%	  	
  	if (typePage != 0) {
  	%>
  		<a href="test.jsp?t_page=<%= typePage - 1 %>&r_type=<%= thisType.getKey() %>"><<</a>
  	<%
  	}
	for (int i = typePage * 2; i < (typePage * 2) + 2 && i < restaurantTypes.size(); i++) {
		RestaurantType restaurantType = restaurantTypes.get(i);
		
	%>
	
	<a href="test.jsp?t_page=<%= typePage %>&r_type=<%= restaurantType.getKey() %>"><%= restaurantType.getRestaurantTypeName() %></a> |
	
	<%
	}
	%>
	
	<%
		if (true) {	
	%>
		<a href="test.jsp?t_page=<%= typePage + 1 %>&r_type=<%= thisType.getKey() %>">>></a>
	<%
		}
	%>
  </div>
  <div>
  	<%
  	for (Restaurant restaurant : restaurants) {
  	%>
  	
  	<%
	if (restaurant.getRestaurantLogo() != null) {
	%>
	    <div>
			<a href="test2.jsp?r_key=<%= KeyFactory.keyToString(restaurant.getKey()) %>">
				<img src="/img?blobkey=<%= restaurant.getRestaurantLogo().getKeyString() %>&s=200">
			</a>
		</div>
	<%
	}
	%>
  	<a href="test2.jsp?r_key=<%= KeyFactory.keyToString(restaurant.getKey()) %>">
  	<%= restaurant.getRestaurantName()%>
  	</a>
  	<%  	
  	}
  	%>
  </div>
    
    
    
<div style="width:940px; height:100px;">  	
	<jsp:include page="../header/footer.html"/> 
</div>

</div>
</body>
</html>