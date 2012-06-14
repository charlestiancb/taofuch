package com.cloudtech.ebusi.index;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.cloudtech.ebusi.crawler.index.Indexer;
import com.cloudtech.ebusi.crawler.utils.FileReaderUtils;

public class CompanyIndexer implements Indexer {
	protected static IndexWriter iw = null;
	/** 分词器，全系统共用这一个 */
	public static Analyzer analyzer = null;
	public static File indexDir = null;
	static {
		String file = FileReaderUtils.readFromFile(CompanyIndexer.class.getResourceAsStream("/context/index/conf.properties"))
										.get("search.index.directory");
		if (StringUtils.isBlank(file)) {
			file = System.getProperty("java.io.tmpDir");
		}
		// TODO 初始化分词器
		IndexWriterConfig iwConf = new IndexWriterConfig(Version.LUCENE_36, analyzer);
		indexDir = new File(file);
		try {
			iw = new IndexWriter(FSDirectory.open(indexDir), iwConf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
