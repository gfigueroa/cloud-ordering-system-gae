<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.MenuItem" %>
<%@ page import="datastore.MenuItemManager" %>
<%@ page import="datastore.MenuItemType" %>
<%@ page import="datastore.MenuItemTypeManager" %>
<%@ page import="datastore.TypeSetMenuItem" %>
<%@ page import="datastore.Set" %>
<%@ page import="datastore.SetManager" %>
<%@ page import="com.google.appengine.api.blobstore.BlobKey" %>
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
  
  boolean readOnly = request.getParameter("readonly") != null ? true : false;
  String returnTo = request.getParameter("returnto") != null ? request.getParameter("returnto") : "listSets";
  String orderKeyString = request.getParameter("o_key");
  String error = request.getParameter("etype");
  String message = request.getParameter("msg");
  String action = request.getParameter("action");
%>

<% 
Printer printer = (Printer)session.getAttribute("printer");
%>

<%
	Set set = SetManager.getSet(KeyFactory.stringToKey(request.getParameter("k")));
	List<MenuItemType> typeList = MenuItemTypeManager.getRestaurantMenuItemTypes(sessionUser.getKey().getParent());
	List<MenuItem> itemList = MenuItemManager.getAllMenuItemsByRestaurant(sessionUser.getKey().getParent());
	

	List<Key> menuItemKeys = set.getMenuItems();
	List<TypeSetMenuItem> typeSetMenuItems = set.getTypeSetMenuItems();
	String tmpKey="";
	int tmpNumberItems=0;
	List<String> menuItemStrings= new ArrayList<String>();
	List<Integer> menuItemNumber= new ArrayList<Integer>();
	
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
	if(tt_mi_fixed_group=='N'){
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
	change_set_type('FIXED_SET');
	load_product_values();
	refresh_menu_item_selection('FIXED');
	refresh_menu_item_selection('TYPE');
	multi_number(<%= menuItemStrings.size()%>,'FIXED',0);
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

<form method="post" id="form1" name="form1" 
      action="/manageRestaurant?action=update&type=E"
      class="form-style">
    
    <input type="text" name="k" value="<%= KeyFactory.keyToString(set.getKey()) %>" style="display:none;" />

    <fieldset>
    
    <legend><%= printer.print("Product Sets") %></legend>
    
   	<% if (message != null && message.equals("success") && action != null && action.equals("update")) { %>
		<div class="success-div"><%= printer.print("Product set successfully updated") %>.</div>
	<% } %>
	
	<div>
	  <h2><%= readOnly ? printer.print("Product Set") : printer.print("Edit Product Set") %></h2>
	</div>
	
	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
  		<div>
        	<label for="set_number"><span><%= printer.print("Set Number") %> <span class="required_field">*</span></span></label>
			<input type="text" name="set_number" class="input_extra_large" value="<%= set.getSetNumber() == null ? "" : set.getSetNumber() %>" <%= readOnly? "readonly=\"readonly\"" : "" %> /><br />
			<div id="set_number"></div>
		</div>
  
    			
	    <div>
			<label for="set_type"><span><%= printer.print("Set Type") %> <span class="required_field">*</span></span></label>
			<select name="set_type" onChange="change_set_type(this.value)" disabled=true>
	        	<option value="FIXED_SET" <%= set.getSetType() == Set.SetType.FIXED_SET ? "selected=\"true\"" : "" %>><%= printer.print("Fixed Set") %></option>
	        	<option value="TYPE_SET" <%= set.getSetType() == Set.SetType.TYPE_SET ? "selected=\"true\"" : "" %>><%= printer.print("Type Set") %></option>
	        	<%// <option value="FAST_FOOD_SET" ><%= printer.print("Fast Food Set") %></option> %>
	        </select>
			<div id="set_type"></div>
		</div>
				    			
        <div>
        	<label for="set_name"><span><%= printer.print("Set Name") %> <span class="required_field">*</span></span></label>
			<input type="text" name="set_name" class="input_extra_large" value="<%= set.getSetName() %>" <%= readOnly? "readonly=\"readonly\"" : "" %> /><br />
			<div id="set_name"></div>
		</div>

     	<div>
        	<label for="set_description"><span><%= printer.print("Description") %> </span></label>
			<input type="text" name="set_description" class="input_extra_large" value="<%= set.getSetDescription() %>" <%= readOnly? "readonly=\"readonly\"" : "" %> /><br />
			<div id="set_description"></div>
		</div>
		
		<div>
	       	<label for="set_fixed_price"><span><%= printer.print("Fixed Price") %> <span class="required_field">*</span></span></label>
			<input type="radio" name="set_fixed_price" checked="true" value="Y" disabled="true"/> <span><%= printer.print("Yes") %> 		
			<input type="radio" name="set_fixed_price" value="N" disabled="true"/> <span><%= printer.print("No") %><br />
			<div id="set_fixed_price"></div>
		</div>
		
		<div>
			<label for="set_price"><span><%= printer.print("Price") %> <span class="required_field">*</span></span></label>
			<input type="text" name="mi_price" class="input_large"  value="<%= set.getSetPrice() %>" <%= readOnly? "readonly=\"readonly\"" : "" %>/><br />
			<div id="set_price"></div>
		</div>
		
		<div>
			<label for="set_discount"><span><%= printer.print("Discount(between 0-1)") %></span></label>
			<input type="text" name="set_discount" class="input_large"  value="<%= set.getSetDiscount() == null ? "" : set.getSetDiscount() %>" <%= readOnly? "readonly=\"readonly\"" : "" %>/><br />
			<div id="set_discount"></div>
		</div>
		
		 <%
	    if (!(set.getSetImage() == null && readOnly)) {
	    %>
	    <div>
	       	<label for="set_image"><span><%= printer.print("Image") %></span></label>
	       	<%
			if (set.getSetImage() != null) {
			%>
				<a target="_new" href="/img?blobkey=<%= set.getSetImage().getKeyString() %>">
				<img src="/img?blobkey=<%= set.getSetImage().getKeyString() %>&s=300">
				</a>
				<br />
			<%
			}
			%>
			<%
			if (!readOnly) {
			%>		
			<label><span> </span></label><input type="file" name="set_image" class="input_extra_large" value="" /><br />
			<%
			}
			%>
			<div id="set_image"></div>
		</div>
		<%
		}
		%>
		
		
		<div>
			<label for="set_serving_time"><span><%= printer.print("Serving time (minutes)") %></span></label>
			<input type="text" name="set_serving_time" class="input_large" value="<%= set.getSetServingTime() == null ? "" : set.getSetServingTime() %>" <%= readOnly? "readonly=\"readonly\"" : "" %> /><br />
			<div id="set_serving_time"></div>
		</div>
	
	<div>
       	<label for="set_isavailable"><span><%= printer.print("Available") %> <span class="required_field">*</span></span></label>
		<input type="radio" <%= set.isAvailable() ? "checked=\"true\"" : "" %> name="set_isavailable" value="Y" <%= readOnly? "disabled=\"true\"" : "" %> /> <%= printer.print("Yes") %>
		<input type="radio" <%= !set.isAvailable() ? "checked=\"true\"" : "" %> name="set_isavailable" value="N" <%= readOnly? "disabled=\"true\"" : "" %> /> <%= printer.print("No") %>
		<br />
		<div id="set_isavailable"></div>
	</div>
	
	<div>
		<label for="set_service_time"><span><%= printer.print("Service Time") %> <span class="required_field">*</span></span></label>
		<select name="set_service_time" <%= readOnly? "disabled=\"true\"" : "" %>>
        <option value="0" <%= set.getSetServiceTime() == 0 ? "selected=\"true\"" : "" %>><%= printer.print("All day") %></option>
        <option value="1" <%= set.getSetServiceTime() == 1 ? "selected=\"true\"" : "" %>><%= printer.print("Breakfast (07:00 - 12:00)") %></option>
        <option value="2" <%= set.getSetServiceTime() == 2 ? "selected=\"true\"" : "" %>><%= printer.print("Lunch (12:00 - 16:30)") %></option>
        <option value="3" <%= set.getSetServiceTime() == 3 ? "selected=\"true\"" : "" %>><%= printer.print("Dinner (16:30 - 23:30)") %></option>
        <option value="4" <%= set.getSetServiceTime() == 4 ? "selected=\"true\"" : "" %>><%= printer.print("Breakfast + Lunch") %></option>
        <option value="5" <%= set.getSetServiceTime() == 5 ? "selected=\"true\"" : "" %>><%= printer.print("Breakfast + Dinner") %></option>
        <option value="6" <%= set.getSetServiceTime() == 6 ? "selected=\"true\"" : "" %>><%= printer.print("Lunch + Dinner") %></option>
        </select>
		<div id="set_service_time"></div>
	</div>
	
	<div>
       	<label for="set_comments"><span><%= printer.print("Comments") %></span></label>
		<textarea  name="set_comments" class="input_extra_large" value="" <%= readOnly? "readonly=\"readonly\"" : "" %>><%= set.getSetComments() %></textarea><br />
		<div id="set_comments"></div>
	</div>
	
	<div id="set_type_fixed">
		<div>
			<label for="set_tf_product_no"><span><%= printer.print("Products") %> <span class="required_field">*</span></span></label>
			<input type="hidden" id="set_tf_product_no" value="<%= menuItemStrings.size()%>">
			<div id="set_tf_product_no"></div>
		</div>	
		
		<% for (int i = 1; i <= 25; i++) { 
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
			
		%>
		
			<div id="set_tf_product_<%= i %>">
			
				<label for="blank"><span><span class="required_field"></span></span></label>
				
				<select name="mit_tf_id_<%= i %>" id="mit_tf_<%= i %>" onChange="refresh_menu_item_selection('FIXED')" <%= readOnly? "disabled=\"true\"" : "" %>>
			        
			        <option value="none">-<%= printer.print("Select Product Type") %>-</option>
			        <% for (MenuItemType menuItemType : typeList) { %>
			        	<option value="<%= KeyFactory.keyToString(menuItemType.getKey()) %>"><%= menuItemType.getMenuItemTypeName() %></option>
			        <% } %>
		        </select>
		        
		       	<select name="mi_id_tf_<%= i %>" id="mi_id_tf_<%= i %>" <%= readOnly? "disabled=\"true\"" : "" %>>
			        
			        <option value="none">-<%= printer.print("Select Item") %>-</option>
			        <% for (MenuItem menuItem : itemList) { %>
			        	<option value="<%= KeyFactory.keyToString(menuItem.getKey()) %>" class="mi_tf_<%= i %>_<%= KeyFactory.keyToString(menuItem.getMenuItemType()) %>"><%=menuItem.getMenuItemName() %></option>
			        <% } %>
		        </select>
		        
		       	<select name="no_product_items_<%= i %>" id="no_product_items_<%= i %>" <%= readOnly? "disabled=\"true\"" : "" %>>
			       		
					<% for (int j = 1; j <= 10; j++) { %>
			        	<option value="<%= j %>"><%= j %></option>
			        <% } %>
		        </select>
		        
		        <% if (!readOnly){%>
		        <input type="button" onClick="remove_item_tf('<%= i%>'); change_product_no(-1,'FIXED', 0);" value="X">
		        <%}%>
			</div>
		<% } %>
		
		<% if (!readOnly){%>
		<div>
			<label></label>
			<input type="button" onClick="change_product_no(1,'FIXED',0)" value="+ <%= printer.print("Add Product")%>">
		</div>
		<%}%>
	</div>
		
	<div id="set_type_type">
		<div>
			<label for="set_tt_product_no"><span><%= printer.print("Products") %> <span class="required_field">*</span></span></label>
			<input type="hidden" id="set_tt_product_no" value="1">
			<div id="set_tt_product_no"></div>
		</div>	
		<div>
			<label></label><p><%= printer.print("Product Type") %>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<%= printer.print("All Items Selected?")%></p>
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
				
				<input type="radio" name="set_tt_fixed_<%= i %>" id="set_tt_fixed_<%= i %>" checked="true" value="Y" onChange="change_tt_fixed(this.value,<%= i %>)"/><%= printer.print("Yes") %> 		
				<input type="radio" name="set_tt_fixed_<%= i %>" id="set_tt_fixed_<%= i %>"  value="N" onChange="change_tt_fixed(this.value,<%= i %>)"/><%= printer.print("No") %>
				
				<input type="button" onClick="remove_item_type_tt(<%= i%>,'TYPE',0); change_product_no(-1,'TYPE',0);" value="X"></br>
		       		
				<input type="hidden" name="set_tt_mi_product_no_<%= i %>" id="set_tt_mi_product_no_<%= i %>" value="3">			
		       		                
		    	<div id="set_tt_fixed_<%= i %>_group" style="display:none;">
				
					
			        <% for (int p = 1; p <= 10; p++) { %>
						<div id="set_tt_mi_product_<%= (i * 100) + p %>">	

								<div><label></label></div>
						       	&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<select name="mi_id_tt_<%= (i * 100) + p %>" id="mi_id_tt_<%= (i * 100) + p %>" onChange="get_price(this.value,<%= (i*100)+p %>);">
							        <option value="none">-<%= printer.print("Select Item") %>-</option>
							        <% for (MenuItem menuItem : itemList) { %>
							        	<option value="<%= KeyFactory.keyToString(menuItem.getKey())%>" class="mi_tt_<%= (i * 100) + p %>_<%= KeyFactory.keyToString(menuItem.getMenuItemType()) %>"><%= menuItem.getMenuItemName() %></option>
							        <% } %>
						        </select>

						        <input type="checkbox" name="set_tt_mi_price_select_<%= (i * 100) + p %>" id="set_tt_mi_price_select_<%= (i * 100) + p %>" value="set_tt_mi_<%= (i * 100) + p %>_price" onChange="change_set_tt_mi_price(this, this.value)" >
						        <input type="text" name="set_tt_mi_<%= (i * 100) + p %>_price" id="set_tt_mi_<%= (i * 100) + p %>_price" class="input_large" value=""  disabled="true">
						        
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
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/<%= returnTo %>.jsp<%= orderKeyString != null ? "?k=" + orderKeyString : "" %>'" class="button-close"/>
	
	<%
	if (!readOnly) {
	%>
		<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>
	<%
	}
	else {
	%>
		<input type="button" value="&nbsp;&nbsp;&nbsp;<%= printer.print("Edit") %>&nbsp;&nbsp;&nbsp;" onClick="location.href='/restaurant/editSets.jsp?k=<%= request.getParameter("k") %>'" class="button_style">
	<%
	}
	%>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
