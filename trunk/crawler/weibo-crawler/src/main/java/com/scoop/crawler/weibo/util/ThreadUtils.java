package com.scoop.crawler.weibo.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.http.impl.client.DefaultHttpClient;

import com.scoop.crawler.weibo.fetch.info.LogonInfo;
import com.scoop.crawler.weibo.request.SinaWeiboRequest;

public class ThreadUtils {
	/** 繁忙的线程数！ */
	// private static ConcurrentHashMap<Type, Integer> busyCount;
	/** 并发的线程数上限 */
	private static final int THREAD_LIMIT = 5;
	private static DefaultHttpClient client;
	private static Executor exec = Executors.newFixedThreadPool(THREAD_LIMIT);

	/**
	 * 释放一个线程
	 */
	public static void freeThread() {
		// busyCount.put(Type.THREAD, busyCount.get(Type.THREAD) - 1);
	}

	/**
	 * 执行一个线程
	 * 
	 * @param command
	 */
	public static void execute(Thread command) {
		exec.execute(command);
	}

	/**
	 * 自动分配，如果没有空闲可用，则新建一个新的
	 * 
	 * @return
	 */
	public static DefaultHttpClient allocateHttpClient() {
		if (client == null) {
			LogonInfo log = LogonInfo.getLogonInfo();
			client = SinaWeiboRequest.getHttpClient(log.getUsername(), log.getPassword());
		}
		return client;
	}
}
