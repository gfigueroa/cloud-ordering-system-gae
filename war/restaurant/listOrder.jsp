<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Set" %>
<%@ page import="util.DateManager" %>
<%@ page import="datastore.Branch" %>
<%@ page import="datastore.BranchManager" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.Customer" %>
<%@ page import="datastore.CustomerManager" %>
<%@ page import="datastore.OrderManager" %>
<%@ page import="datastore.Order" %>
<%@ page import="datastore.OrderManager" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
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
    
  Restaurant restaurant = RestaurantManager.getRestaurant(sessionUser);
  
  char type;
  if (request.getParameter("type") == null) {
  	type = 'C';
  }
  else {
  	type = request.getParameter("type").charAt(0);
  }
%>

<% 
Printer printer = (Printer)session.getAttribute("printer");
%>
<jsp:include page="../header/language-header.jsp" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />
<link type="text/css" href="../stylesheets/colorbox.css" rel="stylesheet" />
<script src="../js/jquery.min.js"></script>
<script src="../js/jquery.colorbox-min.js"></script>
<script src="../js/popup.js"></script>
<script src="../js/confirmAction.js"></script>
<script src="../js/treeview.js"></script>
<script src="../js/auto_refresh.js"></script>

<%@  include file="../header/page-title.html" %>
</head>

<body onload="JavaScript:timedRefresh(60000);">
<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/restaurant-menu.jsp" />
<h1>
<%
if (type == 'C') {
%>
	<%= printer.print("Current Orders") %>
<%
}
else {
%>
	<%= printer.print("Order History") %>
<%
}
%>
</h1>

<div>
	<a href="/restaurant/listOrder.jsp?type=C"><%= printer.print("Current Orders") %></a> | <a href="/restaurant/listOrder.jsp?type=H"><%= printer.print("Order History") %></a>
</div>

<%
	HashMap<Key, ArrayList<Order>> orderMap;
	if (type == 'C')
		orderMap = OrderManager.getAllOrdersGroupedByBranch(restaurant.getKey(), true);
	else
		orderMap = OrderManager.getAllOrdersGroupedByBranch(restaurant.getKey(), false);
    
    Set<Key> branchKeySet = orderMap.keySet();
%>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">

  <tr>
  	<td width="30%"><%= printer.print("Date") %></td>
  	<td width="10%"><%= printer.print("Order Number") %></td>
    <td width="25%"><%= printer.print("Customer Name") %></td>
    <td width="10%"><%= printer.print("Order Type") %></td>
    <td width="15%"><%= printer.print("Status") %></td>
    <td width="10%"><%= printer.print("Actions") %></td>
  </tr>
  
  <tr>
  	<td width="30%"></td>
  	<td width="10%"></td>
  	<td width="25%"></td>
  	<td width="10%"></td>
  	<td width="15%"></td>
    <td width="10%"></td>
  </tr>
</table>
<% 
for (Key branchKey : branchKeySet) {
	Branch branch = BranchManager.getBranch(branchKey);
 	ArrayList<Order> orderArrayList = orderMap.get(branchKey);
%>
 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">
  <tr>
  	<td width="30%"><a href="JavaScript:doMenu('<%=branch.getBranchName()%>');" id='x<%=branch.getBranchName()%>'>[+]</a><%= branch != null ? printer.print("Branch") + ": " + branch.getBranchName() + " (" + orderArrayList.size() + ")": printer.print("Inexistent Branch") %></td>
  	<td width="10%"></td>
  	<td width="25%"></td>
  	<td width="10%"></td>
    <td width="15%"></td>
    <td width="10%"></td>
  </tr>
  </table>
	<div  class="treeview" id='<%=branch.getBranchName()%>' >
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">	

		<% 
		  for (Order order : orderArrayList) {
		  	Customer customer = CustomerManager.getCustomer(order.getCustomer());
		%>
		  <tr>
		  	<td width="30%"><%= DateManager.printDateAsString(order.getOrderTime()) %></td>
		    <td width="10%"><%= order.getKey() %></td>
		    <td width="25%"><%= customer != null ? customer.getCustomerName() : "Inexistent customer" %></td>
		    <td width="10%"><%= printer.print(order.getOrderTypeString()) %></td>
		    <td width="15%"><%= printer.print(order.getOrderStatusString()) %></td>
		    <td width="10%">
		    <% 
		    if (order.isStillOpen()) {
		    %>
		    	<a id="<%= order.getKey() %>" href="/restaurant/editOrder.jsp?k=<%= order.getKey() + "&readonly=true" %>"><%= printer.print("View") %></a>
		    	<br/>
		    	<a href="/restaurant/editOrder.jsp?k=<%= order.getKey() %>"><%= printer.print("Process") %></a>
		    <% 
		    }
		    else {
		    %>
		    	<a id="<%= order.getKey() %>" href="/restaurant/editOrder.jsp?k=<%= order.getKey() + "&readonly=true" %>"><%= printer.print("View") %></a>
		    	<br/>
		    	<a href="javascript:void(0);" onclick="confirmDelete('/manageRestaurant?action=delete&type=O&k=<%= order.getKey() %>', '<%= printer.print("Are you sure you want to delete this Order")%>');"><%= printer.print("Delete") %></a>
		    <%
		    }
		    %>
		    </td>
		  </tr>
		<%  
		  }
		%>
		
	  <tr>
	  	<td width="30%"></td>
	  	<td width="10%"></td>
	  	<td width="25%"></td>
	  	<td width="10%"></td>
	    <td width="15%"></td>
	    <td width="10%"></td>
	  </tr>
    </table>
</div>
<%  
}
%>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>