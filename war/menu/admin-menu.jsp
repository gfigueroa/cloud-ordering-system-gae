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
	<li><a href="/admin/listAdmin.jsp"><%= printer.print("Administrators") %></a></li>
	<li><a href="#" rel="retail_store"><%= printer.print("Retail store") %></a></li>
	<li><a href="#" rel="customers"><%= printer.print("Customers") %></a></li>
	<li><a href="#" rel="multimedia"><%= printer.print("Multimedia Content") %></a></li>
	<li><a href="#" rel="configuration"><%= printer.print("Configuration") %></a></li>
</ul>
</div>

<script type="text/javascript">
ddlevelsmenu.setup("ddtopmenubar", "topbar") //ddlevelsmenu.setup("mainmenuid", "topbar|sidebar")
</script>

<ul id="retail_store" class="ddsubmenustyle">
	<li><a href="/admin/listRestaurant.jsp"><%= printer.print("Retail store") %></a></li>
	<li><a href="/admin/listRestaurantType.jsp"><%= printer.print("Retail Store Types") %></a></li>
	<!--<li><a href="#" rel="service_provider"><%= printer.print("Service Providers") %></a>
		<ul id="service_provider" class="ddsubmenustyle">
			<li><a href="/menu/underConstruction.jsp"><%= printer.print("Salon") %></a></li>
			<li><a href="/menu/underConstruction.jsp"><%= printer.print("Clinic") %></a></li> 
		</ul>
	</li>-->
</ul>

<ul id="customers" class="ddsubmenustyle">
	<li><a href="/admin/listCustomer.jsp"><%= printer.print("Customers") %></a></li>
	<li><a href="/admin/listSmartCash.jsp"><%= printer.print("Smart Cash") %></a></li>
	<li><a href="/menu/underConstruction.jsp"><%= printer.print("Smart Bonus") %></a></li> 
	<!--<li><a href="/menu/underConstruction.jsp"><%= printer.print("Memberships") %></a></li>-->
	<!--<li><a href="/menu/underConstruction.jsp"><%= printer.print("Coupons") %></a></li>-->
	<!--<li><a href="/menu/underConstruction.jsp"><%= printer.print("Stamps") %></a></li>-->
</ul>

<ul id="multimedia" class="ddsubmenustyle">
	<li><a href="/admin/listDMSV.jsp"><%= printer.print("DMSV (Daily Multi Spiritual Vitamin)") %></a></li> 
	<li><a href="/menu/underConstruction.jsp"><%= printer.print("Music Recommendation") %></a></li>
</ul>

<ul id="configuration" class="ddsubmenustyle">
	<li><a href="/admin/listRegion.jsp"><%= printer.print("Regions") %></a></li>
	<li><a href="/admin/editSystem.jsp?readonly=true"><%= printer.print("System") %></a></li> 
</ul>