<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../include/header.jsp" />
<center>
	<table>
		<c:forEach var="system" items="${systems}">
			<tr>
				<th align="right">${system.name}</th>
				<td>
					<table>
						<caption>调用以下</caption>
						<c:forEach var="call" items="${system.calls}">
							<tr><td><label title="${call.slave.introduce}">${call.slave.name}</label></td></tr>
						</c:forEach>
					</table>
					<br/>
					<table>
						<caption>被以下调用</caption>
						<c:forEach var="called" items="${system.calleds}">
							<tr><td><label title="${called.master.introduce}">${called.master.name}</label></td></tr>
						</c:forEach>
					</table>
				</td>
			</tr>
		</c:forEach>
	</table>
</center>
