package com.token.matrix.gene;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.ss.language.model.gibblda.Dictionary;
import com.ss.language.model.gibblda.LDACmdOption;

/**
 * 统计文档中词的频率矩阵。生成三个文档：ids.txt、words.txt、matrix.txt
 * 
 * @author taofucheng
 * 
 */
public class WordMatrixMain {
	public static final String FILE_IDS = "_ids.txt";
	public static final String FILE_WORDS = "_words.txt";
	public static final String FILE_MATRIX = "_matrix.txt";
	public static final String FILE_MATRIX_COMPLEX = "_matrix_complex.txt";

	public static final String enc = "GBK";

	public static void main(String[] args) {
		stat(new File("e://tmp//lmtest.txt"));
	}

	/**
	 * 生成三个文档：ids.txt、words.txt、matrix.txt
	 * 
	 * @param file
	 */
	private static void stat(File file) {
		if (!(file.isFile() && file.canRead())) {
			return;
		}
		try {
			LDACmdOption option = new LDACmdOption();
			option.dir = file.getParent();
			LDACmdOption.curOption.set(option);
			Dictionary dic = new Dictionary();
			dic.nextWordId();// id从1开始编号

			File idFile = new File(file.getAbsolutePath() + FILE_IDS);
			File wordFile = new File(file.getAbsolutePath() + FILE_WORDS);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), enc));
			BufferedWriter idBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(idFile), enc));
			BufferedWriter wordBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(wordFile), enc));
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				// 读取一行文本
				line = line == null ? "" : line.trim();
				if (line.isEmpty()) {
					continue;
				}
				// 将每行文档的词分割出来
				String[] words = line.split(" ");
				for (String word : words) {
					word = word.trim();
					if (word.isEmpty()) {
						continue;
					}
					// 保存词及对应的id
					Integer wordId = dic.getID(word);
					if (wordId == null) {
						// 如果该词没有被保存，则保存到文件中
						wordId = dic.nextWordId();
						dic.storeWordId(String.valueOf(dic.addWord(word)));
						idBw.write(wordId + IOUtils.LINE_SEPARATOR);
						wordBw.write(word + IOUtils.LINE_SEPARATOR);
					}
				}
			}
			// 关闭文件
			idBw.flush();
			wordBw.flush();
			idBw.close();
			wordBw.close();
			System.out.println("id与word文件生成完成！");
			// 生成矩阵
			File matrixFile = new File(file.getAbsolutePath() + FILE_MATRIX);
			BufferedWriter matrixBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(matrixFile), enc));
			br.close();
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), enc));
			int lineNum = 0;
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				System.out.println("在解析文件的" + (++lineNum) + "行！");
				// 读取一行文本
				line = line == null ? "" : line.trim();
				if (line.isEmpty()) {
					continue;
				}
				// 将每行文档的词分割出来
				String[] words = line.split(" ");
				Map<String, Integer> wt = new HashMap<String, Integer>();// 当前这行文档中所有词及出现的次数
				for (String word : words) {// 统计该文档中的词及出现的次数
					word = word.trim();
					if (word.isEmpty()) {
						continue;
					}
					Integer times = wt.get(word);
					if (times == null) {
						// 如果没有出现过，计一次
						wt.put(word, 1);
					} else {
						wt.put(word, times + 1);
					}
				}
				// 按照词的个数生成一个行矩阵
				StringBuffer matrixLine = new StringBuffer();
				BufferedReader wordBr = new BufferedReader(new InputStreamReader(new FileInputStream(wordFile), enc));
				for (String w = wordBr.readLine(); w != null; w = wordBr.readLine()) {
					// 读取一行文本
					w = w == null ? "" : w.trim();
					if (w.isEmpty()) {
						continue;
					}
					if (matrixLine.length() > 0) {
						matrixLine.append(",");
					}
					// 看当前这行的文档中有没有w这个词，如果有，则记录其数量，如果没有，则记0
					Integer times = wt.get(w);
					if (times == null) {
						matrixLine.append("0");
					} else {
						matrixLine.append(times);
					}
				}
				matrixBw.write(matrixLine.toString() + IOUtils.LINE_SEPARATOR);
				// 关闭文件
				wordBr.close();
			}
			matrixBw.flush();
			matrixBw.close();
			System.out.println("matrix矩阵文件生成完成！");
			// 生成一个复杂的矩阵，带有词编号及出现的次数信息
			File matrixComplexFile = new File(file.getAbsolutePath() + FILE_MATRIX_COMPLEX);
			BufferedWriter matrixComplexBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
					matrixComplexFile), enc));
			br.close();
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), enc));
			int lineNumComplex = 0;
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				System.out.println("在解析文件的" + (++lineNumComplex) + "行！");
				// 读取一行文本
				line = line == null ? "" : line.trim();
				if (line.isEmpty()) {
					continue;
				}
				// 将每行文档的词分割出来
				String[] words = line.split(" ");
				Map<String, Integer> wt = new LinkedHashMap<String, Integer>();// 当前这行文档中所有词及出现的次数
				int count = 0;
				for (String word : words) {// 统计该文档中的词及出现的次数
					word = word.trim();
					if (word.isEmpty()) {
						continue;
					}
					++count;
					Integer times = wt.get(word);
					if (times == null) {
						// 如果没有出现过，计一次
						wt.put(word, 1);
					} else {
						wt.put(word, times + 1);
					}
				}
				// 生成行内容
				StringBuffer matrixLine = new StringBuffer();
				matrixLine.append(count);
				for (String word : wt.keySet()) {
					if (matrixLine.length() > 0) {
						matrixLine.append(" ");
					}
					// 看当前这行的文档中有没有w这个词，如果有，则记录其数量，如果没有，则记0
					matrixLine.append(dic.getID(word));
					matrixLine.append(":");
					matrixLine.append(wt.get(word));
				}
				matrixComplexBw.write(matrixLine.toString() + IOUtils.LINE_SEPARATOR);
			}
			matrixComplexBw.flush();
			matrixComplexBw.close();
			System.out.println("matrix矩阵文件生成完成！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
