<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.Customer" %>
<%@ page import="datastore.CustomerManager" %>
<%@ page import="datastore.DMSV" %>
<%@ page import="datastore.DMSVManager" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>
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

<%
	int month=new Integer(request.getParameter("month"));
	String monthString = new DateFormatSymbols().getMonths()[month];
	int year= new Integer(request.getParameter("year"));
	
	List<DMSV> dmsvList = DMSVManager.getDMSVsInMonth(month, year);
%>

<h1><%= (monthString) +" "+ year%> - <%= printer.print("DMSV") %></h1>



<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">

  <tr>
    <td width="40%"><a href="/admin/ListDMSV.jsp"><%= printer.print("Back to DMSV Month List") %></a></td>
    <td width="40%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
  </tr>
  
  <tr>
  	<td width="40%"></td>
    <td width="40%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
  </tr>
  
  <tr>
  	<td width="40%"><%= printer.print("Day") %></td>
    <td width="40%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
  </tr>

<% 
  for (DMSV dmsv : dmsvList) {
%>
  <tr>
    <td width="40%"><a href="/admin/editDMSVmonth.jsp?k=<%=dmsv.getKey() + "&readonly=true" %>"><%= dmsv.getDMSVReleaseDate().getDate() + "-" + dmsv.getDMSVReleaseDate().getMonth() + "-" + (dmsv.getDMSVReleaseDate().getYear()+1900)%></a></td>
    <td width="40%"></td>
    <td width="10%"><img src="../images/edit1.jpg" width="23" height="23" /><a href="/admin/editDMSVmonth.jsp?k=<%= dmsv.getKey()%>"><%= printer.print("Edit") %></a></td>
    <td width="10%"><img src="../images/delete.jpg" width="23" height="23" /><a href="javascript:void(0);"
                       onclick="confirmDelete('/manageObject?action=delete&type=D&k=<%= dmsv.getKey()%>&month=<%=month%>&year=<%=year%>', '<%= printer.print("Are you sure you want to delete this DMSV?")%>');"><%= printer.print("Delete") %></a>
    </td>
  </tr>
<%  
  }
%>
</table>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
