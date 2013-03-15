package com.tfc.system.relationship.service;

import java.util.List;

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
		return HibernateDao.getAll(SystemInfo.class);
	}

}
