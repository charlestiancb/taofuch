package com.scoop.crawler.weibo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class StreamUtils {
	/** 微博搜索相关的文件 */
	public static final String WEIBO_SEARCH_SREAM = "weiboSearchFileProcessing";

	public static void main(String[] args) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("1", 1);
		write("fileMap", map);
		System.out.println(read("fileMap"));

		map.put("2", 2);
		write("fileMap", map);
		System.out.println(read("fileMap"));
	}

	public static void write(String name, Object obj) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(FileUtils.getTempDirectory(),
					name)));
			oos.writeObject(obj);
			oos.close();
		} catch (Exception e) {
			Logger.log("写入失败！", e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T read(String name) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(FileUtils.getTempDirectory(),
					name)));
			T t = (T) ois.readObject();
			ois.close();
			return t;
		} catch (Exception e) {
			Logger.log("读入失败！", e);
		}
		return null;
	}
}
