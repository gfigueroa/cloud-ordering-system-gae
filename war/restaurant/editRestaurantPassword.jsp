<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
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
  	if (sessionUser.getUserType() != User.Type.ADMINISTRATOR && sessionUser.getUserType() != User.Type.RESTAURANT) {
  		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  	}
  }
  
  Key restaurantKey = sessionUser.getUserType() == User.Type.RESTAURANT ?
  		sessionUser.getKey().getParent() : KeyFactory.stringToKey(request.getParameter("r_key"));
  		
  Restaurant restaurant = RestaurantManager.getRestaurant(restaurantKey);
  
  String error = request.getParameter("etype");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />

<script language="JavaScript" type="text/javascript" src="../js/crypto.js">
</script>

<script language="JavaScript" type="text/javascript">
function hashAndSubmit()
{
  var passwordInput = document.getElementsByName("u_password")[0];
  passwordInput.value = Sha256.hash(passwordInput.value);
  
  return true;
}

function checkPasswords()
{
  var passwordInput = document.getElementsByName("u_password")[0];
  var passwordConfirmInput = document.getElementsByName("u_password_confirm")[0];
  
  if (passwordInput.value == passwordConfirmInput.value) {
  	return hashAndSubmit();
  }
  else {
  	alert("<%=printer.print("The password you entered doesn't match the confirmation password")%>.");
    return false;
  }
}
</script>

<%@  include file="../header/page-title.html" %>
</head>

<body>
<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<form method="post" id="form1" name="form1" 
    action="/manageUser?action=update&u_type=R&update_type=P&k=<%= request.getParameter("r_key") %>" 
    onSubmit="return checkPasswords();"
    class="form-style">

    <fieldset>
	<legend><%= printer.print("Edit Retail Store Password") %></legend>

	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
    
    <div>
        <label for="u_email"><span><%= printer.print("E-mail") %> </span></label>
        <input type="text" name="u_email" class="input_extra_large" style="display:none;"
        	value="<%= restaurant.getUser().getUserEmail().getEmail() %>" />
		<input type="text" name="u_email_visible" class="input_extra_large" disabled="true"
			value="<%= restaurant.getUser().getUserEmail().getEmail() %>" /><br />
		<div id="u_email"></div>
	</div>

	<div>
       	<label for="u_password"><span><%= printer.print("New Password") %> <span class="required_field">*</span></span></label>
		<input type="password" name="u_password" class="input_extra_large" value="" /><br />
		<div id="u_password"></div>
	</div>
	
	<div>
       	<label for="u_password_confirm"><span><%= printer.print("Confirm Password") %> <span class="required_field">*</span></span></label>
		<input type="password" name="u_password_confirm" class="input_extra_large" value="" /><br />
		<div id="u_password_confirm"></div>
	</div>
	
	</fieldset>

	<br class="clearfloat" />
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='<%= sessionUser.getUserType() == User.Type.ADMINISTRATOR ? "/admin/editRestaurant.jsp?k=" + request.getParameter("r_key") : "/restaurant/editRestaurant.jsp?k=" + KeyFactory.keyToString(restaurantKey) %>'" class="button-close"/>
	
	<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>