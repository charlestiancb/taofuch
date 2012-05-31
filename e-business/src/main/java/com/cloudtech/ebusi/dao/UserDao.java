package com.cloudtech.ebusi.dao;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cloudtech.ebusi.entity.User;

@Repository
public class UserDao extends BaseDao {
	@Autowired
	private RoleDao roleDao;
	/** 所有用户的缓存 */
	private Map<String, User> users = new ConcurrentHashMap<String, User>();

	public User selectByName(String username) {
		username = StringUtils.trim(username);
		if (StringUtils.isBlank(username)) {
			return null;
		}
		if (users.get(username) != null) {
			return users.get(username);
		}
		List<String> us = readFile("users.txt");
		if (us == null || us.isEmpty()) {
			return null;
		}
		for (String u : us) {
			if (StringUtils.isNotBlank(u) && u.startsWith(username + ",")) {
				String[] userInfo = u.split(",");
				User user = new User(userInfo[0], userInfo[1], true, true, true, true, roleDao.getAuths(userInfo[3]));
				user.setDisplayName(userInfo[2]);
				user.setRole(roleDao.selectByName(userInfo[3]));
				users.put(user.getUsername(), user);
				return user;
			}
		}
		return null;
	}

	/**
	 * 如果用户信息不存在，则添加；如果存在，则修改！
	 * 
	 * @param user
	 */
	public void merge(User user) {
		if (user == null) {
			return;
		}
		User u = selectByName(user.getUsername());
		if (u == null) {
			// 如果没有，则添加
		} else {
			// TODO 完成merge行为！
		}
	}
}
