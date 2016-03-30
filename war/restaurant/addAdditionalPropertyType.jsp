<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
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
  
  String message = request.getParameter("msg");
  String action = request.getParameter("action");
  String error = request.getParameter("etype");
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
      action="/manageRestaurant?action=add&type=P"
      class="form-style">

    <fieldset>
    
    <legend><%= printer.print("Additional Property Type Information") %></legend>
    
	<% if (message != null && message.equals("success") && action != null && action.equals("add")) { %>
		<div class="success-div"><%= printer.print("Additional property type successfully added to the Retail Store") %>.</div>
	<% } %>
		
	<div>
		<h2><%= printer.print("Add Additional Property Type") %></h2>
	</div>
	
	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
    			
    <div>
        <label for="apt_name"><span><%= printer.print("Property Type Name") %> <span class="required_field">*</span></span></label>
		<input type="text" name="apt_name" class="input_extra_large" value="" /><br />
		<div id="apt_name"></div>
	</div>

     <div>
        <label for="apt_description"><span><%= printer.print("Description") %></span></label>
		<input type="text" name="apt_description" class="input_extra_large" value="" /><br />
		<div id="apt_description"></div>
	</div>
		
	</fieldset>
 
	<br class="clearfloat" />
	
	<div>
		<input type="checkbox" name="keep_adding" checked="true" value="true" /> <%= printer.print("Continue adding additional property types") %>
		<div id="keep_adding"></div>
	</div>
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/listAdditionalPropertyType.jsp?k=<%= request.getParameter("k") %>'" class="button-close"/>
				
	<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>

</form>

<jsp:include page="../header/page-footer.jsp" />
</body>
</html>
