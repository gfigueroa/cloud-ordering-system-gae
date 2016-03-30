<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.Administrator" %>
<%@ page import="datastore.AdministratorManager" %>
<%@ page import="datastore.DMSV" %>
<%@ page import="datastore.DMSVManager" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="java.text.DateFormatSymbols" %>
<%@ page import="util.DateManager" %>
<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>


<% 
Printer printer = (Printer)session.getAttribute("printer");
%>
<jsp:include page="../header/language-header.jsp" />

<%
  User sessionUser = (User)session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("../login.jsp");
  else {
  	if (sessionUser.getUserType() != User.Type.ADMINISTRATOR) {
  		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  	}
  }
  
  String message = request.getParameter("msg");
  String action = request.getParameter("action");
  String updateType = request.getParameter("update_type");
  boolean readOnly = request.getParameter("readonly") != null ? true : false;
  String error = request.getParameter("etype");
  Long key= new Long(request.getParameter("k"));
  DMSV dmsv = DMSVManager.getDMSV(key);
  int day =dmsv.getDMSVReleaseDate().getDate();
  int month =dmsv.getDMSVReleaseDate().getMonth();
  int year = 1900+dmsv.getDMSVReleaseDate().getYear();
  String monthString = new DateFormatSymbols().getMonths()[month];
  
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />
<%@  include file="../header/page-title.html" %>
</head>

<body>

<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<form method="post" id="form1" name="form1" 
      action="/manageObject?action=update&type=D&k=<%= request.getParameter("k") %>" class="form-style">

    <fieldset>
	<legend><%= printer.print("DMSV") %> - <%= monthString + "/" + day + "/" + year %></legend>
	
	<% if (message != null && message.equals("success") && action != null && action.equals("update")) { %>
		<div class="success-div"><%= printer.print("DMSV successfully updated") %>.</div>
	<% } %>
	
	<div>
	  <h2><%= readOnly ? printer.print("View DMSV") : printer.print("Edit DMSV") %></h2>
	</div>

	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
	    
		
    <div>
        <label for="d_date"><span><%= printer.print("Date") %> <span class="required_field">*</span></span></label>
		<input type="text" name="d_date" class="input_extra_large" value="<%= DateManager.printDateAsString(dmsv.getDMSVReleaseDate())%>" <%= readOnly ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="d_date"></div>
	</div>
    
    <div>
       	<label for="d_field_1"><span><%= printer.print("Field 1") %></span></label>
		<textarea  name="d_field_1" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= dmsv.getField1().getValue() %></textarea><br />
		<div id="d_field_1"></div>
	</div>
	
	<div>
       	<label for="d_field_2"><span><%= printer.print("Field 2") %></span></label>
		<textarea  name="d_field_2" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= dmsv.getField2().getValue() %></textarea><br />
		<div id="d_field_2"></div>
	</div>
    <div>
       	<label for="d_field_3"><span><%= printer.print("Field 3") %></span></label>
		<textarea  name="d_field_3" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= dmsv.getField3().getValue() %></textarea><br />
		<div id="d_field_3"></div>
	</div>
	
	<div>
       	<label for="d_field_4"><span><%= printer.print("Field 4") %></span></label>
		<textarea  name="d_field_4" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= dmsv.getField4().getValue() %></textarea><br />
		<div id="d_field_4"></div>
	</div>	
    
    <div>
       	<label for="d_field_5"><span><%= printer.print("Field 5") %></span></label>
		<textarea  name="d_field_5" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= dmsv.getField5().getValue() %></textarea><br />
		<div id="d_field_5"></div>
	</div>
	
	<div>
       	<label for="d_field_6"><span><%= printer.print("Field 6") %></span></label>
		<textarea  name="d_field_6" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= dmsv.getField6().getValue() %></textarea><br />
		<div id="d_field_6"></div>
	</div>
    <div>
       	<label for="d_field_7"><span><%= printer.print("Field 7") %></span></label>
		<textarea  name="d_field_7" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= dmsv.getField7().getValue() %></textarea><br />
		<div id="d_field_7"></div>
	</div>
	
	<div>
       	<label for="d_field_8"><span><%= printer.print("Field 8") %></span></label>
		<textarea  name="d_field_8" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= dmsv.getField8().getValue() %></textarea><br />
		<div id="d_field_8"></div>
	</div>
    
    <div>
       	<label for="d_field_9"><span><%= printer.print("Field 9") %></span></label>
		<textarea  name="d_field_9" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= dmsv.getField9().getValue() %></textarea><br />
		<div id="d_field_9"></div>
	</div>
	
	<div>
       	<label for="d_field_10"><span><%= printer.print("Field 10") %></span></label>
		<textarea  name="d_field_10" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= dmsv.getField10().getValue() %></textarea><br />
		<div id="d_field_10"></div>
	</div>
	
    <div>
       	<label for="d_field_11"><span><%= printer.print("Field 11") %></span></label>
		<textarea  name="d_field_11" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= dmsv.getField11().getValue() %></textarea><br />
		<div id="d_field_11"></div>
	</div>
	
	<div>
       	<label for="d_field_12"><span><%= printer.print("Field 12") %></span></label>
		<textarea  name="d_field_12" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= dmsv.getField12().getValue() %></textarea><br />
		<div id="d_field_12"></div>
	</div>	
	
	<div>
       	<label for="d_field_13"><span><%= printer.print("Field 13") %></span></label>
		<textarea  name="d_field_13" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= dmsv.getField13().getValue() %></textarea><br />
		<div id="d_field_13"></div>
	</div>	
	
	<div>
       	<label for="d_field_14"><span><%= printer.print("Field 14") %></span></label>
		<textarea  name="d_field_14" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= dmsv.getField14().getValue() %></textarea><br />
		<div id="d_field_14"></div>
	</div>	
	
	<div>
       	<label for="d_field_15"><span><%= printer.print("Field 15") %></span></label>
		<textarea  name="d_field_15" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= dmsv.getField15().getValue() %></textarea><br />
		<div id="d_field_15"></div>
	</div>	
	
	<div>
       	<label for="d_field_16"><span><%= printer.print("Field 16") %></span></label>
		<textarea  name="d_field_16" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= dmsv.getField16().getValue() %></textarea><br />
		<div id="d_field_16"></div>
	</div>	

	</fieldset>
  	
  	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/admin/listDMSVmonth.jsp?month=<%= month%>&year=<%= year%>'" class="button-close"/>
	
	<%
	if (!readOnly) {
	%>
		<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>
	<%
	}
	else {
	%>
		<input type="button" value="&nbsp;&nbsp;&nbsp;<%= printer.print("Edit") %>&nbsp;&nbsp;&nbsp;" onClick="location.href='/admin/editDMSVmonth.jsp?k=<%= request.getParameter("k") %>'" class="button_style">
	<%
	}
	%>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
