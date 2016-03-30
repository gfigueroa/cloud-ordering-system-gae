<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>

<link rel="stylesheet" type="text/css" href="../js/ddlevelsfiles/ddlevelsmenu-base.css" />
<link rel="stylesheet" type="text/css" href="../js/ddlevelsfiles/ddlevelsmenu-topbar.css" />
<link rel="stylesheet" type="text/css" href="../js/ddlevelsfiles/ddlevelsmenu-sidebar.css" />

<script type="text/javascript" src="../js/ddlevelsfiles/ddlevelsmenu.js">

/***********************************************
* All Levels Navigational Menu- (c) Dynamic Drive DHTML code library (http://www.dynamicdrive.com)
* This notice MUST stay intact for legal use
* Visit Dynamic Drive at http://www.dynamicdrive.com/ for full source code
***********************************************/

</script>	

<% 
Printer printer = (Printer)session.getAttribute("printer");
%>

<div id="ddtopmenubar" class="mattblackmenu">
<ul>
  <li><a href="/restaurant/listOrder.jsp" ><%= printer.print("Orders") %></a></li>  
  <li><a href="#" rel="products"><%= printer.print("Products") %></a></li>
  <li><a href="#" rel="news_polls"><%= printer.print("News & Polls") %></a></li>
  <li><a href="/restaurant/listMessages.jsp"><%= printer.print("Messages") %></a></li> 
  <!--<li><a href="#" rel="bonus"><%= printer.print("Bonus") %></a></li>-->
  <li><a href="#" rel="retail_store"><%= printer.print("Retail store") %></a></li>
</ul>
</div>

<script type="text/javascript">
ddlevelsmenu.setup("ddtopmenubar", "topbar") //ddlevelsmenu.setup("mainmenuid", "topbar|sidebar")
</script>

<ul id="news_polls" class="ddsubmenustyle">
<li><a href="/restaurant/listNews.jsp"><%= printer.print("News") %></a></li>
<li><a href="/restaurant/listOpinionPoll.jsp"><%= printer.print("Opinion Polls") %></a></li>
<li><a href="/restaurant/listSurvey.jsp"><%= printer.print("Surveys") %></a></li>
</ul>

<ul id="products" class="ddsubmenustyle">
<li><a href="/restaurant/listMenuItemType.jsp"><%= printer.print("Product Types") %></a></li>
<li><a href="/restaurant/listMenuItem.jsp"><%= printer.print("Product Items") %></a></li>
<li><a href="/restaurant/listAdditionalPropertyType.jsp"><%= printer.print("Additional Properties") %></a></li>
<li><a href="/restaurant/listSets.jsp"><%= printer.print("Product Item Sets") %></a></li>
</ul>

<ul id="messages" class="ddsubmenustyle">

</ul>

<ul id="bonus" class="ddsubmenustyle">
	<li><a href="/menu/underConstruction.jsp"><%= printer.print("Stamps") %></a></li> 
	<li><a href="/menu/underConstruction.jsp"><%= printer.print("Coupons") %></a></li> 
</ul>

<ul id="retail_store" class="ddsubmenustyle">
<li><a href="/restaurant/listBranch.jsp"><%= printer.print("Branches") %></a></li>
<li><a href="/restaurant/editRestaurant.jsp"><%= printer.print("Retail Store Profile") %></a></li>
</ul>