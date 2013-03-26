package com.tfc.system.relationship.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tfc.system.relationship.dao.HibernateDao;
import com.tfc.system.relationship.entity.SystemGroup;

@Service
public class GroupService {
	public void add(SystemGroup group) {
		HibernateDao.save(group);
	}

	public void delete(String id) {
		SystemGroup group = new SystemGroup();
		group.setGroupId(Long.parseLong(id));
		HibernateDao.delete(group);
	}

	public SystemGroup getById(String id) {
		try {
			return HibernateDao.getById(SystemGroup.class, Long.parseLong(id));
		} catch (Exception e) {
			return null;
		}
	}

	public void modify(SystemGroup group) {
		String hql = "select count(*) from SystemGroup where groupName = ? and groupId != ?";
		long count = HibernateDao.count(hql, group.getGroupName(), group.getGroupId());
		if (count == 0) {
			HibernateDao.update(group);
		} else {
			throw new RuntimeException("该名称的系统已经存在！");
		}
	}

	public List<SystemGroup> getAll() {
		return HibernateDao.get("from SystemGroup order by orderNum");
	}

	public void changeOrder(String sysId, Long orderNum) {
		SystemGroup group = getById(sysId);
		group.setOrderNum(orderNum);
		HibernateDao.update(group);
	}
}
