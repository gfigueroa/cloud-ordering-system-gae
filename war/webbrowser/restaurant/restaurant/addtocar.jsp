<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="java.util.*" %>
<%@ page import="webbrowser.OrderItemList" %>

<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
</head>
<%
	List<List<String>> CarSet = OrderItemList.getCar(session);
	if (CarSet.isEmpty())
	{
		out.println("我是空的");
	}
%>

<body>
<%


	
%>






</body>
</html>