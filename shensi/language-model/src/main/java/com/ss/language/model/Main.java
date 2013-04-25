package com.ss.language.model;

import com.ss.language.model.tf_idf.DocumentProcessor;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new DocumentProcessor().process();// 计算tf*idf值。
	}
}
