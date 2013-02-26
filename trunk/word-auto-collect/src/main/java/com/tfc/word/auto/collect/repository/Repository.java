package com.tfc.word.auto.collect.repository;

import com.tfc.word.auto.collect.repository.entity.FetchOrig;
import com.tfc.word.auto.collect.repository.entity.WordBase;

public abstract class Repository {
	public abstract void saveWord(String word);

	public abstract boolean updateWord(WordBase word);

	public abstract WordBase getWord(String word);

	public abstract void saveOrgi(String _orig);

	public abstract FetchOrig getOrgi(String orig);
}
