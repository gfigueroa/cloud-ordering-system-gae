<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="java.lang.Math" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link type="text/css" rel="stylesheet" href="../css/login.css" />
<link type="text/css" rel="stylesheet" href="../css/deafult.css" />
<link rel="stylesheet" type="text/css" href="../css/header_footer.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>智慧人科技-點餐即時享-會員登入</title>
</head>

<script type="text/javascript" src="../../../js/crypto.js" >
</script>

<script type="text/javascript">
function hashAndSubmit()
{
  if((document.getElementById("username").value=="")||(document.getElementById("password")==""))
  {
	  alert("帳號或密碼不能空白！");
	  return false;
  }else{
	  var passwordInput = document.getElementsByName("password")[0];
	  passwordInput.value = Sha256.hash(passwordInput.value);
  	return true;
  }
}
</script>

<%
  String checkcode="";
  String error = request.getParameter("etype") != null ? request.getParameter("etype") : "";
  for(int a=0; a<4;a++)
  {
  	checkcode = String.valueOf((int)(Math.random()*10))+checkcode;
  }
  String r_key = request.getParameter("r_key") != null ? request.getParameter("r_key") : "";
  String message = request.getParameter("msg") != null ? request.getParameter("msg") : "";
  String action = request.getParameter("action") != null ? request.getParameter("action") : "";
%>

<body class="body">
<div class="body_contant">
<div style="height:150px;"><jsp:include page="../header/header.jsp" flush="true" /></div>
<div class="login_title" ><h2></h2></div>
<div style="width:940px; padding:20px 0px 20px 0px;">
<form  method="post" onSubmit="hashAndSubmit()" action="/attemptlogin_test">
<table cellpadding="0" cellspacing="0" align="center"  class="menu_list_table" width="648px">
	<tr bgcolor="#F1E6D2" height="20px" align="center">
    	<td height="20" ><h1>會員登入</h1></td><td><h1>加入會員</h1></td>
    <tr>
    	<td class="login_format" width="50%">
    <% 	if(error.equals("ErrorCheckCode"))
    	{
    %>
    	<p>　　<font color="#DE3C15">認證碼錯誤，請重新輸入。</font></p>	
    <%	
    	}
    %>  
    	<p><h3>　　帳號&emsp;&emsp;&emsp;&emsp;<input type="text" name="username" id="username" size="10" height="20px" /></h3></p>
        <p><h3>　　密碼&emsp;&emsp;&emsp;&emsp;<input type="password" name="password" id="password" size="10" height="20px" /></h3></p>
        <p><h3>　　輸入認證碼&emsp;<input type="text" name="usercheckcode" size="10" height="20px" /></h3></p>
        <p>　　&emsp;&emsp;<%= checkcode %>&emsp;&emsp;&emsp;<input type="submit" value="" class="login_submit" /></p>
        <p align="center"><a href="#">　　忘記密碼？</a></p>
        <input type="text" name="systemcheckcode" style="display:none;" value="<%= checkcode %>" />
        <input type="text" name="r_key" style="display:none;" value="<%= r_key %>" />
        </td>
        
        
        <td style="border-right-style:none;" class="login_format">
        <p><h3>　　　　你還不是會員嗎？<br />　　　　<a href="#">點此</a>加入會員</h3></p>
        </td>
    </tr>
    </table>
</form>
</div> 
<div style="width:940px; height:100px;"><jsp:include page="../header/footer.jsp"/></div>
</div>

</body>
</html>