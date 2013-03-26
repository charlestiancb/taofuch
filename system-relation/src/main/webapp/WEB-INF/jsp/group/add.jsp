<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="../include/header.jsp"></jsp:include>
<form action="/group" method="post">
	<center><font color='red'>${errMsg}</font></center>
	<center>
		<table>
			<tr>
				<th>分组名称：</th><td><input type="text" name="groupName" value="${systemGroup.groupName}"/></td>
			</tr>
			<tr>
				<th>分组信息描述：</th><td><input type="text" name="descript" value="${systemGroup.descript}"/></td>
			</tr>
			<tr>
				<th>排序：</th><td><input type="text" name="orderNum" value="${systemInfo.orderNum}"/></td>
			</tr>
		</table>
	</center>
	<center><input type="submit" value="添加"/></center>
</form>