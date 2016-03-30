<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.RestaurantType" %>
<%@ page import="datastore.RestaurantTypeManager" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="util.DateManager" %>
<%@ page import="com.google.appengine.api.blobstore.BlobKey" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>

<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>

<%
  User sessionUser = (User)session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("../login.jsp");
  else {
  	if (sessionUser.getUserType() != User.Type.RESTAURANT) {
  		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  	}
  }
    
  String message = request.getParameter("msg");
  String action = request.getParameter("action");
  String updateType = request.getParameter("update_type");
  String error = request.getParameter("etype");
  
  BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
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

<%
	Restaurant restaurant = RestaurantManager.getRestaurant(sessionUser);
	RestaurantType restaurantType = RestaurantTypeManager.getRestaurantType(restaurant.getRestaurantType());
	
	List<RestaurantType> restaurantTypeList = RestaurantTypeManager.getAllRestaurantTypes();
%>

<form method="post" id="form1" name="form1" 
		action="<%= blobstoreService.createUploadUrl("/manageUser?action=update&u_type=R&update_type=I&k=" + KeyFactory.keyToString(restaurant.getKey())) %>"
		class="form-style" enctype="multipart/form-data">

    <fieldset>
	<legend><%= printer.print("Retail store") %></legend>
	
	<div>
		<h2><%= printer.print("Edit Retail Store") %></h2>
	</div>

	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
	
	<div>
    	<label  for="ss_type"><span><%= printer.print("Service Type") %></span></label>
		<input type="text" name="ss_type" class="input_extra_large" value="<%= printer.print(restaurantType != null ? restaurantType.getStoreSuperTypeString() : "Not assigned yet") %>" title="" disabled="true" /><br />
		<div id="ss_type"></div>
	</div>
	
    <div>
    	<label  for="r_type"><span><%= printer.print("Retail Store Type") %></span></label>
		<input type="text" name="r_type" class="input_extra_large" value="<%= restaurantType != null ? restaurantType.getRestaurantTypeName() : printer.print("Not assigned yet") %>" title="" disabled="true" /><br />
		<div id="r_type"></div>
	</div>
	
	<div>
		<label for="services"><span><%= printer.print("Services") %></span></label>
		<input type="checkbox" name="has_ns" checked="true" value="true" disabled="true" /> <%= printer.print("News and Opinion Polls") %><br/>
		<label for="services"><span></span></label>
		<input type="checkbox" name="has_ps" <%= restaurant.hasProductsService() ? "checked=\"true\"" : "" %> value="true" disabled="true" /> <%= printer.print("Product Items") %><br/>
		<label for="services"><span></span></label>
		<input type="checkbox" name="has_sps" <%= restaurant.hasServiceProvidersService() ? "checked=\"true\"" : "" %> value="true" disabled="true" /> <%= printer.print("Service Providers") %><br/>
		<label for="services"><span></span></label>
		<input type="checkbox" name="has_ms" <%= restaurant.hasMessagesService() ? "checked=\"true\"" : "" %> value="true" disabled="true" /> <%= printer.print("Messages") %>
		<div id="services"></div>
	</div>
    
    <div>
    	<label  for="r_name"><span><%= printer.print("Retail Store Name") %> <span class="required_field">*</span></span></label>
		<input type="text" name="r_name" class="input_extra_large"  value="<%= restaurant.getRestaurantName() %>" title="" /><br />
		<div id="r_name"></div>
	</div>
	
	<%
	if (restaurantType.getStoreSuperType() == RestaurantType.StoreSuperType.VIRTUAL_CHANNEL) {
	%>
		<div>
	       	<label  for="c_number"><span><%= printer.print("Channel Number") %> <span class="required_field">*</span></span></label>
			<input type="text" name="c_number" class="input_extra_large" value="<%= restaurant.getChannelNumber() != null ? restaurant.getChannelNumber() : "" %>"  title="" /><br />
			<div id="c_number"></div>
		</div>
	<%
	}
	%>
	
	<div>
       	<label for="r_description"><span><%= printer.print("Description") %> <span class="required_field">*</span></span></label>
		<textarea name="r_description" class="input_extra_large" value=""><%= restaurant.getRestaurantDescription() %></textarea><br />
		<div id="r_description"></div>
	</div>
	
	<div>
        <label for="u_email"><span><%= printer.print("E-mail") %> </span></label>
        <input type="text" name="u_email" class="input_extra_large" style="display:none;"
        	value="<%= restaurant.getUser().getUserEmail().getEmail() %>" />
		<input type="text" name="u_email_visible" class="input_extra_large" disabled="true"
			value="<%= restaurant.getUser().getUserEmail().getEmail() %>" /><br />
		<div id="u_email"></div>
	</div>
	
	<div>
	    <label for="u_password"><span></span></label>
	    <a href="editRestaurantPassword.jsp"><%= printer.print("Change password") %></a>
	</div>
	
    <div>
       	<label for="r_website"><span><%= printer.print("Website") %> </span></label>
		<input type="text" name="r_website" class="input_extra_large" value="<%= restaurant.getRestaurantWebsite().getValue() %>" /><br />
		<div id="r_website"></div>
	</div>
	
    <div>
       	<label for="r_logo"><span><%= printer.print("Logo") %> </span></label>
       	<%
		if (restaurant.getRestaurantLogo() != null) {
		%>
			<a target="_new" href="/img?blobkey=<%= restaurant.getRestaurantLogo().getKeyString() %>">
			<img src="/img?blobkey=<%= restaurant.getRestaurantLogo().getKeyString() %>&s=300">
			</a>
			<br />
		<%
		}
		%>
		<label><span> </span></label><input type="file" name="r_logo" class="input_extra_large" value="" /><br />
		<div id="r_logo"></div>
	</div>
	
	<div>
		<label for="r_ot"><span><%= printer.print("Opening Time") %></span></label>
		<select name="r_oth">
        <option value="0">-hh-</option>
        <% for (int i = 0; i < 24; i++) { %>
        	<option value="<%= i %>" <%= i == DateManager.getHours(restaurant.getRestaurantOpeningTime()) ? "selected=\"true\"" : "" %>><%= i < 10 ? "0" + i : i %></option>
        <% } %>
        </select>
        <select name="r_otm">
        <option value="0">-mm-</option>
        <% for (int i = 0; i < 60; i++) { %>
        	<option value="<%= i %>" <%= i == DateManager.getMinutes(restaurant.getRestaurantOpeningTime()) ? "selected=\"true\"" : "" %>><%= i < 10 ? "0" + i : i %></option>
        <% } %>
        </select>
		<div id="r_ot"></div>
	</div>
	
	<div>
		<label for="r_ct"><span><%= printer.print("Closing Time") %></span></label>
		<select name="r_cth">
        <option value="0">-hh-</option>
        <% for (int i = 0; i < 24; i++) { %>
        	<option value="<%= i %>" <%= i == DateManager.getHours(restaurant.getRestaurantClosingTime()) ? "selected=\"true\"" : "" %>><%= i < 10 ? "0" + i : i %></option>
        <% } %>
        </select>
        <select name="r_ctm">
        <option value="0">-mm-</option>
        <% for (int i = 0; i < 60; i++) { %>
        	<option value="<%= i %>" <%= i == DateManager.getMinutes(restaurant.getRestaurantClosingTime()) ? "selected=\"true\"" : "" %>><%= i < 10 ? "0" + i : i %></option>
        <% } %>
        </select>
		<div id="r_ct"></div>
	</div>
    
    <div>
       	<label for="r_comments"><span><%= printer.print("Comments") %></span></label>
		<textarea name="r_comments" class="input_extra_large" value=""><%= restaurant.getRestaurantComments() %></textarea><br />
		<div id="r_comments"></div>
	</div>
	
	<div>
	<% 
	if (message != null && message.equals("success")) {
		if (action != null && action.equals("update")) {
			if (updateType != null && updateType.equals("P")) {
	%>
				<div class="success-div"><%= printer.print("Retail Store password changed successfully") %>.</div>
	<% 
			}
			else if (updateType != null && updateType.equals("I")) {
	%>
				<div class="success-div"><%= printer.print("Retail Store successfully updated") %></div>
	<%
			}
		}
	} 
	%>
	</div>
	
	</fieldset>
	<br class="clearfloat" />
	<br class="clearfloat" />
	<br class="clearfloat" />
	<br class="clearfloat" />
	
	<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
