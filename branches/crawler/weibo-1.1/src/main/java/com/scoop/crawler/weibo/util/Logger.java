package com.scoop.crawler.weibo.util;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * 日志记录！
 * 
 * @author taofucheng
 * 
 */
public class Logger {
	private static File logFile = null;
	static {
		logFile = new File(FileUtils.getUserDirectory(), "weibo_logs.log");
		try {
			if (logFile.exists()) {
				FileUtils.forceDelete(logFile);
			}
			logFile.createNewFile();
		} catch (Exception e) {
		}
		if (logFile == null || !logFile.isFile()) {
			System.exit(-1);
		} else {
			Logger.log("日志文件：" + logFile);
		}
	}

	public static void log(String msg, Throwable t) {
		synchronized (logFile) {
			try {
				if (msg != null) {
					FileUtils.writeStringToFile(logFile, msg + IOUtils.LINE_SEPARATOR, true);
				}
				if (t != null) {
					FileUtils.writeStringToFile(logFile, ExceptionUtils.getFullStackTrace(t) + IOUtils.LINE_SEPARATOR,
							true);
				}
			} catch (Exception e) {
				Logger.log("保存日志失败！");
				Logger.log(msg);
				Logger.log(t);
			}
		}
	}

	public static void log(String msg) {
		log(msg, null);
	}

	public static void log(Throwable t) {
		log(null, t);
	}
}
