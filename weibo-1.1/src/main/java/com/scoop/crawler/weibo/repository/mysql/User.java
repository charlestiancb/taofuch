package com.scoop.crawler.weibo.repository.mysql;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

/**
 * 这的信息有：<br>
 * http://www.weibo.com/1097414213/info：昵称、所在地、性别、博客、简介、邮箱、公司、大学、标签、 <br>
 * http://www.weibo.com/1280846847/info：生日<br>
 * 所有：关注数、粉丝数、微博数
 * 
 * 
 * @author taofucheng
 * 
 */
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
	private String gender;
	private String introduce;
	private String addr;
	private String blog;
	private String email;
	private String company;
	private String university;
	private String birthday;
	private Long followNum;
	private Long fansNum;
	private Long weiboNum;
	private String tagInfo;
	private String hasRelation;

	public String getUserId() {
		return StringUtils.trim(userId);
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getBlog() {
		return blog;
	}

	public void setBlog(String blog) {
		this.blog = blog;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
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

	public String toString() {
		return userId + ":" + name;
	}
}
