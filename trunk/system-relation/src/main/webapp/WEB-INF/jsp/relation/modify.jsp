<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../include/header.jsp" />
<center>
<a href="/relationship/${systemInfo.sysId}">查询关系</a>
	<table>
		<tr>
			<th align="right" title="${systemInfo.introduce}">${systemInfo.name}</th>
			<td style="border:1px solid blue;">
				<table>
					<caption>调用以下</caption>
					<tr>
					<c:set var="idx" value="1"/>
					<c:forEach var="call" items="${systems}">
							<td title="${call.introduce}">
								<input type="checkbox" value="${call.sysId}" onclick="saveRelation(${call.sysId},1)"
								<c:if test="${systemInfo.hasRelation(systemInfo.sysId,call.sysId)}"> checked="checked" </c:if>id="call_${call.sysId}"/>${call.name}
								<br/>
								<textarea rows="7" cols="20" id="intro_call_${call.sysId}" onblur="saveRelation(${call.sysId},1)">${systemInfo.getRelation(systemInfo.sysId, call.sysId).introduce}</textarea>
							</td>
						<c:if test="${idx%5==0}"></tr></c:if>
						<c:set var="idx" value="${idx+1}"/>
					</c:forEach>
					<c:if test="${(idx - 1)%5>0}"></tr></c:if>
				</table>
				<br/>
				<table>
					<caption>被以下调用</caption>
					<tr>
					<c:set var="idx" value="1"/>
					<c:forEach var="called" items="${systems}">
							<td title="${called.introduce}">
								<input type="checkbox" value="${called.sysId}" onclick="saveRelation(${called.sysId},2)"
								<c:if test="${systemInfo.hasRelation(called.sysId,systemInfo.sysId)}"> checked="checked" </c:if>id="called_${called.sysId}"/>${called.name}
								<br/>
								<textarea rows="7" cols="20" id="intro_called_${called.sysId}" onblur="saveRelation(${called.sysId},2)">${systemInfo.getRelation(called.sysId,systemInfo.sysId).introduce}</textarea>
							</td>
						<c:if test="${idx%5==0}"></tr></c:if>
						<c:set var="idx" value="${idx+1}"/>
					</c:forEach>
					<c:if test="${(idx - 1)%5>0}"></tr></c:if>
				</table>
			</td>
		</tr>
	</table>
</center>
<script>
	function saveRelation(sysId,relationType){
		var type="call_";
		if(relationType===2){
			type="called_";
		}
		var chkBox = document.getElementById(type+sysId);
		var txtArea = document.getElementById("intro_"+type+sysId);
		if(chkBox.checked){
			txtArea.value = txtArea.value?txtArea.value:"无";
		}
		else{
			txtArea.value = "";
		}
		if(chkBox && txtArea){
			$.ajax({
			  type: "POST",
			  url: "/relationship/${systemInfo.sysId}/conf",
			  data: { selectedSysId:sysId,introduce:txtArea.value,RelationType:relationType,operType: chkBox.checked?1:2}
			}).done(function( msg ) {
			  //alert(  msg );
			});
		}
	}
</script>