<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.MenuItemType" %>
<%@ page import="datastore.MenuItemTypeManager" %>
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

<%
//if (!readOnly) {
%>
	<jsp:include page="../header/logout-bar.jsp" />
	<%@  include file="../header/page-banner.html" %>
	<jsp:include page="../menu/main-menu.jsp" />
<%
//}
%>

<%
	MenuItemType type = MenuItemTypeManager.getMenuItemType(KeyFactory.stringToKey(request.getParameter("k")));
%>

<form method="post" id="form1" name="form1" 
      action="/manageRestaurant?action=update&type=T"
      class="form-style">
    
    <input type="text" name="k" value="<%= KeyFactory.keyToString(type.getKey()) %>" style="display:none;" />

    <fieldset>
    
    <legend><%= printer.print("Product Type Information") %></legend>
    
   	<% if (message != null && message.equals("success") && action != null && action.equals("update")) { %>
		<div class="success-div"><%= printer.print("Product item type successfully updated") %>.</div>
	<% } %>
	
	<div>
	  <h2><%= readOnly ? printer.print("Product Item Type") : printer.print("Edit Product Type") %></h2>
	</div>
	
	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
    			
        <div>
        	<label for="mit_name"><span><%= printer.print("Product Type Name") %> <span class="required_field">*</span></span></label>
			<input type="text" name="mit_name" class="input_extra_large" value="<%= type.getMenuItemTypeName() %>" <%= readOnly? "readonly=\"readonly\"" : "" %> /><br />
			<div id="mit_name"></div>
		</div>

     	<div>
        	<label for="mit_description"><span><%= printer.print("Description") %></span></label>
			<input type="text" name="mit_description" class="input_extra_large" value="<%= type.getMenuItemTypeDescription() %>" <%= readOnly? "readonly=\"readonly\"" : "" %> /><br />
			<div id="mit_description"></div>
		</div>
		
		<div>
        	<label for="mit_version"><span><%= printer.print("Product Type Version") %></span></label>
			<input type="text" name="mit_version" class="input_extra_large" value="<%= type.getProductItemTypeVersion() %>" readonly="readonly" /><br />
			<div id="mit_version"></div>
		</div>
		
	</fieldset>

	<br class="clearfloat" />
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/listMenuItemType.jsp?k=<%= request.getParameter("k") %>'" class="button-close"/>
	
	<%
	if (!readOnly) {
	%>
		<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>
	<%
	}
	else {
	%>
		<input type="button" value="&nbsp;&nbsp;&nbsp;<%= printer.print("Edit") %>&nbsp;&nbsp;&nbsp;" onClick="location.href='/restaurant/editMenuItemType.jsp?k=<%= request.getParameter("k") %>'" class="button_style">
	<%
	}
	%>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
