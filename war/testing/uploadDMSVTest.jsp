<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="util.DateManager" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.DMSVManager" %>
<%@ page import="datastore.DMSV" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>

<%
  User sessionUser = (User)session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("/login.jsp");
  else {
  	if (sessionUser.getUserType() != User.Type.ADMINISTRATOR) {
  		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  	}
  }
    
  List<DMSV> dmsvList = DMSVManager.getDMSVsInMonth(Calendar.JULY, 2012);
  //List<DMSV> dmsvList = DMSVManager.getDMSVsInDate(DateManager.getDateValue("9/29/2012 17:49:03"));
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />

<%@  include file="../header/page-title.html" %>
</head>

<body>

<h1>Upload DMSV Test</h1>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">

  <tr>
  	<td width="20%">Release date</td>
  	<td width="20%"></td>
    <td width="20%"></td>
    <td width="20%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
  </tr>
  
  <tr>
  	<td width="20%"></td>
  	<td width="20%"></td>
  	<td width="20%"></td>
  	<td width="20%"></td>
  	<td width="10%"></td>
    <td width="10%"></td>
  </tr>

  <% 
  for (DMSV dmsv : dmsvList) {
  %>
	  <tr>
	  	<td width="20%"><%= DateManager.printDateAsString(dmsv.getDMSVReleaseDate()) %></td>
	    <td width="20%"></td>
	    <td width="20%"></td>
	    <td width="20%"></td>
	    <td width="10%"></td>
	    <td width="10%"></td>
	  </tr>
  <%  
  }
  %>
</table>

<form method="post" id="form1" name="form1"
		action="/test?action=addDMSV"
		class="form-style">
	
	<input type="text" name="r_date" value="7/12/2012 17:49:03" style="display:none;" />
	<input type="text" name="field1" value="1" style="display:none;" />
	<input type="text" name="field2" value="2" style="display:none;" />
	<input type="text" name="field3" value="3" style="display:none;" />
	<input type="text" name="field4" value="4" style="display:none;" />
	<input type="text" name="field5" value="5" style="display:none;" />
	<input type="text" name="field6" value="6" style="display:none;" />
	<input type="text" name="field7" value="7" style="display:none;" />
	<input type="text" name="field8" value="8" style="display:none;" />
	<input type="text" name="field9" value="9" style="display:none;" />
	<input type="text" name="field10" value="10" style="display:none;" />
	<input type="text" name="field11" value="11" style="display:none;" />
	<input type="text" name="field12" value="12" style="display:none;" />
	<input type="text" name="field13" value="13" style="display:none;" />
	<input type="text" name="field14" value="14" style="display:none;" />
	<input type="text" name="field15" value="15" style="display:none;" />
	<input type="text" name="field16" value="16" style="display:none;" />
	
	<br/><br/><br/><br/>
	
	<button type="submit" value="Update" class="button"/>
		<img src="../images/update.jpg" class="img-button"/>
	</button>

</form>

<%@  include file="../header/page-footer.html" %>

</body>
</html>
