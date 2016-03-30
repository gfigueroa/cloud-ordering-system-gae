<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.RestaurantOpinionPoll" %>
<%@ page import="datastore.RestaurantOpinionPollManager" %>
<%@ page import="datastore.RestaurantOpinionPollMultipleChoiceValue" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.Survey" %>
<%@ page import="datastore.SurveyManager" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="util.DateManager" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="java.util.ArrayList" %>
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
  
  Survey survey = SurveyManager.getSurvey(KeyFactory.stringToKey(request.getParameter("k")));
  String surveyType = survey.getSurveyTypeString();
  Restaurant restaurant = RestaurantManager.getRestaurant(survey.getKey().getParent());
  
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
  if(!isInteger(document.getElementsByName("op_rating_lo")[0].value)) {
    alert("<%=printer.print("The rating low value you entered is not valid")%>.");
    return false;
  }
  if(!isInteger(document.getElementsByName("op_rating_hi")[0].value)) {
    alert("<%=printer.print("The rating high value you entered is not valid")%>.");
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
      action="<%= blobstoreService.createUploadUrl("/manageRestaurant?action=update&type=S&k=" + request.getParameter("k")) %>"
      class="form-style" enctype="multipart/form-data">

    <fieldset>
	<legend><%= printer.print("Surveys") %></legend>
	
	<% if (message != null && message.equals("success") && action != null && action.equals("update")) { %>
		<div class="success-div"><%= printer.print("Survey successfully updated") %>.</div>
	<% } %>
	
	<div>
		<h2><%= readOnly ? printer.print("View Survey") : printer.print("Edit Survey") %></h2>
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
       	<label  for="sv_type"><span><%= printer.print("Survey Type") %></span></label>
		<input type="text" name="sv_type" class="input_large" value="<%= printer.print(survey.getSurveyTypeString()) %>" title="" readonly="readonly" /><br />
		<div id="sv_type"></div>
	</div>
	
    <div>
       	<label  for="sv_title"><span><%= printer.print("Survey Title") %> <span class="required_field">*</span></span></label>
		<input type="text" name="sv_title" class="input_extra_large" value="<%= survey.getSurveyTitle() %>" title="" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="sv_title"></div>
	</div>
	
	<div>
       	<label for="sv_description"><span><%= printer.print("Survey Description") %> <span class="required_field">*</span></span></label>
		<textarea  name="sv_description" class="input_extra_large" rows="<%= survey.getSurveyDescription() %>" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "readonly=\"readonly\"" : "" %>><%= survey.getSurveyDescription() %></textarea><br />
		<div id="sv_description"></div>
	</div>
	
	<%
	if (survey.getSurveyType() != Survey.Type.GLOBAL) {
	%>
		<div>
	       	<label for="sv_code"><span><%= printer.print("Survey Validation Code") %> <span class="required_field">*</span></span></label>
			<input type="text"  name="sv_code" class="input_extra_large" value="<%= survey.getValidationCode() %>" rows="4" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "readonly=\"readonly\"" : "" %>><br />
			<div id="sv_code"></div>
		</div>
	<%
	}
	%>
	
	<div>
       	<label for="sv_public_results"><span><%= printer.print("Make results public") %> <span class="required_field">*</span></span></label>
		<input type="radio" name="sv_public_results" <%= survey.resultsArePublic() ? "checked=\"true\"" : "" %> value="true" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "disabled=\"true\"" : "" %> /> <%= printer.print("Yes") %>  		
		<input type="radio" name="sv_public_results" <%= !survey.resultsArePublic() ? "checked=\"true\"" : "" %> value="false" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "disabled=\"true\"" : "" %> /> <%= printer.print("No") %><br />
		<div id="sv_public_results"></div>
	</div>
	
	<div>
        <label for="sv_status"><span><%= printer.print("Status") %></span></label>
		<input type="text" name="sv_status" class="input_extra_large" value="<%= printer.print(survey.getCurrentStatus().toString()) %>" readonly="readonly" /><br />
		<div id="sv_status"></div>
	</div>
	
	<div>
		<label for="curr_clicks"><span><%= printer.print("Current Responses") %></span></label>
		<input type="text" name="curr_clicks" class="input_extra_large" value="<%= survey.getCurrentClicks() %>" readonly="readonly" /><br />
		<div id="curr_clicks"></div>
	</div>
	
	<%
	String statistics = "";
		statistics = SurveyManager.getSurveyStatistics(survey);
	
	%>
	<div>
		<label for="op_stats"><span><%= printer.print("Current Statistics") %></span></label>
		<textarea  name="op_stats" class="input_extra_large" value="" readonly="readonly" rows="<%= statistics.split("\r\n|\r|\n").length + 1 %>"><%= statistics %></textarea><br />
		<div id="op_stats"></div>
	</div>
	
	<div>
		<label for="sv_start"><span><%= printer.print("Starting Date") %> <span class="required_field">*</span></span></label>
		<input id="sv_start" type="text" name="sv_start" class="input_large" value="<%= DateManager.printDateAsString(survey.getSurveyStartingDate()) %>" readonly="readonly" />
		<% 
		if (!readOnly) {
		%>
		<a href="javascript:NewCal('sv_start','mmddyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date and time"></a>
		<%
		}
		%>
		<div id="sv_start"></div>
	</div>
	
	<div>
		<label for="sv_end"><span><%= printer.print("Ending Date") %> <span class="required_field">*</span></span></label>
		<input id="sv_end" type="text" name="sv_end" class="input_large" value="<%= DateManager.printDateAsString(survey.getSurveyEndingDate()) %>" readonly="readonly" />
		<% 
		if (!readOnly) {
		%>
		<a href="javascript:NewCal('sv_end','mmddyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date and time"></a>
		<%
		}
		%>
		<div id="sv_end"></div>
	</div>
	
	<div>
        <label for="sv_priority"><span><%= printer.print("Priority") %> <span class="required_field">*</span></span></label>
		<input type="text" name="sv_priority" class="input_extra_large" value="<%= survey.getSurveyPriority() %>" <%= readOnly || sessionUser.getUserType() != User.Type.ADMINISTRATOR ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="sv_priority"></div>
	</div>
	
	</fieldset>

	<br class="clearfloat" />
	<% if(readOnly)	{%>
		<%--<button onClick="JavaScript:window.print();" class="button_style"><%= printer.print("Print")%></button>--%>
	<% } %>
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/listSurvey.jsp?r_key=<%= KeyFactory.keyToString(restaurant.getKey()) %>'" class="button-close"/>
	
	<%
	if (!readOnly) {
	%>
		<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>
	<%
	}
	else {
	%>
		<input type="button" value="&nbsp;&nbsp;&nbsp;<%= printer.print("Edit") %>&nbsp;&nbsp;&nbsp;" onClick="location.href='/restaurant/editSurvey.jsp?k=<%= request.getParameter("k") %>'" class="button_style">
	<%
	}
	%>
	
</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
