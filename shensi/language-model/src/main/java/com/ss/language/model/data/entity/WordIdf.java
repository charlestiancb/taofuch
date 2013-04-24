package com.ss.language.model.data.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WORD_IDF")
public class WordIdf implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2472614033968207868L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long recId;
	private String word;
	private Double idf;

	public WordIdf() {
	}

	public WordIdf(String word, Double idf) {
		setWord(word);
		setIdf(idf);
	}

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

	public Double getIdf() {
		return idf;
	}

	public void setIdf(Double idf) {
		this.idf = idf;
	}
}
