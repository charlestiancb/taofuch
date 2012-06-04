package com.cloudtech.ebusi.dao;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import com.cloudtech.ebusi.entity.Role;

@Repository
public class RoleDao extends BaseDao {
	private Map<String, Role> roles = new ConcurrentHashMap<String, Role>();

	public Collection<? extends GrantedAuthority> getAuths(String roleName) {
		Set<SimpleGrantedAuthority> auths = new HashSet<SimpleGrantedAuthority>();
		if (StringUtils.isBlank(roleName)) {
			return auths;
		}
		Role r = roles.get(roleName);
		if (r == null) {
			r = selectByName(roleName);
		}
		if (r != null && StringUtils.isNotBlank(r.getRules())) {
			for (String rule : r.getRules().split(";")) {
				auths.add(new SimpleGrantedAuthority(rule));
			}
		}
		return auths;
	}

	public Role selectByName(String roleName) {
		roleName = StringUtils.trim(roleName);
		if (roles.get(roleName) != null) {
			return roles.get(roleName);
		}
		List<String> rs = readFile("roles.txt");
		if (rs != null && !rs.isEmpty()) {
			for (String r : rs) {
				if (StringUtils.isNotBlank(r) && r.startsWith(roleName + ",")) {
					String[] roleInfo = r.split(",");
					roles.put(roleName, new Role(roleInfo[0], roleInfo[1], roleInfo[2]));
					break;
				}
			}
		}
		return roles.get(roleName);
	}
}
