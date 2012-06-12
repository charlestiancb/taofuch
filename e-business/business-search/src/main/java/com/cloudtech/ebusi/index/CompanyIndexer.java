package com.cloudtech.ebusi.index;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.index.IndexWriter;

import com.cloudtech.ebusi.crawler.index.Indexer;
import com.cloudtech.ebusi.crawler.utils.FileReaderUtils;

public class CompanyIndexer implements Indexer {
	private static IndexWriter iw = null;
	static {
		String file = FileReaderUtils.readFromFile(CompanyIndexer.class.getResourceAsStream("/context/index/conf.properties"))
										.get("search.index.directory");
		if (StringUtils.isBlank(file)) {
			file = System.getProperty("java.io.tmpDir");
		}
		// IndexWriterConfig iwConf=new IndexWriterConfig(Version.LUCENE_36,
		// analyzer);
		// iw = new IndexWriter(new File(file),iwConf);
	}
}
