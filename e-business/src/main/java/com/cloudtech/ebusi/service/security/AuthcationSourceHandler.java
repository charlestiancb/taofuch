package com.cloudtech.ebusi.service.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationDetailsSource;

public class AuthcationSourceHandler implements AuthenticationDetailsSource<ApplicationContext, HttpServletRequest> {

	@Override
	public HttpServletRequest buildDetails(ApplicationContext context) {
		// TODO Auto-generated method stub
		return null;
	}

}
