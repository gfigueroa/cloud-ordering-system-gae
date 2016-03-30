<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />

<%@  include file="../header/page-title.html" %>
</head>

<body>

<h1>Modify Order Test</h1>

<form method="post" id="form1" name="form1"
		action="/mobile?action=modify_order"
		class="form-style">
	
	<input type="text" name="u_email" value="customer@smasrv.com" style="display:none;" />
	<input type="text" name="u_password" value="b6c45863875e34487ca3c155ed145efe12a74581e27befec5aa661b8ee8ca6dd" style="display:none;" />
	<input type="text" name="o_key" value="179" style="display:none;" />

	<input type="text" name="o_type" value="takein" style="display:none;" />
	<input type="text" name="o_p_type" value="cash" style="display:none;" />
	<input type="text" name="o_delivery_type" value="" style="display:none;" />
	<input type="text" name="o_phone" value="1234456" style="display:none;" />
	<input type="text" name="d_address" value="none" style="display:none;" />
	<input type="text" name="s_time" value="7/29/2012 15:49:03" style="display:none;" />
	<input type="text" name="n_people" value="2" style="display:none;" />
	
	<input type="text" name="o_detail" value="ahlzbWFzcnYtY29zLXRlc3RpbmctYmFja3VwcjQLEgpSZXN0YXVyYW50IhVyZXN0YXVyYW50QHNtYXNydi5jb20MCxIITWVudUl0ZW0YnwEM,5" style="display:none;" />
	<input type="text" name="o_ap_detail" value="" style="display:none;" />
	<input type="text" name="o_s_detail" value="" style="display:none;" />
	
	<br/><br/><br/><br/>
	
	<button type="submit" value="Update" class="button"/>
		<img src="../images/update.jpg" class="img-button"/>
	</button>

</form>

<%@  include file="../header/page-footer.html" %>

</body>
</html>
