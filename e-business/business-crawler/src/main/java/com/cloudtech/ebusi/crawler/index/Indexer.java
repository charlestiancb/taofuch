package com.cloudtech.ebusi.crawler.index;

import com.cloudtech.ebusi.crawler.parser.CompanyInfo;

/**
 * 建立索引的工作器
 * 
 * @author taofucheng
 * 
 */
public interface Indexer {
	/**
	 * 将
	 * 
	 * @param com
	 */
	public void indexCom(CompanyInfo com);

}
