package com.tfc.word.auto.collect;

import com.tfc.word.auto.collect.config.Configuration;
import com.tfc.word.auto.collect.study.Robot;
import com.tfc.word.auto.collect.study.RobotManager;

public class Demo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 设置一些配置信息
		Configuration.THREAD_COUNT = 3;
		// 启动自动抓取学习程序。
		Robot r = new Robot("http://www.tiexue.net/");
		RobotManager.exec(r);
	}

}
