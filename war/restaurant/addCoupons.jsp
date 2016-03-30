<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="datastore.MenuItemType" %>
<%@ page import="datastore.MenuItemTypeManager" %>
<%@ page import="datastore.MenuItem" %>
<%@ page import="datastore.MenuItemManager" %>
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
  
  String allowResponseString = request.getParameter("rn_allow_response");
  boolean allowResponse = allowResponseString != null ? Boolean.parseBoolean(allowResponseString) : true;
  
  BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
  
  
  List<MenuItemType> typeList = MenuItemTypeManager.getRestaurantMenuItemTypes(sessionUser.getKey().getParent());
  List<MenuItem> itemList = MenuItemManager.getAllMenuItemsByRestaurant(sessionUser.getKey().getParent());
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

<script src="../js/datetimepicker.js" language="javascript" type="text/javascript"></script>

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

function checkNumericValues() {
  if(!isInteger(document.getElementsByName("m_clicks")[0].value)) {
    alert("<%=printer.print("The max responses you entered are not valid")%>.");
    return false;
  }
  return true;
}

function remove_item(item_no){
	var item_no=parseInt(item_no);
	number_coupon_item=parseInt(document.getElementById('c_item_product_no').value);
	for(var i=item_no;i<number_coupon_item;i++){
		var t=i+1;
		mit=document.getElementById('mit_coupon_'+i);
		mit2=document.getElementById('mit_coupon_'+t);
		mi=document.getElementById('mi_coupon_'+i);
		mi2=document.getElementById('mi_coupon_'+t);
		no_items=document.getElementById('no_product_items_'+i);
		no_items2=document.getElementById('no_product_items_'+t);
		
		mit.value=mit2.value;
		mi.value=mi2.value;
		
	    var A= mit.options, L= A.length;
	    while(L){
	        if (A[--L].value== mit2.value){
	            mit.options[L].selected = true;
	            mit.selectedIndex= L;	            
	            L= 0;
	        }
	    }

	   var A= mi.options, L= A.length;
	    while(L){
	        if (A[--L].value== mi2.value){
	            mi.options[L].selected = true;
	            mi.selectedIndex= L;	            
	            L= 0;
	        }
	    }
		no_items.value=no_items2.value;
	
	}
	 refresh_menu_item_selection();
}

function change_type(c_type_value){
	x1=document.getElementById('c_cash');
	p1=document.getElementById('c_items');
	if(c_type_value=='cash'){
		x1.style.display="block";
		p1.style.display="none";
	}else{
		x1.style.display="none";
		p1.style.display="block";
	}
}

function multi_number(coupon_product_num) {
	for (i=1;i<=25;i++) {
					
			product='c_item_product_'+i;
			x1=document.getElementById(product);
			mit=document.getElementById('mit_coupon_'+i);
			mi=document.getElementById('mi_coupon_'+i);
			no_items=document.getElementById('no_product_items_'+i);
			if(i<=1*coupon_product_num){
				x1.style.display="block";
			}
			else{
				x1.style.display="none";
				mit.value='none';
				mi.value='none';
				no_items.value=1;
			}

	}
	refresh_menu_item_selection();
}

function change_product_no(change_value){
	
	x1=document.getElementById('c_item_product_no');
	t=parseInt(x1.value);
	t=t+change_value;
	x1.value=t;
	multi_number(t);
}

function refresh_menu_item_selection(){
	
	mit='mit_coupon_';
	set_item='mi_coupon';
	
	var elems = document.getElementsByTagName('*');
	for (var i = 0 ; i < elems.length; i++){
		if(elems[i].className.indexOf(set_item) > -1){
			elems[i].style.display="none";
		}
	}

		for (var j = 1 ; j <=25 ;j++){
			x1=document.getElementById(mit+j);
			type= set_item + '_' + j + '_' + x1.value;
		   	for (var i = 0 ; i < elems.length; i++){
	    		if(elems[i].className.indexOf(type) > -1){ 
	    			elems[i].style.display="block";
	    		}
	    	}			
		}

}

onload=function() {
	multi_number('3');
	change_type('cash');
	refresh_menu_item_selection();	
}

</script>


</head>

<body>
<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<form method="post" id="form1" name="form1" onSubmit="return checkNumericValues();" 
      action="<%= blobstoreService.createUploadUrl("/manageRestaurant?action=add&type=C") %>"
      class="form-style" enctype="multipart/form-data">

    <fieldset>
	<legend><%= printer.print("Retail Store Coupons") %></legend>
	
	<% if (message != null && message.equals("success") && action != null && action.equals("add")) { %>
		<div class="success-div"><%=printer.print("Coupons successfully created")%>.</div>
	<% } %>
	
	<div>
		<h2><%= printer.print("Add Retail Store Coupons") %></h2>
	</div>

	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
	
	<div>
		<label for="c_type"><span><%= printer.print("Coupon Type") %> <span class="required_field">*</span></span></label>
		<select name="c_type" onChange="change_type(this.value)">
        <option value="cash"><%= printer.print("Cash") %></option>
        <option value="product_items"><%= printer.print("Product Items") %></option>        
        </select>
		<div id="c_type"></div>
	</div>
	
    <div>
       	<label  for="c_title"><span><%= printer.print("Coupon Title") %> <span class="required_field">*</span></span></label>
		<input type="text" name="c_title" class="input_extra_large"  value="" title="" /><br />
		<div id="c_title"></div>
	</div>
	
	<div>
       	<label for="c_content"><span><%= printer.print("Coupon Description") %> <span class="required_field">*</span></span></label>
		<textarea  name="c_content" class="input_extra_large" value="" /></textarea><br />
		<div id="c_content"></div>
	</div>
	
	<div>
		<label for="c_start"><span><%= printer.print("Release Date") %> <span class="required_field">*</span></span></label>
		<input id="c_start" type="text" name="c_start" class="input_large" value="" readonly="readonly" />
		<a href="javascript:NewCal('c_start','mmddyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date and time"></a>
		<br />
		<div id="c_start"></div>
	</div>
	
	<div id="c_cash">
		<div id="c_end_date">
			<label for="c_end"><span><%= printer.print("Expiration Date") %> <span class="required_field">*</span></span></label>
			<input id="c_end" type="text" name="c_end" class="input_large" value="" readonly="readonly" />
			<a href="javascript:NewCal('c_end','mmddyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date and time"></a>
			<br />
			<div id="c_end"></div>
		</div>
		
		<div>
			<label for="c_cash"><span><%= printer.print("Cash") %> <span class="required_field">*</span></span></label>
			<input type="text" name="c_cash" class="input_large"  value="" /><br />
			<div id="c_cash"></div>
		</div>
	</div>
	
	<div>
		<label for="c_price"><span><%= printer.print("Price") %> <span class="required_field">*</span></span></label>
		<input type="text" name="c_price" class="input_large"  value="" /><br />
		<div id="c_price"></div>
	</div>
	
	<div>
		<label for="c_discount"><span><%= printer.print("Discount(between 0-1)") %></span></label>
		<input type="text" name="c_discount" class="input_large"  value="" /><br />
		<div id="c_discount"></div>
	</div>
	
	<div>
       	<label for="c_image"><span><%= printer.print("Image") %> </span></label>
		<input type="file" name="c_image" class="input_extra_large" value="" /><br />
		<div id="c_image">
		</div>
	</div>
	 	
	<div id="c_items">
		<div>
			<label for="c_item_product_no"><span><%= printer.print("Products") %> <span class="required_field">*</span></span></label>
			<input type="hidden" id="c_item_product_no" value="3">
			<div id="c_item_product_no"></div>
		</div>	
		
		<% for (int i = 1; i <= 25; i++) { %>
			<div id="c_item_product_<%= i %>">
			
				<label for="blank"><span><span class="required_field"></span></span></label>
				
				<select name="mit_coupon_<%= i %>" id="mit_coupon_<%= i %>" onChange="refresh_menu_item_selection()">
			        <option value="none">-<%= printer.print("Select Product Type") %>-</option>
			        <% for (MenuItemType menuItemType : typeList) { %>
			        	<option value="<%= KeyFactory.keyToString(menuItemType.getKey()) %>"><%= menuItemType.getMenuItemTypeName() %></option>
			        <% } %>
		        </select>
		        
		       	<select name="mi_coupon_<%= i %>" id="mi_coupon_<%= i %>">
			        <option value="none">-<%= printer.print("Select Item") %>-</option>
			        <% for (MenuItem menuItem : itemList) { %>
			        	<option value="<%= KeyFactory.keyToString(menuItem.getKey()) %>" class="mi_coupon_<%= i %>_<%= KeyFactory.keyToString(menuItem.getMenuItemType()) %>"><%= menuItem.getMenuItemName() %></option>
			        <% } %>
		        </select>
		        
		       	<select name="no_product_items_<%= i %>" id="no_product_items_<%= i %>">
			        <% for (int j = 1; j <= 10; j++) { %>
			        	<option value="<%= j %>"><%= j %></option>
			        <% } %>
		        </select>
		        
		        <input type="button" onClick=" remove_item('<%= i%>'); change_product_no(-1);" value="X">
			</div>
		<% } %>
		
		<div>
			<label></label>
			<input type="button" onClick="change_product_no(1)" value="+ <%= printer.print("Add Product")%>">
		</div>
	</div>
	
	</fieldset>

	<br class="clearfloat" />
	
	<div>
		<input type="checkbox" name="keep_adding" checked="true" value="true" /><%= printer.print("Continue adding retail store Coupons") %> 		
		<div id="keep_adding"></div>
	</div>
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/listCoupons.jsp?k=<%= request.getParameter("k") %>'" class="button-close"/>
	
	<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
