package com.ss.language.model.dirichlet;

import com.ss.language.model.pipe.PipeNode;

/**
 * 计算P(w|D)的值，其公式为：<br>
 * <br>
 * <code>P(t|d)=(tf(t,d)+uP(t|C))/(tf(t,d)总和+u)</code><br>
 * <ul>
 * <li>其中tf(t,d)是词t在文档d中出现的次数</li>
 * <li>tf(t,d)总和表示：文档d中词的总数</li>
 * <li>P(t|C)表示在文档集上的语言模型，计算方式为（1+词t在文档集C中出现的次数）除以（文档集中不同词的数量+文档集C中的总词数）</li>
 * </ul>
 * 参考：http://book.51cto.com/art/201008/219536.htm中间。
 * 
 * @author taofucheng
 * 
 */
public class WordInDocumentProcess extends PipeNode {

	@Override
	public void process() {
		// TODO Auto-generated method stub

	}
}
