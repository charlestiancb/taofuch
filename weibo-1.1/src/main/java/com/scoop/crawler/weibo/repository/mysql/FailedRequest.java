package com.scoop.crawler.weibo.repository.mysql;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.scoop.crawler.weibo.request.failed.FailedNode;

/**
 * 粉丝
 * 
 * @author taofucheng
 * 
 */
@Entity
@Table(name = "FAILED_REQUEST_URL")
public class FailedRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5849727071642487106L;

	public FailedRequest() {
	}

	public FailedRequest(String url, FailedNode node, String failedReason) {
		setUrl(url);
		if (node != null) {
			setFailedNode(node.name());
		}
		setFailedReason(failedReason);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long recId;
	private String url;
	private String failedNode;
	private String failedReason;

	public Long getRecId() {
		return recId;
	}

	public void setRecId(Long recId) {
		this.recId = recId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFailedNode() {
		return failedNode;
	}

	public void setFailedNode(String failedNode) {
		this.failedNode = failedNode;
	}

	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}

	public String getFailedReason() {
		return failedReason;
	}
}
