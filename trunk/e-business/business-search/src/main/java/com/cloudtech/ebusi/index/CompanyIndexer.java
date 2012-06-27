package com.cloudtech.ebusi.index;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.cloudtech.ebusi.crawler.index.Indexer;
import com.cloudtech.ebusi.crawler.parser.CompanyInfo;
import com.cloudtech.ebusi.crawler.utils.FileReaderUtils;
import com.cloudtech.ebusi.search.Searcher;

public class CompanyIndexer implements Indexer {
	protected static IndexWriter iw = null;
	/** 分词器，全系统共用这一个 */
	public static final Analyzer analyzer = new IKAnalyzer(true);
	public static File indexDir = null;
	static {
		String file = FileReaderUtils.readFromFile(CompanyIndexer.class.getResourceAsStream("/context/index/conf.properties"))
										.get("search.index.directory");
		if (StringUtils.isBlank(file)) {
			file = System.getProperty("java.io.tmpDir");
		}
		IndexWriterConfig iwConf = new IndexWriterConfig(Version.LUCENE_36, analyzer);
		indexDir = new File(file);
		try {
			iw = new IndexWriter(FSDirectory.open(indexDir), iwConf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void indexCom(CompanyInfo com) {
		try {
			// 先判断是否已经存在于索引中，如果不存在才重建索引
			String url = StringUtils.trimToEmpty(com.getB2bHomepage());
			if (url.length() > 0) {
				TermQuery query = new TermQuery(new Term(CompanyInfo.INDEX_B2B, url));
				TopDocs top = Searcher.indexSearher.search(query, 1);
				if (top != null && top.totalHits > 0) {
					// 如果已经存在，则直接不操作下面的逻辑
					return;
				}
			}
			Document doc = new Document();
			// base info
			doc.add(new Field(CompanyInfo.INDEX_NAME, com.getCompanyName(), Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field(CompanyInfo.INDEX_INTRO, com.getIntroduce(), Field.Store.YES, Field.Index.ANALYZED));

			doc.add(new Field(CompanyInfo.INDEX_B2B, com.getIntroduce(), Field.Store.YES, Field.Index.NOT_ANALYZED));
			// details
			for (String key : CompanyInfo.COM_INDEX_KEYS) {
				if (com.getDetails().get(key) == null || com.getDetails().get(key).trim().isEmpty()) {
					continue;
				}
				doc.add(new Field(key, com.getDetails().get(key), Field.Store.YES, Field.Index.ANALYZED));
			}
			iw.addDocument(doc, analyzer);
			iw.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
