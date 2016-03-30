<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
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
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />
<link type="text/css" href="../stylesheets/colorbox.css" rel="stylesheet" />
<script src="../js/jquery.min.js"></script>
<script src="../js/jquery.colorbox-min.js"></script>
<script src="../js/popup.js"></script>
<script src="../js/confirmAction.js"></script>

<%@  include file="../header/page-title.html" %>

</head>

<body>
<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />
  <script language="javascript" src='../js/uploadFile.js'></script>


     
       <!-- *************************************** Upload tab****************************************** -->
  <div class="g-unit" id="upload-tab">
  <div class ="message" id="upload-show-message" style="display:none"></div>
  
    <div class="results" style="border:0;" id="upload-list-ctr"> 
    <form  id="file-upload" class="form-style">
    
    	<legend><%= printer.print("Upload Daily Multi-Vitamine Spiritual File") %> </legend>
    
	    <div>
	    	<h2><%= printer.print("Upload CSV File")%></h2>
	    </div>
	    <fieldset>
		    <div>
			    <table width="100%" border="0" cellspacing="0" cellpadding="0">
				    <tr>
				    	<td><label for="chosen_file"><%= printer.print("Choose the file To Upload")%>:</label><br>
				    		<label>(<%= printer.print("File must be in '.csv' format separated by commas")%>)</label></td>
				    	<td><input name="file" type="file" id="file" class="input_extra_large"></td>
				    </tr>
				    <tr>
				    	<td><input type="reset" id="upload-reset" value="reset" style="visibility:hidden"></td>
				    </tr>
			    </table>
		    </div>
	    </fieldset>
	    <br class="clearfloat" />
	    <br class="clearfloat" />
	    <br class="clearfloat" />
	    <br class="clearfloat" />
	    <input type="submit" value="<%= printer.print("Upload")%>" onclick="return validate('D')" class="button_style"> 
    </form></div>
	</div>
 

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
