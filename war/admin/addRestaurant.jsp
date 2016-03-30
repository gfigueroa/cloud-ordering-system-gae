<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.RestaurantType" %>
<%@ page import="datastore.RestaurantTypeManager" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>

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
  
  String error = request.getParameter("etype");

  String storeSuperTypeString = request.getParameter("ss_type");
  RestaurantType.StoreSuperType storeSuperType = storeSuperTypeString != null ? 
  		RestaurantType.getStoreSuperTypeFromString(storeSuperTypeString) : RestaurantType.StoreSuperType.FOOD_DRINK;
  		
  List<RestaurantType> restaurantTypeList = RestaurantTypeManager.getAllRestaurantTypes(storeSuperType);
  
  List<RestaurantType> foodDrinkTypeList = RestaurantTypeManager.getAllRestaurantTypes(RestaurantType.StoreSuperType.FOOD_DRINK);
  List<RestaurantType> shoppingTypeList = RestaurantTypeManager.getAllRestaurantTypes(RestaurantType.StoreSuperType.SHOPPING);
  List<RestaurantType> pollsTypeList = RestaurantTypeManager.getAllRestaurantTypes(RestaurantType.StoreSuperType.POLLS);
  List<RestaurantType> salonTypeList = RestaurantTypeManager.getAllRestaurantTypes(RestaurantType.StoreSuperType.SALON);
  List<RestaurantType> godDwellingPlaceTypeList = RestaurantTypeManager.getAllRestaurantTypes(RestaurantType.StoreSuperType.GOD_DWELLING_PLACE);
  List<RestaurantType> virtualChannelTypeList = RestaurantTypeManager.getAllRestaurantTypes(RestaurantType.StoreSuperType.VIRTUAL_CHANNEL);
  
  BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />

<script language="Javascript" type="text/javascript">

function refresh(ss_type) {
	document.getElementById("channel").style.display="none";
	var elems = document.getElementsByTagName('*');
    for (var i = 0 ; i < elems.length; i++){
 		if(ss_type=='r_type_food_drink'){  
        	if(elems[i].className.indexOf("r_type_food_drink") > -1) elems[i].style.display="block";
        	if(elems[i].className.indexOf("r_type_shopping") > -1
        		||elems[i].className.indexOf("r_type_polls") > -1
        		||elems[i].className.indexOf("r_type_salon") > -1
        		||elems[i].className.indexOf("r_type_gdp") > -1
        		||elems[i].className.indexOf("r_type_vc") > -1
        		) elems[i].style.display="none";	
        }
        if(ss_type=='r_type_shopping'){  
        	if(elems[i].className.indexOf("r_type_shopping") > -1) elems[i].style.display="block";
        	if(elems[i].className.indexOf("r_type_food_drink") > -1
        		||elems[i].className.indexOf("r_type_polls") > -1
        		||elems[i].className.indexOf("r_type_salon") > -1
        		||elems[i].className.indexOf("r_type_gdp") > -1
        		||elems[i].className.indexOf("r_type_vc") > -1
        		) elems[i].style.display="none";	
        }
        if(ss_type=='r_type_polls'){  
        	if(elems[i].className.indexOf("r_type_polls") > -1) elems[i].style.display="block";
        	if(elems[i].className.indexOf("r_type_shopping") > -1
        		||elems[i].className.indexOf("r_type_food_drink") > -1
        		||elems[i].className.indexOf("r_type_salon") > -1
        		||elems[i].className.indexOf("r_type_gdp") > -1
        		||elems[i].className.indexOf("r_type_vc") > -1
        		) elems[i].style.display="none";	
        }
        if(ss_type=='r_type_salon'){  
        	if(elems[i].className.indexOf("r_type_salon") > -1) elems[i].style.display="block";
        	if(elems[i].className.indexOf("r_type_shopping") > -1
        		||elems[i].className.indexOf("r_type_polls") > -1
        		||elems[i].className.indexOf("r_type_food_drink") > -1
        		||elems[i].className.indexOf("r_type_gdp") > -1
        		||elems[i].className.indexOf("r_vc") > -1
        		) elems[i].style.display="none";	
        }
        if(ss_type=='r_type_gdp'){  
        	if(elems[i].className.indexOf("r_type_gdp") > -1) elems[i].style.display="block";
        	if(elems[i].className.indexOf("r_type_shopping") > -1
        		||elems[i].className.indexOf("r_type_polls") > -1
        		||elems[i].className.indexOf("r_type_food_drink") > -1
        		||elems[i].className.indexOf("r_type_salon") > -1
        		||elems[i].className.indexOf("r_type_vc") > -1
        		) elems[i].style.display="none";
        }
        if(ss_type=='r_type_vc'){  
        	if(elems[i].className.indexOf("r_type_vc") > -1){
        	 elems[i].style.display="block";
        	 document.getElementById("channel").style.display="block";
        	}
        	if(elems[i].className.indexOf("r_type_shopping") > -1
        		||elems[i].className.indexOf("r_type_polls") > -1
        		||elems[i].className.indexOf("r_type_food_drink") > -1
        		||elems[i].className.indexOf("r_type_salon") > -1
        		||elems[i].className.indexOf("r_type_gdp") > -1
        		) elems[i].style.display="none";
        }
    }

}

</script>

<script language="JavaScript" type="text/javascript" src="../js/crypto.js"></script>

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

onload=function() {
	refresh("r_type_food_drink");
}

</script>

<%@  include file="../header/page-title.html" %>
</head>

<body>
<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<form method="post" id="form1" name="form1"
		action="<%= blobstoreService.createUploadUrl("/manageUser?action=add") %>"
		onSubmit="return checkPasswords();"
		onLoad="function();"
		class="form-style" enctype="multipart/form-data">
		
	<input type="text" name="u_type" value="R" style="display:none;" />

    <fieldset>
	<legend><%= printer.print("Retail Store Information") %></legend>
	
	<a href="/menu/underConstruction.jsp"><%= printer.print("Batch Upload") %></a>
	
	<div>
	  <h2><%= printer.print("Add a Retail Store") %></h2>
	</div>
	
	<% if (error != null && error.equals("AlreadyExists")) { %>
		<div class="error-div"><%= printer.print("The email you provided is already being used in the system") %></div>
	<% } %>
	<% if (error != null && error.equals("Format")) { %>
		<div class="error-div"><%= printer.print("The email you provided does not conform to the standard formats (you can try something like user@domain.com)") %></div>
	<% } %>
	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
	
	<div>
		<label for="ss_type"><span><%= printer.print("Service Type") %> <span class="required_field">*</span></span></label>
		<select name="ss_type" onChange="refresh(this.value);">
	        <option value="r_type_food_drink" <%= storeSuperType == RestaurantType.StoreSuperType.FOOD_DRINK ? "selected=\"true\"" : "" %>><%= printer.print("Food and Drink") %></option>
	        <option value="r_type_shopping" <%= storeSuperType == RestaurantType.StoreSuperType.SHOPPING ? "selected=\"true\"" : "" %>><%= printer.print("Shopping") %></option>
	        <option value="r_type_polls" <%= storeSuperType == RestaurantType.StoreSuperType.POLLS ? "selected=\"true\"" : "" %>><%= printer.print("News and Opinion Polls") %></option>
	        <option value="r_type_salon" <%= storeSuperType == RestaurantType.StoreSuperType.SALON ? "selected=\"true\"" : "" %>><%= printer.print("Salon") %></option>
	        <option value="r_type_gdp" <%= storeSuperType == RestaurantType.StoreSuperType.GOD_DWELLING_PLACE ? "selected=\"true\"" : "" %>><%= printer.print("God Dwelling Place") %></option>
	        <option value="r_type_vc" <%= storeSuperType == RestaurantType.StoreSuperType.VIRTUAL_CHANNEL ? "selected=\"true\"" : "" %>><%= printer.print("Virtual Channel") %></option>
        </select>
		<div id="ss_type"></div>
	</div>
	
	<div>
		<label for="r_type"><span><%= printer.print("Retail Store type") %> <span class="required_field">*</span></span></label>
		<select name="r_type">
        <option value="">-<%= printer.print("Select Retail Store Type") %>-</option>
        <% for (RestaurantType restaurantType : foodDrinkTypeList) { %>
        	<option class="r_type_food_drink" value="<%= restaurantType.getKey() %>"><%= restaurantType.getRestaurantTypeName() %></option>
        <% } %>
        <% for (RestaurantType restaurantType : shoppingTypeList) { %>
        	<option class="r_type_shopping" value="<%= restaurantType.getKey() %>"><%= restaurantType.getRestaurantTypeName() %></option>
        <% } %>
        <% for (RestaurantType restaurantType : pollsTypeList) { %>
        	<option class="r_type_polls" value="<%= restaurantType.getKey() %>"><%= restaurantType.getRestaurantTypeName() %></option>
        <% } %>
        <% for (RestaurantType restaurantType : salonTypeList) { %>
        	<option class="r_type_salon" value="<%= restaurantType.getKey() %>"><%= restaurantType.getRestaurantTypeName() %></option>
        <% } %>
        <% for (RestaurantType restaurantType : godDwellingPlaceTypeList) { %>
        	<option class="r_type_gdp" value="<%= restaurantType.getKey() %>"><%= restaurantType.getRestaurantTypeName() %></option>
        <% } %>
        <% for (RestaurantType restaurantType : virtualChannelTypeList) { %>
        	<option class="r_type_vc" value="<%= restaurantType.getKey() %>"><%= restaurantType.getRestaurantTypeName() %></option>
        <% } %>
        </select>
		<div id="r_type"></div>
	</div>
	
	<div>
		<label for="services"><span><%= printer.print("Services") %></span></label>
		<input type="checkbox" name="has_ns" checked="true" value="true" disabled="true" /> <%= printer.print("News and Opinion Polls") %><br/>
		<label for="services"><span></span></label>
		<input type="checkbox" name="has_ps" value="true" /> <%= printer.print("Product Items") %><br/>
		<label for="services"><span></span></label>
		<input type="checkbox" name="has_sps" value="true" /> <%= printer.print("Service Providers") %><br/>
		<label for="services"><span></span></label>
		<input type="checkbox" name="has_ms" value="true" /> <%= printer.print("Messages") %>
		<div id="services"></div>
	</div>
	
    <div>
       	<label  for="r_name"><span><%= printer.print("Retail Store Name") %> <span class="required_field">*</span></span></label>
		<input type="text" name="r_name" class="input_extra_large"  value=""  title="" /><br />
		<div id="r_name"></div>
	</div>
	
	<div id="channel">
       	<label  for="c_number"><span><%= printer.print("Channel Number") %> <span class="required_field">*</span></span></label>
		<input type="text" name="c_number" class="input_extra_large" value=""  title="" /><br />
		<div id="c_number"></div>
	</div>
	
	<div>
       	<label for="r_description"><span><%= printer.print("Description") %> <span class="required_field">*</span></span></label>
		<textarea name="r_description" class="input_extra_large" value=""></textarea><br />
		<div id="r_description"></div>
	</div>
	
	<div>
       	<label for="u_email"><span><%= printer.print("E-mail") %> <span class="required_field">*</span></span></label>
		<input type="text" name="u_email" class="input_extra_large" value="" /><br />
		<div id="u_email"></div>
	</div>
	
    <div>
       	<label for="u_password"><span><%= printer.print("Password") %> <span class="required_field">*</span></span></label>
		<input type="password" name="u_password" class="input_extra_large" value="" /><br />
		<div id="u_password"></div>
	</div>
	
	<div>
       	<label for="u_password_confirm"><span><%= printer.print("Confirm Password") %> <span class="required_field">*</span></span></label>
		<input type="password" name="u_password_confirm" class="input_extra_large" value="" /><br />
		<div id="u_password_confirm"></div>
	</div>
	
    <div>
       	<label for="r_website"><span><%= printer.print("Website") %> </span></label>
		<input type="text" name="r_website" class="input_extra_large" value="" /><br />
		<div id="r_website"></div>
	</div>
	
    <div>
       	<label for="r_logo"><span><%= printer.print("Logo") %></span></label>
		<input type="file" name="r_logo" class="input_extra_large" value="" /><br />
		<div id="r_logo"></div>
	</div>
	
	<div>
		<label for="r_ot"><span><%= printer.print("Opening Time") %></span></label>
		<select name="r_oth">
        <option value="0">-hh-</option>
        <% for (int i = 0; i < 24; i++) { %>
        	<option value="<%= i %>"><%= i < 10 ? "0" + i : i %></option>
        <% } %>
        </select>
        <select name="r_otm">
        <option value="0">-mm-</option>
        <% for (int i = 0; i < 60; i++) { %>
        	<option value="<%= i %>"><%= i < 10 ? "0" + i : i %></option>
        <% } %>
        </select>
		<div id="r_ot"></div>
	</div>
	
	<div>
		<label for="r_ct"><span><%= printer.print("Closing Time") %></span></label>
		<select name="r_cth">
        <option value="0">-hh-</option>
        <% for (int i = 0; i < 24; i++) { %>
        	<option value="<%= i %>"><%= i < 10 ? "0" + i : i %></option>
        <% } %>
        </select>
        <select name="r_ctm">
        <option value="0">-mm-</option>
        <% for (int i = 0; i < 60; i++) { %>
        	<option value="<%= i %>"><%= i < 10 ? "0" + i : i %></option>
        <% } %>
        </select>
		<div id="r_ct"></div>
	</div>
    
    <div>
       	<label for="r_comments"><span><%= printer.print("Comments") %></span></label>
		<textarea  name="r_comments" class="input_extra_large" value=""></textarea><br />
		<div id="r_comments"></div>
	</div>
	
	</fieldset>

	<br class="clearfloat" />
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/admin/listRestaurant.jsp'" class="button-close"/>
	
	<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
