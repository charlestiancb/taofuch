package com.cloudtech.ebusi.service.security;

import java.util.Collection;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import com.cloudtech.ebusi.entity.User;
import com.cloudtech.ebusi.utils.AuthUtils;

public class MyVoter implements AccessDecisionVoter<Object> {

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(FilterInvocation.class);
	}

	@Override
	public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
		if (authentication == null) {
			// 如果没有登录信息，则直接不通过！
			return ACCESS_DENIED;
		}
		if (authentication instanceof AnonymousAuthenticationToken) {
			// 如果是匿名的，则直接不通过！
			return ACCESS_DENIED;
		}
		// 获取当前用户所有可以访问的链接正则表达式
		User curUser = AuthUtils.getCurUser();
		Collection<GrantedAuthority> auths = curUser.getAuthorities();
		if (auths == null || auths.isEmpty()) {
			return ACCESS_DENIED;
		}
		FilterInvocation fi = (FilterInvocation) object;
		String requestUri = fi.getRequestUrl();
		for (GrantedAuthority ga : auths) {
			if (StringUtils.isNotEmpty(ga.getAuthority()) && Pattern.matches(ga.getAuthority(), requestUri)) {
				return ACCESS_GRANTED;
			}
		}
		return ACCESS_DENIED;
	}

}
