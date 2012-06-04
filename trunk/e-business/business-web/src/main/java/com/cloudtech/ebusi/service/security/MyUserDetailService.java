package com.cloudtech.ebusi.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cloudtech.ebusi.dao.UserDao;
import com.cloudtech.ebusi.entity.User;

public class MyUserDetailService implements UserDetailsService {
	@Autowired
	private UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("username is " + username);
		User user = this.userDao.selectByName(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return user;
	}
}