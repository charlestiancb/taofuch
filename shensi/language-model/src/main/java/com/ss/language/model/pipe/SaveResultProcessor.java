package com.ss.language.model.pipe;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.ss.language.model.data.DatabaseConfig;
import com.ss.language.model.data.EntitySql;
import com.ss.language.model.data.EntitySql.SqlType;
import com.ss.language.model.gibblda.LDACmdOption;

/**
 * 保存每个词的计算结果，便于最后的联合统计
 * 
 * @author taofucheng
 * 
 */
public class SaveResultProcessor extends PipeNode {

	@Override
	public void process() {
		// 保存word_idf
		String suffix = PipeManager.getCurrentQueryWord().getTableSuffix();
		String backupSql = "DROP TABLE IF EXISTS word_idf" + "_" + suffix;
		DatabaseConfig.executeSql(new EntitySql().setSql(backupSql).setType(SqlType.UPDATE));
		backupSql = "ALTER TABLE word_tf_idf RENAME TO word_idf" + "_" + suffix;
		DatabaseConfig.executeSql(new EntitySql().setSql(backupSql).setType(SqlType.UPDATE));
		// 保存word_tf_idf
		backupSql = "DROP TABLE IF EXISTS word_tf_idf" + "_" + suffix;
		DatabaseConfig.executeSql(new EntitySql().setSql(backupSql).setType(SqlType.UPDATE));
		backupSql = "ALTER TABLE word_tf_idf RENAME TO word_tf_idf" + "_" + suffix;
		DatabaseConfig.executeSql(new EntitySql().setSql(backupSql).setType(SqlType.UPDATE));
		// 保存文件
		LDACmdOption option = LDACmdOption.curOption.get();
		if (option != null && StringUtils.isNotBlank(option.dir)) {
			File resultDir = new File(option.dir.trim());
			if (resultDir.isDirectory()) {
				try {
					resultDir.renameTo(new File(resultDir.getCanonicalPath() + "_" + suffix));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
