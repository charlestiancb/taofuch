package com.ss.language.model;

import com.ss.language.model.dirichlet.DirichletDistributionProcess;
import com.ss.language.model.pipe.PipeManager;
import com.ss.language.model.tf_idf.DocumentProcessor;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PipeManager.regist(new DocumentProcessor());// 这是tf*idf值计算
		PipeManager.regist(new DirichletDistributionProcess());// 这是狄利克雷分布计算平滑参数。

		// 开始运行！
		PipeManager.clearAndstart();
	}
}
