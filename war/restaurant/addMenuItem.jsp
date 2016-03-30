<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.MenuItemType" %>
<%@ page import="datastore.MenuItemTypeManager" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
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
<%@ include file="../header/page-title.html" %>

<script language="Javascript" type="text/javascript">

function isInteger(sText) {
   var ValidChars = "0123456789";
   var IsInteger=true;
   var Char;

 
   for (i = 0; i < sText.length && IsInteger == true; i++) { 
      Char = sText.charAt(i); 
      if (ValidChars.indexOf(Char) == -1) {
         IsInteger = false;
      }
   }
   return IsInteger; 
}

function isDouble(sText) {
   var ValidChars = "0123456789.";
   var IsDouble=true;
   var Char;

 
   for (i = 0; i < sText.length && IsDouble == true; i++) { 
      Char = sText.charAt(i); 
      if (ValidChars.indexOf(Char) == -1) {
         IsDouble = false;
      }
   }
   return IsDouble; 
}

function checkNumericValues() {
  if(!isDouble(document.getElementsByName("mi_price")[0].value)){
    alert("<%=printer.print("The price you entered is not valid")%>.");
    return false;
  }
  if(!isInteger(document.getElementsByName("mi_serving_time")[0].value)){
    alert("<%=printer.print("The serving time you entered is not valid")%>.");
    return false;
  }
  if(!isDouble(document.getElementsByName("mi_discount")[0].value)){
    alert("<%=printer.print("The discount you entered is not valid")%>.");
    return false;
  }
  if(!isInteger(document.getElementsByName("mi_service_time")[0].value)){
    alert("<%=printer.print("The service time you entered is not valid")%>.");
    return false;
  }
  return true;
}

</script>
</head>

<body>
<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<%
	List<MenuItemType> typeList = MenuItemTypeManager.getRestaurantMenuItemTypes(sessionUser.getKey().getParent());
%>

<form method="post" id="form1" name="form1" onSubmit="return checkNumericValues();" 
      action="<%= blobstoreService.createUploadUrl("/manageRestaurant?action=add&type=I") %>"
      class="form-style" enctype="multipart/form-data">

    <fieldset>
	<legend><%= printer.print("Product Items") %></legend>
	
	<% if (message != null && message.equals("success") && action != null && action.equals("add")) { %>
		<div class="success-div"><%= printer.print("Product item successfully added to the Retail Store's menu") %>.</div>
	<% } %>
	
	<div>
		<h2><%= printer.print("Add Product Item") %></h2>
	</div>

	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
	<% if (error != null && error.equals("Format")) { %>
		<div class="error-div"><%= printer.print("The discount must be a decimal value between 0 and 1") %></div>
	<% } %>

    <div>
		<label for="mit_id"><span><%= printer.print("Product Type") %> <span class="required_field">*</span></span></label>
		<select name="mit_id">
        <option value="">-<%= printer.print("Select Type") %>-</option>
        <% for (MenuItemType menuItemType : typeList) { %>
        	<option value="<%= KeyFactory.keyToString(menuItemType.getKey()) %>"><%= menuItemType.getMenuItemTypeName() %></option>
        <% } %>
        </select>
		<div id="mit_id"></div>
	</div>
	
    <div>
       	<label  for="mi_name"><span><%= printer.print("Product Item Name") %> <span class="required_field">*</span></span></label>
		<input type="text" name="mi_name" class="input_extra_large"  value="" title="" /><br />
		<div id="mi_name"></div>
	</div>
	
	<div>
		<label for="mi_price"><span><%= printer.print("Price") %> <span class="required_field">*</span></span></label>
		<input type="text" name="mi_price" class="input_large"  value="" /><br />
		<div id="mi_price"></div>
	</div>
	
	<div>
		<label for="mi_discount"><span><%= printer.print("Discount(between 0-1)") %></span></label>
		<input type="text" name="mi_discount" class="input_large"  value="" /><br />
		<div id="mi_discount"></div>
	</div>
	
 	<div>
       	<label for="mi_description"><span><%= printer.print("Description") %> <span class="required_field">*</span></span></label>
		<textarea name="mi_description" class="input_extra_large" value="" /></textarea><br />
		<div id="mi_description"></div>
	</div>
	
    <div>
       	<label for="mi_image"><span><%= printer.print("Image") %> </span></label>
		<input type="file" name="mi_image" class="input_extra_large" value="" /><br />
		<div id="mi_image">
		</div>
	</div>
	
	<div>
		<label for="mi_serving_time"><span><%= printer.print("Serving time (minutes)") %></span></label>
		<input type="text" name="mi_serving_time" class="input_large" value="" /><br />
		<div id="mi_serving_time"></div>
	</div>
	
	<div>
       	<label for="mi_isavailable"><span><%= printer.print("Available") %> <span class="required_field">*</span></span></label>
		<input type="radio" name="mi_isavailable" checked="true" value="Y" /> <span><%= printer.print("Yes") %> 		
		<input type="radio" name="mi_isavailable" value="N" /> <span><%= printer.print("No") %><br />
		<div id="mi_isavailable"></div>
	</div>
	
	<div>
		<label for="mi_service_time"><span><%= printer.print("Service Time") %> <span class="required_field">*</span></span></label>
		<select name="mi_service_time">
        <option value="0"><%= printer.print("All day") %></option>
        <option value="1"><%= printer.print("Breakfast (07:00 - 12:00)") %></option>
        <option value="2"><%= printer.print("Lunch (12:00 - 16:30)") %></option>
        <option value="3"><%= printer.print("Dinner (16:30 - 23:30)") %></option>
        <option value="4"><%= printer.print("Breakfast + Lunch") %></option>
        <option value="5"><%= printer.print("Breakfast + Dinner") %></option>
        <option value="6"><%= printer.print("Lunch + Dinner") %></option>
        </select>
		<div id="mi_service_time"></div>
	</div>
	
    <div>
       	<label for="mi_comments"><span><%= printer.print("Comments") %></span></label>
		<textarea  name="mi_comments" class="input_extra_large" value="" /></textarea><br />
		<div id="mi_comments"></div>
	</div>
	
	</fieldset>

	<br class="clearfloat" />
	
	<div>
		<input type="checkbox" name="keep_adding" checked="true" value="true" /> <%= printer.print("Continue adding Product Items") %>		
		<div id="keep_adding"></div>
	</div>
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/listMenuItem.jsp?k=<%= request.getParameter("k") %>'" class="button-close"/>
	
	<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>

</form>

<jsp:include page="../header/page-footer.jsp" />
</body>
</html>
