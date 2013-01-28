package com.tfc.word.auto.collect.study;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 网络爬虫自动学习入口管理。
 * 
 * @author taofucheng
 * 
 */
public class RobotManager {
	private static int THREAD_COUNT = 5;
	private static ExecutorService threads = Executors.newFixedThreadPool(THREAD_COUNT);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

	public static void exec(Robot robot) {
		threads.execute(robot);
	}

	/**
	 * 等待执行完成之后关闭。
	 */
	public static void shutdown() {
		threads.shutdown();
	}
}
