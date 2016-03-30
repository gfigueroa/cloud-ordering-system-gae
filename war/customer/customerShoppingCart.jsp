<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.Branch" %>
<%@ page import="datastore.BranchManager" %>
<%@ page import="datastore.Customer" %>
<%@ page import="datastore.CustomerManager" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.MenuItem" %>
<%@ page import="datastore.MenuItemManager" %>
<%@ page import="datastore.Order" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="session.CartManager" %>
<%@ page import="session.MenuItemContainer" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>

<%
  User sessionUser = (User) session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("/login.jsp");
    
  String restaurantKey = request.getParameter("r_key");
  Restaurant restaurant = RestaurantManager.getRestaurant(KeyFactory.stringToKey(restaurantKey));
  
  List<Branch> branches = BranchManager.getRestaurantBranches(restaurant.getKey());
  
  String error = request.getParameter("etype");
  
  Customer customer = CustomerManager.getCustomer(sessionUser);
  List<MenuItemContainer> menuItems = CartManager.getCart(session, restaurant.getKey());
  
  Double total = new Double(0.0);
  
  String branchKeyString = request.getParameter("b_key");
  Branch branch;
  if (branchKeyString != null && !branchKeyString.equals("null")) {
  	branch = BranchManager.getBranch(KeyFactory.stringToKey(branchKeyString));
  }
  else {
  	branch = branches.get(0);
  }
  
  String orderTypeString = request.getParameter("o_type");
  Order.OrderType orderType;
  if (orderTypeString != null && !orderTypeString.equals("null")) {
  	if (branch.hasOrderType(Order.getOrderTypeFromString(orderTypeString))) {
  		orderType = Order.getOrderTypeFromString(orderTypeString);
  	}
  	else {
  		orderType = branch.getFirstAvailableOrderType();
  	}
  }
  else {
  	orderType = branch.getFirstAvailableOrderType();
  }

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />
<link type="text/css" href="../stylesheets/page-style.css" rel="stylesheet" />
<link type="text/css" href="../stylesheets/colorbox.css" rel="stylesheet" />

<script src="../js/jquery.min.js"></script>
<script src="../js/jquery.colorbox-min.js"></script>
<script src="../js/popup.js"></script>
<script src="../Scripts/swfobject_modified.js" type="text/javascript"></script>

<script src="../js/datetimepicker.js" language="javascript" type="text/javascript"></script>

<script language="Javascript" type="text/javascript">

function isInteger(sText) {
   var ValidChars = "123456789";
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

function checkNumericValues() {
  if(!isInteger(document.getElementsByName("n_people")[0].value)){
    alert("The number of people you entered is not valid.");
    return false;
  }
  return true;
}

</script>

<script language="Javascript" type="text/javascript">
function refresh(o_type, r_key, b_key) {
    window.location.replace("customerShoppingCart.jsp?o_type=" + o_type + "&r_key=" + r_key + "&b_key=" + b_key);
}
</script>

<script language="Javascript" type="text/javascript">
function confirmDelete() {
	if (confirm("Are you sure you want to delete this Menu Item from the cart?")) {
		return true;
	}
	return false;
}
</script>

<script language="Javascript" type="text/javascript">
function confirmCheckout() {
	if (confirm("Are you sure you want to buy these items?")) {
		return true;
	}
	return false;
}
</script>

<%@  include file="../header/page-title.html" %>
<meta name="viewport" content="target-densitydpi=device-dpi, width=device-width" />
</head>

<body>
<%@  include file="../header/logout-bar.html" %>
<%@  include file="../header/page-banner.html" %>
<%@  include file="../menu/customer-menu.html" %>

<div class="cart-container">

	<form method="post" id="form1" name="form1"
		action="/addToCart?v=cart&action=buy"
		onSubmit="return confirmCheckout(); return checkNumericValues();"
    	class="form-style">

	    <fieldset>
		<legend>Order to <%= restaurant.getRestaurantName() %></legend>
		
		<div>
			<h2>Order Information</h2>
		</div>
		
		<% if (error != null && error.equals("MissingInfo")) { %>
			<div class="error-div">You are missing some essential information needed by the system</div>
		<% } %>
		<% if (error != null && error.equals("Format")) { %>
			<div class="error-div">The phone you provided does not conform to the standard formats (you can try something like 0975384927)</div>
		<% } %>
		
		<input type="text" name="r_key" value="<%= request.getParameter("r_key") %>" style="display:none;" />
		
		<div>
			<label for="b_key"><span>Branch <span class="required_field">*</span></span></label>
			<select name="b_key" onChange="refresh('<%= request.getParameter("o_type") %>', '<%= request.getParameter("r_key") %>', this.value);">
			<% for (Branch b : branches) { %>
	        	<option value="<%= KeyFactory.keyToString(b.getKey()) %>" <%= branch.equals(b) ? "selected=\"true\"" : "" %>><%= b.getBranchName() %></option>
			<% } %>
	        </select>
	        <a href="customerListBranch.jsp?r_key=<%= request.getParameter("r_key") %>" target="_blank">View Branches</a>
			<div id="b_key"></div>
		</div>
		
		<div>
			<label for="o_type"><span>Order Type <span class="required_field">*</span></span></label>
			<select name="o_type" onChange="refresh(this.value, '<%= request.getParameter("r_key") %>', '<%= request.getParameter("b_key") %>');">
	        <% if (branch.hasDelivery()) { %>
	        	<option value="delivery" <%= orderType == Order.OrderType.DELIVERY ? "selected=\"true\"" : "" %>>Delivery</option>
	        <% } %>
	        <% if (branch.hasTakeOut()) { %>
	        	<option value="takeout" <%= orderType == Order.OrderType.TAKE_OUT ? "selected=\"true\"" : "" %>>Take-Out</option>
	        <% } %>
	        <% if (branch.hasTakeIn()) { %>
	        	<option value="takein" <%= orderType == Order.OrderType.TAKE_IN ? "selected=\"true\"" : "" %>>Dine-In</option>
	        <% } %>
	        </select>
			<div id="o_type"></div>
		</div>
		
	    <div>
	       	<label  for="c_name"><span>Customer Name</span></label>
			<input type="text" name="c_name" class="input_large" value="<%= customer.getCustomerName() %>" title="" disabled="true" /><br />
			<div id="c_name"></div>
		</div>
		
		<div>
			<label for="o_phone"><span>Contact Phone <span class="required_field">*</span></span></label>
			<input type="text" name="o_phone" class="input_large"  value="<%= customer.getCustomerPhone().getNumber() %>" /><br />
			<div id="o_phone"></div>
		</div>
		
		<%
		if (orderType == Order.OrderType.DELIVERY) {
		%>
		
			<div>
	       		<label for="d_address1"><span>Delivery Address <span class="required_field">*</span></span></label>
				<input type="text" name="d_address1" class="input_extra_large" value="<%= customer.getCustomerAddress().getAddress() %>" /><br />
				<div id="d_address1"></div>
			</div>
			
	    	<div>
	       		<label for="d_address2"><span></span></label>
				<input type="text" name="d_address2" class="input_extra_large" value="" /><br />
				<div id="d_address2"></div>
			</div>
		
		<%
		}
		%>
		
		<div>
			<label for="s_time"><span>Date/Time to Serve <span class="required_field">*</span></span></label>
			<input id="time" type="text" name="s_time" class="input_large" value="" readonly="readonly" />
			<a href="javascript:NewCal('time','mmddyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date and time"></a>
			<br />
			<div id="s_time"></div>
		</div>
		
		<%
		if (orderType == Order.OrderType.TAKE_IN) {
		%>
		
			<div>
	       		<label for="n_people"><span>Number of people <span class="required_field">*</span></span></label>
				<input type="text" name="n_people" class="input_large" value="1" /><br />
				<div id="n_people"></div>
			</div>
		
		<%
		}
		%>
		
		<div>
       		<label for="o_comments"><span>Comments</span></label>
			<textarea  name="o_comments" class="input_extra_large" value=""></textarea><br />
			<div id="o_comments"></div>
		</div>

		</fieldset>
	
		<br class="clearfloat" />
	
		<%
		if (menuItems != null && !menuItems.isEmpty()) { 
		%>
	    	<div class="checkout-final">
	    	<input type="image" src="../images/checkout.png" value="Buy" name="Buy">
	    	</div>
	    <% 
	    } 
	    %>
	
	</form>
	
	<br/>
	
  	<h2>Shopping Cart</h2>
	<div class="item-list">
  
  	<div class="cart-item">
    
    <table>
    
      <tr class="cart-item-head">
      <% 
      if (menuItems != null && !menuItems.isEmpty()) { 
      %>
        
        <th colspan="2" class="c1 txtsmall txtleft drkgry">            
          <span class="first-head-column">Description</span>
        </th>
        <th class="c3 txtsmall">Restaurant</th>
        <th class="c4 txtsmall">Quantity</th>
        <th class="c4 txtsmall">Price</th>
      <% 
      } 
      else { 
      %>
      	<td class="no-items">There are no items in your cart.</td>
      <% 
      } 
      %> 
      </tr>
      
      <%
      if (menuItems != null && !menuItems.isEmpty()) {
      	
      	for (MenuItemContainer menuItemContainer : menuItems) {
        	MenuItem menuItem = menuItemContainer.menuItem;
        	double discount = menuItem.getMenuItemDiscount() != null ?
        			menuItem.getMenuItemDiscount() * menuItem.getMenuItemPrice() :
        			0;
        	double price = (menuItem.getMenuItemPrice() - discount) * menuItemContainer.qty;
            total += price;
      %>
      <tr>
        <td class="c1 txtleft" valign="top">
	    <%
	      if (menuItem.getMenuItemImage() != null) {
		 %>
	          <a target="_new" href="/img?blobkey=<%= menuItem.getMenuItemImage().getKeyString() %>">
	          <img src="/img?blobkey=<%= menuItem.getMenuItemImage().getKeyString() %>&s=100" />
	          </a>
		<%
		  } 
		  else {
		%>
        	  <img src="../images/default-image.png" width="100" border="0" />
        <% } %>
        </td>
        
        <td class="c2 txtleft txtnormal" valign="top">
          <p class="title"> 
            <a id="listdetail_<%= KeyFactory.keyToString(menuItem.getKey()) %>" 
            		href="customerMenuItemDetail.jsp?k=<%= KeyFactory.keyToString(menuItem.getKey()) %>">
            	<%= menuItem.getMenuItemName() %>
            </a>
          </p>
          <p class="condition"><p>

          <p class="buttons">
            <form method="post" action="/addToCart?v=cart&action=remove&k=<%= KeyFactory.keyToString(menuItem.getKey()) %>" 
            	onSubmit="return confirmDelete();" >
            <input type="submit" value="Delete" name="submit_delete">
            </form>    
          </p>
		</td>

        <td class="c3"  valign="top">
			<div class="item-details">
	            <p>
	            <%= restaurant.getRestaurantName() %>
	            </p>
			</div>
        </td>
        
        <td  class="c3" valign="top">
        	<div class="item-details">
	            <p class="quantity">
	            <input name="quantity" type="text" size="3" maxlength="3" disabled="true" class="txtctr" 
	            		value="<%= menuItemContainer.qty %>"  />
	            <br/><br/>
	            <a href="/addToCart?v=cart&action=append&k=<%= KeyFactory.keyToString(menuItem.getKey()) %>"><img src="../images/up.png"/>ADD</a> |
	            <a href="/addToCart?v=cart&action=subtract&k=<%= KeyFactory.keyToString(menuItem.getKey()) %>"><img src="../images/down.png"/>SUB</a>
	          	</p>
        	</div>
        </td>

        <td class="c3"  valign="top">
			<div class="item-details">
          		<p class="ourprice"><%= price %></p>
			</div>
        </td>
      </tr>
      <%
            }
        }
      %>
      
    </table>
  </div>

  </div>

  <div id="cart-subtotal" class="subtotal txtlarge txtright">
	    Total:
	    <span ><%= total %></span>
  </div>

  <div class="cart-item-pagination" page="0">
  </div>

</div>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
