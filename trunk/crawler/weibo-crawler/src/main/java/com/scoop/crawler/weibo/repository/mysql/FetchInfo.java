package com.scoop.crawler.weibo.repository.mysql;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

@Entity
@Table(name = "FETCH_INFO")
public class FetchInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2919203120951739122L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long recId;
	private String queryStr;
	private String relationId;
	private String relationType;

	public FetchInfo() {
	}

	public FetchInfo(String queryStr, String relationId, String relationType) {
		setQueryStr(queryStr);
		setRelationId(relationId);
		setRelationType(relationType);
	}

	public Long getRecId() {
		return recId;
	}

	public void setRecId(Long recId) {
		this.recId = recId;
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	/**
	 * 是否需要保存！
	 * 
	 * @return
	 */
	public boolean needSave() {
		return StringUtils.isNotBlank(queryStr);
	}
}
