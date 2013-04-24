package com.ss.language.model.data.repo;

import com.ss.language.model.data.HibernateDataSource;
import com.ss.language.model.data.entity.WordIdf;
import com.ss.language.model.data.entity.WordTfIdf;

public class TfIdfRepository extends HibernateDataSource {
	/**
	 * 保存词。其中每个词只保存一次。
	 * 
	 * @param idf
	 */
	public void saveWord(WordIdf idf) {
		if (idf == null) {
			return;
		}
		WordIdf db = (WordIdf) getCurrentSession().createQuery("from WordIdf where word = ?")
													.setString(0, idf.getWord())
													.uniqueResult();
		if (db == null) {
			save(idf);
		} else {
			idf.setRecId(db.getRecId());
		}
	}

	/**
	 * 保存每个文档中词的tf值。
	 * 
	 * @param tfidf
	 */
	public void saveWordTf(WordTfIdf tfidf) {
		if (tfidf == null) {
			return;
		}
		WordTfIdf db = (WordTfIdf) getCurrentSession().createQuery("from WordTfIdf where documentTitle = ? and wordId = ?")
														.setString(0, tfidf.getDocumentTitle())
														.setLong(1, tfidf.getWordId())
														.uniqueResult();
		if (db == null) {
			save(tfidf);
		} else {
			tfidf.setRecId(db.getRecId());
		}
	}
}
