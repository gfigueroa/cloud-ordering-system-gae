<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.Restaurant" %>
<%@ page import="datastore.RestaurantManager" %>
<%@ page import="datastore.MessageManager" %>
<%@ page import="datastore.Message" %>
<%@ page import="util.DateManager" %>
<%@ page import="com.google.appengine.api.blobstore.BlobInfoFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobKey" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.datastore.Text" %>
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
  String msg = request.getParameter("msg");
  String action = request.getParameter("action");
  
  Message message = MessageManager.getMessage(KeyFactory.stringToKey(request.getParameter("k")));
  Restaurant restaurant = RestaurantManager.getRestaurant(message.getKey().getParent());
  
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
<script src="../js/getURLLocation.js" language="javascript" type="text/javascript"></script>

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
</script>

</head>

<body>

<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<form method="post" id="form1" name="form1" onSubmit="" 
      action="<%= blobstoreService.createUploadUrl("/manageRestaurant?action=update&type=G&k=" + request.getParameter("k")) %>"
      class="form-style" enctype="multipart/form-data">

    <fieldset>
    
    <legend><%= printer.print("Retail Store Messages") %></legend>
	
	<% if (msg != null && msg.equals("success") && action != null && action.equals("update")) { %>
		<div class="success-div"><%= printer.print("Message successfully updated") %>.</div>
	<% } %>
	
	<div>
	  <h2><%= readOnly ? printer.print("View Retail Store Message") : printer.print("Edit Retail Store Message") %></h2>
	</div>
	
	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
	
	<input type="text" name="url_location" value="" style="display:none;" id="url_location" />
	<script type="text/javascript">
	document.getElementById('url_location').value = getURLLocation();
	</script>
	
	<div>
       	<label  for="r_name"><span><%= printer.print("Retail Store Name") %></span></label>
		<input type="text" name="r_name" class="input_extra_large" value="<%= restaurant.getRestaurantName() %>" title="" readonly="readonly" /><br />
		<div id="r_name"></div>
	</div>
	
	<div>
       	<label  for="m_type"><span><%= printer.print("Message Type") %> <span class="required_field">*</span></span></label>
		<select name="m_type" <%= readOnly ? "disabled=\"true\"" : "" %> />
			<option value='text' <%= message.getMessageTypeString().equals("Text")? "selected=true" : "" %> ><%= printer.print("Text")%></option>
			<option value='multimedia'  <%= message.getMessageTypeString().equals("Multimedia")? "selected=true" : "" %>><%= printer.print("Multimedia")%></option>			
		</select><br />
		<div id="m_type"></div>
	</div>
    			
    <div>
        <label for="m_title"><span><%= printer.print("Message Title") %> <span class="required_field">*</span></span></label>
		<input type="text" name="m_title" class="input_extra_large" value="<%= message.getMessageTitle() %>" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="m_title"></div>
	</div>
		
	<div>
       	<label  for="m_author"><span><%= printer.print("Author/Speaker") %> <span class="required_field">*</span></span></label>
		<input type="text" name="m_author" class="input_extra_large"  value="<%= message.getMessageAuthor() %>" title="" <%= readOnly? "readonly=\"readonly\"" : "" %> /><br />
		<div id="m_author"></div>
	</div>
	
	<div>
       	<label for="m_t_content"><span><%= printer.print("Message Text Content") %></span></label>
		<textarea  name="m_t_content" class="input_extra_large" rows="<%= message.getMessageTextContent().getValue().split("\r\n|\r|\n").length + 1 %>" <%= readOnly || sessionUser.getUserType() != User.Type.RESTAURANT ? "readonly=\"readonly\"" : "" %>><%= message.getMessageTextContent().getValue() %></textarea><br />
		<div id="m_t_content"></div>
	</div>
	
	<%
    if (!(message.getMessageMultimediaContent() == null && readOnly)) {
    %>
    <div>
       	<label for="m_m_content"><span><%= printer.print("Message Multimedia Content") %></span></label>
       	<%
		if (message.getMessageMultimediaContent() != null) {
			BlobInfoFactory bif = new BlobInfoFactory();
			BlobKey blobKey = new BlobKey(message.getMessageMultimediaContent().getKeyString());
        	String fileName = bif.loadBlobInfo(blobKey).getFilename();
		%>
			<a href="/fileDownload?file_id=<%= message.getMessageMultimediaContent().getKeyString() %>">
				<%= fileName %>
			</a>
			<br />
		<%
		}
		%>
		<%
		if (!readOnly) {
		%>		
			<label for="m_m_content"><span></span></label><input type="file" name="m_m_content" class="input_extra_large" value="" /><br />
		<%
		}
		%>
		<div id="m_m_content"></div>
	</div>
	<%
	}
	%>

	<div>
       	<label  for="m_url"><span><%= printer.print("Message URL") %></span></label>
		<input type="text" name="m_url" class="input_extra_large" value="<%= message.getMessageURL() != null ? message.getMessageURL().getValue() : "" %>" title="" <%= readOnly? "readonly=\"readonly\"" : "" %> />
		<%
		if (message.getMessageURL() != null) {
			if (message.getMessageURL().getValue().startsWith("http://")) {
		%>
				<a href="<%= message.getMessageURL().getValue() %>" target="_new"><%= printer.print("Go to link") %></a>
		<%
			}
			else if (message.getMessageURL().getValue().startsWith("www.")) {
		%>
				<a href="<%= "http://" + message.getMessageURL().getValue() %>" target="_new"><%= printer.print("Go to link") %></a>
		<%
			}
		}
		%>
		<br />
		<div id="m_url"></div>
	</div>

	<div>
        <label for="m_status"><span><%= printer.print("Status") %></span></label>
		<input type="text" name="m_status" class="input_extra_large" value="<%= printer.print(message.getCurrentStatus().toString()) %>" readonly="readonly" /><br />
		<div id="m_status"></div>
	</div>
	
	<div>
		<label for="m_start"><span><%= printer.print("Release Date") %> <span class="required_field">*</span></span></label>
		<input id="m_start" type="text" name="m_start" class="input_large" value="<%= DateManager.printDateAsString(message.getMessageStartingDate()) %>" readonly="readonly" />
		<% 
		if (!readOnly) {
		%>
			<a href="javascript:NewCal('m_start','mmddyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date and time"></a>
		<%
		}
		%>
		<div id="m_start"></div>
	</div>
	
	<div>
		<label for="m_end"><span><%= printer.print("Expiration Date") %> <span class="required_field">*</span></span></label>
		<input id="m_end" type="text" name="m_end" class="input_large" value="<%= DateManager.printDateAsString(message.getMessageEndingDate()) %>" readonly="readonly" />
		<% 
		if (!readOnly) {
		%>
		<a href="javascript:NewCal('m_end','mmddyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date and time"></a>
		<%
		}
		%>
		<div id="m_end"></div>
	</div>
	
	</fieldset>
  
	<br class="clearfloat" />
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/listMessages.jsp?r_key=<%= KeyFactory.keyToString(restaurant.getKey()) %>'" class="button-close"/>
	
	<%
	if (!readOnly) {
	%>
		<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>
	<%
	}
	else {
	%>
		<input type="button" value="&nbsp;&nbsp;<%= printer.print("Edit") %>&nbsp;&nbsp;" onClick="location.href='/restaurant/editMessages.jsp?k=<%= request.getParameter("k") %>'" class="button_style">
	<%
	}
	%>
	
</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
