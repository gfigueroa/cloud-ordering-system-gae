<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.AdditionalPropertyType" %>
<%@ page import="datastore.AdditionalPropertyTypeManager" %>
<%@ page import="datastore.AdditionalPropertyValue" %>
<%@ page import="datastore.AdditionalPropertyValueManager" %>
<%@ page import="datastore.MenuItem" %>
<%@ page import="datastore.MenuItemManager" %>
<%@ page import="datastore.MenuItemAdditionalPropertyValue" %>
<%@ page import="datastore.MenuItemAdditionalPropertyValueManager" %>
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
  
  MenuItem menuItem = MenuItemManager.getMenuItem(KeyFactory.stringToKey(request.getParameter("mi_k")));
  
  List<MenuItemAdditionalPropertyValue> menuItemAdditionalPropertyValues = MenuItemAdditionalPropertyValueManager.getMenuItemAdditionalPropertyValues(menuItem.getKey());
  
  List<AdditionalPropertyType> typeList = AdditionalPropertyTypeManager.getRestaurantAdditionalPropertyTypes(sessionUser.getKey().getParent());
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

<form method="post" id="form1" name="form1" 
      action="/manageRestaurant?action=update&type=M"
      class="form-style">

    <fieldset>
    
    <legend><%= menuItem.getMenuItemName() %> - <%= printer.print("Additional Properties") %></legend>
	
	<% if (message != null && message.equals("success") && action != null && action.equals("update")) { %>
		<div class="success-div"><%= printer.print("Additional Property Type successfully updated") %>.</div>
	<% } %>
	
	<div>
	  <h2><%= readOnly ? printer.print("View Property Values") : printer.print("Edit Property Values") %></h2>
	</div>
	
	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"<%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
    		
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">

		<tr>
		  	<td width="30%"><%= printer.print("Additional Properties") %></td>
		    <td width="10%"><%= !readOnly ? printer.print("Selected") : "" %></td>
		    <td width="40%" align="center"><%= printer.print("Additional Charge (NT$)") %></td>
		    <td width="20%"></td>
	  	</tr>  	  
	  
	  	<tr>
		  	<td width="30%"></td>
		    <td width="10%"></td>
		    <td width="40%"></td>
		    <td width="20%"></td>
	  	</tr>    		
	
		<%
		int count = 0;
		for (AdditionalPropertyType type : typeList) {
			if (readOnly) {
				boolean includesType = false;
				for (MenuItemAdditionalPropertyValue menuItemAdditionaPropertyValue : menuItemAdditionalPropertyValues) {
					if (menuItemAdditionaPropertyValue.getAdditionalPropertyValue().getParent().equals(type.getKey())) {
						includesType = true;
						break;
					}
				}
				if (!includesType) {
					continue;
				}
			}
		
			List<AdditionalPropertyValue> valueList = AdditionalPropertyValueManager.getAdditionalPropertyValues(type.getKey());
		%>
			<tr>
			    <td width="30%">
			        <label for="apt_name"><span><b><i><u><%= type.getAdditionalPropertyTypeName() %></u></i></b></span></label>
				</td>
				<td width="10%"></td>
				<td width="40%"></td>
				<td width="20%"></td>
			</tr>
			
			<% 
			for (AdditionalPropertyValue value : valueList) {
				String valueKeyString = KeyFactory.keyToString(value.getKey());
				
				// Check if property value is selected for this menu item
				boolean isSelected = false;
				double additionalCharge = 0;
				String menuItemAdditionalPropertyValueKeyString = "";
				for (MenuItemAdditionalPropertyValue menuItemAdditionaPropertyValue : menuItemAdditionalPropertyValues) {
					if (menuItemAdditionaPropertyValue.getAdditionalPropertyValue().equals(value.getKey())) {
						isSelected = true;
						additionalCharge = menuItemAdditionaPropertyValue.getAdditionalCharge() != null ? menuItemAdditionaPropertyValue.getAdditionalCharge() : 0;
						menuItemAdditionalPropertyValueKeyString = KeyFactory.keyToString(menuItemAdditionaPropertyValue.getKey());
						break;
					}
				}
				
				if (!isSelected && readOnly) {
					continue;
				}
			%>
				
				<input type="text" name="apv_k<%= count %>" value="<%= valueKeyString %>" style="display:none;" />
				
				<tr>
				    <td width="30%">
				        <div>
				        	<label for="apv_name<%= count %>"><span>&nbsp;&nbsp;&nbsp;&nbsp;<%= value.getAdditionalPropertyValueName() %></span></label>
							<div id="apv_name<%= count %>"></div>
						</div>
					</td>

					<td width="10%" align="center">
						<%
						if (!readOnly) {
						%>
							<div>
								<input type="checkbox" name="apv_checked<%= count %>" <%= isSelected ? "checked=\"checked\"" : "" %> <%= readOnly ? "disabled=\"true\"" : "" %> />
								<div id="apv_checked<%= count %>"></div>
							</div>
						<%
						}
						%>
					</td>
					
					<td width="40%" align="center">
						<input type="text" name="apv_price<%= count %>" value="<%= isSelected ? additionalCharge : "" %>" <%= readOnly ? "readonly=\"readonly\"" : "" %> />
						<div id="apv_price<%= count %>"></div>
					</td>
					
					<td width="20%" align="center">
					<%
					if (readOnly) {
					%>
						<a href="/restaurant/editProductItemPropertyConstraints.jsp?miapv_k=<%= menuItemAdditionalPropertyValueKeyString %>"><%= printer.print("Constraints") %>
					<%
					}
					%>
					</td>
			  	</tr>	
		<%  
			count++;
			}
		}
		%>
	</table>		
		
	</fieldset>

	<br class="clearfloat" />
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/listMenuItem.jsp'" class="button-close"/>
	<%
	if (!readOnly) {
	%>
		<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>
	<%
	}
	else {
	%>
		<input type="button" value="&nbsp;&nbsp;&nbsp;<%= printer.print("Edit") %>&nbsp;&nbsp;&nbsp;" onClick="location.href='/restaurant/editProductItemProperty.jsp?mi_k=<%= KeyFactory.keyToString(menuItem.getKey()) %>'" class="button_style">
	<%
	}
	%>
	
	<input type="text" name="mi_k" value="<%= request.getParameter("mi_k") %>" style="display:none;" />
	<input type="text" name="count" value="<%= count %>" style="display:none;" />
	
</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
