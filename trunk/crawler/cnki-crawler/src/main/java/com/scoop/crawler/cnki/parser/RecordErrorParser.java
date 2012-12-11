package com.scoop.crawler.cnki.parser;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 记录失败的请求信息！
 * 
 * @author taofucheng
 * 
 */
public class RecordErrorParser {
	private static File ERR_URL_FILE = new File("E:/tmp/cnki/err_urls.txt");

	public static void recordFailedUrl(String url) {
		url = StringUtils.trimToEmpty(url);
		if (url.isEmpty()) {
			return;
		}
		createFile();
		appendToFile(url);
	}

	private static void appendToFile(String url) {
		try {
			FileUtils.write(ERR_URL_FILE, url, "GBK", true);
			FileUtils.write(ERR_URL_FILE, IOUtils.LINE_SEPARATOR, "GBK", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void createFile() {
		if (!ERR_URL_FILE.isFile()) {
			try {
				FileUtils.forceMkdir(ERR_URL_FILE.getParentFile());
				ERR_URL_FILE.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
