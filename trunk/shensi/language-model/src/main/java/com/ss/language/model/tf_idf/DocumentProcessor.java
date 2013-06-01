package com.ss.language.model.tf_idf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.wltea.analyzer.split.WordSplitor;

import com.ss.language.model.data.DatabaseConfig;
import com.ss.language.model.data.EntitySql;
import com.ss.language.model.data.EntitySql.SqlType;
import com.ss.language.model.data.entity.WordIdf;
import com.ss.language.model.data.entity.WordTfIdf;
import com.ss.language.model.data.repo.TfIdfRepository;
import com.ss.language.model.pipe.PipeNode;

public class DocumentProcessor extends PipeNode {
	private TfIdfRepository repo;

	public static void main(String[] args) {
		new DocumentProcessor().process();
		System.out.println(Math.log(1.5) / Math.log(10));
	}

	public void process() {
		try {
			if (repo == null) {
				repo = new TfIdfRepository();
			}
			clearDatas();
			splitWordsAndTf();
			calcIdfAndTfidf();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void clearDatas() {
		EntitySql sqlObj = new EntitySql();
		sqlObj.setSql("delete from WORD_IDF");
		sqlObj.setType(SqlType.DELETE);
		DatabaseConfig.executeSql(sqlObj);

		sqlObj.setSql("delete from WORD_TF_IDF");
		DatabaseConfig.executeSql(sqlObj);
	}

	/**
	 * 读取每篇文档，并将每篇文档中的内容进行分词，然后统计出对应的TF值。同时存储词及词的TF值。
	 */

	protected void splitWordsAndTf() throws SQLException {
		long count = count("SELECT count(1) FROM informationscience");
		long page = count / perPageRecords;
		page = count % perPageRecords > 0 ? page + 1 : page;
		System.out.println("---------------正在计算各文档中各个词的tf值-----------");
		for (int i = 0; i < page; i++) {
			String sql = "SELECT * FROM informationscience limit " + (i * perPageRecords) + "," + perPageRecords;
			Connection conn = DatabaseConfig.openConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			int tmp = 0;
			while (rs.next()) {
				tmp++;
				if (tmp > 500) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}// 等待一下让系统回收
				}
				String abstr = rs.getString("Abstract");
				if (StringUtils.isBlank(abstr)) {
					continue;
				}
				calcTfAndSave(rs.getString("Title"), WordSplitor.splitToArr(abstr));
			}
			rs.close();
			ps.close();
		}
		System.out.println("---------------完成计算各文档中各个词的tf值-----------");
	}

	/**
	 * 计算指定文章中每个词的TF值
	 * 
	 * @param documentTitle
	 *            文档名称，对于微博来说，就是weibo_id。
	 * @param cutWordResult
	 *            内容分词后的所有词
	 * @return
	 */
	protected void calcTfAndSave(String documentTitle, String[] cutWordResult) {
		System.out.println("正在处理：" + documentTitle);
		if (cutWordResult == null || cutWordResult.length == 0) {
			return;
		}
		Map<String, Integer> tfOfWord = new LinkedHashMap<String, Integer>();
		int wordNum = cutWordResult.length;
		for (int i = 0; i < wordNum; i++) {
			Integer num = tfOfWord.get(cutWordResult[i]);
			if (num == null) {
				tfOfWord.put(cutWordResult[i], 1);
			} else {
				tfOfWord.put(cutWordResult[i], num + 1);
			}
		}
		for (String word : tfOfWord.keySet()) {
			WordIdf idf = new WordIdf(word, 0D);
			repo.saveWord(idf);
			WordTfIdf tfidf = new WordTfIdf(documentTitle, idf.getRecId(), tfOfWord.get(word));
			repo.saveWordTf(tfidf);
		}
	}

	/**
	 * 计算每个词的IDF值及每篇文档中每个词的tf/idf值。
	 * 
	 * @throws SQLException
	 */
	protected void calcIdfAndTfidf() throws SQLException {
		System.out.println("---------------正在计算各文档中各个词的idf值与tf*idf值-----------");
		// idf值=lg(总文档数/含有该词的文档数)
		long totalDocument = DatabaseConfig.query("select document_title from WORD_TF_IDF group by document_title")
											.size();
		long page = totalDocument / perPageRecords;
		page = totalDocument % perPageRecords > 0 ? page + 1 : page;
		for (int i = 0; i < page; i++) {
			List<WordIdf> idfs = repo.list(WordIdf.class, i * perPageRecords, perPageRecords);
			if (idfs == null || idfs.isEmpty()) {
				break;
			}
			for (WordIdf wi : idfs) {
				System.out.println("正在计算词：" + wi.getWord());
				// 查询该词在多少文档中存在！
				long documents = count("select count(1) from WORD_TF_IDF where word_id = " + wi.getRecId());
				wi.setIdf(Math.log(totalDocument / 1.0 / documents) / Math.log(10));
				wi.setDf(documents);
				wi.setCf(count("select sum(tf) from WORD_TF_IDF where word_id =" + wi.getRecId()));
				repo.merge(wi);
				// 计算tf/idf值，其值为：每个文档中，每个词的tf*idf
				EntitySql sqlObj = new EntitySql();
				sqlObj.setSql("update WORD_TF_IDF set tf_idf = tf*" + wi.getIdf() + " where word_id = " + wi.getRecId());
				sqlObj.setType(SqlType.UPDATE);
				DatabaseConfig.executeSql(sqlObj);
			}
		}
		System.out.println("---------------完成计算各文档中各个词的idf值与tf*idf值-----------");
	}
}
