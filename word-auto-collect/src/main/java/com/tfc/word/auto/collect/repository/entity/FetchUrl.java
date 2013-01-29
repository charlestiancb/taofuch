package com.tfc.word.auto.collect.repository.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FETCH_URL")
public class FetchUrl implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6810869952602166143L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long recId;
	private String url;
	private Date addTime;

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

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
}
