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
    
  String message = request.getParameter("msg");
  String action = request.getParameter("action");
  String updateType = request.getParameter("update_type");
  String error = request.getParameter("etype");
  boolean readOnly = request.getParameter("readonly") != null ? true : false;
  
  BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
  
  Restaurant restaurant = RestaurantManager.getRestaurant(KeyFactory.stringToKey(request.getParameter("k")));
  RestaurantType restaurantType = RestaurantTypeManager.getRestaurantType(restaurant.getRestaurantType());
  
  String storeSuperTypeString = request.getParameter("ss_type");
  RestaurantType.StoreSuperType storeSuperType = storeSuperTypeString != null ? 
  		RestaurantType.getStoreSuperTypeFromString(storeSuperTypeString) : restaurantType != null ? 
  		restaurantType.getStoreSuperType() : RestaurantType.StoreSuperType.FOOD_DRINK;
  
  List<RestaurantType> restaurantTypeList = RestaurantTypeManager.getAllRestaurantTypes(storeSuperType);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />
<%@  include file="../header/page-title.html" %>

<script language="Javascript" type="text/javascript">

function refresh(ss_type, k) {
    window.location.replace("editRestaurant.jsp?k=" + k + "&ss_type=" + ss_type);
}

</script>

</head>

<body>

<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<form method="post" id="form1" name="form1" 
		action="<%= blobstoreService.createUploadUrl("/manageUser?action=update&u_type=R&update_type=I&k=" + request.getParameter("k")) %>"
		class="form-style" enctype="multipart/form-data">

    <fieldset>
	<legend><%= printer.print("Retail store") %></legend>
	
	<div>
		<h2><%= readOnly ? printer.print("View Retail Store") : printer.print("Edit Retail Store") %></h2>
	</div>

	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
	
	<div>
		<label for="ss_type"><span><%= printer.print("Service Type") %> <span class="required_field">*</span></span></label>
		<select name="ss_type" <%= readOnly ? "disabled=\"true\"" : "" %> onChange="refresh(this.value, '<%= request.getParameter("k") %>');">
	        <option value="food_drink" <%= storeSuperType == RestaurantType.StoreSuperType.FOOD_DRINK ? "selected=\"true\"" : "" %>><%= printer.print("Food and Drink") %></option>
	        <option value="shopping" <%= storeSuperType == RestaurantType.StoreSuperType.SHOPPING ? "selected=\"true\"" : "" %>><%= printer.print("Shopping") %></option>
	        <option value="polls" <%= storeSuperType == RestaurantType.StoreSuperType.POLLS ? "selected=\"true\"" : "" %>><%= printer.print("News and Opinion Polls") %></option>
	        <option value="salon" <%= storeSuperType == RestaurantType.StoreSuperType.SALON ? "selected=\"true\"" : "" %>><%= printer.print("Salon") %></option>
	        <option value="god_dwelling_place" <%= storeSuperType == RestaurantType.StoreSuperType.GOD_DWELLING_PLACE ? "selected=\"true\"" : "" %>><%= printer.print("God Dwelling Place") %></option>
	        <option value="virtual_channel" <%= storeSuperType == RestaurantType.StoreSuperType.VIRTUAL_CHANNEL ? "selected=\"true\"" : "" %>><%= printer.print("Virtual Channel") %></option>
        </select>
		<div id="ss_type"></div>
	</div>
	
	<div>
		<label for="r_type"><span><%= printer.print("Retail Store Type") %> <span class="required_field">*</span></span></label>
        <select name="r_type" <%= readOnly ? "disabled=\"true\"" : "" %>>
        <% for (RestaurantType type : restaurantTypeList) { %>
        	<option value="<%= type.getKey() %>" <%= restaurant.getRestaurantType().longValue() == type.getKey().longValue() ? "selected=\"true\"" : "" %>>
        		<%= type.getRestaurantTypeName() %>
        	</option>
        <% } %>
        </select>
		<div id="r_type"></div>
	</div>
	
	<div>
		<label for="services"><span><%= printer.print("Services") %></span></label>
		<input type="checkbox" name="has_ns" checked="true" value="true" disabled="true" /> <%= printer.print("News and Opinion Polls") %><br/>
		<label for="services"><span></span></label>
		<input type="checkbox" name="has_ps" <%= restaurant.hasProductsService() ? "checked=\"true\"" : "" %> value="true" <%= readOnly ? "disabled=\"true\"" : "" %> /> <%= printer.print("Product Items") %><br/>
		<label for="services"><span></span></label>
		<input type="checkbox" name="has_sps" <%= restaurant.hasServiceProvidersService() ? "checked=\"true\"" : "" %> value="true" <%= readOnly ? "disabled=\"true\"" : "" %> /> <%= printer.print("Service Providers") %><br/>
		<label for="services"><span></span></label>
		<input type="checkbox" name="has_ms" <%= restaurant.hasMessagesService() ? "checked=\"true\"" : "" %> value="true" <%= readOnly ? "disabled=\"true\"" : "" %> /> <%= printer.print("Messages") %>
		<div id="services"></div>
	</div>
    
    <div>
    	<label  for="r_name"><span><%= printer.print("Retail Store Name") %> <span class="required_field">*</span></span></label>
		<input type="text" name="r_name" class="input_extra_large" value="<%= restaurant.getRestaurantName() %>" title="" <%= readOnly ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="s=r_name"></div>
	</div>
	
	<%
	if (storeSuperType == RestaurantType.StoreSuperType.VIRTUAL_CHANNEL) {
	%>
		<div>
	       	<label  for="c_number"><span><%= printer.print("Channel Number") %> <span class="required_field">*</span></span></label>
			<input type="text" name="c_number" class="input_extra_large" value="<%= restaurant.getChannelNumber() != null ? restaurant.getChannelNumber() : "" %>"  title="" <%= readOnly ? "readonly=\"readonly\"" : "" %> /><br />
			<div id="c_number"></div>
		</div>
	<%
	}
	%>
	
	<div>
       	<label for="r_description"><span><%= printer.print("Description") %> <span class="required_field">*</span></span></label>
		<textarea name="r_description" class="input_extra_large" value="" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= restaurant.getRestaurantDescription() %></textarea><br />
		<div id="r_description"></div>
	</div>
	
	<div>
        <label for="u_email"><span><%= printer.print("E-mail") %> </span></label>
        <input type="text" name="u_email" class="input_extra_large" style="display:none;"
        	value="<%= restaurant.getUser().getUserEmail().getEmail() %>" />
		<input type="text" name="u_email_visible" class="input_extra_large" readonly="readonly"
			value="<%= restaurant.getUser().getUserEmail().getEmail() %>" /><br />
		<div id="u_email"></div>
	</div>	
	
	<%
	if (!readOnly) {
	%>
		<div>
	        <label for="u_password"><span></span></label>
	        <a href="../restaurant/editRestaurantPassword.jsp?r_key=<%= request.getParameter("k") %>"><%= printer.print("Change password") %></a>
		</div>
	<%
	}
	%>
	
    <div>
       	<label for="r_website"><span><%= printer.print("Website") %> </span></label>
		<input type="text" name="r_website" class="input_extra_large" value="<%= restaurant.getRestaurantWebsite().getValue() %>" <%= readOnly ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="r_website"></div>
	</div>
	
	<%
    if (!(restaurant.getRestaurantLogo() == null && readOnly)) {
    %>
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
			<%
			if (!readOnly) {
			%>	
				<label><span> </span></label><input type="file" name="r_logo" class="input_extra_large" value="" /><br />
			<%
			}
			%>
			<div id="r_logo"></div>
		</div>
	<%
	}
	%>
	
	<div>
		<label for="r_ot"><span><%= printer.print("Opening Time") %></span></label>
		<select name="r_oth" <%= readOnly ? "disabled=\"true\"" : "" %>>
        <option value="0">-hh-</option>
        <% for (int i = 0; i < 24; i++) { %>
        	<option value="<%= i %>" <%= i == DateManager.getHours(restaurant.getRestaurantOpeningTime()) ? "selected=\"true\"" : "" %>><%= i < 10 ? "0" + i : i %></option>
        <% } %>
        </select>
        <select name="r_otm" <%= readOnly ? "disabled=\"true\"" : "" %>>
        <option value="0">-mm-</option>
        <% for (int i = 0; i < 60; i++) { %>
        	<option value="<%= i %>" <%= i == DateManager.getMinutes(restaurant.getRestaurantOpeningTime()) ? "selected=\"true\"" : "" %>><%= i < 10 ? "0" + i : i %></option>
        <% } %>
        </select>
		<div id="r_ot"></div>
	</div>
	
	<div>
		<label for="r_ct"><span><%= printer.print("Closing Time") %></span></label>
		<select name="r_cth" <%= readOnly ? "disabled=\"true\"" : "" %>>
        <option value="0">-hh-</option>
        <% for (int i = 0; i < 24; i++) { %>
        	<option value="<%= i %>" <%= i == DateManager.getHours(restaurant.getRestaurantClosingTime()) ? "selected=\"true\"" : "" %>><%= i < 10 ? "0" + i : i %></option>
        <% } %>
        </select>
        <select name="r_ctm" <%= readOnly ? "disabled=\"true\"" : "" %>>
        <option value="0">-mm-</option>
        <% for (int i = 0; i < 60; i++) { %>
        	<option value="<%= i %>" <%= i == DateManager.getMinutes(restaurant.getRestaurantClosingTime()) ? "selected=\"true\"" : "" %>><%= i < 10 ? "0" + i : i %></option>
        <% } %>
        </select>
		<div id="r_ct"></div>
	</div>
    
    <div>
       	<label for="r_comments"><span><%= printer.print("Comments") %></span></label>
		<textarea name="r_comments" class="input_extra_large" value="" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= restaurant.getRestaurantComments() %></textarea><br />
		<div id="r_comments"></div>
	</div>
	
	<hr/>

	<div>
		<h2>
			<a href="../restaurant/listBranch.jsp?r_key=<%= request.getParameter("k") %>"><%= printer.print("Branches") %></a> | 
			<a href="../restaurant/listNews.jsp?r_key=<%= request.getParameter("k") %>"><%= printer.print("News") %></a> | 
			<a href="../restaurant/listOpinionPoll.jsp?r_key=<%= request.getParameter("k") %>"><%= printer.print("Opinion Polls") %></a>|
			<a href="../restaurant/listSurvey.jsp?r_key=<%= request.getParameter("k") %>"><%= printer.print("Surveys") %></a>
		<h2>
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

	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/admin/listRestaurant.jsp'" class="button-close"/>
	
	<%
	if (!readOnly) {
	%>
		<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>
	<%
	}
	else {
	%>
		<input type="button" value="&nbsp;&nbsp;&nbsp;<%= printer.print("Edit") %>&nbsp;&nbsp;&nbsp;" onClick="location.href='/admin/editRestaurant.jsp?k=<%= request.getParameter("k") %>'" class="button_style">
	<%
	}
	%>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
