<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../include/header.jsp" />
<center>
	<table>
		<c:forEach var="system" items="${systems}">
			<tr>
				<th align="right">${system.name}</th>
				<td style="border:1px solid blue;">
					<table>
						<caption>调用以下</caption>
						<c:forEach var="call" items="${system.calls}">
							<tr>
								<td width="200px"><label title="${call.slave.name}：${call.slave.introduce}">${call.slave.name}</label></td>
								<td>${system.getRelation(system.sysId,call.slave.sysId).introduce}</td>
							</tr>
						</c:forEach>
					</table>
					<br/>
					<table>
						<caption>被以下调用</caption>
						<c:forEach var="called" items="${system.calleds}">
							<tr>
								<td width="200px"><label title="${called.master.name}：${called.master.introduce}">${called.master.name}</label></td>
								<td>${system.getRelation(called.master.sysId,system.sysId).introduce}</td>
							</tr>
						</c:forEach>
					</table>
				</td>
			</tr>
		</c:forEach>
	</table>
</center>
