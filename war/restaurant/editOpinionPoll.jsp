<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.RestaurantOpinionPoll" %>
<%@ page import="datastore.RestaurantOpinionPollManager" %>
<%@ page import="datastore.RestaurantOpinionPollMultipleChoiceValue" %>
<%@ page import="datastore.SurveyOpinionPoll" %>
<%@ page import="datastore.SurveyOpinionPollManager" %>
<%@ page import="datastore.SurveyOpinionPollMultipleChoiceValue" %>
<%@ page import="datastore.Survey" %>
<%@ page import="datastore.SurveyManager" %>
<%@ page import="datastore.Restaurant" %>
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

  RestaurantOpinionPoll restaurantOpinionPoll=null;
  SurveyOpinionPoll surveyOpinionPoll=null;

  String opinionPollType=null;
  
  Restaurant restaurant=null;
  
  Survey survey=null;
  String surveyTitle=null;
  String parent = null;
  if (request.getParameter("sk") != null) {
 	survey = SurveyManager.getSurvey(KeyFactory.stringToKey(request.getParameter("sk")));
  	surveyTitle= survey.getSurveyTitle();
  	
  	surveyOpinionPoll = SurveyOpinionPollManager.getSurveyOpinionPoll(KeyFactory.stringToKey(request.getParameter("k")));
  	opinionPollType = surveyOpinionPoll.getSurveyOpinionPollTypeString();
  	parent = "survey";
  	restaurant = RestaurantManager.getRestaurant(survey.getKey().getParent());
  }
  else {
  	restaurantOpinionPoll = RestaurantOpinionPollManager.getRestaurantOpinionPoll(KeyFactory.stringToKey(request.getParameter("k")));
  	opinionPollType = restaurantOpinionPoll.getRestaurantOpinionPollTypeString();
  	parent = "store";
  	restaurant = RestaurantManager.getRestaurant(restaurantOpinionPoll.getKey().getParent());
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
      action="<%= blobstoreService.createUploadUrl("/manageRestaurant?action=update&type=L&k=" + request.getParameter("k")) %>"
      class="form-style" enctype="multipart/form-data">

    <fieldset>
	<%
	if(!parent.equals("survey")) {
	%>
		<legend><%= printer.print("Retail Store Opinion Poll") %></legend>
	<%}
	else {
	%>
		<legend><%= printer.print("Survey") %>: <%= surveyTitle %></legend>
		<input type="text" name="sk" value="<%= request.getParameter("sk") %>" style="display:none;" />
	<%
	}
	%>
	
	<% if (message != null && message.equals("success") && action != null && action.equals("update")) { %>
		<div class="success-div"><%= printer.print("Opinion Poll successfully updated") %>.</div>
	<% } %>
	
	<div>
		<h2>
		<%
		if (parent.equalsIgnoreCase("store")) {
		%>
			<%= readOnly ? printer.print("View Retail Store Opinion Poll") : printer.print("Edit Retail Store Opinion Poll") %>
		<%
		}
		else {
		%>
			<%= readOnly ? printer.print("View Survey Opinion Poll") : printer.print("Edit Survey Opinion Poll") %>
		<%
		}
		%>
		</h2>
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
       	<label  for="op_type"><span><%= printer.print("Opinion Poll Type") %></span></label>
		<input type="text" name="op_type" class="input_large" value="<%= (parent.equalsIgnoreCase("store"))?printer.print(restaurantOpinionPoll.getRestaurantOpinionPollTypeString()):printer.print(surveyOpinionPoll.getSurveyOpinionPollTypeString()) %>" title="" readonly="readonly" /><br />
		<div id="op_type"></div>
	</div>
	
    <div>
       	<label  for="op_title"><span><%= printer.print("Opinion Poll Title") %> <span class="required_field">*</span></span></label>
		<input type="text" name="op_title" class="input_extra_large"  value="<%= (parent.equalsIgnoreCase("store"))? restaurantOpinionPoll.getRestaurantOpinionPollTitle():surveyOpinionPoll.getSurveyOpinionPollTitle()  %>" title="" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="op_title"></div>
	</div>
	
	<div>
       	<label for="op_content"><span><%= printer.print("Opinion Poll Content") %> <span class="required_field">*</span></span></label>
		<textarea  name="op_content" class="input_extra_large" rows="<%= (parent.equalsIgnoreCase("store"))? restaurantOpinionPoll.getRestaurantOpinionPollContent().split("\r\n|\r|\n").length + 1: surveyOpinionPoll.getSurveyOpinionPollContent().split("\r\n|\r|\n").length + 1 %>" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "readonly=\"readonly\"" : "" %>><%= (parent.equalsIgnoreCase("store"))? restaurantOpinionPoll.getRestaurantOpinionPollContent(): surveyOpinionPoll.getSurveyOpinionPollContent() %></textarea><br />
		<div id="op_content"></div>
	</div>
	
	<div>
        <label for="op_status"><span><%= printer.print("Status") %></span></label>
		<input type="text" name="op_status" class="input_extra_large" value="<%= (parent.equalsIgnoreCase("store"))?  printer.print(restaurantOpinionPoll.getCurrentStatus().toString()):printer.print(survey.getCurrentStatus().toString()) %>" readonly="readonly" /><br />
		<div id="op_status"></div>
	</div>
	
	<div>
		<label for="curr_clicks"><span><%= printer.print("Poll Current Responses") %></span></label>
		<input type="text" name="curr_clicks" class="input_extra_large" value="<%= (parent.equalsIgnoreCase("store"))?restaurantOpinionPoll.getCurrentClicks():surveyOpinionPoll.getCurrentClicks() %>" readonly="readonly" /><br />
		<div id="curr_clicks"></div>
	</div>
	
	<%
	String statistics = "";
	if(parent.equalsIgnoreCase("store")) { 
		statistics = RestaurantOpinionPollManager.getRestaurantOpinionPollStatistics(restaurantOpinionPoll);
	}
	else {
		statistics = SurveyOpinionPollManager.getSurveyOpinionPollStatistics(surveyOpinionPoll);
	}
	%>
	<div>
		<label for="op_stats"><span><%= printer.print("Current Statistics") %></span></label>
		<textarea  name="op_stats" class="input_extra_large" value="" readonly="readonly" rows="<%= statistics.split("\r\n|\r|\n").length + 1 %>"><%= statistics %></textarea><br />
		<div id="op_stats"></div>
	</div>
	
	<%
	if (parent.equalsIgnoreCase("store")) {
	%>
		<div>
			<label for="op_start"><span><%= printer.print("Release Date") %> <span class="required_field">*</span></span></label>
			<input id="op_start" type="text" name="op_start" class="input_large" value="<%= DateManager.printDateAsString(restaurantOpinionPoll.getRestaurantOpinionPollStartingDate()) %>" readonly="readonly" />
			<% 
			if (!readOnly) {
			%>
			<a href="javascript:NewCal('op_start','mmddyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date and time"></a>
			<%
			}
			%>
			<div id="op_start"></div>
		</div>
		
		<div>
			<label for="op_end"><span><%= printer.print("Expiration Date") %> <span class="required_field">*</span></span></label>
			<input id="op_end" type="text" name="op_end" class="input_large" value="<%= DateManager.printDateAsString(restaurantOpinionPoll.getRestaurantOpinionPollEndingDate()) %>" readonly="readonly" />
			<% 
			if (!readOnly) {
			%>
			<a href="javascript:NewCal('op_end','mmddyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date and time"></a>
			<%
			}
			%>
			<div id="op_end"></div>
		</div>
		
		<div>
	        <label for="op_priority"><span><%= printer.print("Priority") %> <span class="required_field">*</span></span></label>
			<input type="text" name="op_priority" class="input_extra_large" value="<%= restaurantOpinionPoll.getRestaurantOpinionPollPriority() %>" <%= readOnly || sessionUser.getUserType() != User.Type.ADMINISTRATOR ? "readonly=\"readonly\"" : "" %> /><br />
			<div id="op_priority"></div>
		</div>
		
		<div>
	       	<label for="op_public_results"><span><%= printer.print("Make results public") %> <span class="required_field">*</span></span></label>
			<input type="radio" name="op_public_results" <%= restaurantOpinionPoll.resultsArePublic() ? "checked=\"true\"" : "" %> value="true" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "disabled=\"true\"" : "" %> /> <%= printer.print("Yes") %>  		
			<input type="radio" name="op_public_results" <%= !restaurantOpinionPoll.resultsArePublic() ? "checked=\"true\"" : "" %> value="false" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "disabled=\"true\"" : "" %> /> <%= printer.print("No") %><br />
			<div id="op_public_results"></div>
		</div>

		<%
		if (!(restaurantOpinionPoll.getRestaurantOpinionPollImage() == null && (readOnly || sessionUser.getUserType() != User.Type.RESTAURANT))) {
		%>
		    <div>
		       	<label for="op_image"><span><%= printer.print("Image") %></span></label>
		       	<%
				if (restaurantOpinionPoll.getRestaurantOpinionPollImage() != null) {
				%>
					<a target="_new" href="/img?blobkey=<%= restaurantOpinionPoll.getRestaurantOpinionPollImage().getKeyString() %>">
					<img src="/img?blobkey=<%= restaurantOpinionPoll.getRestaurantOpinionPollImage().getKeyString() %>&s=300">
					</a>
					<br />
				<%
				}
				%>
				<% 
				if (!readOnly && sessionUser.getUserType() == User.Type.RESTAURANT) {
				%>
					<label><span> </span></label><input type="file" name="op_image" class="input_extra_large" value="" /><br />
				<%
				}
				%>
				<div id="op_image"></div>
			</div>
		<%
		}
		%>
	<% 
	} 
	%>
	
	<%
	if (opinionPollType.equals("Binary")) {
	%>
		<div>
	       	<label  for="op_binary1"><span><%= printer.print("Binary Choice 1") %> <span class="required_field">*</span></span></label>
			<input type="text" name="op_binary1" class="input_extra_large" value="<%= (parent.equalsIgnoreCase("store"))?restaurantOpinionPoll.getBinaryChoice1() : surveyOpinionPoll.getBinaryChoice1() %>" title="" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "readonly=\"readonly\"" : "" %> /><br />
			<div id="op_binary1"></div>
		</div>
	
		<div>
	       	<label  for="op_binary2"><span><%= printer.print("Binary Choice 2") %> <span class="required_field">*</span></span></label>
			<input type="text" name="op_binary2" class="input_extra_large" value="<%= (parent.equalsIgnoreCase("store"))?restaurantOpinionPoll.getBinaryChoice2() : surveyOpinionPoll.getBinaryChoice2() %>" title="" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "readonly=\"readonly\"" : "" %> /><br />
			<div id="op_binary2"></div>
		</div>
	<%
	}
	%>
	
	<%
	if (opinionPollType.equals("Rating")) {
	%>
		<div>
	       	<label  for="op_rating_lo"><span><%= printer.print("Rating Low Value") %> <span class="required_field">*</span></span></label>
			<input type="text" name="op_rating_lo" class="input_extra_large"  value="<%= (parent.equalsIgnoreCase("store"))?restaurantOpinionPoll.getRatingLowValue():surveyOpinionPoll.getRatingLowValue() %>" title="" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "readonly=\"readonly\"" : "" %> /><br />
			<div id="op_rating_lo"></div>
		</div>
		
		<div>
	       	<label  for="op_rating_hi"><span><%= printer.print("Rating High Value") %> <span class="required_field">*</span></span></label>
			<input type="text" name="op_rating_hi" class="input_extra_large"  value="<%= (parent.equalsIgnoreCase("store"))?restaurantOpinionPoll.getRatingHighValue():surveyOpinionPoll.getRatingHighValue() %>" title="" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "readonly=\"readonly\"" : "" %> /><br />
			<div id="op_rating_hi"></div>
		</div>
	<%
	}
	%>
	
	<%
	if (opinionPollType.equals("Multiple Choice")) {
		ArrayList<RestaurantOpinionPollMultipleChoiceValue> multipleChoiceValues = parent.equalsIgnoreCase("store") ? restaurantOpinionPoll.getMultipleChoiceValues() : null;
		ArrayList<SurveyOpinionPollMultipleChoiceValue> surveymultipleChoiceValues = parent.equalsIgnoreCase("survey") ? surveyOpinionPoll.getMultipleChoiceValues() : null;
		int size = parent.equalsIgnoreCase("store") ? multipleChoiceValues.size() : surveymultipleChoiceValues.size();
	%>
		<%
		if(parent.equalsIgnoreCase("store")) {
		%>
		<div>
	       	<label for="op_allow_multi"><span><%= printer.print("Multiple Selection") %> <span class="required_field">*</span></span></label>
			<input type="radio" name="op_allow_multi" <%=  restaurantOpinionPoll.allowsMultipleSelection() ? "checked=\"true\"" : "" %> value="true" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "disabled=\"true\"" : "" %> /> Yes 		
			<input type="radio" name="op_allow_multi" <%= !restaurantOpinionPoll.allowsMultipleSelection() ? "checked=\"true\"" : "" %> value="false" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "disabled=\"true\"" : "" %> /> No<br />
			<div id="op_allow_multi"></div>
		</div>
		<%
		}
		else {
		%>
		<div>
	       	<label for="op_allow_multi"><span><%= printer.print("Multiple Selection") %> <span class="required_field">*</span></span></label>
			<input type="radio" name="op_allow_multi" <%=  surveyOpinionPoll.allowsMultipleSelection() ? "checked=\"true\"" : "" %> value="true" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "disabled=\"true\"" : "" %> /> Yes 		
			<input type="radio" name="op_allow_multi" <%= !surveyOpinionPoll.allowsMultipleSelection() ? "checked=\"true\"" : "" %> value="false" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "disabled=\"true\"" : "" %> /> No<br />
			<div id="op_allow_multi"></div>
		</div>
		<%
		}
		%>
		
		<div>
			<label for="op_multiple_value_num"><span><%= printer.print("Multiple-Choice No. Items") %> <span class="required_field">*</span></span></label>
			<select name="op_multiple_value_num" readonly="readonly" disabled="disabled">
	       	<%for (int i=1;i<=15;i=i+1){ %>
	       		<%if(i==size){%>
	       			<option value="<%=i%>" selected="true"><%=i%></option>
				<%}else{%>
					<option value="<%=i%>"><%=i%></option>
				<%} %>
			<%} %>
	
	        </select>
			<div id="op_multiple_value_num"></div>
		</div>
		
		<%
		if(parent.equalsIgnoreCase("store")) {
		%>
			<div>
		       	<label  for="op_multi_value12"><span><%= printer.print("Multi-Choice Values") %></span></span></label>
				<%for (int i=1;i<=size;i=i+1){ %>
					<input type="text" id="op_multi_values<%=i%>" name="op_multi_values<%=i%>" class="input_large" value="<%= size > i-1 ? multipleChoiceValues.get(i-1).getMultipleChoiceValue() : "" %>" title="" readonly="readonly" /><br />
					<label  for="op_multi_valuesx<%=i%>" ><span> </span></label>
					
				<%} %>
				<div id="op_multi_values"></div>
			</div>
		
		<%
		}
		else {
		%>
			<div>
		       	<label  for="op_multi_value12"><span><%= printer.print("Multi-Choice Values") %></span></span></label>
				<%for (int i=1;i<=size;i=i+1){ %>
					<input type="text" id="op_multi_values<%=i%>" name="op_multi_values<%=i%>" class="input_large" value="<%= size > i-1 ? surveymultipleChoiceValues.get(i-1).getMultipleChoiceValue() : "" %>" title="" readonly="readonly" /><br />
					<label  for="op_multi_valuesx<%=i%>" ><span> </span></label>
					
				<%} %>
				<div id="op_multi_values"></div>
			</div>
		<%
		}
		%>
	<%
	}
	%>
	
	</fieldset>

	<br class="clearfloat" />
	
	<%
	if(parent.equals("store")){
	%>
		<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/listOpinionPoll.jsp?r_key=<%= KeyFactory.keyToString(restaurant.getKey()) %>'" class="button-close"/>
	<%
	}
	else{
	%>
		<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/listOpinionPoll.jsp?sk=<%= request.getParameter("sk") %>'" class="button-close"/>
	<%
	}
	%>
	<%
	if (!readOnly) {
	%>
		<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>
	<%
	}
	else {
		if(parent.equalsIgnoreCase("store")){
	%>
			<input type="button" value="&nbsp;&nbsp;<%= printer.print("Edit") %>&nbsp;&nbsp;" onClick="location.href='/restaurant/editOpinionPoll.jsp?k=<%= request.getParameter("k")%>'" class="button_style">
	<%
		} 
		else {
	%>
			<input type="button" value="&nbsp;&nbsp;<%= printer.print("Edit") %>&nbsp;&nbsp;" onClick="location.href='/restaurant/editOpinionPoll.jsp?k=<%= request.getParameter("k") + "&sk=" + request.getParameter("sk") %>'" class="button_style">
	<%
		}
	}
	%>
</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
