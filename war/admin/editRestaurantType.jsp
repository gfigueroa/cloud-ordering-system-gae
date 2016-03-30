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
  
  String error = request.getParameter("etype");
  String message = request.getParameter("msg");
  String action = request.getParameter("action");
  boolean readOnly = request.getParameter("readonly") != null ? true : false;
  
  RestaurantType restaurantType = RestaurantTypeManager.getRestaurantType(Long.parseLong(request.getParameter("k")));
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
      action="/manageObject?action=update&type=T&k=<%= request.getParameter("k") %>" class="form-style">

    <fieldset>
	<legend><%= printer.print("Retail Store Type Information") %></legend>
	
	<% 
	if (message != null && message.equals("success") && action != null && action.equals("update")) { 
	%>
		<div class="success-div"><%= printer.print("Retail Store Type successfully updated") %>.</div>
	<% 
	} 
	%>
		
	<div>
	  <h2><%= readOnly ? printer.print("View Retail Store Type") : printer.print("Edit Retail Store Type") %></h2>
	</div>

	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
	
	<div>
		<label for="ss_type"><span><%= printer.print("Service Type") %> <span class="required_field">*</span></span></label>
		<select name="ss_type" <%= readOnly ? "disabled=\"true\"" : "" %>>
	        <option value="food_drink" <%= restaurantType.getStoreSuperType() == RestaurantType.StoreSuperType.FOOD_DRINK ? "selected=\"true\"" : "" %>><%= printer.print("Food and Drink") %></option>
	        <option value="shopping" <%= restaurantType.getStoreSuperType() == RestaurantType.StoreSuperType.SHOPPING ? "selected=\"true\"" : "" %>><%= printer.print("Shopping") %></option>
	        <option value="polls" <%= restaurantType.getStoreSuperType() == RestaurantType.StoreSuperType.POLLS ? "selected=\"true\"" : "" %>><%= printer.print("News and Opinion Polls") %></option>
	        <option value="salon" <%= restaurantType.getStoreSuperType() == RestaurantType.StoreSuperType.SALON ? "selected=\"true\"" : "" %>><%= printer.print("Salon") %></option>
        	<option value="god_dwelling_place" <%= restaurantType.getStoreSuperType() == RestaurantType.StoreSuperType.GOD_DWELLING_PLACE ? "selected=\"true\"" : "" %>><%= printer.print("God Dwelling Place") %></option>
        	<option value="virtual_channel" <%= restaurantType.getStoreSuperType() == RestaurantType.StoreSuperType.VIRTUAL_CHANNEL ? "selected=\"true\"" : "" %>><%= printer.print("Virtual Channel") %></option>
        </select>
		<div id="ss_type"></div>
	</div>
	    	
    <div>
        <label for="rt_name"><span><%= printer.print("Retail Store Type") %> <span class="required_field">*</span></span></label>
		<input type="text" name="rt_name" class="input_extra_large" value="<%= restaurantType.getRestaurantTypeName() %>" <%= readOnly ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="rt_name"></div>
	</div>
    
    <div>
       	<label for="rt_description"><span><%= printer.print("Description") %></span></label>
		<textarea  name="rt_description" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= restaurantType.getRestaurantTypeDescription() %></textarea><br />
		<div id="rt_description"></div>
	</div>
	
	<div>
       	<label for="rt_version"><span><%= printer.print("Version") %></span></label>
		<input type="text" name="rt_version" class="input_extra_large" value="<%= restaurantType.getStoreTypeVersion() %>" readonly="readonly"/><br />
		<div id="rt_version"></div>
	</div>
	
	</fieldset>
  
	<br class="clearfloat" />
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/admin/listRestaurantType.jsp'" class="button-close"/>
	
	<%
	if (!readOnly) {
	%>
		<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>
	<%
	}
	else {
	%>
		<input type="button" value="&nbsp;&nbsp;&nbsp;<%= printer.print("Edit") %>&nbsp;&nbsp;&nbsp;" onClick="location.href='/admin/editRestaurantType.jsp?k=<%= request.getParameter("k") %>'" class="button_style">
	<%
	}
	%>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
