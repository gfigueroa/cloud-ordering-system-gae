<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script src="../../js/datetimepicker.js" language="javascript" type="text/javascript"></script>
<script src="../../Scripts/swfobject_modified.js" type="text/javascript"></script>
<link type="text/css" rel="stylesheet" href="../css/car_check.css" />
<link type="text/css" rel="stylesheet" href="../css/deafult.css" />
<link rel="stylesheet" type="text/css" href="../css/header_footer.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>æ™ºæ…§äººç§‘æŠ€-é»žé¤�å�³æ™‚äº«-è³‡æ–™ç¢ºèª�</title>
</head>
<%
	User sessionUser = (User) session.getAttribute("user");
	if (sessionUser == null)
    	response.sendRedirect("/webbrowser/restaurant/member/login.jsp");
	
	if(request.getParameter("msg") != null){
		if (request.getParameter("msg").equals("success")&& request.getParameter("action").equals("buy")){
			response.getWriter().print("<script type='text/javascript'>alert('è³‡æ–™é€�å‡ºæˆ�åŠŸã€‚')</script>");		
		}
	}
	
	String restaurantKey = request.getParameter("r_key");
	Restaurant restaurant = RestaurantManager.getRestaurant(KeyFactory.stringToKey(restaurantKey));
   
	List<Branch> branches = BranchManager.getRestaurantBranches(restaurant.getKey());
	
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
<script type="text/javascript">
function confirmDelete() {
	if (confirm("Are you sure you want to delete this Menu Item from the cart?")) {
		return true;
	}
	return false;
}

function checkNumericValues() {
	  if(!isInteger(document.getElementsByName("n_people")[0].value)){
	    alert("The number of people you entered is not valid.");
	    return false;
	  }
	  return true;
	}

function confirmCheckout() {
	if (confirm("Are you sure you want to buy these items?")) {
		return true;
	}
	return false;
}

function refresh(o_type, r_key, b_key) {
    window.location.replace("car_check.jsp?o_type=" + o_type + "&r_key=" + r_key + "&b_key=" + b_key);
}
</script>

<body class="body">
 <form method="post" id="form1" name="form1"
		action="/addToCar_test?v=cart&action=buy"
		onSubmit="return confirmCheckout(); return checkNumericValues()">
<div class="body_contant">
<div style="height:150px;"><jsp:include page="../header/header.jsp" flush="true" /></div>
<div class="car_title" style="overflow:hidden;" >
	<div style="width:210px; height:50px; float:left;"><h2><%= restaurant.getRestaurantName() %></h2></div>
    <a href="#"><div style="width:150px; height:50px; float:left;"></div></a>
    <div style="width:50px; height:50px; float:left; padding-left:200px; vertical-align:text-bottom;"><p>
    <select name="b_key" onChange="refresh('<%= request.getParameter("o_type") %>', 
    '<%= request.getParameter("r_key") %>', this.value);">
			<% for (Branch b : branches) { %>
	        	<option value="<%= KeyFactory.keyToString(b.getKey()) %>" <%= branch.equals(b) ? "selected=\"true\"" : "" %>>
				<%= b.getBranchName() %></option>
			<% } %>
	</select></p></div>
</div>
<div style="width:940px; padding:20px 0 20px 42px;">
<table class="menu_list_table" cellpadding="0" cellspacing="0" width="830px"  bgcolor="#FFFFFF">
    	<tr bgcolor="#F1E6D2" height="20px">
        	<td width="10px"></td>
            <td><h1>é¤�é»žå��ç¨±</h1></td>
       	  <td><h1></h1></td>
       	  <td><h1>åƒ¹æ ¼</h1></td>
       	  <td><h1>é»žé¤�æ•¸é‡�</h1></td>
       	  <td><h1>å°�è¨ˆ</h1></td>
   	  <td><h1>åˆªé™¤</h1></td></tr>
   	  
<% 
		double Total= 0;
		int Total_qty =0;
   		for(MenuItemContainer menuItemContainer : menuItems){
   			double price = (menuItemContainer.menuItem.getMenuItemPrice())*(menuItemContainer.qty);
   			Total = price + Total;
			Total_qty = menuItemContainer.qty + Total_qty;
%>
		   	<tr height="10px"><td colspan="7" class="car_list"></td></tr>
        	<tr >
        	<td></td>
        	<td><h4><%= menuItemContainer.menuItem.getMenuItemName() %></h4></td>
        	<td><h4></h4></td>
        	<td width="100px"><h4>$<%= menuItemContainer.menuItem.getMenuItemPrice() %></h4></td>
        	<td width="150px"><h4>
        	<a href="/addToCar_test?v=cart&action=append&k=<%= KeyFactory.keyToString(menuItemContainer.menuItem.getKey()) %>"><img src="img/+.png" width="20" height="20"  border="0"/></a>
            <input name="quantity" type="text" size="2" maxlength="2" value="<%= menuItemContainer.qty %>" />
            <a href="/addToCar_test?v=cart&action=subtract&k=<%= KeyFactory.keyToString(menuItemContainer.menuItem.getKey()) %>"><img src="img/-.png" width="20" height="20" border="0"/></a>
        	
        	
        	</h4></td>
        	<td width="100px"><h4>$<%= price %></h4></td>
        	<td width="50px"><h4>
        	<a href="/addToCar_test?v=cart&action=remove&k=<%= KeyFactory.keyToString(menuItemContainer.menuItem.getKey()) %>" onclick="return confirmDelete();">
        	åˆªé™¤</a></h4></td>
        	</tr>
        	<tr height="30px"><td></td><td colspan="6" valign="top"><h5><%= menuItemContainer.menuItem.getMenuItemDescription() %></h5></td></tr>
        	<tr >
<%   			
   		}
%>
        <tr height="10px"><td colspan="7" class="car_list"></td></tr>
      <tr height="50px">
        <td colspan="3"></td>
        <td colspan="2" valign="top"><h6>é¤�é»žæ•¸é‡�ï¼š<%= Total_qty %></h6></td>
        <td colspan="2" valign="top"><h6>ç¸½è¨ˆï¼š$<%= Total %></h6></td>
        </tr>
    </table>

	<table class="menu_list_table" cellpadding="0" width="830" cellspacing="0" bgcolor="#FFFFFF">
	  <tr bgcolor="#F1E6D2" height="20px">
	    <td width="10"></td>
	    <td colspan="2"><h1>æœƒå“¡è³‡æ–™ç¢ºèª�</h1></td>
	    </tr>
	  <tr height="10px">
	    <td colspan="7" style="border-top-style:solid; border-top-width:1px; border-top-color:#ABA7A4;"></td>
	    </tr>
	  <tr height="30px">
      	<td></td>
	    <td width="130"><h4>é»žé¤�æ–¹å¼�</h4></td>
	    <td width=""><h4><select name="o_type" onChange="refresh(this.value, '<%= request.getParameter("r_key") %>', '<%= request.getParameter("b_key") %>');">
	        <% if (branch.hasDelivery()) { %>
	        	<option value="delivery" <%= orderType == Order.OrderType.DELIVERY ? "selected=\"true\"" : "" %>>å¤–é€�</option>
	        <% } %>
	        <% if (branch.hasTakeOut()) { %>
	        	<option value="takeout" <%= orderType == Order.OrderType.TAKE_OUT ? "selected=\"true\"" : "" %>>å¤–å¸¶</option>
	        <% } %>
	        <% if (branch.hasTakeIn()) { %>
	        	<option value="takein" <%= orderType == Order.OrderType.TAKE_IN ? "selected=\"true\"" : "" %>>å…§ç”¨</option>
	        <% } %>
	        </select></h4></td>
	  </tr>
      <tr height="30px">
      	<td></td>
	    <td width="130"><h4>å§“å��</h4></td>
	    <td><h4><input type="text" name="c_name" size="15" height="20px" value="<%= customer.getCustomerName()  %>" /></h4></td>
	  </tr>
      <tr height="30px">
      	<td></td>
	    <td width="130"><h4>æ‰‹æ©Ÿæˆ–é›»è©±</h4></td>
	    <td><h4><input type="text" name="o_phone" size="15" height="20px" value="<%= customer.getCustomerPhone().getNumber() %>" /></h4></td>
	  </tr>
      <tr height="30px">
      	<td></td>
	    <td width="130"><h4>å�–/ç”¨é¤�æ™‚é–“</h4></td>
	    <td><h4><input id="time" name="s_time" type="text" size="15" height="20px" readonly="readonly" />
	    <a href="javascript:NewCal('time','mmddyyyy',true,24)"><img src="../../images/cal.gif" width="16" height="16" border="0" alt="Pick a date and time"></a>
	    </h4></td>
	  </tr>
<%
	if (orderType == Order.OrderType.DELIVERY) {
%>   
      <tr height="30px">
      	<td></td>
	    <td width="130"><h4>å¤–é€�åœ°å�€</h4></td>
	    <td><h4><input type="text" name="d_address1" class="input_extra_large" value="<%= customer.getCustomerAddress().getAddress() %>" /></h4></td>
	  </tr>
	  
      <tr height="30px">
      	<td></td>
	    <td width="130"><h4></h4></td>
	    <td><h4><input type="text" name="d_address2"  class="input_extra_large" value="" /></h4></td>
	  </tr>		   
<%
	}
	if (orderType == Order.OrderType.TAKE_IN) {
%>
	  <tr height="30px">
      	<td></td>
	    <td width="130"><h4>å…§ç”¨äººæ•¸</h4></td>
	    <td><h4><input type="text" name="n_people" size="15" class="input_large" value="1" /></h4></td>
	  </tr>
<%
	}
%>


      <tr height="30px">
      	<td></td>
	    <td colspan="2"><h4>å‚™è¨»æ¬„ï¼šè«‹å°‡æ‚¨éœ€è¦�æˆ‘å€‘ç‰¹åˆ¥æ³¨æ„�äº‹é …è¼¸å…¥åœ¨ä¸‹æ–¹(é™�200å­—ä»¥å…§)</h4></td>
	  </tr>
      <tr>
      	<td></td>
        <td colspan="2"><textarea style="width:800px;"></textarea></td>
      </tr>
      <tr height="80px" align="center"><td colspan="3" valign="top"><br /><input type="image" src="img/submit.jpg" width="101" height="30" value="Buy" name="Buy" /></td></tr>
	  
	</table>
	<input type="text" name="r_key" value="<%= request.getParameter("r_key") %>" style="display:none;" />
</div>

<div style="width:940px; height:100px;"><jsp:include page="../header/footer.jsp"/></div>
</div>
</form>


</body>
</html>