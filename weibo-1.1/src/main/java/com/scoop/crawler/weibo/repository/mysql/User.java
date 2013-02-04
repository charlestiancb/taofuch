package com.scoop.crawler.weibo.repository.mysql;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 517821528614137051L;
	@Id
	private String userId;
	private String name;
	private String url;
	private String info;
	private String favor;
	private String introduce;
	private Long followNum;
	private Long fansNum;
	private Long weiboNum;
	private String tagInfo;
	private String hasRelation;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getFavor() {
		return favor;
	}

	public void setFavor(String favor) {
		this.favor = favor;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public Long getFollowNum() {
		return followNum;
	}

	public void setFollowNum(Long followNum) {
		this.followNum = followNum;
	}

	public Long getFansNum() {
		return fansNum;
	}

	public void setFansNum(Long fansNum) {
		this.fansNum = fansNum;
	}

	public Long getWeiboNum() {
		return weiboNum;
	}

	public void setWeiboNum(Long weiboNum) {
		this.weiboNum = weiboNum;
	}

	public String getTagInfo() {
		return tagInfo;
	}

	public void setTagInfo(String tagInfo) {
		this.tagInfo = tagInfo;
	}

	public String getHasRelation() {
		return hasRelation;
	}

	public void setHasRelation(String hasRelation) {
		this.hasRelation = hasRelation;
	}
}
