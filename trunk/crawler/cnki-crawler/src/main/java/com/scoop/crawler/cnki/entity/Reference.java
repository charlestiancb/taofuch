package com.scoop.crawler.cnki.entity;

import org.apache.commons.lang.StringUtils;

public class Reference implements Comparable<Reference> {
	private String relationId;
	private int year;
	// 年份后面的补充信�?
	private String yearAdd;
	private String author;
	private String title;
	private String institution;

	/** 获取各个字段的名称 */
	public static String[] getPropNames() {
		return new String[] { "主文章序号", "文章标题", "作者", "期刊名称", "年份", "期数" };
	}

	/** 获取各个字段的值 */
	public String[] getPropValues() {
		return new String[] { relationId, title, author, institution,
				String.valueOf(year), yearAdd };
	}

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
		this.institution = StringUtils.defaultIfBlank(institution, "")
				.replaceAll(" ", " ");
	}

	public String toString() {
		return author + ". " + title + " " + institution + " " + year + "("
				+ yearAdd + ")";
	}

	public int compareTo(Reference o) {
		if (o == null) {
			return -1;
		} else {
			return o.getYear() > getYear() ? 1 : -1;
		}
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
}