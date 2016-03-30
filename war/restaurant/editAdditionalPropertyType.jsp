<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.AdditionalPropertyType" %>
<%@ page import="datastore.AdditionalPropertyTypeManager" %>
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
  
  boolean readOnly = request.getParameter("readonly") != null ? true : false;
  String error = request.getParameter("etype");
  String message = request.getParameter("msg");
  String action = request.getParameter("action");
  
  AdditionalPropertyType type = AdditionalPropertyTypeManager.getAdditionalPropertyType(KeyFactory.stringToKey(request.getParameter("k")));
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
<%@  include file="../header/page-title.html" %>
</head>

<body>

<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<form method="post" id="form1" name="form1" 
      action="/manageRestaurant?action=update&type=P"
      class="form-style">
    
    <input type="text" name="k" value="<%= KeyFactory.keyToString(type.getKey()) %>" style="display:none;" />

    <fieldset>
    
    <legend><%= printer.print("Additional Property Type Information") %></legend>
	
	<% if (message != null && message.equals("success") && action != null && action.equals("update")) { %>
		<div class="success-div"><%= printer.print("Additional Property Type successfully updated") %>.</div>
	<% } %>
	
	<div>
	  <h2><%= readOnly ? printer.print("View Additional Property Type") : printer.print("Edit Additional Property Type") %></h2>
	</div>
	
	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
    			
        <div>
        	<label for="apt_name"><span><%= printer.print("Property Type Name") %> <span class="required_field">*</span></span></label>
			<input type="text" name="apt_name" class="input_extra_large" value="<%= type.getAdditionalPropertyTypeName() %>" <%= readOnly? "readonly=\"readonly\"" : "" %> /><br />
			<div id="apt_name"></div>
		</div>

     	<div>
        	<label for="apt_description"><span><%= printer.print("Description") %> </span></label>
			<input type="text" name="apt_description" class="input_extra_large" value="<%= type.getAdditionalPropertyTypeDescription() %>" <%= readOnly? "readonly=\"readonly\"" : "" %> /><br />
			<div id="apt_description"></div>
		</div>
		
		<div>
			<br><br>
			<a href="/restaurant/listAdditionalPropertyValues.jsp?apt_k=<%= request.getParameter("k")%>"><%= printer.print("Property Values") %></a>
		</div>
		
	</fieldset>
	
	<br class="clearfloat" />
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/listAdditionalPropertyType.jsp'" class="button-close"/>
	<%
	if (!readOnly) {
	%>
		<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>
	<%
	}
	else {
	%>
		<input type="button" value="&nbsp;&nbsp;&nbsp;<%= printer.print("Edit") %>&nbsp;&nbsp;&nbsp;" onClick="location.href='/restaurant/editAdditionalPropertyType.jsp?k=<%= request.getParameter("k") %>'" class="button_style">
	<%
	}
	%>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
