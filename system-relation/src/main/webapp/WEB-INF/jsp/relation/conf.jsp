<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../include/header.jsp" />
<form action="/relationship" method="post">
	<center>
		<table>
			<tr>
				<th> </th>
				<c:forEach var="title" items="${systems}">
					<th title="${title.introduce}">${title.name}</th>
				</c:forEach>
			</tr>
			<c:set var="r" value="1"/>
			<c:forEach var="row" items="${systems}">
				<tr>
					<th title="${row.introduce}">${row.name}</th>
					<c:set var="i" value="1"/>
					<c:forEach var="column" items="${systems}">
						<td title="'${row.name}','${column.name}'">
							<c:if test="${row.sysId!=column.sysId}">
								<c:set var="hasRelation" value="${row.hasRelation(row.sysId, column.sysId)}"/>
								<input type="checkbox" name="relations" value="${row.sysId}_${column.sysId}"
									 id="relations_${i}~${r}" <c:if test="${hasRelation}">checked</c:if> onclick="changeInput(this)"/>
								<br/>
								<textarea name="introduces" id="introduces_${i}~${r}" onblur="checkValue(this)"
									<c:if test="${hasRelation==false}">disabled="disabled"</c:if>>${row.getRelation(row.sysId, column.sysId).introduce}</textarea>
							</c:if>
						</td>
					<c:set var="i" value="${i+1}"/>
					</c:forEach>
				</tr>
			<c:set var="r" value="${r+1}"/>
			</c:forEach>
		</table>
		<input type="submit" value="提交">
	</center>
</form>
<script>
	function changeInput(checkboxEle){
		var id=checkboxEle.id;
		id=id.substring(id.indexOf("_")+1);
		id="introduces_"+id;
		var inputEle=document.getElementById(id);
		if(checkboxEle.checked){
			//如果是选中的，则输入框可以更改。
			inputEle.disabled=false;
			inputEle.value=inputEle.value?inputEle.value:"无";
		}else{
			inputEle.disabled=true;
		}
	}
	
	function checkValue(inputEle){
		inputEle.value=inputEle.value?inputEle.value:"无";
	}
</script>