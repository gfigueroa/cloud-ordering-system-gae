<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="datastore.RestaurantTypeManager" %>
<%@ page import="util.DateManager" %>
<%@ page import="com.google.appengine.api.blobstore.BlobKey" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>

<%
  User sessionUser = (User)session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("../login.jsp");
  
  Restaurant restaurant = RestaurantManager.getRestaurant(KeyFactory.stringToKey(request.getParameter("k")));
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

<form method="post" id="form1" name="form1" 
		class="form-style">

    <fieldset>
		<legend>Restaurant</legend>
	<div>
		<h2>View restaurant</h2>
	</div>
    
    <div>
		<label for="r_type"><span>Restaurant Type</span></label>
		<input type="text" name="r_type" class="input_extra_large"  value="<%= RestaurantTypeManager.getRestaurantType(restaurant.getRestaurantType()).getRestaurantTypeName() %>" title="" disabled="true" /><br />
		<div id="r_type"></div>
	</div>
    
    <div>
    	<label  for="r_name"><span>Name</span></label>
		<input type="text" name="r_name" class="input_extra_large"  value="<%= restaurant.getRestaurantName() %>" title="" disabled="true" /><br />
		<div id="r_name"></div>
	</div>
	
	<div>
       	<label for="r_description"><span>Description</span></label>
		<textarea name="r_description" class="input_extra_large" value="" disabled="true"><%= restaurant.getRestaurantDescription() %></textarea><br />
		<div id="r_description"></div>
	</div>
	
	<div>
        <label for="u_email"><span>E-mail </span></label>
        <input type="text" name="u_email" class="input_extra_large" style="display:none;"
        	value="<%= restaurant.getUser().getUserEmail().getEmail() %>" />
		<input type="text" name="u_email_visible" class="input_extra_large" disabled="true"
			value="<%= restaurant.getUser().getUserEmail().getEmail() %>" /><br />
		<div id="u_email"></div>
	</div>	
	
    <div>
       	<label for="r_website"><span>Website</span></label>
		<input type="text" name="r_website" class="input_extra_large" value="<%= restaurant.getRestaurantWebsite().getValue() %>" 
			disabled="true"/><br />
		<div id="r_website"></div>
	</div>
	
	<%
	if (restaurant.getRestaurantLogo() != null) {
	%>
	    <div>
	       	<label for="r_logo"><span>Logo</span></label>
				<a target="_new" href="/img?blobkey=<%= restaurant.getRestaurantLogo().getKeyString() %>">
					<img src="/img?blobkey=<%= restaurant.getRestaurantLogo().getKeyString() %>&s=300">
				</a>
				<br />
			
			<div id="r_logo"></div>
		</div>
	<%
	}
	%>
	
	<div>
       	<label for="r_ot"><span>Opening Time</span></label>
		<input type="text" name="r_ot" class="input_extra_large" value="<%= DateManager.printDateAsTime(restaurant.getRestaurantOpeningTime()) %>" 
			disabled="true"/><br />
		<div id="r_ot"></div>
	</div>
	
	<div>
       	<label for="r_ct"><span>Closing Time</span></label>
		<input type="text" name="r_ct" class="input_extra_large" value="<%= DateManager.printDateAsTime(restaurant.getRestaurantClosingTime()) %>" 
			disabled="true"/><br />
		<div id="r_ct"></div>
	</div>
	
	</fieldset>

	<br class="clearfloat" />

</form>

</body>
</html>
