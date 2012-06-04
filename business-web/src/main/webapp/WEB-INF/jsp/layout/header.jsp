<%@page import="com.cloudtech.ebusi.utils.AuthUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
table {
	border-collapse: collapse;
}

td {
	border: 1px solid #06c;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${q} <c:if test="${!empty q}">-</c:if> 易商，让您的商业变得简单！</title>
</head>
<body>
<%if(AuthUtils.isLogin()){ %><span id="user" style="right: inherit;"><a href="/logout">退出</a></span><%}%>