<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.MenuItemType" %>
<%@ page import="datastore.MenuItemTypeManager" %>
<%@ page import="datastore.MenuItem" %>
<%@ page import="datastore.MenuItemManager" %>
<%@ page import="datastore.TypeSetMenuItem" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="datastore.Set" %>
<%@ page import="datastore.SetManager" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>

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
  
  String setCount = request.getParameter("set_count") != null ? request.getParameter("set_count") : "";
  
  BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
  
  List<MenuItemType> typeList = MenuItemTypeManager.getRestaurantMenuItemTypes(sessionUser.getKey().getParent());
  List<MenuItem> itemList = MenuItemManager.getAllMenuItemsByRestaurant(sessionUser.getKey().getParent());
  
  Set set = null;
  String saveValues = "";
  String tmpKey="";
  int tmpNumberItems=0;
  List<Key> menuItemKeys = null;
  List<TypeSetMenuItem> typeSetMenuItems = null;
  List<Integer> menuItemNumber = null;
  
  List<String> menuItemStrings = null;
  
  if (request.getParameter("sk") != null) {
	  	saveValues = "true";
	  	set = SetManager.getSet(KeyFactory.stringToKey(request.getParameter("sk")));
	  	
	  	menuItemKeys = set.getMenuItems();
		typeSetMenuItems = set.getTypeSetMenuItems();
	
		menuItemStrings= new ArrayList<String>();
		menuItemNumber= new ArrayList<Integer>();
		
		for (Key key : menuItemKeys){
			if(tmpKey.equals(KeyFactory.keyToString(key)) ){
				tmpNumberItems=tmpNumberItems+1;
			}else{
				tmpKey=KeyFactory.keyToString(key);
				menuItemStrings.add(tmpKey);
				if(tmpNumberItems>0){
					menuItemNumber.add(tmpNumberItems);
				}
				tmpNumberItems=1;
				
			}
			
		}
		menuItemNumber.add(tmpNumberItems);
		
  }
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
  if(!isInteger(document.getElementsByName("set_number")[0].value)){
    alert("<%=printer.print("The set number you entered is not valid")%>.");
    return false;
  }
  if(!isDouble(document.getElementsByName("set_price")[0].value)){
    alert("<%=printer.print("The price you entered is not valid")%>.");
    return false;
  }
  if(!isDouble(document.getElementsByName("set_discount")[0].value)){
    alert("<%=printer.print("The discount you entered is not valid")%>.");
    return false;
  }
  return true;
}

function change_fixed_price(fixed_price_value){
	x1=document.getElementById('set_price');
	if(fixed_price_value=="true"){
		x1.disabled=false;
	}else{
		x1.disabled=true;
	}
}

function change_tt_fixed(tt_mi_fixed_group,number){

	x1=document.getElementById('set_tt_fixed_' + number + '_group');
	if(tt_mi_fixed_group=="false"){
		x1.style.display="block";
	}else{
		x1.style.display="none";
	}
	
}

function get_price(key_string,item_no){

	var elems = document.getElementsByTagName('*');
	for (var i = 0 ; i < elems.length; i++){
		if(elems[i].className.indexOf(key_string) > -1){
			price = elems[i].value;
		}
	}
	x1=document.getElementById('set_tt_mi_' + item_no + '_price');
	x1.value=price;
}

function change_set_tt_mi_price(tt_price_checkbox,price_checkbox){

	x1=document.getElementById(price_checkbox);
	
	if(tt_price_checkbox.checked){
		x1.disabled=false;
	}else{
		x1.disabled=true;
	}
}

function remove_item_tf(item_no){
	var item_no=parseInt(item_no);
	number_coupon_item=parseInt(document.getElementById('set_tf_product_no').value);
	for(var i=item_no;i<number_coupon_item;i++){
		var t=i+1;
		mit=document.getElementById('mit_tf_'+i);
		mit2=document.getElementById('mit_tf_'+t);
		mi=document.getElementById('mi_id_tf_'+i);
		mi2=document.getElementById('mi_id_tf_'+t);
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
	 refresh_menu_item_selection('FIXED');
}

function remove_item_type_tt(item_no,item_or_type,mi_product_no){
	var item_no=parseInt(item_no);
	if(item_or_type=='TYPE'){
		number_coupon_item=parseInt(document.getElementById('set_tt_product_no').value);
		for(var i=item_no;i<number_coupon_item;i++){
			var t=i+1;
			mit=document.getElementById('mit_tt_'+i);
			mit2=document.getElementById('mit_tt_'+t);
			
			mit.value=mit2.value;
			
		    var A= mit.options, L= A.length;
		    while(L){
		        if (A[--L].value== mit2.value){
		            mit.options[L].selected = true;
		            mit.selectedIndex= L;	            
		            L= 0;
		        }
		    }

		}
	 
	}else{
		number_coupon_item=parseInt(document.getElementById('set_tt_mi_product_no_'+ mi_product_no).value);

		for(var i=item_no;i<number_coupon_item;i++){
			var t1=((mi_product_no*100)+ i);
			var t2=t1+1;
			mit=document.getElementById('mi_id_tt_' + t1);
			mit2=document.getElementById('mi_id_tt_' + t2);
			price=document.getElementById('set_tt_mi_' + t1 +'_price');
			price2=document.getElementById('set_tt_mi_' + t2 +'_price');
			mit.value=mit2.value;
			price.value=price2.value;
			
		    var A= mit.options, L= A.length;
		    while(L){
		    	
		        if (A[--L].value== mit2.value){
		            mit.options[L].selected = true;
		            mit.selectedIndex= L;	            
		            L= 0;
		        }
		    }

		}
	 		
	}
	refresh_menu_item_selection('TYPE');			
}

function multi_number(set_product_num,set_type,set_item_num) {
	for (i=1;i<=25;i++) {
		if(set_type=='FIXED'||set_type=='TYPE'){
			if(set_type=='FIXED'){		
				product='set_tf_product_'+i;
				mit=document.getElementById('mit_tf_'+i);
				mi=document.getElementById('mi_id_tf_'+i);
				no_items=document.getElementById('no_product_items_'+i);
						
				x1=document.getElementById(product);
				if(i<=1*set_product_num){
					x1.style.display="block";
				}
				else{
					x1.style.display="none";
					mit.value='none';
					mi.value='none';
					no_items.value=1;
					
				}
			}
			if(set_type=='TYPE'){		
				product='set_tt_product_'+i;
				mit=document.getElementById('mit_tt_'+i);				
				
				x1=document.getElementById(product);
				if(i<=1*set_product_num){
					x1.style.display="block";
				}
				else{
					x1.style.display="none";
						mit.value='none';
				}
			
			}
		}else{
		
			if(set_type=='TYPE_MI'){		
				for(p=1;p<=10;p++){
					product='set_tt_mi_product_'+((i*100)+p);
					mi=document.getElementById('mi_id_tt_'+((i*100)+p));
					price=document.getElementById('set_tt_mi_' + ((i*100)+p) +'_price');
					x1=document.getElementById(product);
					if(i==set_item_num){
						if(p<=1*set_product_num){
							x1.style.display="block";
						}else{
							x1.style.display="none";
							mi.value="none";
							price.value="";
						}
					}
					
				}
			}
		}
	}
}

function change_product_no(change_value,set_type,item_number){
	if(set_type=='FIXED'){
		x1=document.getElementById('set_tf_product_no');
	}
	if(set_type=='TYPE'){
		x1=document.getElementById('set_tt_product_no');
	}
	if(set_type=='TYPE_MI'){
		x1=document.getElementById('set_tt_mi_product_no_'+item_number);
	}
	var t=parseInt(x1.value);
	t=t + parseInt(change_value);
	x1.value=t;
	multi_number(t,set_type,item_number);
	refresh_menu_item_selection('FIXED');
}

function refresh_menu_item_selection(set_type){
	if(set_type=='FIXED'){
		mit_tf='mit_tf_';
		set_item='mi_tf';
	}
	if(set_type=='TYPE'){
		mit_tf='mit_tt_';
		set_item='mi_tt';
	}

	var elems = document.getElementsByTagName('*');
	for (var i = 0 ; i < elems.length; i++){
		if(elems[i].className.indexOf(set_item) > -1){
			elems[i].style.display="none";
		}
	}
    if(set_type=='FIXED'){
		for (var j = 1 ; j <=25 ;j++){
			x1=document.getElementById(mit_tf+j);
			type= set_item + '_' + j + '_' + x1.value;
		   	for (var i = 0 ; i < elems.length; i++){
	    		if(elems[i].className.indexOf(type) > -1){ 
	    			elems[i].style.display="block";
	    		}
	    	}			
		}
	}else{
		for (var j = 1 ; j <=25 ;j++){
			for(var k= 1; k<=10;k++){
				x1=document.getElementById(mit_tf+j);
				type= set_item + '_' + ((j*100)+k) + '_' + x1.value;
			   	for (var i = 0 ; i < elems.length; i++){
		    		if(elems[i].className.indexOf(type) > -1){ 
		    			elems[i].style.display="block";
		    		}
		    	}	
		    }		
		}
	}
}

function change_set_type(set_type){
	x1=document.getElementById('set_type_fixed');
	x2=document.getElementById('set_type_type');
	x3=document.getElementById('set_type_ffood');
	switch(set_type){
	case 'FIXED_SET':
		x1.style.display="block";
		x2.style.display="none";
		x3.style.display="none";
		break;
	case 'TYPE_SET':
		x1.style.display="none";
		x2.style.display="block";
		x3.style.display="none";
		break;
	case 'FAST_FOOD_SET':
		x1.style.display="none";
		x2.style.display="none";
		x3.style.display="block";
		break;
	default:
		x1.style.display="none";
		x2.style.display="none";
		x3.style.display="none";
	}
}

function load_product_values(){

	for(var i=1; i<=25; i++){
	
		mit=document.getElementById('mit_tf_'+i);
		mi=document.getElementById('mi_id_tf_'+i);
		no_items=document.getElementById('no_product_items_'+i);

		mit.value=document.getElementById('load_type_' + i).value;
		mi.value=document.getElementById('load_item_' + i).value;
	    no_items.value=document.getElementById('load_number_' + i).value;
	    
	    refresh_menu_item_selection('FIXED');
	}
}

onload=function() {
	<% if(saveValues.equals("true")){%>
		load_product_values();
	<%}%>
	change_set_type('FIXED_SET');
	refresh_menu_item_selection('FIXED');
	refresh_menu_item_selection('TYPE');
	multi_number(3,'FIXED',0);
	multi_number(1,'TYPE',0);
	for(var i=1;i<=25;i++){
		multi_number(3,'TYPE_MI',i);
	}
}

</script>
</head>

<body>

<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<form method="post" id="form1" name="form1" onSubmit="return checkNumericValues();" 
      action="<%= blobstoreService.createUploadUrl("/manageRestaurant?action=add&type=E") %>"
      class="form-style" enctype="multipart/form-data">

    <fieldset>
	<legend><%= printer.print("Product Items Sets") %></legend>
	
	<% if (message != null && message.equals("success") && action != null && action.equals("add")) { %>
		<div class="success-div"><%= printer.print("Product item Set successfully added to the Retail Store's menu") %>.</div>
	<% } %>
	
	<div>
		<h2><%= printer.print("Add Product Item Set") %></h2>
	</div>

	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
	<% if (error != null && error.equals("Format")) { %>
		<div class="error-div"><%= printer.print("The discount must be a decimal value between 0 and 1") %></div>
	<% } %>
	
	<div>
       	<label  for="set_number"><span><%= printer.print("Set Number") %></span></label>
		<input type="text" name="set_number" class="input_extra_large"  value="<%= setCount %>" title="" /><br />
		<div id="set_number"></div>
	</div>
	
	<div>
		<label for="set_type"><span><%= printer.print("Set Type") %> <span class="required_field">*</span></span></label>
		<select name="set_type" onChange="change_set_type(this.value)">
        	<option value="FIXED_SET"><%= printer.print("Fixed Set") %></option>
        	<option value="TYPE_SET"><%= printer.print("Type Set") %></option>
        	<%//<option value="FAST_FOOD_SET"><%= printer.print("Fast Food Set") %></option>%>
        </select>
		<div id="set_type"></div>
	</div>
	
    <div>
       	<label  for="set_name"><span><%= printer.print("Set Name") %> <span class="required_field">*</span></span></label>
		<input type="text" name="set_name" class="input_extra_large"  value="" title="" /><br />
		<div id="set_name"></div>
	</div>
	
	<div>
       	<label for="set_description"><span><%= printer.print("Description") %> <span class="required_field">*</span></span></label>
		<textarea name="set_description" class="input_extra_large" value="" /></textarea><br />
		<div id="set_description"></div>
	</div>
	
	<div>
       	<label for="set_fixed_price"><span><%= printer.print("Fixed Price") %> <span class="required_field">*</span></span></label>
		<input type="radio" name="set_fixed_price" checked="checked" value="true" onChange="change_fixed_price(this.value)" disabled="true"/> <span><%= printer.print("Yes")%> 		
		<input type="radio" name="set_fixed_price" value="false" onChange="change_fixed_price(this.value)" disabled="true"/> <span><%= printer.print("No") %><br />
		<div id="set_fixed_price"></div>
	</div>
	
	<div>
		<label for="set_price"><span><%= printer.print("Price") %></span></label>
		<input type="text" name="set_price" id="set_price" class="input_large"  value="" /><br />
		<div id="set_price"></div>
	</div>
	
	<div>
		<label for="set_discount"><span><%= printer.print("Discount(between 0-1)") %></span></label>
		<input type="text" name="set_discount" class="input_large"  value="" /><br />
		<div id="set_discount"></div>
	</div>
	
    <div>
       	<label for="set_image"><span><%= printer.print("Image") %> </span></label>
		<input type="file" name="set_image" class="input_extra_large" value="" /><br />
		<div id="set_image">
		</div>
	</div>
	
	<div>
		<label for="set_serving_time"><span><%= printer.print("Serving time (minutes)") %></span></label>
		<input type="text" name="set_serving_time" class="input_large" value="" /><br />
		<div id="set_serving_time"></div>
	</div>
	
	<div>
       	<label for="set_is_available"><span><%= printer.print("Available") %> <span class="required_field">*</span></span></label>
		<input type="radio" name="set_is_available" checked="checked" value="Y" /> <span><%= printer.print("Yes") %> 		
		<input type="radio" name="set_is_available" value="N" /> <span><%= printer.print("No") %><br />
		<div id="set_is_available"></div>
	</div>
	
	<div>
		<label for="set_service_time"><span><%= printer.print("Service Time") %> <span class="required_field">*</span></span></label>
		<select name="set_service_time">
        <option value="0"><%= printer.print("All day") %></option>
        <option value="1"><%= printer.print("Breakfast (07:00 - 12:00)") %></option>
        <option value="2"><%= printer.print("Lunch (12:00 - 16:30)") %></option>
        <option value="3"><%= printer.print("Dinner (16:30 - 23:30)") %></option>
        <option value="4"><%= printer.print("Breakfast + Lunch") %></option>
        <option value="5"><%= printer.print("Breakfast + Dinner") %></option>
        <option value="6"><%= printer.print("Lunch + Dinner") %></option>
        </select>
		<div id="set_service_time"></div>
	</div>
	
    <div>
       	<label for="set_comments"><span><%= printer.print("Comments") %></span></label>
		<textarea  name="set_comments" class="input_extra_large" value="" /></textarea><br />
		<div id="set_comments"></div>
	</div>
	
	<div id="set_type_fixed">
		<div>
			<label for="set_tf_product_no"><span><%= printer.print("Products") %> <span class="required_field">*</span></span></label>
			<input type="hidden" id="set_tf_product_no" value="3">
			<div id="set_tf_product_no"></div>
		</div>	
		
		<% for (int i = 1; i <= 25; i++) { %>
		
		<%
		if(saveValues.equals("true")){
				MenuItem menuItems = null;
				MenuItemType menuItemTypes = null;
				String menuItemString="-" + printer.print("Select Item") + "-";
				String menuItemTypeString="-" + printer.print("Select Product Type") + "-";
				int numberProductsItems = 1;							
			if(i<=menuItemStrings.size()){
				menuItems = MenuItemManager.getMenuItem(KeyFactory.stringToKey(menuItemStrings.get(i-1)));
				menuItemString = menuItems.getMenuItemName();
				menuItemTypes = MenuItemTypeManager.getMenuItemType(menuItems.getMenuItemType());
				menuItemTypeString = menuItemTypes.getMenuItemTypeName();
				numberProductsItems=menuItemNumber.get(i-1);
			%>	
				<input type="hidden" id="load_item_<%= i%>" value="<%= KeyFactory.keyToString(menuItems.getKey()) %>">
				<input type="hidden" id="load_type_<%= i%>" value="<%= KeyFactory.keyToString(menuItemTypes.getKey()) %>">
				<input type="hidden" id="load_number_<%= i%>" value="<%= numberProductsItems %>">
			<%
			}else{
			%>
				<input type="hidden" id="load_item_<%= i%>" value="none">
				<input type="hidden" id="load_type_<%= i%>" value="none">
				<input type="hidden" id="load_number_<%= i%>" value="1">
			<%
			}
		}	
		%>
		
			<div id="set_tf_product_<%= i %>">
			
				<label for="blank"><span><span class="required_field"></span></span></label>
				
				<select name="mit_tf_id_<%= i %>" id="mit_tf_<%= i %>" onChange="refresh_menu_item_selection('FIXED')">
			        <option value="none">-<%= printer.print("Select Product Type") %>-</option>
			        <% for (MenuItemType menuItemType : typeList) { %>
			        	<option value="<%= KeyFactory.keyToString(menuItemType.getKey()) %>"><%= menuItemType.getMenuItemTypeName() %></option>
			        <% } %>
		        </select>
		        
		       	<select name="mi_id_tf_<%= i %>" id="mi_id_tf_<%= i %>">
			        <option value="none">-<%= printer.print("Select Item") %>-</option>
			        <% for (MenuItem menuItem : itemList) { %>
			        	<option value="<%= KeyFactory.keyToString(menuItem.getKey()) %>" class="mi_tf_<%= i %>_<%= KeyFactory.keyToString(menuItem.getMenuItemType()) %>"><%= menuItem.getMenuItemName() %></option>
			        <% } %>
		        </select>
		        
		       	<select name="no_product_items_<%= i %>" id="no_product_items_<%= i %>">
			        <% for (int j = 1; j <= 10; j++) { %>
			        	<option value="<%= j %>"><%= j %></option>
			        <% } %>
		        </select>
		        
		        <input type="button" onClick="remove_item_tf('<%= i%>'); change_product_no(-1,'FIXED', 0);" value="X">
			</div>
		<% } %>
		
		<div>
			<label></label>
			<input type="button" onClick="change_product_no(1,'FIXED',0)" value="+ <%= printer.print("Add Product")%>">
		</div>
	</div>
		
	<div id="set_type_type">
		<div>
			<label for="set_tt_product_no"><span><%= printer.print("Products") %> <span class="required_field">*</span></span></label>
			<input type="hidden" id="set_tt_product_no" value="1">
			<div id="set_tt_product_no"></div>
		</div>	
		<div>
			<label></label><p><%= printer.print("Product Type") %>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<%= printer.print("All Items selected?")%></p>
		</div>	
		<% for (int i = 1; i <= 25; i++) { %>
			<div id="set_tt_product_<%= i%>">
			
				<label for=""><span><span class="required_field"></span></span></label>
				
				<select name="mit_tt_<%= i %>" id="mit_tt_<%= i %>" onChange="refresh_menu_item_selection('TYPE')">
			        <option value="none">-<%= printer.print("Select Product Type") %>-</option>
			        <% for (MenuItemType menuItemType : typeList) { %>
			        	<option value="<%= KeyFactory.keyToString(menuItemType.getKey()) %>"><%= menuItemType.getMenuItemTypeName() %></option>
			        <% } %>
		        </select>	
				
				<input type="radio" name="set_tt_fixed_<%= i %>" id="set_tt_fixed_<%= i %>" checked="true" value="true" onChange="change_tt_fixed(this.value,<%= i %>)"/><%= printer.print("Yes") %> 		
				<input type="radio" name="set_tt_fixed_<%= i %>" id="set_tt_fixed_<%= i %>" value="false" onChange="change_tt_fixed(this.value,<%= i %>)"/><%= printer.print("No") %>
				
				<input type="button" onClick="remove_item_type_tt(<%= i%>,'TYPE',0); change_product_no(-1,'TYPE',0);" value="X"></br>
		       		
				<input type="hidden" name="set_tt_mi_product_no_<%= i %>" id="set_tt_mi_product_no_<%= i %>" value="3">			
		       		                
		    	<div id="set_tt_fixed_<%= i %>_group" style="display:none;">
					
			        <% for (int p = 1; p <= 10; p++) { %>
						<div id="set_tt_mi_product_<%= (i * 100) + p %>">	

								<div><label></label></div>
						       	&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
						       	<select name="mi_id_tt_<%= (i * 100) + p %>" id="mi_id_tt_<%= (i * 100) + p %>" onChange="get_price(this.value,<%= (i*100) + p %>);">
							        <option value="none">-<%= printer.print("Select Item") %>-</option>
							        <% for (MenuItem menuItem : itemList) { %>
							        	<option value="<%= KeyFactory.keyToString(menuItem.getKey())%>" class="mi_tt_<%= (i * 100) + p %>_<%= KeyFactory.keyToString(menuItem.getMenuItemType()) %>"><%= menuItem.getMenuItemName() %></option>
							        <% } %>
						        </select>

						        <input type="checkbox" name="set_tt_mi_price_select_<%= (i * 100) + p %>" id="set_tt_mi_price_select_<%= (i * 100) + p %>" value="set_tt_mi_<%= (i * 100) + p %>_price" onChange="change_set_tt_mi_price(this, this.value)" >
						        <input type="text" name="set_tt_mi_<%= (i * 100) + p %>_price" id="set_tt_mi_<%= (i * 100) + p %>_price" class="input_large" value="" disabled="true">
						        
						        <input type="button" value="X" onClick="remove_item_type_tt(<%= p %>,'TYPE_MI',<%= i %>); change_product_no(-1,'TYPE_MI',<%= i %>);">
						        
						       	</br>
						       	<label></label>
						 </div>   
						
					<% } %>
					 &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<input type="button" value="+ <%= printer.print("Add Item")%>" onClick="change_product_no(1,'TYPE_MI',<%= i %>)">
				</div>

			</div>
		<% } %>
		
		 <div style="display:none;">
		       	<select name="menu_item_price" id="menu_item_price">
			        <% for (MenuItem menuItem : itemList) { %>
			        	<option value="<%= menuItem.getMenuItemPrice()%>" class="<%= KeyFactory.keyToString(menuItem.getKey())%>"></option>
			        <% } %>
		        </select>
	    </div>
		
		<div>
			<label></label>
			<input type="button" onClick="change_product_no(1,'TYPE',0)" value="+ <%= printer.print("Add Product Type")%>">
		</div>
	</div>
		
	<div id="set_type_ffood">
	</div>
	
	</fieldset>

	<br class="clearfloat" />
	
	<div>
		<input type="checkbox" name="keep_adding" checked="true" value="true" /> <%= printer.print("Continue adding Product Items Sets") %>		
		<div id="keep_adding"></div>
	</div>
	
	<div>
		<input type="checkbox" name="save_values" value="false" /> <%= printer.print("Save Values") %>		
		<div id="save_values"></div>
	</div>
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/listSets.jsp?k=<%= request.getParameter("k") %>'" class="button-close"/>
	
	<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
