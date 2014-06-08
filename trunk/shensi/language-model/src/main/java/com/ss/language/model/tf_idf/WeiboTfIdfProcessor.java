package com.ss.language.model.tf_idf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

import com.ss.language.model.data.DatabaseConfig;
import com.ss.language.model.pipe.PipeManager;

public class WeiboTfIdfProcessor extends DocumentProcessor {

	/**
	 * 读取微博信息，并对微博的内容进行分词，然后统计出对应的TF值。同时存储词及词的TF值。
	 */

	protected void splitWordsAndTf() throws SQLException {
		String word = PipeManager.getCurrentQueryWord().getWord();
		String sql = "SELECT" + //
				"cloudcomputing_cleared.weibo_id," + //
				"cloudcomputing_cleared.content," + //
				"cloudcomputing_cleared.content_tag," + //
				"cloudcomputing_cleared.content_pure," + //
				"cloudcomputing_cleared.forword_num," + //
				"cloudcomputing_cleared.comment_num," + //
				"cloudcomputing_cleared.temporaltoken," + //
				"cloudcomputing_cleared.publish_time" + //
				"FROM" + //
				"cloudcomputing_cleared" + //
				"WHERE" + //
				"cloudcomputing_cleared.content_pure LIKE  '%" + word + "%'" + //
				"union all" + //
				"SELECT" + //
				"dataming_cleared.weibo_id," + //
				"dataming_cleared.content," + //
				"dataming_cleared.content_tag," + //
				"dataming_cleared.content_pure," + //
				"dataming_cleared.forword_num," + //
				"dataming_cleared.comment_num," + //
				"dataming_cleared.temporaltoken," + //
				"dataming_cleared.publish_time" + //
				"FROM" + //
				"dataming_cleared_cleared" + //
				"WHERE" + //
				"dataming_cleared_cleared.content_pure LIKE  '%" + word + "%'";
		long count = count("SELECT count(1) FROM (" + sql + ") t");
		long page = count / perPageRecords;
		page = count % perPageRecords > 0 ? page + 1 : page;
		System.out.println("---------------正在计算各文档中各个词的tf值-----------");
		prepareSaveTitleAndContent();
		for (int i = 0; i < page; i++) {
			String sqlTmp = "SELECT * FROM (" + sql
					+ ") t order by publish_time asc,forword_num asc,temporaltoken asc limit " + (i * perPageRecords)
					+ "," + perPageRecords;
			Connection conn = DatabaseConfig.openConn();
			PreparedStatement ps = conn.prepareStatement(sqlTmp);
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
				String abstr = StringUtils.trimToEmpty(rs.getString("content_tag"));
				abstr += " " + StringUtils.trimToEmpty(rs.getString("content_pure"));
				abstr += " " + StringUtils.trimToEmpty(rs.getString("temporaltoken"));
				if (StringUtils.isBlank(abstr)) {
					continue;
				}
				saveTitleAndContent(rs.getString("weibo_id"), abstr);
				calcTfAndSave(rs.getString("weibo_id"), abstr.split(" "));
				System.out.println("当前处理进度：" + ((i + 1) * perPageRecords + tmp) + "/" + count);
			}
			rs.close();
			ps.close();
		}
		System.out.println("---------------完成计算各文档中各个词的tf值-----------");
	}
}
