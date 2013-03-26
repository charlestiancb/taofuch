package com.tfc.system.relationship.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.tfc.system.relationship.dao.HibernateDao;

@Entity
@Table(name = "SYSTEM_INFO")
public class SystemInfo implements Serializable {
	private static final long serialVersionUID = -9018123057924489347L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long sysId;
	private String name;
	private String url;
	private String introduce;
	private long orderNum;

	/** 调用的对象 */
	@Transient
	private List<SystemRelationship> calls;
	/** 被它们调用 */
	@Transient
	private List<SystemRelationship> calleds;

	public Long getSysId() {
		return sysId;
	}

	public void setSysId(Long sysId) {
		this.sysId = sysId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		if (url == null || url.trim().isEmpty()) {
			return "";
		}
		return url.trim().toLowerCase().indexOf("://") != -1 ? url : "http://" + url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public List<SystemRelationship> getCalls() {
		if (calls == null && getSysId() != null) {
			String hql = "from SystemRelationship where mid = ?";
			calls = HibernateDao.get(hql, getSysId());
		}
		return calls;
	}

	public void setCalls(List<SystemRelationship> calls) {
		this.calls = calls;
	}

	public List<SystemRelationship> getCalleds() {
		if (calleds == null && getSysId() != null) {
			String hql = "from SystemRelationship where sid = ?";
			calleds = HibernateDao.get(hql, getSysId());
		}
		return calleds;
	}

	public void setCalleds(List<SystemRelationship> calleds) {
		this.calleds = calleds;
	}

	public boolean hasRelation(Long mid, Long sid) {
		if (mid == null || sid == null) {
			return false;
		}
		String hql = "select count(*) from SystemRelationship where mid = ? and sid = ?";
		long cnt = HibernateDao.count(hql, mid, sid);
		return cnt > 0;
	}

	public SystemRelationship getRelation(Long mid, Long sid) {
		if (mid == null || sid == null) {
			return null;
		}
		String hql = "from SystemRelationship where mid = ? and sid = ?";
		return (SystemRelationship) HibernateDao.unique(hql, mid, sid);
	}

	public long getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(long orderNum) {
		this.orderNum = orderNum;
	}
}
