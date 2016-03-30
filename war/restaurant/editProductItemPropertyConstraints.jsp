<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.AdditionalPropertyConstraint" %>
<%@ page import="datastore.AdditionalPropertyConstraintManager" %>
<%@ page import="datastore.AdditionalPropertyType" %>
<%@ page import="datastore.AdditionalPropertyTypeManager" %>
<%@ page import="datastore.AdditionalPropertyValue" %>
<%@ page import="datastore.AdditionalPropertyValueManager" %>
<%@ page import="datastore.MenuItem" %>
<%@ page import="datastore.MenuItemManager" %>
<%@ page import="datastore.MenuItemAdditionalPropertyValue" %>
<%@ page import="datastore.MenuItemAdditionalPropertyValueManager" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
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

  String error = request.getParameter("etype");
  String message = request.getParameter("msg");
  String action = request.getParameter("action");
  
  Key menuItemAdditionalPropertyValueKey = KeyFactory.stringToKey(request.getParameter("miapv_k"));
  MenuItemAdditionalPropertyValue menuItemAdditionalPropertyValue = MenuItemAdditionalPropertyValueManager.getMenuItemAdditionalPropertyValue(menuItemAdditionalPropertyValueKey);
  
  List<AdditionalPropertyConstraint> additionalPropertyConstraints = AdditionalPropertyConstraintManager.getAdditionalPropertyConstraints(menuItemAdditionalPropertyValueKey);
  
  AdditionalPropertyValue additionalPropertyValue = AdditionalPropertyValueManager.getAdditionalPropertyValue(menuItemAdditionalPropertyValue.getAdditionalPropertyValue());
  AdditionalPropertyType additionalPropertyType = AdditionalPropertyTypeManager.getAdditionalPropertyType(additionalPropertyValue.getKey().getParent());
  
  MenuItem menuItem = MenuItemManager.getMenuItem(menuItemAdditionalPropertyValueKey.getParent());
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
      action="/manageRestaurant?action=update&type=C"
      class="form-style">

    <fieldset>
    
    <legend><%= menuItem.getMenuItemName() + " - " + additionalPropertyType.getAdditionalPropertyTypeName() + ": " + additionalPropertyValue.getAdditionalPropertyValueName() + " - " + printer.print("Constraints") %></legend>
	
	<% if (message != null && message.equals("success") && action != null && action.equals("update")) { %>
		<div class="success-div"><%= printer.print("Additional Property Constraints successfully updated") %>.</div>
	<% } %>
	
	<div class="warning-div">
		<hr/>
		<%= printer.print("By selecting one or more of the property values below, you will automatically constraint this property value to only allow combinations with the selected property values. If no property value constraint is selected in this screen, then the menu item can allow any combination of property values") %>.
		<hr/>
	</div>
	
	<br/>
	
	<div>
	  <h2><%= printer.print("Edit Property Constraints") %></h2>
	</div>
	
	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"<%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
    		
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">

		<tr>
		  	<td width="80%"><%= printer.print("Additional Properties") %></td>
		    <td width="10%" align="center"><%= printer.print("Selected") %></td>
		    <td width="10%"></td>
		</tr>  	  
		
		<tr>
		  	<td width="80%"></td>
		    <td width="10%"></td>
		    <td width="10%"></td>
		</tr>    		
		
		<% 
		int count = 0;
		for (AdditionalPropertyType type : typeList) {
			boolean includesType = false;
			ArrayList<MenuItemAdditionalPropertyValue> valueList = new ArrayList<MenuItemAdditionalPropertyValue>();
			for (MenuItemAdditionalPropertyValue value : menuItemAdditionalPropertyValues) {
				if (value.getAdditionalPropertyValue().getParent().equals(type.getKey())) {
					includesType = true;
					valueList.add(value);
				}
			}
			if (!includesType || type.equals(additionalPropertyType)) {
				continue;
			}
		%>
			<tr>	
				<td width="80%">
			        <div>
			        	<label for="apt_name"><span><b><i><u><%= type.getAdditionalPropertyTypeName() %></u></i></b></span></label>
						<div id="apt_name"></div>
					</div>
				</td>
				<td width="10%"></td>
				<td width="10%"></td>
			</tr>
			<% 
			for (MenuItemAdditionalPropertyValue miapv : valueList) {
			  	
			  	AdditionalPropertyValue value = AdditionalPropertyValueManager.getAdditionalPropertyValue(miapv.getAdditionalPropertyValue());
			  	String miapvKeyString = KeyFactory.keyToString(miapv.getKey());
			  	
			  	// Check if menu item additional property value constraint is selected for this property value
				boolean valueIsSelected = false;
				for (AdditionalPropertyConstraint apc : additionalPropertyConstraints) {
					MenuItemAdditionalPropertyValue miapv1 = MenuItemAdditionalPropertyValueManager.getMenuItemAdditionalPropertyValue(apc.getMenuItemAdditionalPropertyValue1());
					MenuItemAdditionalPropertyValue miapv2 = MenuItemAdditionalPropertyValueManager.getMenuItemAdditionalPropertyValue(apc.getMenuItemAdditionalPropertyValue2());
					if (miapv1.equals(miapv) || miapv2.equals(miapv)) {
						valueIsSelected = true;
						break;
					}
				}
			%>  
				<input type="text" name="miapv_k<%= count %>" value="<%= miapvKeyString %>" style="display:none;" />
			
				<tr>
				    <td width="80%">
				        <div>
				        	<label for="apv_name<%= count %>"><span>&nbsp;&nbsp;&nbsp;&nbsp;<%= value.getAdditionalPropertyValueName() %></span></label>
							<div id="apv_name<%= count %>"></div>
						</div>
					</td>
				
				    <td width="10%" align="center">
						<div>
							<input type="checkbox" name="apc_checked<%= count %>" <%= valueIsSelected ? "checked=\"checked\"" : "" %> />
							<div id="apc_checked<%= count %>"></div>
						</div>
					</td>
				
					<td width="10%"></td>
			  	</tr>
			<%  
			count++;
			}
		}
		%>		
	</table>	
		
	</fieldset>

	<br class="clearfloat" />
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/editProductItemProperty.jsp?readonly=true&mi_k=<%= KeyFactory.keyToString(menuItem.getKey()) %>'" class="button-close"/>
	<input type="submit" value="<%= printer.print("Update") %>" class="button_style" />
	
	<input type="text" name="miapv_k" value="<%= request.getParameter("miapv_k") %>" style="display:none;" />
	<input type="text" name="count" value="<%= count %>" style="display:none;" />

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
