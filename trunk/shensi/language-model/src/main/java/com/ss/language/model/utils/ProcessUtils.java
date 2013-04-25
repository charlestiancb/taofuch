package com.ss.language.model.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 进度工具条
 * 
 * @author taofucheng
 * 
 */
public class ProcessUtils {
	private static File record = null;

	/**
	 * 将当前的类添加到进度信息中
	 * 
	 * @param clazz
	 */
	public static void recordProcess(String processNode) {
		if (!hasProcessed(processNode)) {
			try {
				FileUtils.writeStringToFile(record, processNode + IOUtils.LINE_SEPARATOR, "UTF-8", true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 清除所有已记录的进度。
	 */
	public static void clearProcess() {
		if (record == null) {
			record = new File(FileUtils.getUserDirectory(), "language_model.process.percent.dat");
		}
		if (record.isFile()) {
			try {
				FileUtils.forceDelete(record);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 判断是否已经执行过！
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean hasProcessed(String processNode) {
		if (record == null) {
			record = new File(FileUtils.getUserDirectory(), "language_model.process.percent.dat");
			if (!record.isFile()) {
				try {
					record.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			List<String> lines = FileUtils.readLines(record, "UTF-8");
			if (lines != null && lines.size() > 0) {
				for (String line : lines) {
					if (StringUtils.isNotBlank(line) && line.trim().equals(processNode)) {
						return true;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
