package com.cloudtech.template.web.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;

@SuppressWarnings("deprecation")
public class LoginUrlAuthenticationEntryPoint extends
		org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint {
	protected String determineUrlToUseForThisRequest(
			HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException exception) {
		// 将当前的请求链接保存在cookie中，以便登录成功后可以跳转！
		return getLoginFormUrl();
	}
}
