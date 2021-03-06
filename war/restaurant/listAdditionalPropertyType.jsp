<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.AdditionalPropertyType" %>
<%@ page import="datastore.AdditionalPropertyTypeManager" %>
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

<h1><%= printer.print("Additional Properties") %></h1>
<%
	List<AdditionalPropertyType> typeList = AdditionalPropertyTypeManager.getRestaurantAdditionalPropertyTypes(sessionUser.getKey().getParent());
%>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">

<tr>
    <td width="70%"><a href="/restaurant/addAdditionalPropertyType.jsp">+ <%= printer.print("Add New Additional Property Type") %></a></td>
    <td width="5%"></td>
    <td width="15%"></td>
    <td width="10%"></td>
  </tr>
  
  <tr>
  	<td width="70%"></td>
    <td width="5%"></td>
    <td width="15%"></td>
    <td width="10%"></td>
  </tr>

<% 
  for (AdditionalPropertyType type : typeList) {
  	String typeKeyString = KeyFactory.keyToString(type.getKey());
%>
  <tr>
    <td width="70%"><a id="<%= typeKeyString %>" href="/restaurant/editAdditionalPropertyType.jsp?k=<%= typeKeyString + "&readonly=true" %>"><%= type.getAdditionalPropertyTypeName() %></a></td>
    <td width="5%"><img src="../images/edit1.jpg" width="23" height="23" /><a href="/restaurant/editAdditionalPropertyType.jsp?k=<%= typeKeyString %>"><%= printer.print("Edit") %></a></td>
    <td width="15%"><img src="../images/edit1.jpg" width="23" height="23" /><a href="/restaurant/listAdditionalPropertyValues.jsp?apt_k=<%= typeKeyString %>"><%= printer.print("Property Values") %></a></td>
    <td width="10%"><img src="../images/delete.jpg" width="23" height="23" /><a href="javascript:void(0);" onclick="confirmDelete('/manageRestaurant?action=delete&type=P&k=<%= typeKeyString %>', '<%= printer.print("Are you sure you want to delete this Additional Property Type")%>');"><%= printer.print("Delete") %></a></td>
  </tr>
<%  
  }
%>
</table>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
