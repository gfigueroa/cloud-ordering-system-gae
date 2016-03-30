<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />

<%@  include file="../header/page-title.html" %>
</head>

<body>

<h1>Click News Test</h1>

<form method="post" id="form1" name="form1"
		action="/mobile?action=click_news"
		class="form-style">
	
	<input type="text" name="n_key" value="<%= request.getParameter("n_key")%>" style="display:none;" />
	<input type="text" name="o_phone" value="<%= request.getParameter("o_phone")%>" style="display:none;" />
	<input type="text" name="u_email" value="<%= request.getParameter("u_email")%>" style="display:none;" />
	<input type="text" name="u_password" value="<%= request.getParameter("u_password")%>" style="display:none;" />
	
	<button type="submit" value="Update" class="button"/>
		<img src="../images/update.jpg" class="img-button"/>
	</button>

</form>

<%@  include file="../header/page-footer.html" %>

</body>
</html>
