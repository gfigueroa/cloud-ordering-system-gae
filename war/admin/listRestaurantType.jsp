<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.RestaurantType" %>
<%@ page import="datastore.RestaurantTypeManager" %>
<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>

<% 
Printer printer = (Printer)session.getAttribute("printer");
%>
<jsp:include page="../header/language-header.jsp" />

<%
  User sessionUser = (User)session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("../login.jsp");
  else {
  	if (sessionUser.getUserType() != User.Type.ADMINISTRATOR) {
  		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  	}
  }
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
<script src="../js/confirmAction.js"></script>

<%@  include file="../header/page-title.html" %>
</head>

<body>
<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<h1><%= printer.print("Retail Store Type List") %></h1>

<%
	List<RestaurantType> restaurantTypeList = RestaurantTypeManager.getAllRestaurantTypes();
%>

<div>
	<h3></h3>
</div>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">
  
  <tr>
    <td width="50%"><a href="/admin/addRestaurantType.jsp">+ <%= printer.print("Add New Retail Store Type") %></a></td>
    <td width="40%"></td>
    <td width="10%"></td>
  </tr>
  
  <tr>
  	<td width="50%"></td>
    <td width="40%"></td>
    <td width="10%"></td>
  </tr>
  
  <tr>
  	<td width="50%"><%= printer.print("Retail Store Type") %></td>
    <td width="40%"><%= printer.print("Service Type") %></td>
    <td width="10%"><%= printer.print("Actions") %></td>
  </tr>
  
  <tr>
  	<td width="50%"></td>
    <td width="40%"></td>
    <td width="10%"></td>
  </tr>
  
<% 
  for (RestaurantType restaurantType : restaurantTypeList) {
%>

  <tr>
    <td width="50%"><a href="editRestaurantType.jsp?k=<%= restaurantType.getKey() + "&readonly=true" %>"><%= restaurantType.getRestaurantTypeName() %></a></td>
    <td width="40%"><%= printer.print(restaurantType.getStoreSuperTypeString()) %></td>
    <td width="10%">
    	<a href="editRestaurantType.jsp?k=<%= restaurantType.getKey() %>"><%= printer.print("Edit") %></a>
    	<br/>
    	<a href="javascript:void(0);" onclick="confirmDelete('/manageObject?action=delete&type=T&k=<%= restaurantType.getKey() %>', '<%= printer.print("Are you sure you want to delete this Retail Store Type")%>');"><%= printer.print("Delete") %></a>
    </td>
  </tr>
<%  
  }
%>
</table>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
