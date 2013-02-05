package org.wltea.analyzer.split;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class WordSplitor {
	/**
	 * 将给定的文本进行分词，然后将各个词用空格分开
	 * 
	 * @param str
	 * @return
	 */
	public static String splitToStr(String str) {
		List<Lexeme> words = split(str);
		StringBuffer sb = new StringBuffer("");
		for (Lexeme l : words) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(l.getLexemeText());
		}
		return sb.toString();
	}

	public static List<Lexeme> split(String str) {
		List<Lexeme> ls = new ArrayList<Lexeme>();
		if (str == null || str.trim().length() < 1) {
			return ls;
		}
		try {
			IKSegmenter _IKImplement = new IKSegmenter(new StringReader(str), true);
			for (Lexeme nextLexeme = _IKImplement.next(); nextLexeme != null; nextLexeme = _IKImplement.next()) {
				ls.add(nextLexeme);
			}
		} catch (IOException e) {
			return ls;
		}
		return ls;
	}

	public static String[] splitToArr(String str) {
		List<Lexeme> words = split(str);
		String[] result = new String[words.size()];
		for (int i = 0; i < words.size(); i++) {
			String tmp = words.get(i).getLexemeText();
			result[i] = tmp == null ? "" : tmp.trim();
		}
		return result;
	}
}
