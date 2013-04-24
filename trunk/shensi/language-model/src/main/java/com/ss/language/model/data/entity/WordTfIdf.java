package com.ss.language.model.data.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WORD_TF_IDF")
public class WordTfIdf implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3415261727802552301L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long recId;
	private String documentTitle;
	private Long wordId;
	private int tf;
	private Double tfIdf;

	public WordTfIdf() {
	}

	public WordTfIdf(String documentTitle, Long wordId, int tf) {
		setDocumentTitle(documentTitle);
		setWordId(wordId);
		setTf(tf);
	}

	public Long getRecId() {
		return recId;
	}

	public void setRecId(Long recId) {
		this.recId = recId;
	}

	public Long getWordId() {
		return wordId;
	}

	public void setWordId(Long wordId) {
		this.wordId = wordId;
	}

	public int getTf() {
		return tf;
	}

	public void setTf(int tf) {
		this.tf = tf;
	}

	public Double getTfIdf() {
		return tfIdf;
	}

	public void setTfIdf(Double tfIdf) {
		this.tfIdf = tfIdf;
	}

	public String getDocumentTitle() {
		return documentTitle;
	}

	public void setDocumentTitle(String documentTitle) {
		this.documentTitle = documentTitle;
	}
}
