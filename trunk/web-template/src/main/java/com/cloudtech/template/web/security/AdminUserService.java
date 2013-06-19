package com.cloudtech.template.web.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 后台权限相关的用户资源操作的服务类
 * 
 * @author taofucheng
 * 
 */
public class AdminUserService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		System.out.println("进入AdminUserService类中！");
		return null;
	}
}
