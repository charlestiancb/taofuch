package com.scoop.crawler.cnki.entity;

import java.util.List;

/**
 * 搜索结果的每条记录
 * 
 * @author taofucheng
 * 
 */
public class SearchRecord {
	/** 序号 */
	private String index;
	/** 被引题名 */
	private String title;
	/** 被引作者 */
	private String author;
	/** 被引文献来源 */
	private String origin;
	/** 发表时间 */
	private String publishTime;
	/** 被引次数 */
	private String count;
	/** 被引用的文献 */
	private List<RefStatistic> refs;

	/** 获取各个字段的名称 */
	public String[] getPropNames() {
		return new String[] { "序号", "被引题名", "被引作者", "被引文献来源", "发表时间", "被引次数" };
	}

	/** 获取各个字段的值 */
	public String[] getPropValues() {
		return new String[] { index, title, author, origin, publishTime, count };
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public List<RefStatistic> getRefs() {
		return refs;
	}

	public void setRefs(List<RefStatistic> refs) {
		this.refs = refs;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
}
