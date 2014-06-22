package com.ss.language.model.dirichlet;

import com.ss.language.model.gibblda.Estimator;
import com.ss.language.model.gibblda.Inferencer;
import com.ss.language.model.gibblda.LDACmdOption;
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
		LDACmdOption.curOption.set(null);
		System.out.println("开始LDA计算……");
		estimate(option);
		System.out.println("完成LDA计算");
	}

	public void estimate(LDACmdOption option) {
		// LDADataset.tableName表中的数据已经在DocumentProcessor中指定了。
		// 开始lda计算
		if (option.est || option.estc) {
			Estimator estimator = new Estimator(option);
			estimator.estimate();
		} else if (option.inf) {
			Inferencer inferencer = new Inferencer(option);

			Model newModel = inferencer.inference();

			for (int i = 0; i < newModel.phi.getxLen(); ++i) {
				// phi: K * V
				System.out.println("-----------------------\ntopic" + i + " : ");
				for (int j = 0; j < 10; ++j) {
					System.out.println(inferencer.globalDict.getWord(j) + "\t" + newModel.phi.fetch(i, j));
				}
			}
		}
	}
}
