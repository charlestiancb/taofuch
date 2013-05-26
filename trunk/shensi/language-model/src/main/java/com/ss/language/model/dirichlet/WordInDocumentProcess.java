package com.ss.language.model.dirichlet;

import java.sql.SQLException;
import java.util.List;

import com.ss.language.model.data.entity.WordIdf;
import com.ss.language.model.data.entity.WordTfIdf;
import com.ss.language.model.data.repo.TfIdfRepository;
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
	private TfIdfRepository repo;
	/** 文档中所有词的数量 */
	private long totalWords = 0;
	/** 文档中所有不同的词的数量 */
	private long distinctTotalWords = 0;
	private double u = 1000;

	@Override
	public void process() {
		try {
			if (repo == null) {
				repo = new TfIdfRepository();
			}
			calc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 对word_tf_idf表中每个文档中每个词进行计算。
	 * 
	 * @throws SQLException
	 */
	private void calc() throws SQLException {
		long count = count("SELECT count(1) FROM word_tf_idf");
		long page = count / perPageRecords;
		page = count % perPageRecords > 0 ? page + 1 : page;
		System.out.println("---------------正在计算各文档中各个词的P(w|D)值-----------");
		for (int i = 0; i < page; i++) {
			List<WordTfIdf> words = repo.list(WordTfIdf.class, i * perPageRecords, perPageRecords);
			if (words != null && words.size() > 0) {
				for (WordTfIdf wti : words) {
					// P(t|d)=(tf(t,d)+uP(t|C))/(tf(t,d)总和+u)
					// 每个词进行计算并保存
					double tf = wti.getTf();
					// 词t所在的文档中所有词数
					double etf = count(	"select sum(tf) from word_tf_idf where document_title = ?",
										wti.getDocumentTitle());
					double ptc = calcPtc(wti);
					double result = (tf + u * ptc) / (etf + u);
					wti.setPtd(result);
					repo.merge(wti);
				}
			}
		}
		System.out.println("---------------完成计算各文档中各个词的P(w|D)值-----------");
	}

	/**
	 * 计算P(t|C)模型值，计算方式为（1+词t在文档集C中出现的次数）除以（文档集中不同词的数量+文档集C中的总词数）
	 * 
	 * @return
	 * @throws SQLException
	 */
	private double calcPtc(WordTfIdf wti) throws SQLException {
		WordIdf wi = (WordIdf) repo.getSessionFactory().openSession().get(WordIdf.class, wti.getWordId());
		if (totalWords < 1) {
			totalWords = count("select sum(tf) from word_tf_idf");
		}
		if (distinctTotalWords < 1) {
			distinctTotalWords = count("select count(1) from word_idf");
		}
		return ((1 + wi.getCf()) * 1.0) / (totalWords + distinctTotalWords);
	}
}
