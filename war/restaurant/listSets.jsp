<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.Set" %>
<%@ page import="datastore.SetManager" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>

<%
  User sessionUser = (User)session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("/login.jsp");
  else {
  	if (sessionUser.getUserType() != User.Type.RESTAURANT) {
  		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  	}
  }
  
  List<Set> setList = SetManager.getAllSetsByStore(sessionUser.getKey().getParent());
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
<script src="../js/popup.js"></script>
<script src="../js/confirmAction.js"></script>

<%@  include file="../header/page-title.html" %>
</head>

<body>
<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<h1><%= printer.print("Product Item Sets") %></h1>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">

  <tr>
    <td width="40%"><a href="/restaurant/addSets.jsp?set_count=<%= setList.size() + 1 %>">+ <%= printer.print("Add Set") %></a></td>
    <td width="20%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
  </tr>
  
  <tr>
  	<td width="40%"></td>
    <td width="20%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
  </tr>
  
  <tr>
  	<td width="40%"><%= printer.print("Set Name")%></td>
    <td width="20%"><%= printer.print("Set No.")%></td>
    <td width="10%"><%= printer.print("Set Type")%></td>
    <td width="10%"><%= printer.print("No. Items")%></td>
    <td width="10%"></td>
    <td width="10%"></td>
  </tr>
  
  <tr>
  	<td width="40%"></td>
    <td width="20%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
  </tr>

  <% 
  for (Set set : setList) {
  	String setKeyString = KeyFactory.keyToString(set.getKey());
  %>
	  <tr>
	    <td width="40%"><a id="<%= setKeyString %>" href="/restaurant/editSets.jsp?k=<%= setKeyString + "&readonly=true" %>"><%= set.getSetName() %></a></td>
	    <td width="20%"><%= set.getSetNumber() != null ? set.getSetNumber() : "" %></td>
	    <td width="10%"><%= set.getSetTypeString() %></td>
	    <td width="10%"><%= set.getSetType() == Set.SetType.FIXED_SET ? set.getMenuItems().size() : set.getTypeSetMenuItems().size() %></td>
	    <!--  
	    <td width="10%"><img src="../images/edit1.jpg" width="23" height="23" /><a href="/restaurant/editSets.jsp?k=<%= setKeyString %>"><%= printer.print("Edit") %></a></td>
	    -->
	    <td width="10%"><img src="../images/edit1.jpg" width="23" height="23" /><a href="/menu/underConstruction.jsp"><%= printer.print("Edit") %></a></td>
	    <td width="10%"><img src="../images/delete.jpg" width="23" height="23" /><a href="javascript:void(0);" onclick="confirmDelete('/manageRestaurant?action=delete&type=E&k=<%= setKeyString %>', '<%= printer.print("Are you sure you want to delete this Product Item Set")%>');"><%= printer.print("Delete") %></a></td>
	  </tr>
  <%  
  }
  %>
</table>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
