package com.tfc.data.access;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * 使用Lucence存取数据。
 * 
 * @author taofucheng
 * 
 */
public class LuceneDataAccess {
	private static FSDirectory mmapDir;
	private static IndexWriter iw;
	private static IndexSearcher is;

	private static final String KEY = "key";
	private static final String VALUE = "value";
	/** 临时存储lucene文件的目录 */
	public static String tempDir;

	private LuceneDataAccess() {
	}

	/**
	 * 初始化Lucene的一些设置
	 * 
	 * @param option
	 */
	private static void init() {
		if (mmapDir != null) {
			// 如果已经初始化过，则不进行
			return;
		}
		tempDir = StringUtils.defaultIfBlank(tempDir, FileUtils.getUserDirectoryPath());
		// 新建临时文件夹，如果已经存在，删除之！如果删除失败，即退出！
		File dir = new File(tempDir, "lucene-tmp");
		if (dir.isDirectory()) {
			try {
				FileUtils.deleteDirectory(dir);
				FileUtils.forceMkdir(dir);
			} catch (IOException e) {
				throw new IllegalAccessError("初始化文件系统失败！" + e);
			}
		}
		try {
			// 初始化Lucene
			mmapDir = FSDirectory.open(dir);
		} catch (IOException e) {
			throw new IllegalAccessError("初始化Lucene失败！" + e);
		}
	}

	private static void releaseIw() {
		if (iw != null) {
			try {
				iw.commit();
				// iw.close();
				// iw = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 保存键值对的数据
	 * 
	 * @param key
	 * @param value
	 */
	public static void save(String key, String value) {
		try {
			init();
			if (iw == null) {
				iw = new IndexWriter(mmapDir, new IndexWriterConfig(Version.LUCENE_36, null));
			}
			org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
			doc.add(new Field(KEY, key, Store.YES, Index.NOT_ANALYZED));
			doc.add(new Field(VALUE, value, Store.YES, Index.NOT_ANALYZED));
			// System.err.println("存储：" + key + "=" + value);
			iw.addDocument(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String findValueByKey(String key) {
		try {
			initSearch();
			Term t = new Term(KEY, key);
			TermQuery query = new TermQuery(t);
			query.setBoost(1);
			TopDocs hits = is.search(query, 1);
			if (hits.totalHits > 0) {
				int docId = hits.scoreDocs[0].doc;
				org.apache.lucene.document.Document doc = is.doc(docId);
				return doc.get(VALUE);
			}
			releaseIs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String findKeyByValue(String value) {
		try {
			initSearch();
			Term t = new Term(VALUE, value);
			TermQuery query = new TermQuery(t);
			query.setBoost(1);
			TopDocs hits = is.search(query, 1);
			if (hits.totalHits > 0) {
				int docId = hits.scoreDocs[0].doc;
				org.apache.lucene.document.Document doc = is.doc(docId);
				return doc.get(KEY);
			}
			releaseIs();
		} catch (Exception e) {
		}
		return null;
	}

	private static void releaseIs() throws IOException {
		is.close();
		is = null;
	}

	private static void initSearch() throws CorruptIndexException, IOException {
		releaseIw();
		init();
		if (is == null) {
			is = new IndexSearcher(IndexReader.open(mmapDir));
		}
	}
}
