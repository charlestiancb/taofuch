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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.ss.language.model.data.DatabaseConfig;

public class LDADataset {
	public static final String tableName = "temp_title_for_lda";
	// ---------------------------------------------------------------
	// Instance Variables
	// ---------------------------------------------------------------

	public Dictionary localDict; // local dictionary
	public Document[] docs; // a list of documents
	public int M; // number of documents
	public int V; // number of words

	// map from local coordinates (id) to global ones
	// null if the global dictionary is not set
	public Map<Integer, Integer> lid2gid;

	// link to a global dictionary (optional), null for train data, not null for
	// test data
	public Dictionary globalDict;

	// --------------------------------------------------------------
	// Constructor
	// --------------------------------------------------------------
	public LDADataset() {
		localDict = new Dictionary();
		M = 0;
		V = 0;
		docs = null;

		globalDict = null;
		lid2gid = null;
	}

	public LDADataset(int M) {
		localDict = new Dictionary();
		this.M = M;
		this.V = 0;
		docs = new Document[M];

		globalDict = null;
		lid2gid = null;
	}

	public LDADataset(int M, Dictionary globalDict) {
		localDict = new Dictionary();
		this.M = M;
		this.V = 0;
		docs = new Document[M];

		this.globalDict = globalDict;
		lid2gid = new HashMap<Integer, Integer>();
	}

	// -------------------------------------------------------------
	// Public Instance Methods
	// -------------------------------------------------------------
	/**
	 * set the document at the index idx if idx is greater than 0 and less than
	 * M
	 * 
	 * @param doc
	 *            document to be set
	 * @param idx
	 *            index in the document array
	 */
	public void setDoc(Document doc, int idx) {
		if (0 <= idx && idx < M) {
			docs[idx] = doc;
		}
	}

	public static String removeBomIfNessecery(String line)
			throws UnsupportedEncodingException {
		if (line == null) {
			return line;
		}
		byte[] bs = line.getBytes("UTF-8");
		if (bs.length > 3 && bs[0] == -17 && bs[1] == -69 && bs[2] == -65) {
			byte[] result = new byte[bs.length - 3];
			System.arraycopy(bs, 3, result, 0, bs.length - 3);
			return new String(result, "UTF-8");
		} else {
			return line;
		}
	}

	/**
	 * set the document at the index idx if idx is greater than 0 and less than
	 * M
	 * 
	 * @param str
	 *            string contains doc
	 * @param idx
	 *            index in the document array
	 */
	public void setDoc(String str, int idx) {
		if (0 <= idx && idx < M) {
			String[] words = str.split("[ \\t\\n]");

			Vector<Integer> ids = new Vector<Integer>();

			for (String word : words) {
				boolean exist = true;
				Integer _id = localDict.getID(word);
				if (_id == null) {
					exist = false;
					_id = localDict.nextWordId();
				}

				if (globalDict != null) {
					// get the global id
					Integer id = globalDict.getID(word);
					// System.out.println(id);

					if (id != null) {
						localDict.addWord(word);

						lid2gid.put(_id, id);
						ids.add(_id);
					} else { // not in global dictionary
								// do nothing currently
					}
				} else {
					localDict.addWord(word);
					ids.add(_id);
				}
				if (!exist) {
					// 如果id是新生成的，则添加到文件中！
					localDict.storeWordId(String.valueOf(_id));
				}
			}

			Document doc = new Document(ids, str);
			docs[idx] = doc;
			V = localDict.wordSize();
		}
	}

	// ---------------------------------------------------------------
	// I/O methods
	// ---------------------------------------------------------------

	/**
	 * read a dataset from a stream, create new dictionary
	 * 
	 * @return dataset if success and null otherwise
	 */
	public static LDADataset readDataSet() {
		try {
			// read number of document
			int M = (int) DatabaseConfig.count("select count(1) from "
					+ tableName);

			LDADataset data = new LDADataset(M);
			readFromDatabase(M, data);
			return data;
		} catch (Exception e) {
			System.out.println("Read Dataset Error: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private static void readFromDatabase(int total, LDADataset data)
			throws IOException {
		// 生成一个文章id对应的文件。
		File docIdxFile = new File(LDACmdOption.curOption.get().dir,
				LDACmdOption.curOption.get().docIdFile);
		if (docIdxFile.isFile()) {
			FileUtils.forceDelete(docIdxFile);
		}
		if (!docIdxFile.getParentFile().isDirectory()) {
			FileUtils.forceMkdir(docIdxFile.getParentFile());
		}
		docIdxFile.createNewFile();
		BufferedWriter br = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(docIdxFile),
				LDACmdOption.curOption.get().fileEncoding));
		for (int i = 0; i < total; ++i) {
			// 一个文章一个文章地获取
			String titleSql = "select document_title from " + tableName
					+ " order by rec_id asc limit " + i + "," + 1;
			List<Map<String, Object>> result = DatabaseConfig.query(titleSql);
			if (result == null || result.isEmpty()) {
				break;
			}
			for (Map<String, Object> record : result) {
				String title = (String) record.get("document_title");
				String contentSql = "select b.word from word_tf_idf a,word_idf b where a.word_id=b.rec_id and a.document_title=? order by a.rec_id";
				List<Map<String, Object>> contentResult = DatabaseConfig.query(
						contentSql, title);
				if (contentResult == null || contentResult.isEmpty()) {
					break;
				}
				StringBuilder sb = new StringBuilder();
				for (Map<String, Object> word : contentResult) {
					if (sb.length() > 0) {
						sb.append(" ");
					}
					sb.append(word.get("word"));
				}
				data.setDoc(sb.toString(), i);
				br.write(title + IOUtils.LINE_SEPARATOR);
			}
		}
		br.flush();
		br.close();
	}

	/**
	 * read a dataset from a stream with respect to a specified dictionary
	 * 
	 * @param reader
	 *            stream from which we read dataset
	 * @param dict
	 *            the dictionary
	 * @return dataset if success and null otherwise
	 */
	public static LDADataset readDataSet(Dictionary dict) {
		try {
			// read number of document
			int M = (int) DatabaseConfig.count("select count(1) from "
					+ tableName);
			System.out.println("NewM:" + M);

			LDADataset data = new LDADataset(M, dict);
			readFromDatabase(M, data);
			return data;
		} catch (Exception e) {
			System.out.println("Read Dataset Error: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * read a dataset from a string, create new dictionary
	 * 
	 * @param str
	 *            String from which we get the dataset, documents are seperated
	 *            by newline character
	 * @return dataset if success and null otherwise
	 */
	public static LDADataset readDataSet(String[] strs) {
		LDADataset data = new LDADataset(strs.length);

		for (int i = 0; i < strs.length; ++i) {
			data.setDoc(strs[i], i);
		}
		return data;
	}

	/**
	 * read a dataset from a string with respect to a specified dictionary
	 * 
	 * @param str
	 *            String from which we get the dataset, documents are seperated
	 *            by newline character
	 * @param dict
	 *            the dictionary
	 * @return dataset if success and null otherwise
	 */
	public static LDADataset readDataSet(String[] strs, Dictionary dict) {
		// System.out.println("readDataset...");
		LDADataset data = new LDADataset(strs.length, dict);

		for (int i = 0; i < strs.length; ++i) {
			// System.out.println("set doc " + i);
			data.setDoc(strs[i], i);
		}
		return data;
	}
}
