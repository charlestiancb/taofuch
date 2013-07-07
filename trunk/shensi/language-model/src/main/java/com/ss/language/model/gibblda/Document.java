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

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class Document {
	private static int DOC_ID = 0;
	private static Object DOC_LOCK = new Object();

	public static final String DOC_PRE = "doc_";

	// ----------------------------------------------------
	// Instance Variables
	// ----------------------------------------------------
	// all words
	private int docId;
	// document length
	private int length;

	// ----------------------------------------------------
	// Constructors
	// ----------------------------------------------------

	public Document(Vector<Integer> doc) {
		this.length = doc.size();
		storeDoc(doc);
	}

	public Document(Vector<Integer> doc, String rawStr) {
		this.length = doc.size();
		storeDoc(doc);
	}

	/**
	 * 将指定的词存储到文件中
	 * 
	 * @param words
	 */
	private void storeDoc(Vector<Integer> words) {
		if (words.size() > 0) {
			docId = nextDocId();
			StringBuilder wordIds = new StringBuilder();
			Map<Integer, Integer> maps = new LinkedHashMap<Integer, Integer>();
			for (Integer wordId : words) {
				if (wordId != null) {
					wordIds.append(wordId);
					wordIds.append(",");
					Integer times = maps.get(wordId);
					if (times == null) {
						times = 1;
					} else {
						times += 1;
					}
					maps.put(wordId, times);
				}
			}
			LuceneDataAccess.save(DOC_PRE + docId, wordIds.substring(0, wordIds.length() - 1));
			// 保存到词出现次数文件中！
			LDACmdOption option = LDACmdOption.curOption.get();
			File file = null;
			if (option.eachwords != null && option.eachwords.trim().length() > 0) {
				file = new File(option.dir + File.separator + option.eachwords);
				StringBuffer sb = new StringBuffer();
				for (Integer wordId : maps.keySet()) {
					sb.append(wordId);
					sb.append(":");
					sb.append(maps.get(wordId));
					sb.append(", ");
				}
				String line = sb.length() > 0 ? sb.substring(0, sb.length() - 2) : sb.toString();
				line += IOUtils.LINE_SEPARATOR;
				try {
					FileUtils.write(file, line, "UTF-8", true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	//
	// methods
	//
	protected int nextDocId() {
		synchronized (DOC_LOCK) {
			DOC_ID++;
			return DOC_ID;
		}
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * 获取第几个词
	 * 
	 * @param n
	 *            从0开始
	 * @return
	 */
	public int getWord(int n) {
		String[] words = getAllWords();
		if (words != null && words.length > n) {
			return Integer.parseInt(words[n]);
		}
		return 0;
	}

	/**
	 * 获取该文档中所有的词（其实是词的id）
	 * 
	 * @return
	 */
	public String[] getAllWords() {
		String docContent = LuceneDataAccess.findValueByKey(DOC_PRE + docId);
		if (docContent != null) {
			return docContent.split(",");
		} else {
			return null;
		}
	}

	public int getDocId() {
		return docId;
	}
}
