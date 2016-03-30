<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />

<%@  include file="../header/page-title.html" %>
</head>

<body>

<h1>Order Test</h1>

<form method="post" id="form1" name="form1"
		action="/mobile?action=upload_order"
		class="form-style">
	
	<input type="text" name="u_email" value="customer@smasrv.com" style="display:none;" />
	<input type="text" name="o_detail" value="ahlzbWFzcnYtY29zLXRlc3RpbmctYmFja3VwcjQLEgpSZXN0YXVyYW50IhVyZXN0YXVyYW50QHNtYXNydi5jb20MCxIITWVudUl0ZW0YmgEM,10" style="display:none;" />
	<input type="text" name="o_ap_detail" value="" style="display:none;" />
	<input type="text" name="o_s_detail" value="" style="display:none;" />
	<input type="text" name="b_key" value="ahlzbWFzcnYtY29zLXRlc3RpbmctYmFja3VwcjILEgpSZXN0YXVyYW50IhVyZXN0YXVyYW50QHNtYXNydi5jb20MCxIGQnJhbmNoGJUBDA" style="display:none;" />
	<input type="text" name="o_phone" value="543534532" style="display:none;" />
	<input type="text" name="o_type" value="delivery" style="display:none;" />
	<input type="text" name="o_p_type" value="smart_cash" style="display:none;" />
	<input type="text" name="o_delivery_type" value="ups" style="display:none;" />
	<input type="text" name="d_address" value="Hsinchu city" style="display:none;" />
	<input type="text" name="s_time" value="6/29/2012 13:49:03" style="display:none;" />
	<input type="text" name="n_people" value="0" style="display:none;" />
	
	<br/><br/><br/><br/>
	
	<button type="submit" value="Update" class="button"/>
		<img src="../images/update.jpg" class="img-button"/>
	</button>

</form>

<%@  include file="../header/page-footer.html" %>

</body>
</html>
