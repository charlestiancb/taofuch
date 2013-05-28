package com.ss.language.model.tf_idf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.wltea.analyzer.split.WordSplitor;

import com.ss.language.model.data.DatabaseConfig;

public class WeiboTfIdfProcessor extends DocumentProcessor {

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
}
