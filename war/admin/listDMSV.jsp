<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="datastore.DMSV" %>
<%@ page import="datastore.DMSVManager" %>
<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.DateFormatSymbols" %>


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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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

<h1><%= printer.print("Daily Multi-Spiritual Vitamin List") %></h1>



<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">

  <tr>
    <td width="40%"><a href="/admin/addDMSV.jsp">+ <%= printer.print("Upload DMSV File") %></a></td>
    <td width="30%"></td>
    <td width="30%"></td>

  </tr>
  
  <tr>
  	<td width="40%"></td>
    <td width="30%"></td>
    <td width="30%"></td>

  </tr>
  
  <tr>
  	<td width="40%"><%= printer.print("Month & Year") %></td>
    <td width="30%"><b><%= printer.print("No. Items") %></b></td>
    <td width="30%"></td>
  </tr>

<% 
  for (int i=0;i<24;i++) {
	  Date today = new Date();
	  Date setDate= new Date(2012,i,1);
	  String monthString = new DateFormatSymbols().getMonths()[setDate.getMonth()];
	  List<DMSV> dmsvList = DMSVManager.getDMSVsInMonth(i, 2012);
%>
  <tr>
    <td width="40%"><a href="/admin/listDMSVmonth.jsp?month=<%=setDate.getMonth()%>&year=<%=setDate.getYear()%>"><%= monthString + " " + setDate.getYear()%></a></td>
    <td width="30%"><%= dmsvList.size()%></td>
    <td width="30%"><img src="../images/edit1.jpg" width="23" height="23" /><a href="/admin/listDMSVmonth.jsp?month=<%=setDate.getMonth()%>&year=<%=setDate.getYear()%>"><%= printer.print("View") %></a></td>

  </tr>
<%  
  }
%>
</table>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
