package com.scoop.crawler.cnki.export;

import com.scoop.crawler.cnki.entity.SearchRecord;

public abstract class Export {
	/**
	 * 将每条搜索记录存储到文件中！
	 * 
	 * @param record
	 */
	public abstract void write(SearchRecord record);

	/**
	 * 关闭输出流
	 */
	public abstract void close();
}
