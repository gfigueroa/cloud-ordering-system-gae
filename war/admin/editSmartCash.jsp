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
  
  Customer customer = CustomerManager.getCustomer(KeyFactory.stringToKey(request.getParameter("k")));
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

<a href="listSmartCash.jsp" class="back-link"><%= printer.print("Back to Smart Cash") %></a>

<h1><%= printer.print("Smart Cash") %><br>
<%= customer.getCustomerName() %> - <%= customer.getUser().getUserEmail().getEmail()  %> </h1>

<%
	List<Customer> customerList = CustomerManager.getAllCustomers();
	List<SmartCashTransaction> scTransactions = SmartCashTransactionManager.getAllSmartCashTransactionsFromCustomer(customer.getKey());
%>

<h3>&nbsp&nbsp&nbsp&nbsp <%= printer.print("Current balance") + ": " + SmartCashTransactionManager.getSmartCashBalanceFromCustomer(customer.getKey()) %> 	</h3>

</hr>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">

  <tr>
    <td width="30%"><a href="/admin/addSmartCash.jsp?k=<%= request.getParameter("k")%>">+ <%= printer.print("Deposit Smart Cash") %></a></td>
    <td width="30%"></td>
    <td width="20%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
  </tr>
  
  <tr>
  	<td width="30%"></td>
    <td width="30%"></td>
    <td width="20%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
  </tr>
  
  <tr>
  	<td width="30%"><%= printer.print("Date") %></td>
    <td width="30%"><%= printer.print("Type") %></td>
    <td width="20%"><%= printer.print("Amount") %></td>
    <td width="10%"><%= printer.print("Order Id") %></td>
    <td width="10%"></td>
  </tr>

<% 
  for (SmartCashTransaction trans : scTransactions) {
%>
	  <tr>
	    <td width="30%"><a href="/admin/editSmartCashDetails.jsp?k=<%= trans.getKey() %>"><%= DateManager.printDateAsString(trans.getDate()) %></a></td>
	    <td width="30%"><%= trans.getTransactionTypeString() %></td>
	    <td width="20%"><%= trans.getAmount() %></td>
	    <td width="10%"><%= trans.getOrderId() != null ? trans.getOrderId() : "N/A" %></td>
	    <td width="10%"><img src="../images/edit1.jpg" width="23" height="23" /><a href="/admin/editSmartCashDetails.jsp?k=<%= trans.getKey() %>"><%= printer.print("View") %></a></td>
	  </tr>
<%  
  }
%>
</table>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>