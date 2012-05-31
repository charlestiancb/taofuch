<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>提交xmlrpc请求数据</title>
</head>
<body>
<form action="xmlrpc_request" method="post" enctype="multipart/form-data">
	请指定请求的数据：<input type="file" name="data"/><br>
	<input type="submit" value="提 交" />
</form>
</body>
</html>