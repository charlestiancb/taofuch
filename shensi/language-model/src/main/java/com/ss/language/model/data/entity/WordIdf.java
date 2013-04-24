package com.ss.language.model.data.entity;

import javax.persistence.Id;

public class WordIdf {
	@Id
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
