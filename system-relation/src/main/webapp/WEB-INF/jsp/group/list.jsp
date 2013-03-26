<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../include/header.jsp" />
<center>
	<table>
		<tr>
			<td>分组名称</td>
			<td>描述</td>
			<td>排序</td>
			<td nowrap="nowrap"><a href="/group/new">添加</a></td>
		</tr>
		<c:forEach var="group" items="${groups}">
			<tr>
				<td><a href="/group/${group.groupId}/show">${group.groupName}</a></td>
				<td>${group.descript}</td>
				<td>
					<input type="hidden" id="hid_${group.groupId}" value="${group.orderNum}"/>
					<input title="输入新的排序，将自动保存" type="text" id="ord_${group.groupId}" value="${group.orderNum}" onblur="change(this)"/>
				</td>
				<td nowrap="nowrap">
					<a href="/group/${group.groupId}">修改</a>&nbsp;
					<a href="#" onclick="delSys(${group.groupId})">删除</a>
				</td>
			</tr>
		</c:forEach>
	</table>
</center>
<script>
	function delSys(sysId){
		if (confirm("删除它吗？删除之后，与它相关的所有关系配置也都将被删除！"))  {
			var url="/group/"+sysId+"/delete";
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
				  url: "/group/"+id.substring(4)+"/order",
				  data: { orderNum: obj.value }
				}).done(function( msg ) {
				  //alert(  msg );
				});
			}
		}
	}
</script>