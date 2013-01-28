package com.tfc.word.auto.collect.repository.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WORD_BASE")
public class WordBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5772488651271981256L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long recId;
	/** 具体的词语内容 */
	private String word;
	/** 专用类型，如学科分类。0：通用类型。 */
	private String specialType;
	/** 审核状态，是否已经被审核通过。0：未审核；1：审核通过 */
	private String checkStatus;
	/** 自动学习时，统计得到的数字 */
	private Long statNum;
	private Date addTime;

	public Long getRecId() {
		return recId;
	}

	public void setRecId(Long recId) {
		this.recId = recId;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getSpecialType() {
		return specialType;
	}

	public void setSpecialType(String specialType) {
		this.specialType = specialType;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public Long getStatNum() {
		return statNum;
	}

	public void setStatNum(Long statNum) {
		this.statNum = statNum;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
}
