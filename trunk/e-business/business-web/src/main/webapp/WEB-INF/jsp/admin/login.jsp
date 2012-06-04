<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="../layout/header.jsp" />
<form action="/admin/logon" method="post">
	<center>
		<table>
			<tr>
				<th>登录名：</th><td><input type="text" name="j_username"/></td>
			</tr>
			<tr>
				<th>密码：</th><td><input type="password" name="j_password"/></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="登录"/></td>
			</tr>
		</table>
	</center>
</form>
<jsp:include page="../layout/footer.jsp" />