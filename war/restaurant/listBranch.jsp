<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.Branch" %>
<%@ page import="datastore.BranchManager" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>

<%
  User sessionUser = (User)session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("../login.jsp");
  else {
  	if (sessionUser.getUserType() != User.Type.ADMINISTRATOR && sessionUser.getUserType() != User.Type.RESTAURANT) {
  		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  	}
  }
  
  Key restaurantKey = sessionUser.getUserType() == User.Type.RESTAURANT ?
  		sessionUser.getKey().getParent() : KeyFactory.stringToKey(request.getParameter("r_key"));

  Restaurant restaurant = RestaurantManager.getRestaurant(restaurantKey);
  List<Branch> branchList = BranchManager.getRestaurantBranches(restaurant.getKey());
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
<%--<script src="../js/popup.js"></script>--%>
<script src="../js/confirmAction.js"></script>

<%@  include file="../header/page-title.html" %>
</head>

<body>
<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<%if (sessionUser.getUserType()!= User.Type.RESTAURANT) {%>
	<a href="../admin/listRestaurant.jsp" class="back-link-RS"><%= printer.print("Back to Retail Store") %></a> 
<%}%>

<h1><%= restaurant.getRestaurantName() + " - " + printer.print("Branches") %></h1>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">

  <tr>
    <td width="80%"><a href="addBranch.jsp?r_key=<%= KeyFactory.keyToString(restaurantKey) %>">+ <%= printer.print("Add New Branch") %></a></td>
    <td width="10%"></td>
    <td width="10%"></td>
  </tr>
  
  <tr>
  	<td width="80%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
  </tr>

<% 
  for (Branch branch : branchList) {
%>
  <tr>
    <td width="80%"><a id="<%= KeyFactory.keyToString(branch.getKey()) %>" href="editBranch.jsp?k=<%= KeyFactory.keyToString(branch.getKey()) + "&readonly=true" %>"><%= branch.getBranchName() %></a></td>
    <td width="10%"><img src="../images/edit1.jpg" width="23" height="23" /><a href="editBranch.jsp?k=<%= KeyFactory.keyToString(branch.getKey()) %>"><%= printer.print("Edit") %></a></td>
    <td width="10%"><img src="../images/delete.jpg" width="23" height="23" /><a href="javascript:void(0);" onclick="confirmDelete('/manageRestaurant?action=delete&type=B&k=<%= KeyFactory.keyToString(branch.getKey()) %>', '<%= printer.print("Are you sure you want to delete this Branch")%>');"><%= printer.print("Delete") %></a></td>
  </tr>
<%
  }
%>
</table>


<jsp:include page="../header/page-footer.jsp" />


</body>
</html>
