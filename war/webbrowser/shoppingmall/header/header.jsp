<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="datastore.RestaurantType" %>
<%@ page import="datastore.RestaurantTypeManager" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.Customer" %>
<%@ page import="datastore.CustomerManager" %>
<%
List<RestaurantType> restaurantTypes = RestaurantTypeManager.getAllRestaurantTypes();
	int total=0;int count=1;
	for(RestaurantType restaurantType : restaurantTypes){
		
		if(restaurantType.getStoreSuperType().toString().equals("SHOPPING")){
		total++;
		}
	}
	User sessionUser = (User) session.getAttribute("user");

%>
<div id="fb-root"></div>

<script>(function(d, s, id) {
	  var js, fjs = d.getElementsByTagName(s)[0];
	  if (d.getElementById(id)) return;
	  js = d.createElement(s); js.id = id;
	  js.src = "//connect.facebook.net/zh_TW/all.js#xfbml=1";
	  fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'));</script>
<script type="text/javascript">


function DisplayLeft(total)
{
	var barname="";
	var count = 1;
	var display_nbar="";
	var display_bar="";
	var temp = 1;
	barname = "bar"+total;
	if(document.getElementById(barname).style.display == "none")
	{
		document.getElementById("displayleft").style.visibility = "hidden";
		for(count;count<=parseInt(total);count++)
		{
			barname = "bar"+count.toString();
			if(temp == 1){
				if(document.getElementById(barname).style.display != "none")
				{
					display_nbar = barname;
					temp++;
				}
			}else{
				if(document.getElementById(barname).style.display == "none")
				{
					document.getElementById(barname).style.display = '';
					display_bar = barname;

					
					var showindex = 6;
					DisplayTimeOut(display_nbar,display_bar,showindex,total);
					break;
				}
			}
		}
		document.getElementById("displayright").style.visibility = "";
	}
	
}

function DisplayTimeOut(display_nbar,display_bar,showindex,total)
{
	var hiddlist = new Array("20px","40px","60px","80px","100px","120px", "140px");
	var showlist = new Array("140px","120px","100px","80px","60px","40px", "20px");
	document.getElementById(display_nbar).style.visibility = "hidden";
	document.getElementById(display_bar).style.visibility = "";
	document.getElementById(display_bar).style.display = "";
	document.getElementById(display_nbar).style.width = hiddlist[showindex];
	document.getElementById(display_bar).style.width = showlist[showindex];
	showindex--;
	
	if(showindex >= 0)
	{	//alert(showindex);
		setTimeout(function (){DisplayTimeOut(display_nbar,display_bar,showindex,total)},100);
	}else{
		document.getElementById(display_nbar).style.display = "none";
		if(document.getElementById("bar1").style.display == '')
		{
			document.getElementById("displayright").style.visibility = 'hidden';
		}else{
			document.getElementById("displayright").style.visibility = '';
		}
		
		if(document.getElementById("bar"+total).style.display == '')
		{
			document.getElementById("displayleft").style.visibility = 'hidden';
		}else{
			document.getElementById("displayleft").style.visibility = '';
		}
		
		
	}
}

function DisplayRight(total)
{
	var barname="";
	var display_nbar="";
	var display_bar="";
	var count = parseInt(total);
	var temp = 1;
	barname = "bar1";
	if(document.getElementById(barname).style.display == "none")
	{
		
		for(count;count>=1;count--)
		{
			barname = "bar"+count.toString();
			if(temp == 1){
				if(document.getElementById(barname).style.display != "none")
				{
					display_nbar = barname;
					//document.getElementById(barname).style.display = 'none';
					temp++;
				}
			}else{
				if(document.getElementById(barname).style.display == "none")
				{
					document.getElementById(barname).style.display = '';
					display_bar = barname;
					document.getElementById("displayright").style.visibility = 'hidden';
					var showindex = 6;
					DisplayTimeOut(display_nbar,display_bar,showindex,total);					

						
					break;
				}
			}
		}
		document.getElementById("displayleft").style.visibility = '';
	}
	
}

</script>
<div class="contant" style="text-align:right;" >
<h7>
<%
	if(sessionUser == null) 
	{
%>
	你尚未登入 <a href="../member/login.jsp">登入</a>
<%		
	}else{
%>
		Hi,<%= sessionUser.getUserEmail().getEmail() %>　<a href="/handleSession_test?action=destroy">登出</a>
<%	
	}

%>


</h7>
</div>

<div class="contant">
	<div class="logo_left"><div style="float:left;"><a href="http://www.smasrv.com"><img src="../img/logo.png" width="220" border="0" /></a></div>
	<div style="width:90px; height:70px; margin-left:10px; float:left;"><br /><div class="fb-like" data-href="http://www.facebook.com/pages/%E6%99%BA%E6%85%A7%E4%BA%BA%E7%A7%91%E6%8A%80%E6%9C%8D%E5%8B%99%E8%82%A1%E4%BB%BD%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8/199537620130898" data-send="false" data-layout="button_count" data-width="450" data-show-faces="true" data-font="tahoma"></div></div>
	</div>
  <div class="logo_right">
  	  <input type="text" width="100px" height="20px" />
      <input type="button" value="搜尋" class="search_button" />
      
  </div>
  <div class="resaturantTypeBar">
  <div id="displayleft" class="restaurant_select" style="visibility:hidden;"><a href="javascript:void(0);" onclick="DisplayLeft('<%= total%>');"><<</a></div>
  <div class="restaurant_type_bar"><a href="../index/index.jsp">回首頁</a></div>
  <div style="float:left; overflow:hidden; width:560px; height:38px;">
  <%
	for(RestaurantType restaurantType : restaurantTypes){
		if(restaurantType.getStoreSuperType().toString().equals("SHOPPING")){
			
  %>
  	<div id="bar<%= count %>" class="restaurant_type_bar" <%= (total-count)<4 ? "":"style=\"display:none;\"" %> >
  	<a href="../restaurant/restaurant_list.jsp?rt_key=<%= restaurantType.getKey() %>">
  	<%= restaurantType.getRestaurantTypeName() %></a></div>
  <% 			
		count++;
		}
	}
  	
  %>

  </div>
  
  <div id="displayright" class="restaurant_select" ><a href="javascript:void(0);" onclick="DisplayRight('<%= total%>');">>></a></div>

 </div>
</div>