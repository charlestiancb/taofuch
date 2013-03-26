<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="../include/header.jsp"></jsp:include>
<center>
	<table>
		<tr>
			<th>分组名称：</th><td>${systemGroup.groupName}</td>
		</tr>
		<tr>
			<th>分组信息描述：</th><td>${systemGroup.descript}</td>
		</tr>
		<tr>
			<th>排序：</th><td>${systemInfo.orderNum}</td>
		</tr>
	</table>
</center>
