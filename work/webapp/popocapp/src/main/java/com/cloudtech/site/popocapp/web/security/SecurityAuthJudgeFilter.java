package com.cloudtech.site.popocapp.web.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityAuthJudgeFilter implements Filter {
	private String loginUrl;

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		loginUrl = StringUtils.defaultIfBlank(
				filterConfig.getInitParameter("login-url"), "");
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			HttpServletResponse res = (HttpServletResponse) response;
			if (StringUtils.isNotBlank(loginUrl)) {
				res.sendRedirect(loginUrl);
				return;
			} else {
				res.sendError(HttpServletResponse.SC_UNAUTHORIZED,
						"Authentication Failed!");
			}
		}
		chain.doFilter(request, response);
	}

	public void destroy() {
	}
}
