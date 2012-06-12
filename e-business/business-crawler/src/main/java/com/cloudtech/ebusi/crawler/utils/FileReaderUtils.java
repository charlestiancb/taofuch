package com.cloudtech.ebusi.crawler.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 文件读取工具类
 * 
 * @author taofucheng
 * 
 */
public class FileReaderUtils {
	public static LinkedHashMap<String, String> readFromFile(InputStream is) {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		try {
			List<String> lines = IOUtils.readLines(is, "UTF-8");
			if (lines != null && !lines.isEmpty()) {
				for (String line : lines) {
					line = StringUtils.trimToEmpty(line);
					if (line.length() == 0 || line.startsWith("#")) {
						continue;
					}
					int idx = line.indexOf("=");
					if (idx == -1) {
						map.put(line, null);
					} else {
						map.put(line.substring(0, idx).trim(), line.substring(idx + 1).trim());
					}
				}
			}
		} catch (IOException e) {
		}
		return map;
	}
}
