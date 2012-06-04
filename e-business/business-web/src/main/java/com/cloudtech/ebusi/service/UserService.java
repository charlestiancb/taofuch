package com.cloudtech.ebusi.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudtech.ebusi.dao.UserDao;
import com.cloudtech.ebusi.entity.User;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;

	public boolean validate(String j_username, String j_password) {
		if (StringUtils.isBlank(j_username) || StringUtils.isBlank(j_password)) {
			return false;
		}
		User user = userDao.selectByName(j_username);
		if (user == null || !DigestUtils.shaHex(j_password).equals(user.getPassword())) {
			return false;
		}
		return true;
	}
}
