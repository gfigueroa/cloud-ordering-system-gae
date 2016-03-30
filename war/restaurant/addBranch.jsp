<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.Region" %>
<%@ page import="datastore.RegionManager" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>

<%
  User sessionUser = (User)session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("../login.jsp");
  else {
  	if (sessionUser.getUserType() != User.Type.ADMINISTRATOR && sessionUser.getUserType() != User.Type.RESTAURANT) {
  		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  	}
  }
  
  String message = request.getParameter("msg");
  String action = request.getParameter("action");
  String error = request.getParameter("etype");
  
  List<Region> regionList = RegionManager.getAllRegions();
  Key restaurantKey = sessionUser.getUserType() == User.Type.RESTAURANT ?
  		sessionUser.getKey().getParent() : KeyFactory.stringToKey(request.getParameter("r_key"));

  Restaurant restaurant = RestaurantManager.getRestaurant(restaurantKey);
  
  String hasDeliveryString = request.getParameter("has_delivery");
  boolean hasDelivery = 
  		hasDeliveryString != null ? Boolean.parseBoolean(hasDeliveryString) :
  				true;
%>

<% 
Printer printer = (Printer)session.getAttribute("printer");
%>
<jsp:include page="../header/language-header.jsp" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />

<%@  include file="../header/page-title.html" %>

<script language="Javascript" type="text/javascript">

//function refresh(r_key, has_delivery) {
function refresh(has_delivery) {
    //window.location.replace("addBranch.jsp?r_key=" + r_key + "&has_delivery=" + has_delivery);
    delivery=document.getElementById('delivery');
    if(has_delivery=="true"){
    	delivery.style.display="block";
    }else{
    	delivery.style.display="none";
    }
}

</script>

</head>

<body>
<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<form method="post" id="form1" name="form1"
		action="/manageRestaurant?action=add"
		class="form-style">
		
	<input type="text" name="type" value="B" style="display:none;" />
	<input type="text" name="r_key" value="<%= request.getParameter("r_key") %>" style="display:none;" />

    <fieldset>
	<legend><%= printer.print("Branch Information") %></legend>
	
	<% if (message != null && message.equals("success") && action != null && action.equals("add")) { %>
		<div class="success-div"><%=printer.print("Branch successfully added to the Retail store")%>.</div>
	<% } %>
	
	<div>
	  <h2><%= printer.print("Add a branch") %></h2>
	</div>
	
	<% if (error != null && error.equals("Format")) { %>
		<div class="error-div"><%= printer.print("The email or phone you provided does not conform to the standard formats (you can try something like 0975384927 and user@domain.com)") %></div>
	<% } %>
	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
	<% if (error != null && error.equals("Selection")) { %>
		<div class="error-div"><%= printer.print("A branch must offer at least one of the three different services") %></div>
	<% } %>
	
	<div>
       	<label  for="r_name"><span><%= printer.print("Retail Store Name") %></span></label>
		<input type="text" name="r_name" class="input_extra_large" value="<%= restaurant.getRestaurantName() %>"  title="" readonly="readonly" /><br />
		<div id="r_name"></div>
	</div>
	
	<div>
		<label for="b_region"><span><%= printer.print("Region") %> <span class="required_field">*</span></span></label>
		<select name="b_region">
        <option value="">-<%= printer.print("Select Region") %>-</option>
        <% for (Region region : regionList) { %>
        	<option value="<%= region.getKey() %>"><%= region.getRegionName() %></option>
        <% } %>
        </select>
		<div id="b_region"></div>
	</div>
	
    <div>
       	<label  for="b_name"><span><%= printer.print("Branch Name") %> <span class="required_field">*</span></span></label>
		<input type="text" name="b_name" class="input_extra_large" value="" title="" /><br />
		<div id="b_name"></div>
	</div>
	
	<div>
       	<label  for="b_email"><span><%= printer.print("Branch Email") %></span></label>
		<input type="text" name="b_email" class="input_extra_large" value="" title="" /><br />
		<div id="b_email"></div>
	</div>
	
	<div>
       	<label for="b_address1"><span><%= printer.print("Address") %> <span class="required_field">*</span></span></label>
		<input type="text" name="b_address1" class="input_extra_large" value="" /><br />
		<div id="b_address1"></div>
	</div>
	
    <div>
       	<label for="b_address2"><span></span></label>
		<input type="text" name="b_address2" class="input_extra_large" value="" /><br />
		<div id="b_address2"></div>
	</div>
	
	<!--
	<div>
		<label for="b_location"><%= printer.print("Location") %> <a href="https://maps.google.com/" target="_blank"> (Google Maps)</a></label>
		<input type="text" name="b_location" class="input_extra_large" value="" readonly="true" /><br />
		<div id="b_location"></div>
	</div>
	-->
	
    <div>
       	<label for="b_phone"><span><%= printer.print("Phone number") %> <span class="required_field">*</span></span></label>
		<input type="text" name="b_phone" class="input_extra_large" value="" /><br />
		<div id="b_phone"></div>
	</div>
	
	<div>
       	<label for="has_delivery"><span><%= printer.print("Has Delivery Service") %> <span class="required_field">*</span></span></label>
		<input type="radio" name="has_delivery" <%= hasDelivery ? "checked=\"true\"" : "" %> value="true" onClick="refresh(this.value);" /> <%= printer.print("Yes") %> 		
		<input type="radio" name="has_delivery" <%= !hasDelivery ? "checked=\"true\"" : "" %> value="false" onClick="refresh(this.value);" /> <%= printer.print("No") %>
		<br />
		<div id="has_delivery"></div>
	</div>
	
	<div id="delivery">
	<%
	//if (hasDelivery) {
	%>
		<hr/>
		<div>
	       	<label for="has_regular_delivery"><span><%= printer.print("Has Regular Delivery") %> <span class="required_field">*</span></span></label>
			<input type="radio" name="has_regular_delivery" checked="true" value="true" /> <%= printer.print("Yes") %> 		
			<input type="radio" name="has_regular_delivery" value="false" /> <%= printer.print("No") %>
			<br />
			<div id="has_regular_delivery"></div>
		</div>
		
		<div>
	       	<label for="has_postal_delivery"><span><%= printer.print("Has Postal Office Delivery") %> <span class="required_field">*</span></span></label>
			<input type="radio" name="has_postal_delivery" checked="true" value="true" /> <%= printer.print("Yes") %> 		
			<input type="radio" name="has_postal_delivery" value="false" /> <%= printer.print("No") %>
			<br />
			<div id="has_postal_delivery"></div>
		</div>
		
		<div>
	       	<label for="has_ups_delivery"><span><%= printer.print("Has UPS Delivery") %> <span class="required_field">*</span></span></label>
			<input type="radio" name="has_ups_delivery" checked="true" value="true" /> <%= printer.print("Yes") %> 		
			<input type="radio" name="has_ups_delivery" value="false" /> <%= printer.print("No") %>
			<br />
			<div id="has_ups_delivery"></div>
		</div>
		
		<div>
	       	<label for="has_cs_delivery"><span><%= printer.print("Has Convenience Store Delivery") %> <span class="required_field">*</span></span></label>
			<input type="radio" name="has_cs_delivery" checked="true" value="true" /> <%= printer.print("Yes") %> 		
			<input type="radio" name="has_cs_delivery" value="false" /> <%= printer.print("No") %>
			<br />
			<div id="has_cs_delivery"></div>
		</div>
		<hr/>
	<%
	//}
	%>
	</div>
	
	<div>
       	<label for="has_takeout"><span><%= printer.print("Has Take-Out Service") %> <span class="required_field">*</span></span></label>
		<input type="radio" name="has_takeout" checked="true" value="true" /> <%= printer.print("Yes") %> 		
		<input type="radio" name="has_takeout" value="false" /> <%= printer.print("No") %>
		<br />
		<div id="has_takeout"></div>
	</div>
	
	<div>
       	<label for="has_takein"><span><%= printer.print("Has Dine-In Service") %> <span class="required_field">*</span></span></label>
		<input type="radio" name="has_takein" checked="true" value="true" /> <%= printer.print("Yes") %> 		
		<input type="radio" name="has_takein" value="false" /> <%= printer.print("No") %>
		<br />
		<div id="has_takein"></div>
	</div>
	
	</fieldset>

	<br class="clearfloat" />
	
	<div>
		<input type="checkbox" name="keep_adding" checked="true" value="true" /> <%= printer.print("Continue adding branches") %>
		<div id="keep_adding"></div>
	</div>
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/listBranch.jsp?r_key=<%= request.getParameter("r_key") %>'" class="button-close"/>
	
	<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
