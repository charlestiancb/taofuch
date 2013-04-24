package com.ss.language.model.tf_idf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.wltea.analyzer.split.WordSplitor;

import com.ss.language.model.data.DatabaseConfig;
import com.ss.language.model.data.EntityManager;
import com.ss.language.model.data.entity.WordIdf;
import com.ss.language.model.data.entity.WordTfIdf;

public class DocumentProcessor {
	/** 每次处理数据数量 */
	private static final int perPageRecords = 100;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

	public void process() {
		try {
			splitWordsAndTf();
			calcIdfAndTfidf();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取每篇文档，并将每篇文档中的内容进行分词，然后统计出对应的TF值。同时存储词及词的TF值。
	 */

	protected void splitWordsAndTf() throws SQLException {
		long count = count();
		long page = count / perPageRecords;
		page = count % perPageRecords > 0 ? page + 1 : page;
		System.out.println("---------------正在计算normalTFOfAll值-----------");
		for (int i = 0; i < count; i++) {
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
				System.out.println("当前处理的记录：" + rs.getString("id") + "，当前内存状态：总内存/可用内存-"
						+ Runtime.getRuntime().totalMemory() + "/" + Runtime.getRuntime().freeMemory());
				if (StringUtils.isBlank(abstr)) {
					continue;
				}
				calcTfAndSave(WordSplitor.splitToArr(abstr));
			}
			rs.close();
			ps.close();
		}
	}

	/**
	 * 计算指定文章中每个词的TF值
	 * 
	 * @param cutWordResult
	 * @return
	 */
	protected void calcTfAndSave(String[] cutWordResult) {
		if (cutWordResult == null || cutWordResult.length == 0) {
			return;
		}
		Map<String, Integer> tfOfWord = new HashMap<String, Integer>();
		// TODO 这个地方的性能需要提升！！！！一次循环即可！
		int wordNum = cutWordResult.length;
		int wordtf = 1;
		for (int i = 0; i < wordNum; i++) {
			wordtf = 1;
			if (cutWordResult[i].trim().length() > 0) {
				for (int j = i + 1; j < wordNum; j++) {
					if (cutWordResult[j].trim().length() == 0) {
						continue;// 如果为空，则表示已经被统计过清空的。
					}
					if (cutWordResult[i].equals(cutWordResult[j])) {
						cutWordResult[j] = "";
						wordtf++;
					}
				}
				tfOfWord.put(cutWordResult[i], wordtf);
				cutWordResult[i] = "";
			}
		}
		// TODO 存储单词与文档的TF！
		for (String word : tfOfWord.keySet()) {
			WordIdf idf = new WordIdf(word, 0D);
			DatabaseConfig.executeSql(EntityManager.createInsertSQL(idf));// 保存单词
			WordTfIdf tfidf = new WordTfIdf();
		}
	}

	private long count() throws SQLException {
		String sql = "SELECT count(1) FROM informationscience";
		Connection conn = DatabaseConfig.openConn();
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		long count = 0;
		while (rs.next()) {
			count = rs.getLong(1);
		}
		rs.close();
		ps.close();
		return count;
	}

	/**
	 * 计算每个词的IDF值及每篇文档中每个词的tf/idf值。
	 */
	protected void calcIdfAndTfidf() {
		// TODO Auto-generated method stub
	}
}
