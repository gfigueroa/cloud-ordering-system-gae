<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="util.DateManager" %>
<%@ page import="datastore.AdditionalPropertyValue" %>
<%@ page import="datastore.AdditionalPropertyValueManager" %>
<%@ page import="datastore.Branch" %>
<%@ page import="datastore.BranchManager" %>
<%@ page import="datastore.Customer" %>
<%@ page import="datastore.CustomerManager" %>
<%@ page import="datastore.Order" %>
<%@ page import="datastore.OrderDetail" %>
<%@ page import="datastore.OrderDetailMenuItemAdditionalProperty" %>
<%@ page import="datastore.OrderDetailSet" %>
<%@ page import="datastore.OrderManager" %>
<%@ page import="datastore.MenuItem" %>
<%@ page import="datastore.MenuItemManager" %>
<%@ page import="datastore.MenuItemAdditionalPropertyValue" %>
<%@ page import="datastore.MenuItemAdditionalPropertyValueManager" %>
<%@ page import="datastore.MenuItemType" %>
<%@ page import="datastore.MenuItemTypeManager" %>
<%@ page import="datastore.Set" %>
<%@ page import="datastore.SetManager" %>
<%@ page import="datastore.TypeSetMenuItem" %>
<%@ page import="datastore.TypeSetMenuItemManager" %>
<%@ page import="datastore.User" %>
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
    
  boolean readOnly = request.getParameter("readonly") != null ? Boolean.parseBoolean(request.getParameter("readonly")) : false;
  String error = request.getParameter("etype");
  String message = request.getParameter("msg");
  String action = request.getParameter("action");
  
  Long orderKey = Long.parseLong(request.getParameter("k"));
  Order order = OrderManager.getOrder(orderKey);
  readOnly = (readOnly || !order.isStillOpen());
  Customer customer = CustomerManager.getCustomer(order.getCustomer());
  Branch branch = BranchManager.getBranch(order.getBranch());
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
<link type="text/css" href="../stylesheets/colorbox.css" rel="stylesheet" />
<script src="../js/jquery.min.js"></script>
<script src="../js/jquery.colorbox-min.js"></script>
<script src="../js/popup.js"></script>
<script src="../js/print_page.js"></script>

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
  if(!isDouble(document.getElementsByName("d_fee")[0].value)){
    alert("<%=printer.print("The delivery fee you entered is not valid")%>.");
    return false;
  }
  return true;
}

</script>

<%@  include file="../header/page-title.html" %>
</head>

<body>

<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<br>

<div id="order_summary">	
<%
if (!order.getOrderDetails().isEmpty() || !order.getOrderAdditionalPropertyDetails().isEmpty() || !order.getOrderSetDetails().isEmpty()) {
%>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">
	  
		  <tr>
		  	<td width="20%">
		  		<h2> <%= printer.print("Order Detail") %> </h2>
		  	</td>
		  	<td width="10%"></td>
		  	<td width="30%"></td>
		  	<td width="10%"></td>
		  	<td width="10%"></td>
		  	<td width="10%"></td>
		  	<td width="10%"></td>
		  </tr>
		  
		  <tr>
		  	<td width="20%"></td>
		  	<td width="10%"></td>
		  	<td width="30%"></td>
		    <td width="10%"></td>
		    <td width="10%"></td>
		    <td width="10%"></td>
		    <td width="10%"></td>
		  </tr>
		  
		  <tr>
		  	<td width="20%"><%= printer.print("Product Item Name") %></td>
		  	<td width="10%" align="left"><b><%= printer.print("Product Type") %></b></td>
		  	<td width="30%" align="left"><b><%= printer.print("Additional Properties") + "/" + printer.print("Product Items") %></b></td>
		    <td width="10%" align="right"><b><%= printer.print("Quantity") %></b></td>
		    <td width="10%" align="right"><b><%= printer.print("Unit Price") %></b></td>
		    <td width="10%" align="right"><b><%= printer.print("Discount") %> (%)</b></td>
		    <td width="10%" align="right"><b><%= printer.print("Subtotal") %></b></td>
		  </tr>
		  
		  <tr>
		  	<td width="20%"></td>
		    <td width="10%"></td>
		    <td width="30%"></td>
		    <td width="10%"></td>
		    <td width="10%"></td>
		    <td width="10%"></td>
		    <td width="10%"></td>
		  </tr>
		  
		  <%
		  for (OrderDetail detail : order.getOrderDetails()) {
		  	  MenuItem menuItem = MenuItemManager.getMenuItem(detail.getMenuItem());
		  	  
		  	  if (menuItem == null) {
		  	  	break;
		  	  }
		  	  
		  	  MenuItemType menuItemType = MenuItemTypeManager.getMenuItemType(menuItem.getMenuItemType());
		  	  double discount = menuItem.getMenuItemDiscount() != null ? menuItem.getMenuItemDiscount() : 0;
		  	  int quantity = detail.getOrderDetailQuantity();
		  %>
		  
			  <tr>
			    <td width="20%"><a id="<%= KeyFactory.keyToString(menuItem.getKey()) %>" href="editMenuItem.jsp?k=<%= KeyFactory.keyToString(menuItem.getKey()) + "&readonly=true&returnto=editOrder&o_key=" + request.getParameter("k") %>"><%= menuItem.getMenuItemName() %></a></td>
			    <td width="10%" align="left"><%= menuItemType.getMenuItemTypeName() %></td>
			    <td width="30%" align="left"></td>
			    <td width="10%" align="right"><%= quantity %></td>
			    <td width="10%" align="right"><%= menuItem.getMenuItemPrice() %></td>
			    <td width="10%" align="right"><%= String.valueOf(discount * 100) + "%" %></td>
			    <td width="10%" align="right"><%= (menuItem.getMenuItemPrice() - (menuItem.getMenuItemPrice() * discount)) * quantity %></td>
			  </tr>
		  
		  <%
		  }
		  %>
		  
		  <%
		  for (OrderDetailMenuItemAdditionalProperty detail : order.getOrderAdditionalPropertyDetails()) {
		  	  ArrayList<MenuItemAdditionalPropertyValue> menuItemAdditionalPropertyValues = new ArrayList<MenuItemAdditionalPropertyValue>();
		  	  for (Key key : detail.getMenuItemAdditionalPropertyValues()) {
		  	  	MenuItemAdditionalPropertyValue miapv = MenuItemAdditionalPropertyValueManager.getMenuItemAdditionalPropertyValue(key);
		  	  	menuItemAdditionalPropertyValues.add(miapv);
		  	  }
		  	  
		  	  MenuItem menuItem = MenuItemManager.getMenuItem(menuItemAdditionalPropertyValues.get(0).getKey().getParent());
		  	  
		  	  if (menuItem == null) {
		  	  	break;
		  	  }
		  	  
		  	  MenuItemType menuItemType = MenuItemTypeManager.getMenuItemType(menuItem.getMenuItemType());
		  	  double discount = menuItem.getMenuItemDiscount() != null ? menuItem.getMenuItemDiscount() : 0;
		  	  
		  	  int quantity = detail.getOrderDetailMenuItemAdditionalPropertyQuantity();
		  	  double totalAdditionalCharge = 0;
		  %>
		  
			  <tr>
			    <td width="20%"><a id="<%= KeyFactory.keyToString(menuItem.getKey()) %>" href="editMenuItem.jsp?k=<%= KeyFactory.keyToString(menuItem.getKey()) + "&readonly=true&returnto=editOrder&o_key=" + request.getParameter("k") %>"><%= menuItem.getMenuItemName() %></a></td>
			    <td width="10%" align="left"><%= menuItemType.getMenuItemTypeName() %></td>
			    <td width="30%" align="left">
			    <%
			    for (MenuItemAdditionalPropertyValue miapv : menuItemAdditionalPropertyValues) {
			    	AdditionalPropertyValue value = AdditionalPropertyValueManager.getAdditionalPropertyValue(miapv.getAdditionalPropertyValue());
			    	double additionalCharge = miapv.getAdditionalCharge() != null ? miapv.getAdditionalCharge() : 0;
			    	totalAdditionalCharge += additionalCharge;
			    %>
			    	<%= value.getAdditionalPropertyValueName() + (additionalCharge > 0 ? " (+" + additionalCharge + ")" : "") %>
			    	<br/>
			    <%
			    }
			    %>
			    </td>
			    <td width="10%" align="right"><%= quantity %></td>
			    <td width="10%" align="right"><%= menuItem.getMenuItemPrice() + totalAdditionalCharge %></td>
			    <td width="10%" align="right"><%= String.valueOf(discount * 100) + "%" %></td>
			    <td width="10%" align="right"><%= ((menuItem.getMenuItemPrice() - (menuItem.getMenuItemPrice() * discount)) * quantity) + (totalAdditionalCharge * quantity) %></td>
			  </tr>
		  
		  <%
		  }
		  %>
		  
		  <%
		  for (OrderDetailSet detail : order.getOrderSetDetails()) {
		  	  Set set = SetManager.getSet(detail.getSet());
		  	  
		  	  if (set == null) {
		  	  	break;
		  	  }
		  	  
		  	  if (set.hasFixedPrice()) {
		  	  
			  	  double discount = set.getSetDiscount() != null ? set.getSetDiscount() : 0;
			  	  int quantity = detail.getOrderDetailSetQuantity();
			  	  
			  	  if (set.getSetType() == Set.SetType.FIXED_SET) {
		  %>
		  
					  <tr>
					    <td width="20%"><a id="<%= KeyFactory.keyToString(set.getKey()) %>" href="editSets.jsp?k=<%= KeyFactory.keyToString(set.getKey()) + "&readonly=true&returnto=editOrder&o_key=" + request.getParameter("k") %>"><%= set.getSetName() %></a></td>
					    <td width="10%" align="left"><%= set.getSetTypeString() %></td>
					    <td width="30%" align="left"></td>
					    <td width="10%" align="right"><%= quantity %></td>
					    <td width="10%" align="right"><%= set.getSetPrice() %></td>
					    <td width="10%" align="right"><%= String.valueOf(discount * 100) + "%" %></td>
					    <td width="10%" align="right"><%= (set.getSetPrice() - (set.getSetPrice() * discount)) * quantity %></td>
					  </tr>
		  
		  <%
		  		  }
		  		  else if (set.getSetType() == Set.SetType.TYPE_SET) {
		  %>
		  			  <tr>
					    <td width="20%"><a id="<%= KeyFactory.keyToString(set.getKey()) %>" href="editSets.jsp?k=<%= KeyFactory.keyToString(set.getKey()) + "&readonly=true&returnto=editOrder&o_key=" + request.getParameter("k") %>"><%= set.getSetName() %></a></td>
					    <td width="10%" align="left"><%= set.getSetTypeString() %></td>
					    <td width="30%" align="left">
					    <%
					    for (Key key : detail.getTypeSetMenuItems()) {
					    	TypeSetMenuItem typeSetMenuItem = TypeSetMenuItemManager.getTypeSetMenuItem(key);
					    	MenuItem menuItem = MenuItemManager.getMenuItem(typeSetMenuItem.getMenuItem());
					    %>
					    	<%= menuItem.getMenuItemName() %>
					    	<br/>
					    <%
					    }
					    %>
					    </td>
					    <td width="10%" align="right"><%= quantity %></td>
					    <td width="10%" align="right"><%= set.getSetPrice() %></td>
					    <td width="10%" align="right"><%= String.valueOf(discount * 100) + "%" %></td>
					    <td width="10%" align="right"><%= (set.getSetPrice() - (set.getSetPrice() * discount)) * quantity %></td>
					  </tr>
		  <%		  
		  		  }
		  	  }
		  }
		  %>
		  
		  <tr >
		    <td width="20%" align="right"><strong><%= printer.print("Total") %></strong></td>
		    <td width="10%"></td>
		    <td width="30%"></td>
		    <td width="10%" align="right"><strong><%= order.getOrderTotalItems() %></strong></td>
		    <td width="10%"></td>
		    <td width="10%"></td>
		    <td width="10%" align="right"><strong><%= order.getOrderTotal() != null ? order.getOrderTotal() : 0 %></strong></td>
		  </tr>
		  
		</table>
<%
}
%>
</div>

<br>

<form method="post" id="form1" name="form1" class="form-style"
		onSubmit="return checkNumericValues();" 
		action="/manageRestaurant?action=update&type=O&k=<%= orderKey %>">

    <fieldset>
	<legend><%= readOnly? printer.print("View Order") : printer.print("Process Order") %></legend>
	
	<% if (message != null && message.equals("success") && action != null && action.equals("update")) { %>
		<div class="success-div"><%= printer.print("Order successfully updated") %>.</div>
	<% } %>
	
	<div>
		<h2><%= printer.print("Order information") %></h2>
	</div>
 
	<% if (error != null && error.equals("Format")) { %>
		<div class="error-div"> printer.print("The phone you provided does not conform to the standard formats (you can try something like (123)456-7890)")</div>
	<% } %>
	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div">printer.print("You are missing some essential information needed by the system")</div>
	<% } %>
    
    <div>
    	<label  for="o_key"><span><%= printer.print("Order Number") %></span></label>
		<input type="text" name="o_key" class="input_extra_large" value="<%= order.getKey() %>"
			title="" readonly="readonly" /><br />
		<div id="o_key"></div>
	</div>
    
    <div>
    	<label  for="b_name"><span><%= printer.print("Branch Name") %></span></label>
		<input type="text" name="b_name" class="input_extra_large" value="<%= branch != null ? branch.getBranchName() : "" %>"
			title="" readonly="readonly" /><br />
		<div id="b_name"></div>
	</div>
    
    <div>
    	<label  for="c_name"><span><%= printer.print("Customer Name") %></span></label>
		<input type="text" name="c_name" class="input_extra_large" value="<%= customer != null ? customer.getCustomerName() : "Inexistent customer" %>"
			title="" readonly="readonly" /><br />
		<div id="c_name"></div>
	</div>
	
	<div>
    	<label  for="c_email"><span><%= printer.print("Customer E-mail") %></span></label>
		<input type="text" name="c_email" class="input_extra_large" value="<%= customer != null ? customer.getUser().getUserEmail().getEmail() : "Inexistent customer" %>"
			title="" readonly="readonly" /><br />
		<div id="c_email"></div>
	</div>
	
	<div>
    	<label  for="o_time"><span><%= printer.print("Order Time") %></span></label>
		<input type="text" name="o_time" class="input_large" value="<%= DateManager.printDateAsString(order.getOrderTime()) %>"
			title="" readonly="readonly" /><br />
		<div id="o_time"></div>
	</div>
	
	<%
	if (!readOnly && order.isStillOpen() && order.getOrderStatus() != Order.Status.CANCELLED) {
	%>
	<div>
		<label for="o_change"><span><%= printer.print("Propose Time Change") %> <span class="required_field">*</span></span></label>
		<input id="o_change" type="text" name="o_change" class="input_large" value="" readonly="readonly" />
		<a href="javascript:NewCal('o_change','mmddyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date and time"></a>
		<br />
		<div id="o_change"></div>
	</div>
	<%
	}
	%>
	
	<div>
    	<label  for="o_phone"><span><%= printer.print("Order Contact Phone") %></span></label>
		<input type="text" name="o_phone" class="input_extra_large" value="<%= order.getOrderContactPhone().getNumber() %>"
			title="" readonly="readonly" /><br />
		<div id="o_phone"></div>
	</div>
	
	<div>
    	<label  for="o_type"><span><%= printer.print("Order Type") %></span></label>
		<input type="text" name="o_type" class="input_extra_large" value="<%= printer.print(order.getOrderTypeString()) %>"
			title="" readonly="readonly" /><br />
		<div id="o_type"></div>
	</div>
	
	<div>
    	<label  for="o_p_type"><span><%= printer.print("Order Payment Type") %></span></label>
		<input type="text" name="o_p_type" class="input_extra_large" value="<%= printer.print(order.getOrderPaymentTypeString()) %>"
			title="" readonly="readonly" /><br />
		<div id="o_p_type"></div>
	</div>
	
	<div>
    	<label  for="s_time"><span><%= printer.print("Time to Serve") %></span></label>
		<input type="text" name="s_time" class="input_extra_large" value="<%= DateManager.printDateAsString(order.getTimeToServe()) %>"
			title="" readonly="readonly" /><br />
		<div id="s_time"></div>
	</div>
	
	<%
	if (order.getOrderType() == Order.OrderType.DELIVERY) {
	%>
		<div>
	    	<label  for="d_type"><span><%= printer.print("Delivery Type") %></span></label>
			<input type="text" name="d_type" class="input_extra_large" value="<%= order.getOrderDeliveryTypeString() %>"
				title="" readonly="readonly" /><br />
			<div id="d_type"></div>
		</div>
		
		<div>
	    	<label  for="d_address"><span><%= printer.print("Delivery Address") %></span></label>
			<input type="text" name="d_address" class="input_extra_large" value="<%= order.getOrderDeliveryAddress().getAddress() %>"
				title="" readonly="readonly" /><br />
			<div id="d_address"></div>
		</div>
		
		<div>
	    	<label  for="d_fee"><span><%= printer.print("Delivery Fee") %></span></label>
			<input type="text" name="d_fee" class="input_extra_large" value="<%= order.getOrderDeliveryFee() != null ? order.getOrderDeliveryFee() : 0 %>" title="" <%= readOnly? "readonly=\"readonly\"" : "" %> /><br />
			<div id="d_fee"></div>
		</div>
	<%
	}
	%>
	
	<%
	if (order.getOrderType() == Order.OrderType.TAKE_IN) {
	%>
		<div>
	    	<label  for="n_people"><span><%= printer.print("Number of People") %></span></label>
			<input type="text" name="n_people" class="input_extra_large" value="<%= order.getNumberOfPeople() %>"
				title="" readonly="readonly" /><br />
			<div id="n_people"></div>
		</div>
	<%
	}
	%>
	
	<div>
		<label for="o_status"><span><%= printer.print("Order Status") %> <span class="required_field">*</span></span></label>
				
        <select name="o_status" <%= readOnly? "disabled=\"true\"" : "" %>>	
        	<%
        	if (order.getOrderStatus() == Order.Status.CHANGE_REQUEST) {
        	%>
        		<option value="change_request" <%= order.getOrderStatus() == Order.Status.CHANGE_REQUEST ? "selected=\"true\"" : "" %>>
	        		<%= printer.print("Change Request") %>
	        	</option>
        	<%
        	}
        	%>
        	<%
        	if (order.getOrderStatus() != Order.Status.CANCELLED && order.getOrderStatus() != Order.Status.CLOSED_CANCELLED) {
        	%>
	        	<option value="unprocessed" <%= order.getOrderStatus() == Order.Status.UNPROCESSED ? "selected=\"true\"" : "" %>>
	        		<%= printer.print("Unprocessed") %>
	        	</option>
	        	<option value="processing" <%= order.getOrderStatus() == Order.Status.PROCESSING ? "selected=\"true\"" : "" %>>
	        		<%= printer.print("Processing") %>
	        	</option>
	        	<option value="accepted" <%= order.getOrderStatus() == Order.Status.ACCEPTED ? "selected=\"true\"" : "" %>>
	        		<%= printer.print("Accepted") %>
	        	</option>
	        	<option value="ready" <%= order.getOrderStatus() == Order.Status.READY ? "selected=\"true\"" : "" %>>
	        		<%= printer.print("Ready") %>
	        	</option>
	        	<option value="en_route" <%= order.getOrderStatus() == Order.Status.EN_ROUTE ? "selected=\"true\"" : "" %>>
	        		<%= printer.print("En-Route") %>
	        	</option>
	        	<option value="unclaimed" <%= order.getOrderStatus() == Order.Status.UNCLAIMED ? "selected=\"true\"" : "" %>>
	        		<%= printer.print("Unclaimed") %>
	        	</option>
	        	<option value="closed" <%= order.getOrderStatus() == Order.Status.CLOSED ? "selected=\"true\"" : "" %>>
	        		<%= printer.print("Closed") %>
	        	</option>
	        	<option value="revoked" <%= order.getOrderStatus() == Order.Status.REVOKED ? "selected=\"true\"" : "" %>>
	        		<%= printer.print("Revoked") %>
	        	</option>
        	<%
        	}
        	else {
        	%>
	        	<option value="cancelled" <%= order.getOrderStatus() == Order.Status.CANCELLED ? "selected=\"true\"" : "" %>>
	        		<%= printer.print("Cancelled") %>
	        	</option>
	        	<option value="closed_cancelled" <%= order.getOrderStatus() == Order.Status.CLOSED_CANCELLED ? "selected=\"true\"" : "" %>>
	        		<%= printer.print("Close and Cancel") %>
	        	</option>
	        <%
	        }
	        %>
        </select>
        <%
        if (order.getOrderStatus() == Order.Status.CANCELLED) {
        %>
        	<%= printer.print("Close cancelled order?") %>
        <%
        }
        %>
		<div id="o_status"></div>
	</div>
	
	<%
	if (order.getOrderStatus() == Order.Status.CANCELLED || order.getOrderStatus() == Order.Status.CLOSED_CANCELLED) {
	%>
		<div>
	       	<label for="c_comments"><span><%= printer.print("Cancellation Comments") %></span></label>
			<textarea name="c_comments" class="input_extra_large" readonly="readonly"><%= order.getCancellationComments() != null ? order.getCancellationComments() : "" %></textarea><br/>
			<div id="c_comments"></div>
		</div>
	<%
	}
	%>
	
	<div>
       	<label for="o_comments"><span><%= printer.print("Comments") %></span></label>
		<textarea name="o_comments" class="input_extra_large" <%= readOnly? "readonly=\"readonly\"" : "" %>><%= order.getOrderComments() != null ? order.getOrderComments() : "" %></textarea><br />
		<div id="o_comments"></div>
	</div>
    
	</fieldset>
	
	<br class="clearfloat" />
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/listOrder.jsp?k=<%= request.getParameter("k") %>'" class="button-close"  />

	<%
	if (readOnly && order.isStillOpen()) {
	%>
		<input type="button" value="<%= printer.print("Edit") %>" onClick="location.href='/restaurant/editOrder.jsp?k=<%= request.getParameter("k") %>'" class="button_style">

	<%
	}
	%>

	<%
	if (!readOnly) {
	%>
		<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>
	<%
	}
	%>
	
</form>

<button onClick="printPage('order_summary','form1');return false;" class="button-print"> <%= printer.print("Print")%></button>
<br/>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>