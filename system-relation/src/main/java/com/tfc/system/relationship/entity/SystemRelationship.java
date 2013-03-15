package com.tfc.system.relationship.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.tfc.system.relationship.dao.HibernateDao;

@Entity
@Table(name = "SYSTEM_RELATIONSHIP")
public class SystemRelationship implements Serializable {
	private static final long serialVersionUID = 9107785232142469865L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long recId;
	private Long mid;
	private Long sid;
	private String introduce;

	@Transient
	private SystemInfo master;
	@Transient
	private SystemInfo slave;

	public Long getRecId() {
		return recId;
	}

	public void setRecId(Long recId) {
		this.recId = recId;
	}

	public Long getMid() {
		return mid;
	}

	public void setMid(Long mid) {
		this.mid = mid;
	}

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		if (!"无".equals(introduce)) {
			this.introduce = introduce;
		}
	}

	public SystemInfo getMaster() {
		if (master == null) {
			master = HibernateDao.getById(SystemInfo.class, getMid());
		}
		return master;
	}

	public void setMaster(SystemInfo master) {
		this.master = master;
	}

	public SystemInfo getSlave() {
		if (slave == null) {
			slave = HibernateDao.getById(SystemInfo.class, getSid());
		}
		return slave;
	}

	public void setSlave(SystemInfo slave) {
		this.slave = slave;
	}
}
