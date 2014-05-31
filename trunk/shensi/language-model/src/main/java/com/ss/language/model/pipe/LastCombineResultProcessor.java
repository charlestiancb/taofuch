package com.ss.language.model.pipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ss.language.model.data.DatabaseConfig;
import com.ss.language.model.data.EntitySql;
import com.ss.language.model.data.EntitySql.SqlType;
import com.ss.language.model.pipe.PipeManager.QueryWord;

/**
 * 权限各个词的统计规则进行分值加权
 * 
 * @author taofucheng
 * 
 */
public class LastCombineResultProcessor extends PipeNode {

	public void process(List<QueryWord> qws) {
		if (qws != null && qws.size() > 0) {
			// 将所有的结果汇总到一个表中
			String sql = "DROP TABLE IF EXISTS `temp_combine_result`";
			EntitySql sqlObj = new EntitySql();
			sqlObj.setSql(sql);
			sqlObj.setType(SqlType.INSERT);
			DatabaseConfig.executeSql(sqlObj);
			//
			sql = "CREATE TABLE `temp_combine_result` (" //
					+ "  `rec_id` bigint(20) NOT NULL AUTO_INCREMENT,"//
					+ "  `document_title` varchar(500) COLLATE utf8_bin NOT NULL,"//
					+ "  `word_id` bigint(20) NOT NULL,"//
					+ "  `document_word` varchar(520) NOT NULL COMMENT '只是为了方便建索引，提高处理速度',"//
					+ "  `ptd` double DEFAULT NULL COMMENT '综合PTD的值',"//
					+ "  PRIMARY KEY (`rec_id`),"//
					+ "  INDEX `document_word_idx` (`document_word` ASC)"//
					+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='文档中每个词对应的tf和tf/idf值'";
			sqlObj.setSql(sql);
			DatabaseConfig.executeSql(sqlObj);
			// 以汇总表为核心，计算所有词的最终结果，并保存到csv文件中
			List<QueryWord> words = new ArrayList<QueryWord>();
			for (QueryWord qw : qws) {
				if (qw != null && StringUtils.isNotBlank(qw.getWord())) {
					String tableName = "word_tf_idf" + "_" + qw.getTableSuffix();
					sqlObj.setSql("insert into temp_combine_result(document_title,word_id,document_word)  select document_title,word_id,concat(document_title,'_',word_id) from "
							+ tableName
							+ " where concat(document_title,'_',word_id) not in (select document_word from temp_combine_result)");
					DatabaseConfig.executeSql(sqlObj);
					words.add(qw);
				}
			}
			if (words.size() > 0) {
				// 读取一条条的记录，并进行重新计算其ptd值
				try {
					calc(words);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void calc(List<QueryWord> words) throws SQLException {
		long count = count("SELECT count(1) FROM temp_combine_result");
		long page = count / perPageRecords;
		page = count % perPageRecords > 0 ? page + 1 : page;
		System.out.println("---------------正在综合计算-----------");
		for (int i = 0; i < page; i++) {
			String sql = "SELECT document_word FROM temp_combine_result limit " + (i * perPageRecords) + ","
					+ perPageRecords;
			Connection conn = DatabaseConfig.openConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			int tmp = 0;
			while (rs.next()) {
				tmp++;
				if (tmp > 500) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}// 等待一下让系统回收
				}
				String dw = rs.getString("document_word");
				if (StringUtils.isBlank(dw)) {
					continue;
				}
				int idx = dw.lastIndexOf("_");
				String dt = dw.substring(0, idx);
				int wi = Integer.parseInt(dw.substring(idx + 1).trim());
				double total = 0;
				for (QueryWord word : words) {
					total += calcWordPtd(word, dt, wi);
				}
				DatabaseConfig.executeSql(new EntitySql()
						.setSql("update temp_combine_result set ptd=? where document_word=?").addArg(total).addArg(dw)
						.setType(SqlType.UPDATE));
				System.out.println("当前处理进度：" + ((i + 1) * perPageRecords + tmp) + "/" + count);
			}
			rs.close();
			ps.close();
		}
		System.out.println("---------------完成综合计算-----------");
	}

	/**
	 * 
	 * @param word
	 *            查询的词
	 * @param dt
	 *            文档
	 * @param wi
	 *            具体的词
	 * @return
	 */
	private double calcWordPtd(QueryWord word, String dt, int wi) {
		List<Map<String, Object>> lines = DatabaseConfig.query("select ptd from word_tf_idf_" + word.getTableSuffix()
				+ " where document_title=? and word_id=?", dt, wi);
		if (lines != null && lines.size() > 0) {
			Map<String, Object> line = lines.get(0);
			Double d = (Double) line.get("ptd");
			if (d == null) {
				d = (Double) line.get("PTD");
			}
			if (d != null) {
				return d * word.getWeight();
			}
		}
		return 0;
	}

	@Override
	public void process() {
		// 没有操作……
	}
}
