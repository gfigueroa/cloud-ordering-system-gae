<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.Region" %>
<%@ page import="datastore.RegionManager" %>
<%@ page import="datastore.Branch" %>
<%@ page import="datastore.BranchManager" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="util.GeoCoder" %>
<%@ page import="util.Location" %>
<%@ page import="util.GeocodeResponse" %>
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
  
  boolean readOnly = request.getParameter("readonly") != null ? true : false;
  String error = request.getParameter("etype");
  String message = request.getParameter("msg");
  String action = request.getParameter("action");
  
  List<Region> regionList = RegionManager.getAllRegions();
  String branchKeyString = request.getParameter("k");
  Key branchKey = KeyFactory.stringToKey(branchKeyString);
  Branch branch = BranchManager.getBranch(branchKey);
  Restaurant restaurant = RestaurantManager.getRestaurant(branchKey.getParent());
  
  String hasDeliveryString = request.getParameter("has_delivery");
  boolean hasDelivery = 
  		hasDeliveryString != null ? Boolean.parseBoolean(hasDeliveryString) :
  				branch.hasDelivery();
  boolean hasRegularDelivery = branch.hasRegularDelivery() != null ? branch.hasRegularDelivery() : false;
  boolean hasPostalDelivery = branch.hasPostalDelivery() != null ? branch.hasPostalDelivery() : false;
  boolean hasUPSDelivery = branch.hasUPSDelivery() != null ? branch.hasUPSDelivery() : false;
  boolean hasConvenienceStoreDelivery = branch.hasConvenienceStoreDelivery() != null ? branch.hasConvenienceStoreDelivery() : false;
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

function refresh(k, has_delivery) {
    window.location.replace("editBranch.jsp?k=" + k + "&has_delivery=" + has_delivery);
}

</script>

</head>

<body>

<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<form method="post" id="form1" name="form1"
		action="/manageRestaurant?action=update"
		class="form-style">
		
	<input type="text" name="type" value="B" style="display:none;" />
	<input type="text" name="k" value="<%= request.getParameter("k") %>" style="display:none;" />

    <fieldset>
	<legend><%= printer.print("Branch Information") %></legend>
		
	<% if (message != null && message.equals("success") && action != null && action.equals("update")) { %>
		<div class="success-div"><%= printer.print("Branch successfully updated") %>.</div>
	<% } %>
	
	
	<div>
	  <h2><%= readOnly ? printer.print("View a branch") : printer.print("Edit a Branch") %></h2>
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
		<input type="text" name="r_name" class="input_extra_large" value="<%= restaurant.getRestaurantName() %>" title="" readonly="readonly" /><br />
		<div id="r_name"></div>
	</div>
	
	<div>
		<label for="b_region"><span><%= printer.print("Region") %> <span class="required_field">*</span></span></label>
		<select name="b_region" <%= readOnly ? "disabled=\"true\"" : "" %>>
        <option value="">-<%= printer.print("Select Region") %>-</option>
        <% for (Region region : regionList) { %>
        	<option value="<%= region.getKey() %>" <%= branch.getRegion().longValue() == region.getKey().longValue() ? "selected=\"true\"" : "" %>><%= region.getRegionName() %></option>
        <% } %>
        </select>
		<div id="b_region"></div>
	</div>
	
    <div>
       	<label  for="b_name"><span><%= printer.print("Branch Name") %> <span class="required_field">*</span></span></label>
		<input type="text" name="b_name" class="input_extra_large" value="<%= branch.getBranchName() %>"  title="" <%= readOnly ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="b_name"></div>
	</div>
	
		<div>
       	<label  for="b_email"><span><%= printer.print("E-mail") %></span></label>
		<input type="text" name="b_email" class="input_extra_large" value="<%= branch.getBranchEmail() != null ? branch.getBranchEmail().getEmail() : "" %>" title="" <%= readOnly ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="b_email"></div>
	</div>
	
	<div>
       	<label for="b_address1"><span><%= printer.print("Address") %> <span class="required_field">*</span></span></label>
		<input type="text" name="b_address1" class="input_extra_large" value="<%= branch.getBranchAddress().getAddress() %>" <%= readOnly ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="b_address1"></div>
	</div>
	
    <div>
       	<label for="b_address2"><span></span></label>
		<input type="text" name="b_address2" class="input_extra_large" value="" <%= readOnly ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="b_address2"></div>
	</div>
	
	<%
	double lat=0;
	double lng=0;
	GeoCoder  gcoor = new GeoCoder ();
	GeocodeResponse t= gcoor.getLocation(branch.getBranchAddress().getAddress());
	List<GeocodeResponse.Result> listResults = t.getResults();
	for (GeocodeResponse.Result result:listResults){
		GeocodeResponse.Geometry geometry = result.getGeometry();
		Location location = geometry.getLocation();
		lat=location.getLat();
		lng=location.getLng();
	}
	%>
	
	<div>
       	<label for="b_location"><span><%= printer.print("Location") %></span></label>
		<input type="text" name="b_location" class="input_extra_large" value="<%= lat %>,<%= lng %>" readOnly=true /><br />
		<div id="b_location"></div>
	</div>
	
    <div>
       	<label for="b_phone"><span><%= printer.print("Phone number") %> <span class="required_field">*</span></span></label>
		<input type="text" name="b_phone" class="input_extra_large" value="<%= branch.getBranchPhone().getNumber() %>" <%= readOnly ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="b_phone"></div>
	</div>
	
	<div>
       	<label for="has_delivery"><span><%= printer.print("Has Delivery Service") %> <span class="required_field">*</span></span></label>
		<input type="radio" name="has_delivery" <%= hasDelivery ? "checked=\"true\"" : "" %> value="true" onClick="refresh('<%= branchKeyString %>', this.value);" <%= readOnly ? "disabled=\"true\"" : "" %> /> <%= printer.print("Yes") %> 		
		<input type="radio" name="has_delivery" <%= !hasDelivery ? "checked=\"true\"" : "" %> value="false" onClick="refresh('<%= branchKeyString %>', this.value);" <%= readOnly ? "disabled=\"true\"" : "" %> /> <%= printer.print("No") %>
		<br />
		<div id="has_delivery"></div>
	</div>
	
	<%
	if (hasDelivery) {
	%>
		<hr/>
		<div>
	       	<label for="has_regular_delivery"><span><%= printer.print("Has Regular Delivery") %> <span class="required_field">*</span></span></label>
			<input type="radio" name="has_regular_delivery" <%= hasRegularDelivery ? "checked=\"true\"" : "" %> value="true" <%= readOnly ? "disabled=\"true\"" : "" %> /> <%= printer.print("Yes") %> 		
			<input type="radio" name="has_regular_delivery" <%= !hasRegularDelivery ? "checked=\"true\"" : "" %> value="false" <%= readOnly ? "disabled=\"true\"" : "" %> /> <%= printer.print("No") %>
			<br />
			<div id="has_regular_delivery"></div>
		</div>
		
		<div>
	       	<label for="has_postal_delivery"><span><%= printer.print("Has Postal Delivery") %> <span class="required_field">*</span></span></label>
			<input type="radio" name="has_postal_delivery" <%= hasPostalDelivery ? "checked=\"true\"" : "" %> value="true" <%= readOnly ? "disabled=\"true\"" : "" %> /> <%= printer.print("Yes") %> 		
			<input type="radio" name="has_postal_delivery" <%= !hasPostalDelivery ? "checked=\"true\"" : "" %> value="false" <%= readOnly ? "disabled=\"true\"" : "" %> /> <%= printer.print("No") %>
			<br />
			<div id="has_postal_delivery"></div>
		</div>
		
		<div>
	       	<label for="has_ups_delivery"><span><%= printer.print("Has UPS Delivery") %> <span class="required_field">*</span></span></label>
			<input type="radio" name="has_ups_delivery" <%= hasUPSDelivery ? "checked=\"true\"" : "" %> value="true" <%= readOnly ? "disabled=\"true\"" : "" %> /> <%= printer.print("Yes") %> 		
			<input type="radio" name="has_ups_delivery" <%= !hasUPSDelivery ? "checked=\"true\"" : "" %> value="false" <%= readOnly ? "disabled=\"true\"" : "" %> /> <%= printer.print("No") %>
			<br />
			<div id="has_ups_delivery"></div>
		</div>
		
		<div>
	       	<label for="has_cs_delivery"><span><%= printer.print("Has Convenience Store Delivery") %> <span class="required_field">*</span></span></label>
			<input type="radio" name="has_cs_delivery" <%= hasConvenienceStoreDelivery ? "checked=\"true\"" : "" %> value="true" <%= readOnly ? "disabled=\"true\"" : "" %> /> <%= printer.print("Yes") %> 		
			<input type="radio" name="has_cs_delivery" <%= !hasConvenienceStoreDelivery ? "checked=\"true\"" : "" %> value="false" <%= readOnly ? "disabled=\"true\"" : "" %> /> <%= printer.print("No") %>
			<br />
			<div id="has_cs_delivery"></div>
		</div>
		<hr/>
	<%
	}
	%>
	
	<div>
       	<label for="has_takeout"><span><%= printer.print("Has Take-Out Service") %> <span class="required_field">*</span></span></label>
		<input type="radio" name="has_takeout" <%= branch.hasTakeOut() ? "checked=\"true\"" : "" %> value="true" <%= readOnly ? "disabled=\"true\"" : "" %> /> <%= printer.print("Yes") %> 		
		<input type="radio" name="has_takeout" <%= !branch.hasTakeOut() ? "checked=\"true\"" : "" %> value="false" <%= readOnly ? "disabled=\"true\"" : "" %> /> <%= printer.print("No") %>
		<br />
		<div id="has_takeout"></div>
	</div>
	
	<div>
       	<label for="has_takein"><span><%= printer.print("Has Dine-In Service") %> <span class="required_field">*</span></span></label>
		<input type="radio" name="has_takein" <%= branch.hasTakeIn() ? "checked=\"true\"" : "" %> value="true" <%= readOnly ? "disabled=\"true\"" : "" %> /> <%= printer.print("Yes") %> 		
		<input type="radio" name="has_takein" <%= !branch.hasTakeIn() ? "checked=\"true\"" : "" %> value="false" <%= readOnly ? "disabled=\"true\"" : "" %> /> <%= printer.print("No") %>
		<br />
		<div id="has_takein"></div>
	</div>
	
	</fieldset>

	<br class="clearfloat" />
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/listBranch.jsp?r_key=<%= KeyFactory.keyToString(restaurant.getKey()) %>'" class="button-close"/>
	
	<%
	if (!readOnly) {
	%>
		<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>
	<%
	}
	else {
	%>
		<input type="button" value="&nbsp;&nbsp;<%= printer.print("Edit") %>&nbsp;&nbsp;" onClick="location.href='/restaurant/editBranch.jsp?k=<%= request.getParameter("k") %>'" class="button_style">
	<%
	}
	%>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
