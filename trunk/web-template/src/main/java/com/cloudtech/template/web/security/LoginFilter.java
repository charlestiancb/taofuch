package com.cloudtech.template.web.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.web.context.ContextLoader;

import com.cloudtech.template.utils.EncryptorUtils;
import com.cloudtech.template.web.controller.CapchaController;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		// 验证码
		Cookie[] cs = request.getCookies();
		if (cs != null && cs.length > 0) {
			for (Cookie c : cs) {
				if (c == null || !CapchaController.CAPCHA_COOKIE_KEY.equals(c.getName())) {
					continue;
				} else {
					// 找到验证码cookie，然后取出来进行验证
					EncryptorUtils enc = ContextLoader.getCurrentWebApplicationContext().getBean(EncryptorUtils.class);
					if (enc.checkPass(request.getParameter("validcode"), c.getValue())) {
						return super.attemptAuthentication(request, response);
					}
				}
			}
		}
		throw new NonceExpiredException("验证码错误");
	}
}
