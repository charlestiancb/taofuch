<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../include/header.jsp" />
<center>
	<table>
		<tr>
			<td>系统名称</td>
			<td>主页地址</td>
			<td>简介</td>
			<td nowrap="nowrap"><a href="/sys/new">添加</a></td>
		</tr>
		<c:forEach var="system" items="${systems}">
			<tr>
				<td><a href="/sys/${system.sysId}/show">${system.name}</a></td>
				<td><a href="${system.url}" target="_blank">${system.url}</a></td>
				<td>${system.introduce}</td>
				<td nowrap="nowrap"><a href="/sys/${system.sysId}">修改</a> <a
					href="/relationship/${system.sysId}">查看调用关系</a></td>
			</tr>
		</c:forEach>
	</table>
</center>
