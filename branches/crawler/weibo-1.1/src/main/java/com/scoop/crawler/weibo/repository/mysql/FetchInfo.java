package com.scoop.crawler.weibo.repository.mysql;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.scoop.crawler.weibo.entity.Query;

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
	private String topicvector;
	private String relationId;
	private String relationType;

	public FetchInfo() {
	}

	/**
	 * 指定的抓取的相关内容。
	 * 
	 * @param queryStr
	 *            URL查询的词语
	 * @param relationId
	 *            保存该抓取信息的功能中记录的id
	 * @param relationType
	 *            保存该抓取信息的功能
	 */
	public FetchInfo(Query query, String relationId, String relationType) {
		if (query != null) {
			setQueryStr(query.getQueryStr());
			setTopicvector(query.getCollectionName());
		}
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
	 * 是否需要保存！如果有查询词，则要保存！
	 * 
	 * @return
	 */
	public boolean needSave() {
		return StringUtils.isNotBlank(queryStr);
	}

	public String getTopicvector() {
		return topicvector;
	}

	public void setTopicvector(String topicvector) {
		this.topicvector = topicvector;
	}

}
