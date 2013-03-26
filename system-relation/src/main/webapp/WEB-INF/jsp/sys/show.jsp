<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="../include/header.jsp"></jsp:include>
<center>
	<table>
		<tr>
			<th>系统名称：</th><td>${systemInfo.name}</td>
		</tr>
		<tr>
			<th>系统主页网址：</th><td><a href="${systemInfo.url}" target="_blank">${systemInfo.url}</a></td>
		</tr>
		<tr>
			<th>系统简介：</th><td>${systemInfo.introduce}</td>
		</tr>
		<tr>
			<th>系统排序：</th><td>${systemInfo.orderNum}</td>
		</tr>
		<tr>
			<th>隶属分组：</th><td>${systemInfo.group.groupName}</td>
		</tr>
	</table>
</center>
