package com.ss.language.model.data.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "HLDA_RESULT")
public class HldaResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1673037818170242604L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long recId;
	private String documentId;
	private String ldaIndex;
	private String ldaProbability;
	private String ldaLabel;

	public Long getRecId() {
		return recId;
	}

	public void setRecId(Long recId) {
		this.recId = recId;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getLdaIndex() {
		return ldaIndex;
	}

	public void setLdaIndex(String ldaIndex) {
		this.ldaIndex = ldaIndex;
	}

	public String getLdaProbability() {
		return ldaProbability;
	}

	public void setLdaProbability(String ldaProbability) {
		this.ldaProbability = ldaProbability;
	}

	public String getLdaLabel() {
		return ldaLabel;
	}

	public void setLdaLabel(String ldaLabel) {
		this.ldaLabel = ldaLabel;
	}
}
