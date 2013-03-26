package com.tfc.system.relationship.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.tfc.system.relationship.dao.HibernateDao;
import com.tfc.system.relationship.entity.SystemInfo;

@Service
public class SystemService {

	public void add(SystemInfo sys) {
		HibernateDao.save(sys);
	}

	public void delete(String id) {
		SystemInfo sys = new SystemInfo();
		sys.setSysId(Long.parseLong(id));
		HibernateDao.delete(sys);
	}

	public SystemInfo getById(String id) {
		try {
			return HibernateDao.getById(SystemInfo.class, Long.parseLong(id));
		} catch (Exception e) {
			return null;
		}
	}

	public List<SystemInfo> getByIds(String... ids) {
		List<SystemInfo> result = new ArrayList<SystemInfo>();
		try {
			for (String id : ids) {
				id = StringUtils.trim(id);
				if (id == null || id.isEmpty()) {
					continue;
				}
				SystemInfo sys = HibernateDao.getById(SystemInfo.class, Long.parseLong(id));
				if (sys != null) {
					result.add(sys);
				}
			}
		} catch (Exception e) {
			return new ArrayList<SystemInfo>();
		}
		return result;
	}

	public void modify(SystemInfo sys) {
		String hql = "select count(*) from SystemInfo where name = ? and sysId != ?";
		long count = HibernateDao.count(hql, sys.getName(), sys.getSysId());
		if (count == 0) {
			HibernateDao.update(sys);
		} else {
			throw new RuntimeException("该名称的系统已经存在！");
		}
	}

	public List<SystemInfo> getAll() {
		return HibernateDao.get("from SystemInfo order by groupId,orderNum");
	}

	public void changeOrder(String sysId, Long orderNum) {
		SystemInfo sys = getById(sysId);
		sys.setOrderNum(orderNum);
		HibernateDao.update(sys);
	}

	public List<SystemInfo> getByGroupId(Long groupId) {
		if (groupId == null) {
			return new ArrayList<SystemInfo>();
		}
		return HibernateDao.get("from SystemInfo where groupId = ? order by orderNum", groupId);
	}

}
