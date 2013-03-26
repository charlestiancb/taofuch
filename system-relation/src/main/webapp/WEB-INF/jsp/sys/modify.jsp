<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="../include/header.jsp"></jsp:include>
<form action="/sys/${systemInfo.sysId}" method="post">
	<center><font color='red'>${errMsg}</font></center>
	<input type="hidden" name="_method" value="put"/>
	<input type="hidden" name="sysId" value="${systemInfo.sysId}"/>
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
			<tr>
				<th>系统排序：</th><td><input type="text" name="orderNum" value="${systemInfo.orderNum}"/></td>
			</tr>
		</table>
	</center>
	<center><input type="submit" value="修改"/></center>
</form>