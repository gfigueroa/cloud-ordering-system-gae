<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.AdditionalPropertyValue" %>
<%@ page import="datastore.AdditionalPropertyValueManager" %>
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

  AdditionalPropertyType propertyType = AdditionalPropertyTypeManager.getAdditionalPropertyType(KeyFactory.stringToKey(request.getParameter("apt_k")));
  List<AdditionalPropertyValue> valueList = AdditionalPropertyValueManager.getAdditionalPropertyValues(propertyType.getKey());
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

<a href="listAdditionalPropertyType.jsp" class="back-link"><%= printer.print("Back to Types") %></a>

<h1><%= propertyType.getAdditionalPropertyTypeName() %> - <%= printer.print("Properties Values") %></h1>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">

<tr>
    <td width="80%"><a href="/restaurant/addAdditionalPropertyValues.jsp?apt_k=<%= KeyFactory.keyToString(propertyType.getKey()) %>">+ <%= printer.print("Add Property Value") %></a></td>
    <td width="10%"></td>
    <td width="10%"></td>
  </tr>
  
  <tr>
  	<td width="80%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
  </tr>

<% 
  for (AdditionalPropertyValue value : valueList) {
  	String valueKeyString = KeyFactory.keyToString(value.getKey());
%>
	  <tr>
	    <td width="80%"><a id="<%= valueKeyString %>" href="/restaurant/editAdditionalPropertyValues.jsp?k=<%= valueKeyString + "&readonly=true" %>"><%= value.getAdditionalPropertyValueName() %></a></td>
	    <td width="10%"><img src="../images/edit1.jpg" width="23" height="23" /><a href="/restaurant/editAdditionalPropertyValues.jsp?k=<%= valueKeyString %>"><%= printer.print("Edit") %></a></td>
	    <td width="10%"><img src="../images/delete.jpg" width="23" height="23" /><a href="javascript:void(0);" onclick="confirmDelete('/manageRestaurant?action=delete&type=V&k=<%= valueKeyString %>', '<%= printer.print("Are you sure you want to delete this Property Value")%>');"><%= printer.print("Delete") %></a></td>
	  </tr>
<%  
  }
%>
</table>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
