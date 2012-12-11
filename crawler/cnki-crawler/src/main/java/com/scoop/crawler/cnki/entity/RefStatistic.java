package com.scoop.crawler.cnki.entity;

import java.util.ArrayList;
import java.util.List;


/**
 * 引文统计信息
 * 
 * @author taofucheng
 * 
 */
public class RefStatistic {
	private int year;
	private int count;
	private List<Reference> refs = new ArrayList<Reference>();

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Reference> getRefs() {
		return refs;
	}

	public void addRef(Reference ref) {
		if (ref != null) {
			refs.add(ref);
		}
	}

	public String toString() {
		return year + "\t" + count + "\t" + getRefsString();
	}

	private String getRefsString() {
		StringBuilder sb = new StringBuilder();
		for (Reference r : refs) {
			sb.append(r.toString());
			sb.append("；");
		}
		return sb.length() > 1 ? sb.substring(0, sb.length() - 1) : sb.toString();
	}
}
