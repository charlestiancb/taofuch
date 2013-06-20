<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<form action="/admin/logon" method="post">
	<center>
		<table>
			<tr>
				<th>用户名</th><td><input type="text" name="j_username"/></td>
			</tr>
			<tr>
				<th>密码</th><td><input type="password" name="j_password"/></td>
			</tr>
			<tr>
				<th>验证码</th>
				<td>
					<input type="text" name="validcode"/>
					<img title="" src="/validcode?n=<%=System.nanoTime()%>" onclick="reload(this);">
				</td>
			</tr>
			<tr>
				<td><input type="submit" value="登录"/></td>
				<td><input type="reset" value="重置"/></td>
			</tr>
		</table>
	</center>
</form>
<script>
function reload(obj){
	obj.src="/validcode?n="+new Date().getTime();
}
</script>