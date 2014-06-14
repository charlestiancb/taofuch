/*
 * Copyright (C) 2007 by
 * 
 * 	Xuan-Hieu Phan
 *	hieuxuan@ecei.tohoku.ac.jp or pxhieu@gmail.com
 * 	Graduate School of Information Sciences
 * 	Tohoku University
 * 
 *  Cam-Tu Nguyen
 *  ncamtu@gmail.com
 *  College of Technology
 *  Vietnam National University, Hanoi
 *
 * JGibbsLDA is a free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * JGibbsLDA is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JGibbsLDA; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 */
package com.ss.language.model.gibblda;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class Dictionary {
	private int WORD_ID = 0;
	private Object WORD_LOCK = new Object();
	/** 存放所有词的id，用于输出wordMapFile文件的，即id与词的对应关系 */
	private File ids;
	private String dicName;

	// --------------------------------------------------
	// constructors
	// --------------------------------------------------

	public Dictionary() {
		dicName = System.nanoTime() + "" + new Random().nextInt(20) + "_";
		init(LDACmdOption.curOption.get());
	}

	public int nextWordId() {
		synchronized (WORD_LOCK) {
			return WORD_ID++;
		}
	}

	public int wordSize() {
		return WORD_ID + 1;
	}

	/**
	 * 将给定的所有文档中的词编号都存储在一个文件中！
	 * 
	 * @param wordId
	 */
	public void storeWordId(String wordId) {
		try {
			// 这里不需要处理编码问题，因为这里存储的都是数字
			FileUtils.writeLines(ids, Arrays.asList(new String[] { wordId }), true);
		} catch (IOException e) {
		}
	}

	public File getWordIdsFile() {
		return ids;
	}

	public void init(LDACmdOption option) {
		ids = new File(option.dir, "word-ids.txt");
		try {
			if (ids.isFile()) {
				FileUtils.forceDelete(ids);
			}
			ids.createNewFile();
		} catch (IOException e) {
		}
	}

	// ---------------------------------------------------
	// get/set methods
	// ---------------------------------------------------

	public String getWord(int id) {
		return LuceneDataAccess.findValueByKey(dicName + id);
	}

	public Integer getID(String word) {
		String id = LuceneDataAccess.findKeyByValue(word);
		if (id == null || id.trim().isEmpty()) {
			return null;
		} else {
			return Integer.parseInt(id.substring(id.indexOf("_") + 1));
		}
	}

	// ----------------------------------------------------
	// checking methods
	// ----------------------------------------------------
	/**
	 * check if this dictionary contains a specified word
	 */
	public boolean contains(String word) {
		return getID(word) != null;
	}

	public boolean contains(int id) {
		return getWord(id) != null;
	}

	// ---------------------------------------------------
	// manupulating methods
	// ---------------------------------------------------
	/**
	 * add a word into this dictionary return the corresponding
	 * id。使用当前的id存储！不会生成一个新的id！
	 */
	public int addWord(String word) {
		if (!contains(word)) {
			int id = WORD_ID - 1;
			LuceneDataAccess.save(dicName + id, word);
			return id;
		} else
			return getID(word);
	}

	// ---------------------------------------------------
	// I/O methods
	// ---------------------------------------------------
	/**
	 * read dictionary from file
	 */
	public boolean readWordMap(String wordMapFile) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(wordMapFile), "UTF-8"));
			String line;

			// read the number of words
			line = reader.readLine();
			line = LDADataset.removeBomIfNessecery(line);
			int nwords = Integer.parseInt(line);

			// read map
			for (int i = 0; i < nwords; ++i) {
				line = reader.readLine();
				if (line == null || line.trim().isEmpty()) {
					continue;
				}
				StringTokenizer tknr = new StringTokenizer(line, " \t\n\r");

				if (tknr.countTokens() != 2)
					continue;

				String id = tknr.nextToken();
				String word = tknr.nextToken();
				System.out.println(word);
				LuceneDataAccess.save(dicName + id, word);
			}

			reader.close();
			return true;
		} catch (Exception e) {
			System.out.println("Error while reading dictionary:" + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将所有文档中的词写入文件中！
	 * 
	 * @param wordMapFile
	 * @return
	 */
	public boolean writeWordMap(String wordMapFile) {
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(wordMapFile),
					"UTF-8"));
			// write number of words
			writer.write(wordSize() + IOUtils.LINE_SEPARATOR);
			// write word to id
			BufferedReader br = new BufferedReader(new FileReader(getWordIdsFile()));
			for (String key = br.readLine(); key != null; key = br.readLine()) {
				key = LDADataset.removeBomIfNessecery(key);
				String value = LuceneDataAccess.findValueByKey(dicName + key);
				writer.write(key + " " + value + IOUtils.LINE_SEPARATOR);
			}
			writer.close();
			br.close();
			return true;
		} catch (Exception e) {
			System.out.println("Error while writing word map " + e.getMessage());
			e.printStackTrace();
			return false;
		}

	}
}
