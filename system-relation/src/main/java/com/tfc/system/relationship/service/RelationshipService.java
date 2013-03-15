package com.tfc.system.relationship.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.tfc.system.relationship.dao.HibernateDao;
import com.tfc.system.relationship.entity.SystemRelationship;

@Service
public class RelationshipService {

	public void modify(String[] relations, String[] introduces) {
		// 先将所有的指定类型的关系都删除！然后将这些关系写进去！
		String hql = "delete from SystemRelationship";
		HibernateDao.executeHql(hql);
		// 将新的关系写入！
		if (relations != null && relations.length > 0) {
			for (int i = 0; i < relations.length; i++) {
				String r = StringUtils.trim(relations[i]);
				if (StringUtils.isEmpty(r)) {
					continue;
				}
				String[] msid = r.split("_");
				if (msid.length != 2) {
					continue;
				}
				SystemRelationship relation = new SystemRelationship();
				relation.setMid(Long.parseLong(msid[0]));
				relation.setSid(Long.parseLong(msid[1]));
				relation.setIntroduce(introduces == null || introduces.length < i + 1 ? "" : introduces[i]);
				HibernateDao.save(relation);
			}
		}
	}
}
