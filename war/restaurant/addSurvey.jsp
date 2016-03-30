<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.RestaurantOpinionPoll" %>
<%@ page import="datastore.RestaurantOpinionPollManager" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="datastore.Survey" %>
<%@ page import="datastore.SurveyManager" %>
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
  
  String surveyType = request.getParameter("survey_type");
  if (surveyType == null) {
  	surveyType = "global";
  }
  
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
  if(!isInteger(document.getElementsByName("sv_code")[0].value)) {
    alert("<%=printer.print("The validation code you entered is not valid")%>.");
    return false;
  }
  return true;
}

</script>

<script language="Javascript" type="text/javascript">

function refresh(sv_type) {
     g=document.getElementById('sv_code');

     if (sv_type=="global") {
     		g.style.display="none";
	 }
	 else{
	 		g.style.display="block";
	 }
}

</script>

</head>

<body onload="refresh('global');">
<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<form method="post" id="form1" name="form1" onSubmit="return checkNumericValues();" 
      action="<%= blobstoreService.createUploadUrl("/manageRestaurant?action=add&type=S") %>"
      class="form-style" enctype="multipart/form-data">

    <fieldset>
	<legend><%= printer.print("Surveys") %></legend>
	
	<% if (message != null && message.equals("success") && action != null && action.equals("add")) { %>
		<div class="success-div"><%= printer.print("Survey successfully created") %>.</div>
	<% } %>
	
	<div>
		<h2><%= printer.print("Add Survey") %></h2>
	</div>

	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
	
	<div>
		<label for="sv_type"><span><%= printer.print("Survey Type") %> <span class="required_field">*</span></span></label>
		<select name="sv_type" onChange="refresh(this.value);">
	        <option value="global" <%= surveyType.equals("global") ? "selected=\"true\"" : "" %>><%= printer.print("Global") %></option>
	        <option value="private" <%= surveyType.equals("private") ? "selected=\"true\"" : "" %>><%= printer.print("Private") %></option>
	        <option value="invitation" <%= surveyType.equals("invitation") ? "selected=\"true\"" : "" %>><%= printer.print("Invitation") %></option>
        </select>
		<div id="sv_type"></div>
	</div>
	
    <div>
       	<label  for="sv_title"><span><%= printer.print("Survey Title") %> <span class="required_field">*</span></span></label>
		<input type="text" name="sv_title" class="input_extra_large" value="" title="" /><br />
		<div id="sv_title"></div>
	</div>
	
	<div>
       	<label for="sv_description"><span><%= printer.print("Survey Description") %> <span class="required_field">*</span></span></label>
		<textarea  name="sv_description" class="input_extra_large" value="" /></textarea><br />
		<div id="sv_description"></div>
	</div>
	
	<div id="sv_code">
       	<label for="sv_code"><span><%= printer.print("Validation Code") %> <span class="required_field">*</span></span></label>
		<input type="text"  name="sv_code" maxlength="4" class="input_extra_large" value="<%= Survey.generateRandomValidationCode(4) %>" /><br />
		<div id="sv_code"></div>
	</div>
	
	<div>
		<label for="sv_start"><span><%= printer.print("Starting Date") %> <span class="required_field">*</span></span></label>
		<input id="sv_start" type="text" name="sv_start" class="input_large" value="" readonly="readonly" />
		<a href="javascript:NewCal('sv_start','mmddyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date and time"></a>
		<br />
		<div id="sv_start"></div>
	</div>
	
	<div>
		<label for="sv_end"><span><%= printer.print("Ending Date") %> <span class="required_field">*</span></span></label>
		<input id="sv_end" type="text" name="sv_end" class="input_large" value="" readonly="readonly" />
		<a href="javascript:NewCal('sv_end','mmddyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date and time"></a>
		<br />
		<div id="sv_end"></div>
	</div>
	
	<div>
       	<label for="sv_public_results"><span><%= printer.print("Make results public") %> <span class="required_field">*</span></span></label>
		<input type="radio" name="sv_public_results" checked="true" value="true" /> <%= printer.print("Yes") %>  		
		<input type="radio" name="sv_public_results" value="false" /> <%= printer.print("No") %><br />
		<div id="sv_public_results"></div>
	</div>
	
	</fieldset>

	<br class="clearfloat" />
	
	<div>
		<input type="checkbox" name="keep_adding" checked="true" value="true" /><%= printer.print("Continue adding surveys") %> 		
		<div id="keep_adding"></div>
	</div>
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/listSurvey.jsp?k=<%= request.getParameter("k") %>'" class="button-close"/>
	
	<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
