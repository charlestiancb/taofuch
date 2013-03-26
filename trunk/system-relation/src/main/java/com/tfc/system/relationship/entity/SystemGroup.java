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
@Table(name = "SYSTEM_GROUP")
public class SystemGroup implements Serializable {
	private static final long serialVersionUID = 4835380591640919832L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long groupId;
	private String groupName;
	private String descript;
	private long orderNum = 1L;
	@Transient
	private List<SystemInfo> systems;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public long getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(long orderNum) {
		this.orderNum = orderNum;
	}

	public List<SystemInfo> getSystems() {
		if (systems == null && getGroupId() != null) {
			String hql = "from SystemInfo where groupId = ? order by orderNum";
			systems = HibernateDao.get(hql, getGroupId());
		}
		return systems;
	}

	public void setSystems(List<SystemInfo> systems) {
		this.systems = systems;
	}
}
