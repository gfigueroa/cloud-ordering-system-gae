<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="datastore.RestaurantNews" %>
<%@ page import="datastore.RestaurantNewsManager" %>
<%@ page import="util.DateManager" %>
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
  	if (sessionUser.getUserType() != User.Type.ADMINISTRATOR && sessionUser.getUserType() != User.Type.RESTAURANT) {
  		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  	}
  }
  
  boolean readOnly = request.getParameter("readonly") != null ? true : false;
  String error = request.getParameter("etype");
  String message = request.getParameter("msg");
  String action = request.getParameter("action");
  
  RestaurantNews news = RestaurantNewsManager.getRestaurantNews(KeyFactory.stringToKey(request.getParameter("k")));
  Restaurant restaurant = RestaurantManager.getRestaurant(news.getKey().getParent());
  
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
  if(!isInteger(document.getElementsByName("max_clicks")[0].value)){
    alert("<%=printer.print("The max responses you entered are not valid")%>.");
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

<form method="post" id="form1" name="form1" onSubmit="return checkNumericValues();" 
      action="<%= blobstoreService.createUploadUrl("/manageRestaurant?action=update&type=N&k=" + request.getParameter("k")) %>"
      class="form-style" enctype="multipart/form-data">

    <fieldset>
    
    <legend><%= printer.print("Retail Store News") %></legend>
	
	<% if (message != null && message.equals("success") && action != null && action.equals("update")) { %>
		<div class="success-div"><%= printer.print("News successfully updated") %>.</div>
	<% } %>
	
	<div>
	  <h2><%= readOnly ? printer.print("View Retail Store News") : printer.print("Edit Retail Store News") %></h2>
	</div>
	
	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
	
	<div>
       	<label  for="r_name"><span><%= printer.print("Retail Store Name") %></span></label>
		<input type="text" name="r_name" class="input_extra_large" value="<%= restaurant.getRestaurantName() %>" title="" readonly="readonly" /><br />
		<div id="r_name"></div>
	</div>
    			
    <div>
        <label for="rn_title"><span><%= printer.print("News Title") %> <span class="required_field">*</span></span></label>
		<input type="text" name="rn_title" class="input_extra_large" value="<%= news.getRestaurantNewsTitle() %>" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="rn_title"></div>
	</div>
	
	<div>
       	<label for="rn_content"><span><%= printer.print("News Content") %> <span class="required_field">*</span></span></label>
		<textarea  name="rn_content" class="input_extra_large" rows="<%= news.getRestaurantNewsContent().split("\r\n|\r|\n").length + 1 %>" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "readonly=\"readonly\"" : "" %>><%= news.getRestaurantNewsContent() %></textarea><br />
		<div id="rn_content"></div>
	</div>
	
	<div>
        <label for="rn_allow_response"><span><%= printer.print("Allow Responses") %></span></label>
		<input type="text" name="rn_allow_response" class="input_extra_large" value="<%= news.allowsResponse() == null ? printer.print("Yes") : (news.allowsResponse() ? printer.print("Yes") : printer.print("No")) %>" readonly="readonly" /><br />
		<div id="rn_allow_response"></div>
	</div>
	
	<div>
        <label for="rn_status"><span><%= printer.print("Status") %></span></label>
		<input type="text" name="rn_status" class="input_extra_large" value="<%= printer.print(news.getCurrentStatus().toString()) %>" readonly="readonly" /><br />
		<div id="rn_status"></div>
	</div>
	
	<% 
	if (news.allowsResponse() == null || news.allowsResponse()) {
	%>
		<div>
	        <label for="curr_clicks"><span><%= printer.print("Current Responses") %></span></label>
			<input type="text" name="curr_clicks" class="input_extra_large" value="<%= news.getCurrentClicks() %>" readonly="readonly" /><br />
			<div id="curr_clicks"></div>
		</div>
		
		<div>
	        <label for="max_clicks"><span><%= printer.print("Max. Responses") %> <span class="required_field">*</span></span></label>
			<input type="text" name="max_clicks" class="input_extra_large" value="<%= news.getMaxClicks() %>" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "readonly=\"readonly\"" : "" %> /><br />
			<div id="max_clicks"></div>
		</div>
	<%
	}
	%>
	
	<div>
		<label for="rn_start"><span><%= printer.print("Release Date") %> <span class="required_field">*</span></span></label>
		<input id="rn_start" type="text" name="rn_start" class="input_large" value="<%= DateManager.printDateAsString(news.getRestaurantNewsStartingDate()) %>" readonly="readonly" />
		<% 
		if (!readOnly) {
		%>
		<a href="javascript:NewCal('rn_start','mmddyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date and time"></a>
		<%
		}
		%>
		<div id="rn_start"></div>
	</div>
	
	<div>
		<label for="rn_end"><span><%= printer.print("Expiration Date") %> <span class="required_field">*</span></span></label>
		<input id="rn_end" type="text" name="rn_end" class="input_large" value="<%= DateManager.printDateAsString(news.getRestaurantNewsEndingDate()) %>" readonly="readonly" />
		<% 
		if (!readOnly) {
		%>
		<a href="javascript:NewCal('rn_end','mmddyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date and time"></a>
		<%
		}
		%>
		<div id="rn_end"></div>
	</div>
	
	<div>
        <label for="rn_priority"><span><%= printer.print("Priority") %> <span class="required_field">*</span></span></label>
		<input type="text" name="rn_priority" class="input_extra_large" value="<%= news.getRestaurantNewsPriority() %>" <%= readOnly || sessionUser.getUserType() != User.Type.ADMINISTRATOR ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="rn_priority"></div>
	</div>
	
	<div>
       	<label for="rn_is_private"><span><%= printer.print("News is private") %> <span class="required_field">*</span></span></label>
		<input type="radio" name="rn_is_private" <%= news.isPrivate() ? "checked=\"true\"" : "" %> value="true" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "disabled=\"true\"" : "" %> /> <%= printer.print("Yes") %>  		
		<input type="radio" name="rn_is_private" <%= !news.isPrivate() ? "checked=\"true\"" : "" %> value="false" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "disabled=\"true\"" : "" %> /> <%= printer.print("No") %><br />
		<div id="rn_is_private"></div>
	</div>
	
	<%
	if (!(news.getRestaurantNewsImage() == null && (readOnly || sessionUser.getUserType() != User.Type.RESTAURANT))) {
	%>
	    <div>
	       	<label for="rn_image"><span><%= printer.print("Image") %></span></label>
	       	<%
			if (news.getRestaurantNewsImage() != null) {
			%>
				<a target="_new" href="/img?blobkey=<%= news.getRestaurantNewsImage().getKeyString() %>">
				<img src="/img?blobkey=<%= news.getRestaurantNewsImage().getKeyString() %>&s=300">
				</a>
				<br />
			<%
			}
			%>
			<% 
			if (!readOnly && sessionUser.getUserType() == User.Type.RESTAURANT) {
			%>
				<label><span> </span></label><input type="file" name="rn_image" class="input_extra_large" value="" /><br />
			<%
			}
			%>
			<div id="rn_image"></div>
		</div>
	<%
	}
	%>
	
	</fieldset>
  
	<br class="clearfloat" />
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/listNews.jsp?r_key=<%= KeyFactory.keyToString(restaurant.getKey()) %>'" class="button-close"/>
	
	<%
	if (!readOnly) {
	%>
		<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>
	<%
	}
	else {
	%>
		<input type="button" value="&nbsp;&nbsp;<%= printer.print("Edit") %>&nbsp;&nbsp;" onClick="location.href='/restaurant/editNews.jsp?k=<%= request.getParameter("k") %>'" class="button_style">
	<%
	}
	%>
	

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
