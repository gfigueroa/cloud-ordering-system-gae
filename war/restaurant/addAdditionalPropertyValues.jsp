<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="datastore.AdditionalPropertyType" %>
<%@ page import="datastore.AdditionalPropertyTypeManager" %>

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
  
  AdditionalPropertyType type = AdditionalPropertyTypeManager.getAdditionalPropertyType(KeyFactory.stringToKey(request.getParameter("apt_k")));
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
      action="/manageRestaurant?action=add&type=V"
      class="form-style">

    <fieldset>
    
    <legend><%= type.getAdditionalPropertyTypeName() %> - <%= printer.print("Additional Property Values") %></legend>
    
    <% if (message != null && message.equals("success") && action != null && action.equals("add")) { %>
		<div class="success-div"><%= printer.print("Additional property value successfully added") %>.</div>
	<% } %>
	
	<div>
		<h2><%= printer.print("Add Additional Property Value") %></h2>
	</div>
	
	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
    
    <input type="text" name="apt_k" value="<%= request.getParameter("apt_k") %>" style="display:none;" />
    	
    <div>
        <label for="apt_name"><span><%= printer.print("Property Type Name") %></span></label>
		<input type="text" name="apt_name" class="input_extra_large" value="<%= type.getAdditionalPropertyTypeName() %>" readonly="true" /><br />
		<div id="apt_name"></div>
	</div>

    <div>
    <label for="apv_name"><span><%= printer.print("Property Value") %></span></label>
		<input type="text" name="apv_name" class="input_extra_large" value="" /><br />
		<div id="apv_name"></div>
	</div>
		
	</fieldset>
 
	<br class="clearfloat" />
	
	<div>
		<input type="checkbox" name="keep_adding" checked="true" value="true" /> <%= printer.print("Continue adding additional property values") %>
		<div id="keep_adding"></div>
	</div>
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/listAdditionalPropertyValues.jsp?apt_k=<%= request.getParameter("apt_k") %>'" class="button-close"/>
				
	<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>

</form>

<jsp:include page="../header/page-footer.jsp" />
</body>
</html>
