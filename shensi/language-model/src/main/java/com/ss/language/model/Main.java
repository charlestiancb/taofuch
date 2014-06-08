package com.ss.language.model;

import com.ss.language.model.dirichlet.DirichletDistributionProcess;
import com.ss.language.model.dirichlet.WordInDocumentProcess;
import com.ss.language.model.dirichlet.WordOfLdaProcess;
import com.ss.language.model.pipe.PipeManager;
import com.ss.language.model.pipe.PipeManager.QueryWord;
import com.ss.language.model.pipe.SaveResultProcessor;
import com.ss.language.model.tf_idf.WeiboTfIdfProcessor;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 请注意：以下各个节点的注入顺序就是它们的执行顺序，即计算顺序。
		PipeManager.regist(new WeiboTfIdfProcessor());// 这是tf*idf值计算
		PipeManager.regist(new WordInDocumentProcess());// 计算P(w|D)值
		PipeManager.regist(new WordOfLdaProcess());// 计算lda模型下的P(w|D)值
		PipeManager.regist(new DirichletDistributionProcess());// 这是狄利克雷分布计算平滑参数，即将上面三个P值计算结果进行平滑处理。
		PipeManager.regist(new SaveResultProcessor());// 保存每个词的计算结果，便于最后的联合统计

		// 开始运行！
		PipeManager.clearAndstart("weibo", new QueryWord("本体", 0.6), new QueryWord("语义", 0.4));
	}
}
