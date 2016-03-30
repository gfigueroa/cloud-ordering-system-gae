<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.RestaurantOpinionPoll" %>
<%@ page import="datastore.RestaurantOpinionPollManager" %>
<%@ page import="datastore.SurveyOpinionPoll" %>
<%@ page import="datastore.SurveyOpinionPollManager" %>
<%@ page import="datastore.Survey" %>
<%@ page import="datastore.SurveyManager" %>
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
  
  String opinionPollType = request.getParameter("op_type");
  if (opinionPollType == null) {
  	opinionPollType = "binary";
  }
  
  BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

  Survey survey=null;
  String surveyTitle=null;
  String parent = null;
  if (request.getParameter("sk") != null) {
 	survey = SurveyManager.getSurvey(KeyFactory.stringToKey(request.getParameter("sk")));
  	surveyTitle= survey.getSurveyTitle();
  	parent = "survey";
  }
  else {
    parent = "store";
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

<script language="Javascript" type="text/javascript">

function refresh(op_type) {
    //window.location.replace("addOpinionPoll.jsp?op_type=" + op_type);
     b=document.getElementById('binary');
     r=document.getElementById('rating');
     m=document.getElementById('multiple_choice');
     f=document.getElementById('free_response');
     if (op_type=="binary") {
     		b.style.display="block";
	 		r.style.display="none";
	 		m.style.display="none";
	 		f.style.display="none";
	 }
	 if (op_type=="rating") {
     		b.style.display="none";
	 		r.style.display="block";
	 		m.style.display="none";
	 		f.style.display="none";
	 }
	 if (op_type=="multiple_choice") {
     		b.style.display="none";
	 		r.style.display="none";
	 		m.style.display="block";
	 		f.style.display="none";
	 }
	 if (op_type=="free_response") {
     		b.style.display="none";
	 		r.style.display="none";
	 		m.style.display="none";
	 		f.style.display="block";
	 }
}

function multi_number(op_multi_value_num) {
	for (i=1;i<=15;i++) {
		option_value='op_multi_values'+i;
		x1=document.getElementById(option_value);
		if(i<=1*op_multi_value_num){
			x1.style.display="block";
		}
		else{
			x1.style.display="none";
			x1.value="";
		}
	}
}

function rating(rating_option){
	a1=document.getElementById('rating_default');
	b1=document.getElementById('rating_non_default');
	if(rating_option=="true"){
		a1.style.display="block";
		b1.style.display="none";
	}
	else{
		a1.style.display="none";
		b1.style.display="block";
	}
}

onload=function() {
	rating('false'); 
	multi_number('3');
	refresh('binary'); 
}

</script>

</head>

<body>
<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<form method="post" id="form1" name="form1" onSubmit="return checkNumericValues();" 
      action="<%= blobstoreService.createUploadUrl("/manageRestaurant?action=add&type=L") %>"
      class="form-style" enctype="multipart/form-data">

    <fieldset>
	
	<%
	if(!parent.equals("survey")) {
	%>
		<legend><%= printer.print("Retail Store Opinion Poll") %></legend>
	<%
	}
	else {
	%>
		<legend><%= printer.print("Survey") %>: <%= surveyTitle %></legend>
		<input type="text" name="sk" value="<%= request.getParameter("sk") %>" style="display:none;" />
	<%
	}
	%>
	
	<% if (message != null && message.equals("success") && action != null && action.equals("add")) { %>
		<div class="success-div"><%= printer.print("Opinion poll successfully created") %>.</div>
	<% } %>
	
	<div>
		<h2><%= parent.equals("store") ? printer.print("Add Retail Store Opinion Poll") : printer.print("Add Survey Opinion Poll") %></h2>
	</div>

	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
	
	<div>
		<label for="op_type"><span><%= printer.print("Opinion Poll Type") %> <span class="required_field">*</span></span></label>
		<select name="op_type" onChange="refresh(this.value);">
        <option value="binary" <%= opinionPollType.equals("binary") ? "selected=\"true\"" : "" %>><%= printer.print("Binary") %></option>
        <option value="rating" <%= opinionPollType.equals("rating") ? "selected=\"true\"" : "" %>><%= printer.print("Rating") %></option>
        <option value="multiple_choice" <%= opinionPollType.equals("multiple_choice") ? "selected=\"true\"" : "" %>><%= printer.print("Multiple Choice") %></option>
        <option value="free_response" <%= opinionPollType.equals("free_response") ? "selected=\"true\"" : "" %>><%= printer.print("Free response") %></option>
        </select>
		<div id="op_type"></div>
	</div>
	
    <div>
       	<label  for="op_title"><span><%= printer.print("Opinion Poll Title") %> <span class="required_field">*</span></span></label>
		<input type="text" name="op_title" class="input_extra_large"  value="" title="" /><br />
		<div id="op_title"></div>
	</div>
	
	<div>
       	<label for="op_content"><span><%= printer.print("Opinion Poll Content") %> <span class="required_field">*</span></span></label>
		<textarea  name="op_content" class="input_extra_large" value="" /></textarea><br />
		<div id="op_content"></div>
	</div>
	
	<%
	if(!parent.equals("survey")){
	%>	
		<div>
			<label for="op_start"><span><%= printer.print("Release Date") %> <span class="required_field">*</span></span></label>
			<input id="op_start" type="text" name="op_start" class="input_large" value="" readonly="readonly" />
			<a href="javascript:NewCal('op_start','mmddyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date and time"></a>
			<br />
			<div id="op_start"></div>
		</div>
		
		<div>
			<label for="op_end"><span><%= printer.print("Expiration Date") %> <span class="required_field">*</span></span></label>
			<input id="op_end" type="text" name="op_end" class="input_large" value="" readonly="readonly" />
			<a href="javascript:NewCal('op_end','mmddyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date and time"></a>
			<br />
			<div id="op_end"></div>
		</div>
		
		<div>
	       	<label for="op_public_results"><span><%= printer.print("Make results public") %> <span class="required_field">*</span></span></label>
			<input type="radio" name="op_public_results" checked="true" value="true" /> <%= printer.print("Yes") %>  		
			<input type="radio" name="op_public_results" value="false" /> <%= printer.print("No") %><br />
			<div id="op_public_results"></div>
		</div>
	
	
    <div>
       	<label for="op_image"><span><%= printer.print("Image") %> </span></label>
		<input type="file" name="op_image" class="input_extra_large" value="" /><br />
		<div id="op_image">
		</div>
	</div>
	
	<% 
	} 
	%>
	
	<div id="binary">
		<div>
	       	<label  for="op_binary1"><span><%= printer.print("Binary Choice 1") %> <span class="required_field">*</span></span></label>
			<input type="text" name="op_binary1" class="input_extra_large"  value="" title="" /><br />
			<div id="op_binary1"></div>
		</div>
	
		<div>
	       	<label  for="op_binary2"><span><%= printer.print("Binary Choice 2") %> <span class="required_field">*</span></span></label>
			<input type="text" name="op_binary2" class="input_extra_large"  value="" title="" /><br />
			<div id="op_binary2"></div>
		</div>
	</div>
	
	<div id="rating">
		<div>
	       	<label for="op_rating"><span><%= printer.print("Use default Rating values? (1 to 5)") %> <span class="required_field">*</span></span></label>
			<input type="radio" name="op_rating"  value="true" onClick="rating(this.value); "/> <%= printer.print("Yes") %>  		
			<input type="radio" name="op_rating" checked="true" value="false" onClick="rating(this.value);" /> <%= printer.print("No") %><br />
			<div id="op_rating"></div>
		</div>
		
		<div id="rating_default">
			<div>
		       	<label  for="op_rating_1"><span><%= printer.print("Value:1 (Lowest)") %> <span class="required_field">*</span></span></label>
				<input type="text" name="op_rating_1" class="input_extra_large"  value="<%= printer.print("Strongly Disagree") %>" title="" /><br />
				<div id="op_rating_1"></div>
			</div>
			
			<div>
		       	<label  for="op_rating_2"><span><%= printer.print("Value:2") %> <span class="required_field">*</span></span></label>
				<input type="text" name="op_rating_2" class="input_extra_large"  value="<%= printer.print("Disagree") %>" title="" /><br />
				<div id="op_rating_2"></div>
			</div>
			<div>
		       	<label  for="op_rating_3"><span><%= printer.print("Value:3") %> <span class="required_field">*</span></span></label>
				<input type="text" name="op_rating_3" class="input_extra_large"  value="<%= printer.print("Neutral") %>" title="" /><br />
				<div id="op_rating_3"></div>
			</div>
			
			<div>
		       	<label  for="op_rating_4"><span><%= printer.print("Value:4") %> <span class="required_field">*</span></span></label>
				<input type="text" name="op_rating_4" class="input_extra_large"  value="<%= printer.print("Agree") %>" title="" /><br />
				<div id="op_rating_4"></div>
			</div>
			<div>
		       	<label  for="op_rating_5"><span><%= printer.print("Value:5 (Highest)") %> <span class="required_field">*</span></span></label>
				<input type="text" name="op_rating_5" class="input_extra_large"  value="<%= printer.print("Strongly Agree") %>" title="" /><br />
				<div id="op_rating_5"></div>
			</div>
		
		</div>
		
		<div id="rating_non_default">
			<div>
		       	<label  for="op_rating_lo"><span><%= printer.print("Rating Low Value") %> <span class="required_field">*</span></span></label>
				<input type="text" name="op_rating_lo" class="input_extra_large"  value="" title="" /><br />
				<div id="op_rating_lo"></div>
			</div>
			
			<div>
		       	<label  for="op_rating_hi"><span><%= printer.print("Rating High Value") %> <span class="required_field">*</span></span></label>
				<input type="text" name="op_rating_hi" class="input_extra_large"  value="" title="" /><br />
				<div id="op_rating_hi"></div>
			</div>
		</div>
	</div>
	
	<div id="multiple_choice">
		<div>
	       	<label for="op_allow_multi"><span><%= printer.print("Multiple Selection") %> <span class="required_field">*</span></span></label>
			<input type="radio" name="op_allow_multi" checked="true" value="true" /> <%= printer.print("Yes") %>  		
			<input type="radio" name="op_allow_multi" value="false" /> <%= printer.print("No") %><br />
			<div id="op_allow_multi"></div>
		</div>
		
		<div>
			<label for="op_multiple_value_num"><span><%= printer.print("Multiple-Choice No. Items") %> <span class="required_field">*</span></span></label>
			<select name="op_multiple_value_num" onChange="multi_number(this.value);">
	       	<%for (int i=1;i<=15;i=i+1){ %>
	       		<%if(i==3){%>
	       			<option value="<%=i%>" selected="true"><%=i%></option>
				<%}else{%>
					<option value="<%=i%>"><%=i%></option>
				<%} %>
			<%} %>
	
	        </select>
			<div id="op_multiple_value_num"></div>
		</div>	
		
		<div>
	       	<label  for="op_multi_value12"><span><%= printer.print("Multi-Choice Values") %> <span class="required_field">*</span></span></label>
			<%for (int i=1;i<=15;i=i+1){ %>
				<input type="text" id="op_multi_values<%=i%>" name="op_multi_values<%=i%>" class="input_large" value="" title="" /><br />
				<label  for="op_multi_valuesx<%=i%>" ><span> </span></label>
				
			<%} %>
			<div id="op_multi_values"></div>
		</div>
	</div>
	
	</fieldset>

	<br class="clearfloat" />
	
	<div>
		<input type="checkbox" name="keep_adding" checked="true" value="true" /><%= printer.print("Continue adding opinion polls") %> 		
		<div id="keep_adding"></div>
	</div>
	<%
	if(!parent.equals("survey")){
		%>	
		<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/listOpinionPoll.jsp?k=<%= request.getParameter("k") %>'" class="button-close"/>
		<%
	}
	else{
		%>
		<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/listOpinionPoll.jsp?sk=<%= request.getParameter("sk") %>'" class="button-close"/>
		<%
	}
	%>

	<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
