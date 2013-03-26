package com.tfc.system.relationship.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfc.system.relationship.dao.HibernateDao;
import com.tfc.system.relationship.entity.SystemRelationship;

@Service
public class RelationshipService {
	@Autowired
	private SystemService sysService;

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

	/**
	 * 更改一个指定的关系。
	 * 
	 * @param sysId
	 *            当前操作的系统。
	 * @param selectedSysId
	 *            选中的编号
	 * @param introduce
	 *            关系说明
	 * @param relationType
	 *            关系类型，调用，还是被调用。1：调用；2：被调用
	 * @param operType
	 *            操作类型，1：增加或修改；2：删除
	 */
	public void modifyRelation(long sysId, long selectedSysId, String introduce, int relationType, int operType) {
		String where = " where ";
		long mid = sysId;
		long sid = selectedSysId;
		if (relationType == 1) {
			// 调用
			where += "mid = ? and sid = ? ";
		} else if (relationType == 2) {
			// 被调用
			where += "sid = ? and mid = ? ";
			mid = selectedSysId;
			sid = sysId;
		}
		if (operType == 1) {
			// 增加或修改
			String hql = "from SystemRelationship" + where;
			List<SystemRelationship> res = HibernateDao.get(hql, sysId, selectedSysId);
			if (res == null || res.isEmpty()) {
				// 不存在，则是增加
				SystemRelationship srs = new SystemRelationship();
				srs.setIntroduce(introduce);
				srs.setMid(mid);
				srs.setSid(sid);
				HibernateDao.save(srs);
			} else {
				// 否则就是修改
				SystemRelationship srs = res.get(0);
				srs.setIntroduce(introduce);
				srs.setMid(mid);
				srs.setSid(sid);
				HibernateDao.update(srs);
			}
		} else if (operType == 2) {
			// 删除
			String hql = "delete from SystemRelationship" + where;
			HibernateDao.executeHql(hql, sysId, selectedSysId);
		}
	}
}
