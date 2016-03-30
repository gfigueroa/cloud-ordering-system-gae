<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.Customer" %>
<%@ page import="datastore.CustomerManager" %>
<%@ page import="datastore.SmartCashTransaction" %>
<%@ page import="datastore.SmartCashTransactionManager" %>
<%@ page import="util.DateManager" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>

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

  Long transactionKey= new Long(request.getParameter("k"));
  SmartCashTransaction transaction = SmartCashTransactionManager.getSmartCashTransaction(transactionKey);
  Customer customer = CustomerManager.getCustomer(transaction.getCustomer());
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />
<%@  include file="../header/page-title.html" %>
</head>

<body>

<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<form method="post" id="form1" name="form1" 
    action="/manageUser?action=update&u_type=S&c_key=<%= KeyFactory.keyToString(customer.getKey()) %>" 
    class="form-style">

    <fieldset>
	<legend><%= printer.print("Smart Cash Transaction") %></legend>
	
	<div>
		<h2><%= printer.print("View Transaction") %></h2>
	</div>

	<div>
		<label for="t_date"><span><%= printer.print("Transaction Date") %></span></label>
		<input type="text" name="t_date" class="input_large" value="<%= DateManager.printDateAsString(transaction.getDate())%>" readOnly="true" /><br />
		<div id="t_date"></div>
	</div>
	
	<div>
    	<label  for="c_name"><span><%= printer.print("Customer Name") %></span></label>
		<input type="text" name="c_name" class="input_extra_large"  value="<%= customer.getCustomerName() %>" title="" readOnly="true"  /><br />
		<div id="c_name"></div>
	</div>
    
    <div>
        <label for="u_email"><span><%= printer.print("E-mail") %> </span></label>
        <input type="text" name="u_email" class="input_extra_large" style="display:none;"
        	value="<%= customer.getUser().getUserEmail().getEmail() %>" />
		<input type="text" name="u_email_visible" class="input_extra_large" readonly="readonly"
			value="<%= customer.getUser().getUserEmail().getEmail() %>" /><br />
		<div id="u_email"></div>
	</div>
		
	<div>
		<label for="c_type"><span><%= printer.print("Transaction Type") %></span></label>
		<input type="text" name="c_type" class="input_large" value="<%= transaction.getTransactionTypeString()%>" readOnly="true" /><br />
		<div id="c_type"></div>
	</div>
	
	<%
	if (transaction.getTransactionType() == SmartCashTransaction.SmartCashTransactionType.DEBIT) {
	%>
		<div>
	    	<label  for="c_oid"><span><%= printer.print("Order Id") %></span></label>
			<input type="text" name="c_oid" class="input_extra_large"  value="<%= transaction.getOrderId() %>" title="" readOnly="true" /><br />
			<div id="c_oid"></div>
		</div>
	<%
	}
	%>
	
	<div>
		<label for="c_amount"><span><%= printer.print("Transaction Amount") %></span></label>
		<input type="text" name="c_amount" class="input_large" value="<%= transaction.getAmount()%>" readonly="readonly"/><br />
		<div id="c_amount"></div>
	</div>
	
	</fieldset>

	<br class="clearfloat" />
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/admin/editSmartCash.jsp?k=<%= KeyFactory.keyToString(customer.getKey()) %>'" class="button-close"/>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
