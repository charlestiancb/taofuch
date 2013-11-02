package com.scoop.crawler.weibo.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import com.scoop.crawler.weibo.entity.LogonInfo;
import com.scoop.crawler.weibo.request.SinaWeiboRequest;

public class ThreadUtils {
	/** 繁忙的线程数！ */
	// private static ConcurrentHashMap<Type, Integer> busyCount;
	private static DefaultHttpClient client;
	private static ExecutorService exec = Executors.newFixedThreadPool(5);
	private static boolean hasCommentExecuted = false;
	private static boolean hasUserRelationExecuted = false;
	private static long preTime = System.currentTimeMillis();

	public static ExecutorService getRunnaleExecutor() {
		return exec;
	}

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
	public static void executeCommnet(Thread command) {
		if (!hasCommentExecuted) {
			hasCommentExecuted = true;
			command.start();
		}
	}

	/**
	 * 结束了对评论的抓取
	 */
	public static void finishComment() {
		hasCommentExecuted = false;
	}

	/**
	 * 执行一个线程
	 * 
	 * @param command
	 */
	public static void executeUserRelation(Thread command) {
		if (!hasUserRelationExecuted) {
			hasUserRelationExecuted = true;
			command.start();
		}
	}

	/**
	 * 结束了对评论的抓取
	 */
	public static void finishUserRelation() {
		hasUserRelationExecuted = false;
	}

	/**
	 * 自动分配，如果没有空闲可用，则新建一个新的
	 * 
	 * @return
	 */
	public static DefaultHttpClient allocateHttpClient() {
		if (client == null || shouldLogAgain()) {
			LogonInfo log = LogonInfo.getLogonInfo();
			client = SinaWeiboRequest.getHttpClient(log.getUsername(),
					log.getPassword());
		}
		return client;
	}

	private static boolean shouldLogAgain() {
		// 12小时就要重新登录一次
		boolean need = System.currentTimeMillis() - preTime > 12 * 3600 * 1000;
		if (need) {
			preTime = System.currentTimeMillis();
		}
		return need;
	}

	public static void setClient(DefaultHttpClient client) {
		ThreadUtils.client = client;
	}

	public static long nextSleepInterval() {
		return (10 + RandomUtils.nextInt(10)) * 100L;
	}
}
