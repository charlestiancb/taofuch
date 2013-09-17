package com.ss.language.model.hlda;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.ss.language.model.data.DatabaseConfig;
import com.ss.language.model.data.EntitySql;
import com.ss.language.model.data.EntitySql.SqlType;
import com.ss.language.model.data.HibernateDataSource;
import com.ss.language.model.data.entity.HldaResult;
import com.ss.language.model.gibblda.LDADataset;
import com.ss.language.model.pipe.PipeNode;

public class HLdaProcessor extends PipeNode {
	private HibernateDataSource dataSource;

	public static void main(String[] args) {
		new HLdaProcessor().process();
		//
	}

	@Override
	public void process() {
		try {
			createTable();
			String file = "E:/tmp/process/hlda.txt";
			renderToTxt(file, "GBK");
			// TODO 这里调用HLDA算法，算出结果及文件
			// 将结果保存到数据库中，每条数据保存示例如下：
			renderToDb("1770717263_A8a27rLiN", "Topic0,tipic6",
					"0.42622,0.573", null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 消除并创建新的hlda_result表。
	 */
	private void createTable() {
		String sql = "DROP TABLE IF EXISTS `HLDA_RESULT`";
		EntitySql sqlObj = new EntitySql();
		sqlObj.setSql(sql);
		sqlObj.setType(SqlType.INSERT);
		DatabaseConfig.executeSql(sqlObj);
		sql = "CREATE  TABLE `weibo`.`HLDA_RESULT` ("
				+ "  `rec_id` BIGINT NOT NULL AUTO_INCREMENT ,"
				+ "  `document_id` VARCHAR(200) NOT NULL COMMENT '文献唯一标志' ,"
				+ "  `lda_index` TINYTEXT NULL COMMENT '文献所属主题等' ,"
				+ "  `lda_probability` VARCHAR(100) NULL COMMENT '文献的概率值' ,"
				+ "  `lda_label` TINYTEXT NULL COMMENT '标签说明' ,"
				+ "  PRIMARY KEY (`rec_id`) ,"
				+ "  INDEX `idx_hr_document_id` (`document_id` ASC) )"
				+ "DEFAULT CHARACTER SET = utf8" + "COMMENT = 'HLDA计算结果'";
		sqlObj.setSql(sql);
		DatabaseConfig.executeSql(sqlObj);
	}

	/**
	 * 将需要处理的内容输出为指定的格式的文本文件
	 * 
	 * @param destTxtFile
	 *            生成的文本文件的路径及名称
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	private void renderToTxt(String destTxtFile, String encoding)
			throws IOException, SQLException {
		int total = (int) DatabaseConfig.count("select count(1) from "
				+ LDADataset.tableName);
		if (total < 1) {
			return;
		}
		File dtf = new File(destTxtFile);
		if (dtf.isFile()) {
			FileUtils.forceDelete(dtf);
		}
		if (!dtf.getParentFile().isDirectory()) {
			FileUtils.forceMkdir(dtf.getParentFile());
		}
		dtf.createNewFile();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(dtf), encoding));
		try {
			int perpage = 50;
			for (int i = 0; i < total; i = i + perpage) {
				// 一个文章一个文章地获取
				String titleSql = "select document_title from "
						+ LDADataset.tableName + " order by rec_id asc limit "
						+ i + "," + perpage;
				List<Map<String, Object>> result = DatabaseConfig
						.query(titleSql);
				if (result == null || result.isEmpty()) {
					break;
				}
				for (Map<String, Object> record : result) {
					String title = (String) record.get("document_title");
					String contentSql = "select b.word from word_tf_idf a,word_idf b where a.word_id=b.rec_id and a.document_title=? order by a.rec_id";
					List<Map<String, Object>> contentResult = DatabaseConfig
							.query(contentSql, title);
					if (contentResult == null || contentResult.isEmpty()) {
						break;
					}
					StringBuilder sb = new StringBuilder();
					for (Map<String, Object> word : contentResult) {
						sb.append(word.get("word"));
					}
					bw.write(title + "\t" + sb.toString()
							+ IOUtils.LINE_SEPARATOR);
				}
			}
			bw.flush();
		} catch (IOException e) {
		} finally {
			bw.close();
		}
	}

	/**
	 * 将每条结果的内容解析并写到数据库中。
	 * 
	 * @param documentId
	 * @param ldaIndex
	 * @param ldaProbability
	 * @param ldaLabel
	 */
	private void renderToDb(String documentId, String ldaIndex,
			String ldaProbability, String ldaLabel) {
		HldaResult hr = new HldaResult();
		hr.setDocumentId(documentId);
		hr.setLdaIndex(ldaIndex);
		hr.setLdaProbability(ldaProbability);
		hr.setLdaLabel(ldaLabel);
		dataSource.save(hr);
	}

}
