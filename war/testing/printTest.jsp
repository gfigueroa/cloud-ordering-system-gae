<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>

<%@ page import="util.GeoCoder" %>
<%@ page import="util.Location" %>
<%@ page import="util.GeocodeResponse" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />

<% 
Printer printer = new Printer(Dictionary.Language.CHINESE);
 String setLanguage=printer.getLanguageString();
%>

<%@  include file="../header/page-title.html" %>
<script language="JavaScript" type="text/javascript">
function alerts(){
alert("<%=printer.print("Actions")%>");
}
</script>
</head>

<body>

<h1>Print Test</h1>

<%= printer.print("COMMENTS") %>
<%= printer.print("Good bye") %>
<%= printer.print("Comments") %>
<%= printer.print("Actions") %>
<%= printer.print("Restaurant Name") %>
<%= printer.print("Welcomes") %>
<p><%= setLanguage%></p>
<%@  include file="../header/page-footer.html" %>
<input type="button" onClick="alerts();" value="hola">
<%
GeoCoder  gcoor = new GeoCoder ();
GeocodeResponse t= gcoor.getLocation("National Tsing Hua University, Hsinchu city");
List<GeocodeResponse.Result> listResults = t.getResults();

%>

<% for (GeocodeResponse.Result result:listResults){
 GeocodeResponse.Geometry geometry = result.getGeometry();
Location location = geometry.getLocation();
double lat=location.getLat();
double lng=location.getLng();
%>
<%=lat%>,
<%=lng%>
<% }%>
</body>
</html>
