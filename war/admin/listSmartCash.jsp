<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.Customer" %>
<%@ page import="datastore.CustomerManager" %>
<%@ page import="datastore.SmartCashTransaction" %>
<%@ page import="datastore.SmartCashTransactionManager" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="util.DateManager" %>

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

<%@  include file="../header/page-title.html" %>
</head>

<body>
<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<h1><%= printer.print("Smart Cash") %></h1>

<%
	List<Customer> customerList = CustomerManager.getAllCustomers();
%>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">

  <tr>
    <td width="30%"><%= printer.print("Customer List") %></a></td>
    <td width="20%"></td>
    <td width="20%"></td>
    <td width="20%"></td>
    <td width="10%"></td>
  </tr>
  
  <tr>
  	<td width="30%"></td>
    <td width="20%"></td>
    <td width="20%"></td>
    <td width="20%"></td>
    <td width="10%"></td>
  </tr>
  
  <tr>
  	<td width="30%"><%= printer.print("Name") %></td>
    <td width="20%"><%= printer.print("E-mail") %></td>
    <td width="20%"><%= printer.print("Balance") %></td>
    <td width="20%"><%= printer.print("Last Transaction Date") %></td>
    <td width="10%"></td>
  </tr>

<% 
  for (Customer customer : customerList) {
  	List<SmartCashTransaction> scTransaction = SmartCashTransactionManager.getAllSmartCashTransactionsFromCustomer(customer.getKey());
%>
  <tr>
    <td width="30%"><a href="/admin/editSmartCash.jsp?k=<%= KeyFactory.keyToString(customer.getKey()) %>"><%= customer.getCustomerName() %></a></td>
    <td width="20%"><%= customer.getUser().getUserEmail().getEmail() %></td>
    <td width="20%"><%= SmartCashTransactionManager.getSmartCashBalanceFromCustomer(customer.getKey())%></td>
    <td width="20%"><%= scTransaction.size()>0? DateManager.printDateAsString(scTransaction.get(0).getDate()): printer.print("none")%></td>
    <td width="10%"><img src="../images/edit1.jpg" width="23" height="23" /><a href="/admin/editSmartCash.jsp?k=<%= KeyFactory.keyToString(customer.getKey()) %>"><%= printer.print("Edit") %></a></td>
    
  </tr>
<%  
  }
%>
</table>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
