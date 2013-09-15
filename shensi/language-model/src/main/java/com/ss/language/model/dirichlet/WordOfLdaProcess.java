package com.ss.language.model.dirichlet;

import com.ss.language.model.data.DatabaseConfig;
import com.ss.language.model.data.EntitySql;
import com.ss.language.model.data.EntitySql.SqlType;
import com.ss.language.model.gibblda.Estimator;
import com.ss.language.model.gibblda.Inferencer;
import com.ss.language.model.gibblda.LDACmdOption;
import com.ss.language.model.gibblda.LDADataset;
import com.ss.language.model.gibblda.Model;
import com.ss.language.model.pipe.PipeNode;

/**
 * 计算lda模型下P(w|D)的值
 * 
 * @author taofucheng
 * 
 */
public class WordOfLdaProcess extends PipeNode {

	@Override
	public void process() {
		LDACmdOption option = new LDACmdOption();
		option.dir = "E:\\tmp\\topic model\\result";
		option.est = true;
		option.inf = false;
		estimate(option);
	}

	public void estimate(LDACmdOption option) {
		// 先将存在的表删除
		String delSql = "drop table " + LDADataset.tableName;
		DatabaseConfig.executeSql(new EntitySql().setSql(delSql).setType(
				SqlType.DELETE));
		// 先将所有标题汇总，作为文章来看。
		String createSql = "create table "
				+ LDADataset.tableName
				+ "(`rec_id` bigint(20) NOT NULL AUTO_INCREMENT,`document_title` varchar(500) COLLATE utf8_bin NOT NULL,PRIMARY KEY (`rec_id`))";
		DatabaseConfig.executeSql(new EntitySql().setSql(createSql).setType(
				SqlType.UPDATE));
		String sql = "insert into "
				+ LDADataset.tableName
				+ "(document_title) select document_title from word_tf_idf group by document_title order by rec_id";
		DatabaseConfig.executeSql(new EntitySql().setSql(sql).setType(
				SqlType.UPDATE));
		// 开始lda计算
		if (option.est || option.estc) {
			Estimator estimator = new Estimator(option);
			estimator.estimate();
		} else if (option.inf) {
			Inferencer inferencer = new Inferencer(option);

			Model newModel = inferencer.inference();

			for (int i = 0; i < newModel.phi.getxLen(); ++i) {
				// phi: K * V
				System.out
						.println("-----------------------\ntopic" + i + " : ");
				for (int j = 0; j < 10; ++j) {
					System.out.println(inferencer.globalDict.getWord(j) + "\t"
							+ newModel.phi.fetch(i, j));
				}
			}
		}
	}
}
