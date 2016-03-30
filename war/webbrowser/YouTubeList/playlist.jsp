<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>播放清單</title>
<link type="text/css" rel="stylesheet" href="css/playlist.css" />
<link rel="stylesheet" href="css/default.css" />
<script type="text/javascript" src="javascript/jquery-1.7.2.js"></script>
<script type="text/javascript" src="javascript/swfobject/swfobject.js"></script>
<script type="text/javascript" src="javascript/playlist.js"></script>

</head>

<script>(function(d, s, id) {
	  var js, fjs = d.getElementsByTagName(s)[0];
	  if (d.getElementById(id)) return;
	  js = d.createElement(s); js.id = id;
	  js.src = "//connect.facebook.net/zh_TW/all.js#xfbml=1";
	  fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'));</script>



<body >

<div id="fb-root"></div>
<div style="width:960px; overflow:visible; margin-left:auto; margin-right:auto;">

<jsp:include page="playlisthead.jsp"/>

<div style="width:856px; overflow:hidden; margin:0px auto 0px auto;">
	<div style="padding:0px 0px 0px 0px; margin-top:10px;"><img src="img/contant.png" width="854" /></div>
	<div style="float:left; width:216px;">
    <a href="../restaurant/index/index.jsp"><img src="../img/food_drink.png" border="0" /></a><br/>
    <img src="../img/learning.png" /><br/>
    <img src="../img/spiritual.png"/><br/>
    <img src="../img/shopingmall.png" />
    </div>	
    <div style="padding-top:10px;">
	<div class="video" id="video"></div>
    </div>
    <div style="overflow-x:scroll;overflow-y:hidden; width:640px; margin-top:10px;">
    <jsp:include page="playlistsuggestion.jsp"/>
    </div>
    
</div>
<div style="margin:20px 0px 20px 0px;;">
<jsp:include page="../footer_index.jsp"/>
</div>    
</div>

</body>
</html>