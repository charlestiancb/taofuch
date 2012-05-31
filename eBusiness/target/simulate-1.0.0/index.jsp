<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
table {
	border-collapse: collapse;
}

td {
	border: 1px solid #06c;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>整个网站请求功能汇总</title>
</head>
<body>
	<center>
		<table>
			<tr style="background: #cee1f9;">
				<td>功能介绍</td>
				<td>链接地址</td>
			</tr>
			<tr style="background: #cea1f9;">
				<td colspan="2">xmlrpc</td>
			</tr>
			<tr>
				<td>请求指定系统的xmlrpc接口</td>
				<td><a href="xmlrpc_request">/xmlrpc_request</a></td>
			</tr>
			<tr>
				<td>响应指定系统的xmlrpc请求，并返回响应数据</td>
				<td><a href="xmlrpc_response">/xmlrpc_response</a></td>
			</tr>
			<tr>
				<td>响应指定系统的http JSON请求，并返回响应的JSON数据</td>
				<td><a href="xmlrpc_json?apiMethod=">/xmlrpc_json</a></td>
			</tr>
		</table>
	</center>
</body>
</html>