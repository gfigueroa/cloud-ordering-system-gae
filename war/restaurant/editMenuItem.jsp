<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.MenuItem" %>
<%@ page import="datastore.MenuItemManager" %>
<%@ page import="datastore.MenuItemType" %>
<%@ page import="datastore.MenuItemTypeManager" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="com.google.appengine.api.blobstore.BlobKey" %>
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
  
  boolean readOnly = request.getParameter("readonly") != null ? true : false;
  String returnTo = request.getParameter("returnto") != null ? request.getParameter("returnto") : "listMenuItem";
  String orderKeyString = request.getParameter("o_key");
  String error = request.getParameter("etype");
  String message = request.getParameter("msg");
  String action = request.getParameter("action");
  
  MenuItem menuItem = MenuItemManager.getMenuItem(KeyFactory.stringToKey(request.getParameter("k")));
  
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
      action="<%= blobstoreService.createUploadUrl("/manageRestaurant?action=update&type=I&k=" + request.getParameter("k")) %>"
      class="form-style" enctype="multipart/form-data">
      
    <input type="text" name="u_key" value="<%= KeyFactory.keyToString(sessionUser.getKey()) %>" style="display:none;" />

    <fieldset>
	<legend><%= printer.print("Product Items") %></legend>
	
	<% if (message != null && message.equals("success") && action != null && action.equals("update")) { %>
		<div class="success-div"><%= printer.print("Product item successfully updated") %>.</div>
	<% } %>
	
	<div>
		<h2><%= readOnly ? printer.print("View Product Item") : printer.print("Edit Product Item") %></h2>
	</div>
	
	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
	<% if (error != null && error.equals("Format")) { %>
		<div class="error-div"><%= printer.print("The discount must be a decimal value between 0 and 1") %></div>
	<% } %>
    
    <div>
		<label for="mit_id"><span><%= printer.print("Product Item Type") %> <span class="required_field">*</span></span></label>
        <select name="mit_id" <%= readOnly? "disabled=\"true\"" : "" %>>
        <% for (MenuItemType menuItemType : typeList) { %>
        	<option value="<%= KeyFactory.keyToString(menuItemType.getKey()) %>" <%= KeyFactory.keyToString(menuItem.getMenuItemType()).equals(KeyFactory.keyToString(menuItemType.getKey())) ? "selected=\"true\"" : "" %>>
        		<%= menuItemType.getMenuItemTypeName() %>
        	</option>
        <% } %>
        </select>
		<div id="mit_id"></div>
	</div>
	
    <div>
       	<label  for="mi_name"><span><%= printer.print("Product Item Name") %> <span class="required_field">*</span></span></label>
		<input type="text" name="mi_name" class="input_extra_large" value="<%= menuItem.getMenuItemName() %>" title="" <%= readOnly? "readonly=\"readonly\"" : "" %> /><br />
		<div id="mi_name"></div>
	</div>
	
	<div>
		<label for="mi_price"><span><%= printer.print("Price") %> <span class="required_field">*</span></span></label>
		<input type="text" name="mi_price" class="input_large" value="<%= menuItem.getMenuItemPrice() %>" <%= readOnly? "readonly=\"readonly\"" : "" %> /><br />
		<div id="mi_price"></div>
	</div>
	
	<div>
		<label for="mi_discount"><span><%= printer.print("Discount(between 0-1)") %> </span></label>
		<input type="text" name="mi_discount" class="input_large" value="<%= menuItem.getMenuItemDiscount() == null ? "" : menuItem.getMenuItemDiscount() %>" <%= readOnly? "readonly=\"readonly\"" : "" %> /><br />
		<div id="mi_discount"></div>
	</div>
	
 	<div>
       	<label for="mi_description"><span><%= printer.print("Description") %> <span class="required_field">*</span></span></label>
		<textarea name="mi_description" class="input_extra_large" value="" <%= readOnly? "readonly=\"readonly\"" : "" %>><%= menuItem.getMenuItemDescription() %></textarea>
		<br />
		<div id="mi_description"></div>
	</div>
    
    <%
    if (!(menuItem.getMenuItemImage() == null && readOnly)) {
    %>
    <div>
       	<label for="mi_image"><span><%= printer.print("Image") %></span></label>
       	<%
		if (menuItem.getMenuItemImage() != null) {
		%>
			<a target="_new" href="/img?blobkey=<%= menuItem.getMenuItemImage().getKeyString() %>">
			<img src="/img?blobkey=<%= menuItem.getMenuItemImage().getKeyString() %>&s=300">
			</a>
			<br />
		<%
		}
		%>
		<%
		if (!readOnly) {
		%>		
		<label><span> </span></label><input type="file" name="mi_image" class="input_extra_large" value="" /><br />
		<%
		}
		%>
		<div id="mi_image"></div>
	</div>
	<%
	}
	%>
	
	<div>
		<label for="mi_serving_time"><span><%= printer.print("Serving time (minutes)") %></span></label>
		<input type="text" name="mi_serving_time" class="input_large" value="<%= menuItem.getMenuItemServingTime() == null ? "" : menuItem.getMenuItemServingTime() %>" <%= readOnly? "readonly=\"readonly\"" : "" %> /><br />
		<div id="mi_serving_time"></div>
	</div>
	
	<div>
       	<label for="mi_isavailable"><span><%= printer.print("Available") %> <span class="required_field">*</span></span></label>
		<input type="radio" <%= menuItem.isAvailable() ? "checked=\"true\"" : "" %> name="mi_isavailable" value="Y" <%= readOnly? "disabled=\"true\"" : "" %> /> <%= printer.print("Yes") %>
		<input type="radio" <%= !menuItem.isAvailable() ? "checked=\"true\"" : "" %> name="mi_isavailable" value="N" <%= readOnly? "disabled=\"true\"" : "" %> /> <%= printer.print("No") %>
		<br />
		<div id="mi_isavailable"></div>
	</div>
	
	<div>
		<label for="mi_service_time"><span><%= printer.print("Service Time") %> <span class="required_field">*</span></span></label>
		<select name="mi_service_time" <%= readOnly? "disabled=\"true\"" : "" %>>
        <option value="0" <%= menuItem.getMenuItemServiceTime() == 0 ? "selected=\"true\"" : "" %>><%= printer.print("All day") %></option>
        <option value="1" <%= menuItem.getMenuItemServiceTime() == 1 ? "selected=\"true\"" : "" %>><%= printer.print("Breakfast (07:00 - 12:00)") %></option>
        <option value="2" <%= menuItem.getMenuItemServiceTime() == 2 ? "selected=\"true\"" : "" %>><%= printer.print("Lunch (12:00 - 16:30)") %></option>
        <option value="3" <%= menuItem.getMenuItemServiceTime() == 3 ? "selected=\"true\"" : "" %>><%= printer.print("Dinner (16:30 - 23:30)") %></option>
        <option value="4" <%= menuItem.getMenuItemServiceTime() == 4 ? "selected=\"true\"" : "" %>><%= printer.print("Breakfast + Lunch") %></option>
        <option value="5" <%= menuItem.getMenuItemServiceTime() == 5 ? "selected=\"true\"" : "" %>><%= printer.print("Breakfast + Dinner") %></option>
        <option value="6" <%= menuItem.getMenuItemServiceTime() == 6 ? "selected=\"true\"" : "" %>><%= printer.print("Lunch + Dinner") %></option>
        </select>
		<div id="mi_service_time"></div>
	</div>
	
    <div>
       	<label for="mi_comments"><span><%= printer.print("Comments") %></span></label>
		<textarea name="mi_comments" class="input_extra_large" value="" <%= readOnly? "readonly=\"readonly\"" : "" %>><%= menuItem.getMenuItemComments() %></textarea><br />
		<div id="mi_comments"></div>
	</div>
	
	<div>
		<a href="/restaurant/editProductItemProperty.jsp?readonly=true&mi_k=<%= KeyFactory.keyToString(menuItem.getKey()) %>"><%= printer.print("Additional Properties") %></a>
	</div>
	
	</fieldset>
	
	<br class="clearfloat" />
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/<%= returnTo %>.jsp<%= orderKeyString != null ? "?k=" + orderKeyString : "" %>'" class="button-close"/>
	
	<%
	if (!readOnly) {
	%>
		<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>
	<%
	}
	else {
	%>
		<input type="button" value="&nbsp;&nbsp;<%= printer.print("Edit") %>&nbsp;&nbsp;" onClick="location.href='/restaurant/editMenuItem.jsp?k=<%= request.getParameter("k") %>'" class="button_style">
	<%
	}
	%>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>