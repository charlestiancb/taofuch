<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../include/header.jsp" />
<center>
<form action="/relationship/relations" method="post">
<c:forEach var="group" items="${groups}">
	<table>
		<caption>组名：${group.groupName}</caption>
		<tr>
			<td>系统名称</td>
			<td>主页地址</td>
			<td>简介</td>
			<td>排序</td>
			<td nowrap="nowrap"><a href="/sys/new">添加</a>&nbsp;<a
				href="/relationship">整体关系一览</a>&nbsp;<a
				href="/group">分组信息</a></td>
		</tr>
		<c:forEach var="system" items="${group.systems}">
			<tr>
				<td><input type="checkbox" name="ids" value="${system.sysId}"/><a href="/sys/${system.sysId}/show">${system.name}</a></td>
				<td><c:if test="${!empty system.url}"><a href="${system.url}" target="_blank">${system.url}</a></c:if></td>
				<td>${system.introduce}</td>
				<td>
					<input type="hidden" id="hid_${system.sysId}" value="${system.orderNum}"/>
					<input title="输入新的排序，将自动保存" type="text" id="ord_${system.sysId}" value="${system.orderNum}" onblur="change(this)"/>
				</td>
				<td nowrap="nowrap"><a href="/sys/${system.sysId}">修改</a>&nbsp;<a
					href="#" onclick="delSys(${system.sysId})">删除</a>&nbsp;<a
					href="/relationship/${system.sysId}">查看调用关系</a></td>
			</tr>
		</c:forEach>
	</table>
</c:forEach>
	<input type="submit" value="查看选中的对象关系"/>
</form>
</center>
<script>
	function delSys(sysId){
		if (confirm("删除它吗？删除之后，与它相关的所有关系配置也都将被删除！"))  {
			var url="/sys/"+sysId+"/delete";
			window.location.href=url;
		}
	}
	/**ajax方式更改排序*/
	function change(obj){
		if(obj){
			var id=obj.id;
			id = "hid_"+id.substring(4);
			var hid = document.getElementById(id);
			//先判断是否一致，如果一致，则不需要修改；如果不一致，表示需要修改。
			if(obj.value && obj.value !== hid.value){
				$.ajax({
				  type: "POST",
				  url: "/sys/"+id.substring(4)+"/order",
				  data: { orderNum: obj.value }
				}).done(function( msg ) {
				  //alert(  msg );
				});
			}
		}
	}
</script>