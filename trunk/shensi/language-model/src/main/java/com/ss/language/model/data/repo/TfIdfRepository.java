package com.ss.language.model.data.repo;

import com.ss.language.model.data.HibernateDataSource;
import com.ss.language.model.data.entity.WordIdf;

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
		}
	}

}
