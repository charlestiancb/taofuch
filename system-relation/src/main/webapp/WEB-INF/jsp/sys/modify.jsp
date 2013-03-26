<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="../include/header.jsp"></jsp:include>
<form:form modelAttribute="systemInfo" action="/sys/${systemInfo.sysId}" method="post">
	<center><font color='red'>${errMsg}</font></center>
	<input type="hidden" name="_method" value="put"/>
	<input type="hidden" name="sysId" value="${systemInfo.sysId}"/>
	<center>
		<table>
			<tr>
				<th>系统名称：</th><td><form:input path="name"/></td>
			</tr>
			<tr>
				<th>系统主页网址：</th><td><form:input path="url"/></td>
			</tr>
			<tr>
				<th>系统简介：</th><td><form:textarea cols="100" rows="5" path="introduce"/></td>
			</tr>
			<tr>
				<th>系统排序：</th><td><form:input path="orderNum"/></td>
			</tr>
			<tr>
				<th>隶属分组：</th>
				<td>
					<form:select path="groupId" items="${groups}" itemLabel="groupName" itemValue="groupId"/>
				</td>
			</tr>
		</table>
	</center>
	<center><input type="submit" value="修改"/></center>
</form>