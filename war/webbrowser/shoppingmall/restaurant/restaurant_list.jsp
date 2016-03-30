<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.RestaurantType" %>
<%@ page import="datastore.RestaurantTypeManager" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.Branch" %>
<%@ page import="datastore.BranchManager" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory;" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link type="text/css" rel="stylesheet" href="../css/restaurant_list.css" />
<link type="text/css" rel="stylesheet" href="../css/deafult.css" />
<link rel="stylesheet" type="text/css" href="../css/header_footer.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>智慧人科技-點餐即時享-店家資訊</title>
</head>
<%
	String restaurantTypeKeyString = request.getParameter("rt_key");
	RestaurantType restaurantTypeKey = null;
	if (restaurantTypeKeyString != null) {
		restaurantTypeKey = RestaurantTypeManager.getRestaurantType(Long.parseLong(restaurantTypeKeyString));
	}
	else {
		response.sendRedirect("../index/index.jsp");
	}
	
	List<Restaurant> restaurants = RestaurantManager.getAllRestaurantsByType(Long.parseLong(restaurantTypeKeyString));
%>
<body class="body">
<div class="body_contant">
  <div style="height:150px;">
  <%@ page contentType="text/html; charset=utf-8" %>
  <jsp:include page="../header/header.jsp" flush="true" />
  </div>  
  <div style="padding-left:39px;"><img src="../restaurant/img/index_contant.gif" width="865px"/></div>
  <div style="height:20px;"></div>
  
  <div style="width:940px; overflow:hidden;">
  <div style="background-image:url(../restaurant/img/contant_background/ctl.jpg);" class="list_contant list_contant_b"></div>
  <div style="background-image:url(../restaurant/img/contant_background/ctm.jpg);" class="list_contant_c"></div>
    
  <div style="background-image:url(../restaurant/img/contant_background/ctr.jpg);" class="list_contant"></div>
  </div>
  <div style="width:940px;">

  <table border="0" cellpadding="0" cellspacing="0">
  <tr>
  <td style="background-image:url(../restaurant/img/contant_background/cml.jpg);" class="list_contant_b left"></td>
  <td style="background-image:url(../restaurant/img/contant_background/cmm.jpg);" class="middle">
  <img src="../restaurant/img/list_title.gif" />
  <table border="0" cellpadding="0" cellspacing="0" width="100%">
	<%
	int count=0;
  	for (Restaurant restaurant : restaurants) {
  		if(count!=0){
  	%>
  	<tr><td colspan="5"><p><img src="../restaurant/img/hr.gif" /></p></td></tr>
  	<%}
  		count=1;
  	%>
  	<tr>
    <td width="25%" align="center">
  	<%
	if (restaurant.getRestaurantLogo() != null) {
	%><p>
	<a href="">
		<img src="/img?blobkey=<%= restaurant.getRestaurantLogo().getKeyString() %>&s=150">
	</a></p>
	<%
	}
	%>
    </td>
    <td width="37%"><p>
  	<%= restaurant.getRestaurantDescription() %></p></td>
    <td width="16%">店家選擇<br />
    <select id=<%= KeyFactory.keyToString(restaurant.getKey()) %> style="width:80px;">
    <option>請選擇</option>
    <% List<Branch> branches= BranchManager.getRestaurantBranches(restaurant.getKey());
    	for(Branch branch : branches)
    	{
    %>
    <option value="<%= KeyFactory.keyToString(branch.getKey()) %>"><%= branch.getBranchName() %></option>
    <%} %>
    </select></td>
    <td width="15%"><a href="../restaurant/menu_list.jsp?r_key=<%= KeyFactory.keyToString(restaurant.getKey()) %>"><img src="../restaurant/img/link.gif" border="0" /></a></td>
    <td width="7%"><img src="../restaurant/img/click.gif" /></td>
    </tr>
    
	
  	<%  	
  	}
  	%>
 
  
  
  
 </table>
  
  
  
  
  
  
  </td>
  <td style="background-image:url(../restaurant/img/contant_background/cmr.jpg);" class="bottom"></td>
  </tr>
  </table>
  </div>
    
  <div style="width:940px; overflow:hidden;">
  <div style="background-image:url(../restaurant/img/contant_background/cbl.jpg);" class="list_contant list_contant_b"></div>
  <div style="background-image:url(../restaurant/img/contant_background/cbm.jpg);" class="list_contant_c"></div>
  <div style="background-image:url(../restaurant/img/contant_background/cbr.jpg);" class="list_contant"></div>
  </div>
    
    
<div style="width:940px; height:100px;">  	
        <jsp:include page="../header/footer.jsp"/> 
 	</div>
</div>
</body>
</html>