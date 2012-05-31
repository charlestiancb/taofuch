package com.cloudtech.ebusi.utils;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cloudtech.ebusi.entity.User;

public class AuthUtils {
	/**
	 * 判断当前用户是否已经登录
	 * 
	 * @return
	 */
	public static boolean isLogin() {
		return getCurUser() != null;
	}

	/**
	 * 获取当前登录用户的信息
	 * 
	 * @return
	 */
	public static User getCurUser() {
		User curUser = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (context != null && context.getAuthentication() != null) {
			curUser = (User) context.getAuthentication().getPrincipal();
		}
		return curUser;
	}
}
