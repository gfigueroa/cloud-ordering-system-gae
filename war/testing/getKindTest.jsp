<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>
<%@ page import="datastore.MenuItemManager" %>
<%@ page import="datastore.MenuItem" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />

<% 
Printer printer = new Printer(Dictionary.Language.CHINESE);
 String setLanguage=printer.getLanguageString();
 
List<MenuItem> restaurants = MenuItemManager.getAllMenuItemsByType(KeyFactory.stringToKey("agpzbWFzcnYtY29zcjkLEgpSZXN0YXVyYW50IhZDaGluZXNlRm9vZEBzbWFzcnYuY29tDAsSDE1lbnVJdGVtVHlwZRjpAQw"));
 
%>

<%@  include file="../header/page-title.html" %>
<script language="JavaScript" type="text/javascript">
function alerts(){
alert("<%=printer.print("Actions")%>");
}
</script>
</head>

<body>

<%
for (MenuItem restaurant : restaurants) {
%>

|<%= restaurant.getKey().getKind() %>|

<br/>
<%
}
%>


<p><%= setLanguage%></p>
<%@  include file="../header/page-footer.html" %>
<input type="button" onClick="alerts();" value="hola">
</body>
</html>
