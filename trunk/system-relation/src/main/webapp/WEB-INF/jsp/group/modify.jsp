<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="../include/header.jsp"></jsp:include>
<form action="/group/${systemGroup.groupId}" method="post">
	<center><font color='red'>${errMsg}</font></center>
	<input type="hidden" name="_method" value="put"/>
	<input type="hidden" name="groupId" value="${systemGroup.groupId}"/>
	<center>
		<table>
			<tr>
				<th>分组名称：</th><td><input type="text" name="groupName" value="${systemGroup.groupName}"/></td>
			</tr>
			<tr>
				<th>分组信息描述：</th><td><input type="text" name="descript" value="${systemGroup.descript}"/></td>
			</tr>
			<tr>
				<th>排序：</th><td><input type="text" name="orderNum" value="${systemGroup.orderNum}"/></td>
			</tr>
		</table>
	</center>
	<center><input type="submit" value="修改"/></center>
</form>