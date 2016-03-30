<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">



<html>
<head>
<script type="text/javascript" src="javascript/swfobject/swfobject.js"></script>
    <script type="text/javascript">
    var params = { allowScriptAccess: "always" };
    var atts = { id: "video" };
	
function onload(){
	
    swfobject.embedSWF("http://www.youtube.com/v/QlxsSj1h16g?enablejsapi=1&playerapiid=viedo&version=3&amp;rel=0",
                       "video", "425", "356", "8", null, null, params, atts);
	//swfobject.embedSWF(swfUrlStr, replaceElemIdStr, widthStr, heightStr, swfVersionStr, xiSwfUrlStr, flashvarsObj, parObj, attObj)
}
function onYouTubePlayerReady(playerId) {
  ytplayer = document.getElementById("video");
  ytplayer.addEventListener("onStateChange", "onytplayerStateChange");
}

function playlist()
{
	document.getElementById("video").innerHTML = "";
	
	  
	  
}

function onytplayerStateChange(newState) {
		
}
  </script>
</head>


  <body onload="onload();">
  	  <div id="video">
    You need Flash player 8+ and JavaScript enabled to view this video.
  </div>
	<div id="test"></div>
   
  </body>
</html>