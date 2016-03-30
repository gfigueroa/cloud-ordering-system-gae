<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.RestaurantNews" %>
<%@ page import="datastore.RestaurantNewsManager" %>
<%@ page import="datastore.RestaurantManager" %>
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

</script>

<script language="Javascript" type="text/javascript">
function refresh(rn_allow_response) {
    window.location.replace("addNews.jsp?rn_allow_response=" + rn_allow_response);
}
</script>

</head>

<body>
<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<form method="post" id="form1" name="form1" onSubmit="return checkNumericValues();" 
      action="<%= blobstoreService.createUploadUrl("/manageRestaurant?action=add&type=N") %>"
      class="form-style" enctype="multipart/form-data">

    <fieldset>
	<legend><%= printer.print("Retail Store News") %></legend>
	
	<% if (message != null && message.equals("success") && action != null && action.equals("add")) { %>
		<div class="success-div"><%=printer.print("News successfully created")%>.</div>
	<% } %>
	
	<div>
		<h2><%= printer.print("Add Retail Store News") %></h2>
	</div>

	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
	
    <div>
       	<label  for="rn_title"><span><%= printer.print("News Title") %> <span class="required_field">*</span></span></label>
		<input type="text" name="rn_title" class="input_extra_large"  value="" title="" /><br />
		<div id="rn_title"></div>
	</div>
	
	<div>
       	<label for="rn_content"><span><%= printer.print("News Content") %> <span class="required_field">*</span></span></label>
		<textarea  name="rn_content" class="input_extra_large" value="" /></textarea><br />
		<div id="rn_content"></div>
	</div>
	
	<div>
       	<label for="rn_allow_response"><span><%= printer.print("Allow Responses") %> <span class="required_field">*</span></span></label>
		<input type="radio" name="rn_allow_response" <%= allowResponse ? "checked=\"true\"" : "" %> value="true" onClick="refresh(this.value);" /><%= printer.print("Yes") %>  		
		<input type="radio" name="rn_allow_response" <%= !allowResponse ? "checked=\"true\"" : "" %> value="false" onClick="refresh(this.value);" /><%= printer.print("No") %> <br />
		<div id="rn_allow_response"></div>
	</div>
	
	<%
	if (allowResponse) {
	%>
	
		<div>
	       	<label  for="max_clicks"><span><%= printer.print("Max. Responses") %> <span class="required_field">*</span></span></label>
			<input type="text" name="max_clicks" class="input_small" value="" title="" /><input type="checkbox" name="u_responses" value="true" /><%= printer.print("Unlimited") %>
			<div id="max_clicks"></div>
		</div>
	<%
	}
	%>

	<div>
		<label for="rn_start"><span><%= printer.print("Release Date") %> <span class="required_field">*</span></span></label>
		<input id="rn_start" type="text" name="rn_start" class="input_large" value="" readonly="readonly" />
		<a href="javascript:NewCal('rn_start','mmddyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date and time"></a>
		<br />
		<div id="rn_start"></div>
	</div>
	
	<div>
		<label for="rn_end"><span><%= printer.print("Expiration Date") %> <span class="required_field">*</span></span></label>
		<input id="rn_end" type="text" name="rn_end" class="input_large" value="" readonly="readonly" />
		<a href="javascript:NewCal('rn_end','mmddyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date and time"></a>
		<br />
		<div id="rn_end"></div>
	</div>
	
	<div>
       	<label for="rn_is_private"><span><%= printer.print("News is private") %> <span class="required_field">*</span></span></label>
		<input type="radio" name="rn_is_private" value="true" /> <%= printer.print("Yes") %>  		
		<input type="radio" name="rn_is_private" checked="true" value="false" /> <%= printer.print("No") %><br />
		<div id="rn_is_private"></div>
	</div>
	
    <div>
       	<label for="rn_image"><span><%= printer.print("Image") %> </span></label>
		<input type="file" name="rn_image" class="input_extra_large" value="" /><br />
		<div id="rn_image">
		</div>
	</div> 
	
	</fieldset>

	<br class="clearfloat" />
	
	<div>
		<input type="checkbox" name="keep_adding" checked="true" value="true" /><%= printer.print("Continue adding retail store news") %> 		
		<div id="keep_adding"></div>
	</div>
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/listNews.jsp?k=<%= request.getParameter("k") %>'" class="button-close"/>
	
	<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
