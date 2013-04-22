package com.scoop.crawler.weibo.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS  ");
	static {
		logFile = new File(FileUtils.getUserDirectory(), "weibo_logs_" + System.nanoTime() + ".log");
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
			System.out.println(formatCurTime() + "日志文件：" + logFile);
		}
	}

	public static void log(String msg, Throwable t) {
		synchronized (logFile) {
			try {
				if (msg != null) {
					System.out.println(formatCurTime() + msg);
					FileUtils.writeStringToFile(logFile, formatCurTime() + msg + IOUtils.LINE_SEPARATOR, true);
				}
				if (t != null) {
					System.out.println(formatCurTime());
					t.printStackTrace();
					FileUtils.writeStringToFile(logFile,
												ExceptionUtils.getFullStackTrace(t) + IOUtils.LINE_SEPARATOR,
												true);
				}
			} catch (Exception e) {
				System.out.println(formatCurTime() + "保存日志失败！");
				System.out.println(formatCurTime() + msg);
				System.out.println(formatCurTime() + t);
			}
		}
	}

	private static String formatCurTime() {
		return format.format(new Date());
	}

	public static void log(String msg) {
		log(msg, null);
	}

	public static void log(Throwable t) {
		log(null, t);
	}

	public static void main(String[] args) {
		System.out.println(formatCurTime());
	}
}
