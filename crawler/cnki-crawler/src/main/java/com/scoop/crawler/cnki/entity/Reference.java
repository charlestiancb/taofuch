package com.scoop.crawler.cnki.entity;

public class Reference implements Comparable<Reference> {
	private int year;
	// 年份后面的补充信�?
	private String yearAdd;
	private String author;
	private String title;
	private String institution;

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getYearAdd() {
		return yearAdd;
	}

	public void setYearAdd(String yearAdd) {
		this.yearAdd = yearAdd;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String toString() {
		return author + ". " + title + " " + institution + " " + year + "(" + yearAdd + ")";
	}

	public int compareTo(Reference o) {
		if (o == null) {
			return -1;
		} else {
			return o.getYear() > getYear() ? 1 : -1;
		}
	}
}