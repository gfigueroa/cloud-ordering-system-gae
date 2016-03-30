<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<% User sessionUser = (User) session.getAttribute("user"); %>
<div style="width:960px; overflow:hidden;">
<div style="text-align:right;padding-top:0px;width:940px;" >
<h7>
<%	if(sessionUser == null) 
	{
%>
	你尚未登入 <a href="../restaurant/member/login.jsp">登入</a>
<%	}else{  %>
		Hi,<%= sessionUser.getUserEmail().getEmail() %>　<a href="/handleSession_test?action=destroy">登出</a>
<%	}  %>
</h7>
</div>
<div style="text-align:right;padding-top:0px;width:940px;" >
	<div class="logo_left">
    	<div style="float:left;"><a href="http://www.smasrv.com"><img src="img/logo.png" width="220" border="0" /></a></div>
		<div style="width:90px; height:70px; margin-left:10px; float:left;"><br />
        	<div class="fb-like" data-href="http://www.facebook.com/pages/%E6%99%BA%E6%85%A7%E4%BA%BA%E7%A7%91%E6%8A%80%E6%9C%8D%E5%8B%99%E8%82%A1%E4%BB%BD%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8/199537620130898" data-send="false" data-layout="button_count" data-width="450" data-show-faces="true" data-font="tahoma"></div>
   	  </div>
	</div>
    </div>
 	<div class="logo_right">
  	  <input type="text" width="100px" height="20px" />
      <input type="button" value="搜尋" class="search_button" />
  	</div>    
</div>

	<div class="menubar">
    	<ul class="menubar">
    	<li ><a href="playlist.jsp?m_type=a">流行福音</a></li>
    	<li ><a href="playlist.jsp?m_type=b">讚美</a></li>
        <li ><a href="playlist.jsp?m_type=c">救恩</a></li>
        <li ><a href="playlist.jsp?m_type=d">醫治安慰</a></li>
        <li ><a href="playlist.jsp?m_type=e">團契</a></li>
        <li ><a href="playlist.jsp?m_type=f">節慶</a></li>
		</ul>
	</div> 
