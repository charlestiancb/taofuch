package com.tfc.word.auto.collect.repository;

import java.util.List;

import com.tfc.word.auto.collect.repository.entity.FetchOrig;
import com.tfc.word.auto.collect.repository.entity.WordBase;

public abstract class Repository {
	public abstract void saveWord(String word);

	/**
	 * 更新词的次数，加一次。同时，如果加一次之后次数达到审核次数，则自动审核通过。
	 * 
	 * @param word
	 *            指定的词
	 * @return 审核通过-true；否则-false
	 */
	public abstract boolean updateWord(WordBase word);

	public abstract WordBase getWord(String word);

	public abstract void saveOrgi(String _orig);

	public abstract FetchOrig getOrgi(String orig);

	public abstract List<WordBase> getAllCheckedWords();
}
