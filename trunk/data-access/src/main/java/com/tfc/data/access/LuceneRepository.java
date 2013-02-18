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

public class LuceneRepository extends Repository {
	private FSDirectory mmapDir;
	private IndexWriter iw;
	private IndexSearcher is;

	private EhcacheRepository ehcache = new EhcacheRepository(true);

	private static final String ID = "id";
	private static final String KEY = "key";
	private static final String VALUE = "value";
	/** 临时存储lucene文件的目录 */
	public String tempDir;

	public LuceneRepository() {
		this(null);
	}

	public LuceneRepository(String tmpDir) {
		this.tempDir = tmpDir;
		init();
	}

	/**
	 * 初始化Lucene的一些设置
	 * 
	 * @param option
	 */
	private void init() {
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

	private void releaseIw() {
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
	public SaveType save(String id, String key, String value) {
		try {
			init();
			if (iw == null) {
				iw = new IndexWriter(mmapDir, new IndexWriterConfig(Version.LUCENE_36, null));
			}
			ehcache.save(key, value);
			org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
			doc.add(new Field(ID, id, Store.YES, Index.NOT_ANALYZED));
			doc.add(new Field(KEY, key, Store.YES, Index.NOT_ANALYZED));
			doc.add(new Field(VALUE, value, Store.YES, Index.NOT_ANALYZED));
			// System.err.println("存储：" + key + "=" + value);
			iw.updateDocument(new Term(KEY, key), doc);
			releaseIw();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return SaveType.save;
	}

	/**
	 * 保存键值对的数据
	 * 
	 * @param key
	 * @param value
	 */
	public SaveType save(String key, String value) {
		return save("0", key, value);
	}

	public String findValueByKey(String key) {
		try {
			initSearch();
			String result = ehcache.findValueByKey(key);
			if (result != null) {
				return result;
			}
			Term t = new Term(KEY, key);
			TermQuery query = new TermQuery(t);
			query.setBoost(1);
			TopDocs hits = is.search(query, 1);
			if (hits.totalHits > 0) {
				int docId = hits.scoreDocs[0].doc;
				org.apache.lucene.document.Document doc = is.doc(docId);
				String ret = doc.get(VALUE);
				// System.err.println("根据key读取：" + key + "=" + ret);
				ehcache.save(key, ret);
				return ret;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				releaseIs();
			} catch (IOException e) {
			}
		}
		return null;
	}

	public String findKeyByValue(String value) {
		try {
			initSearch();
			Term t = new Term(VALUE, value);
			TermQuery query = new TermQuery(t);
			query.setBoost(1);
			TopDocs hits = is.search(query, 1);
			if (hits.totalHits > 0) {
				int docId = hits.scoreDocs[0].doc;
				org.apache.lucene.document.Document doc = is.doc(docId);
				String ret = doc.get(KEY);
				// System.err.println("根据value读取：" + ret + "=" + value);
				return ret;
			}
		} catch (Exception e) {
		} finally {
			try {
				releaseIs();
			} catch (IOException e) {
			}
		}
		return null;
	}

	public String findKeyById(String id) {
		try {
			initSearch();
			Term t = new Term(ID, id);
			TermQuery query = new TermQuery(t);
			query.setBoost(1);
			TopDocs hits = is.search(query, 1);
			if (hits.totalHits > 0) {
				int docId = hits.scoreDocs[0].doc;
				org.apache.lucene.document.Document doc = is.doc(docId);
				String ret = doc.get(KEY);
				// System.err.println("根据id读取：" + id + "=" + ret);
				return ret;
			}
		} catch (Exception e) {
		} finally {
			try {
				releaseIs();
			} catch (IOException e) {
			}
		}
		return null;
	}

	public String findValueById(String id) {
		try {
			initSearch();
			Term t = new Term(ID, id);
			TermQuery query = new TermQuery(t);
			query.setBoost(1);
			TopDocs hits = is.search(query, 1);
			if (hits.totalHits > 0) {
				int docId = hits.scoreDocs[0].doc;
				org.apache.lucene.document.Document doc = is.doc(docId);
				String ret = doc.get(VALUE);
				// System.err.println("根据id读取：" + id + "=" + ret);
				return ret;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				releaseIs();
			} catch (IOException e) {
			}
		}
		return null;
	}

	private void releaseIs() throws IOException {
		is.close();
		is = null;
	}

	private void initSearch() throws CorruptIndexException, IOException {
		// releaseIw();
		init();
		if (is == null) {
			is = new IndexSearcher(IndexReader.open(mmapDir));
		}
	}

	public static void main(String[] args) {
		LuceneRepository lr = new LuceneRepository(null);
		lr.save("", "hehehehe", "hahahahaha");
		System.out.println(lr.findValueByKey("hehehehe"));
	}

	@Override
	public void close() {
		try {
			releaseIs();
		} catch (IOException e) {
			e.printStackTrace();
		}
		releaseIw();
	}
}
