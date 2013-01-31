package com.tfc.word.spell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.tfc.word.spell.convert.CnToSpellHelper;

/**
 * 词库转换工具，便于处理词库的。
 * 
 * @author taofucheng
 * 
 */
public class CharDatabaseUtils {
	private static final String enc = "UTF-8";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			toHanzi("F:/tmp/char_spell.dic");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 转换成汉字
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static void toHanzi(String file) throws Exception {
		File f = new File(file);
		if (f.isFile()) {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), enc));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(
					f.getParentFile(), "hanzi.txt")), enc));
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				line = StringUtils.trim(line);
				if (StringUtils.isEmpty(line) || line.startsWith("//")) {
					continue;
				} else {
					String[] ls = line.split("\\|", 2);
					String key = CnToSpellHelper.parseToCn(ls[0]);
					bw.write(key);
					bw.write(IOUtils.LINE_SEPARATOR);
					bw.flush();
				}
			}
			bw.close();
			br.close();
		}
	}

	/**
	 * 转换成ascii码方式
	 * 
	 * @param file
	 */
	public static void toAscii(String file) {

	}
}
