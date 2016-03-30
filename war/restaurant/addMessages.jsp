<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.Message" %>
<%@ page import="datastore.MessageManager" %>
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
</head>

<body>
<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<form method="post" id="form1" name="form1" onSubmit="" 
      action="<%= blobstoreService.createUploadUrl("/manageRestaurant?action=add&type=G") %>"
      class="form-style" enctype="multipart/form-data">

    <fieldset>
	<legend><%= printer.print("Messages") %></legend>
	
	<% if (message != null && message.equals("success") && action != null && action.equals("add")) { %>
		<div class="success-div"><%=printer.print("Message successfully created")%>.</div>
	<% } %>
	
	<div>
		<h2><%= printer.print("Add Message") %></h2>
	</div>

	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
	
	<input type="text" name="url_location" value="" style="display:none;" id="url_location" />
	<script type="text/javascript">
	document.getElementById('url_location').value = getURLLocation();
	</script>
	
	<div>
       	<label  for="m_type"><span><%= printer.print("Message Type") %> <span class="required_field">*</span></span></label>
		<select name="m_type" />
			<option value='text' selected=true><%= printer.print("Text")%></option>
			<option value='multimedia'><%= printer.print("Multimedia")%></option>			
		</select><br />
		<div id="m_type"></div>
	</div>
	
    <div>
       	<label  for="m_title"><span><%= printer.print("Message Title") %> <span class="required_field">*</span></span></label>
		<input type="text" name="m_title" class="input_extra_large"  value="" title="" /><br />
		<div id="m_title"></div>
	</div>
	
	<div>
       	<label  for="m_author"><span><%= printer.print("Author/Speaker") %> <span class="required_field">*</span></span></label>
		<input type="text" name="m_author" class="input_extra_large"  value="" title="" /><br />
		<div id="m_author"></div>
	</div>
	
	<div>
       	<label for="m_t_content"><span><%= printer.print("Message Text Content") %></span></label>
		<textarea  name="m_t_content" class="input_extra_large" value="" /></textarea><br />
		<div id="m_t_content"></div>
	</div>
	
	<div>
       	<label for="m_m_content"><span><%= printer.print("Message Multimedia Content") %></span></label>
		<input type="file" name="m_m_content" class="input_extra_large" value="" /><br />
		<div id="m_m_content">
		</div>
	</div>
	
	<div>
       	<label  for="m_url"><span><%= printer.print("Message URL") %></span></label>
		<input type="text" name="m_url" class="input_extra_large" value="" title="" /><br />
		<div id="m_url"></div>
	</div>
	
	<div>
		<label for="m_start"><span><%= printer.print("Release Date") %> <span class="required_field">*</span></label>
		<input id="m_start" type="text" name="m_start" class="input_large" value="" readonly="readonly" />
		<a href="javascript:NewCal('m_start','mmddyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date and time"></a>
		<br />
		<div id="m_start"></div>
	</div>
	
	<div>
		<label for="m_end"><span><%= printer.print("Expiration Date") %> <span class="required_field">*</span></label>
		<input id="m_end" type="text" name="m_end" class="input_large" value="" readonly="readonly" />
		<a href="javascript:NewCal('m_end','mmddyyyy',true,24)"><img src="../images/cal.gif" width="16" height="16" border="0" alt="Pick a date and time"></a>
		<br />
		<div id="m_end"></div>
	</div>
	
	</fieldset>

	<br class="clearfloat" />
	
	<div>
		<input type="checkbox" name="keep_adding" checked="true" value="true" /><%= printer.print("Continue adding messages") %> 		
		<div id="keep_adding"></div>
	</div>
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/restaurant/listMessages.jsp?k=<%= request.getParameter("k") %>'" class="button-close"/>
	
	<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
