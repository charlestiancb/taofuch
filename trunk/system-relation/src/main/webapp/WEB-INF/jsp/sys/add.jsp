<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="../include/header.jsp"></jsp:include>
<form action="/sys" method="post">
	<center><font color='red'>${errMsg}</font></center>
	<center>
		<table>
			<tr>
				<th>系统名称：</th><td><input type="text" name="name" value="${systemInfo.name}"/></td>
			</tr>
			<tr>
				<th>系统主页网址：</th><td><input type="text" name="url" value="${systemInfo.url}"/></td>
			</tr>
			<tr>
				<th>系统简介：</th><td><input type="text" name="introduce" value="${systemInfo.introduce}"/></td>
			</tr>
		</table>
	</center>
	<center><input type="submit" value="添加"/></center>
</form>