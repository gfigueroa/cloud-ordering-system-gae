<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.Branch" %>
<%@ page import="datastore.BranchManager" %>
<%@ page import="datastore.RegionManager" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>

<%
  User sessionUser = (User)session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("../login.jsp");
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

<%
	Restaurant restaurant = RestaurantManager.getRestaurant(KeyFactory.stringToKey(request.getParameter("r_key")));
	List<Branch> branchList = BranchManager.getRestaurantBranches(restaurant.getKey());
%>

<h1><%
    if (restaurant.getRestaurantLogo() != null) {
    %>
    	<img src="/img?blobkey=<%= restaurant.getRestaurantLogo().getKeyString() %>&s=100">
    <%
    }
    %>
    <%= restaurant.getRestaurantName() %> - Branches
</h1>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">
  
  <tr>
  	<td width="10%">Region</td>
  	<td width="20%">Branch name</td>
  	<td width="40%">Address</td>
    <td width="10%">Phone number</td>
    <td width="30%">Services</td>
  </tr>
  
  <tr>
  	<td width="10%"></td>
  	<td width="20%"></td>
  	<td width="40%"></td>
    <td width="10%"></td>
    <td width="30%"></td>
  </tr>

<% 
  for (Branch branch : branchList) {
%>
  <tr>
    <td width="10%"><%= RegionManager.getRegion(branch.getRegion()).getRegionName() %></td>
    <td width="20%"><%= branch.getBranchName() %></td>
    <td width="40%"><%= branch.getBranchAddress().getAddress() %></td>
    <td width="10%"><%= branch.getBranchPhone().getNumber() %></td>
    <td width="30%">
    <% 
    if (branch.hasDelivery()) { 
    %>
    	<p>&#x2713; Delivery</p>
    <% 
    } 
    %>
    <% 
    if (branch.hasTakeOut()) { 
    %>
    	<p>&#x2713; Take-Out</p>
    <% 
    } 
    %>
    <% 
    if (branch.hasTakeIn()) { 
    %>
    	<p>&#x2713; Dine-In</p>
    <% 
    } 
    %>
    </td>
  </tr>
<%  
  }
%>
</table>

<jsp:include page="../header/page-footer.jsp" />
</body>
</html>
