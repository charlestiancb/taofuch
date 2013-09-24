package com.ss.language.model.gibblda;

public class LDACmdOption {
	/** 当前线程上的操作配置 */
	public static ThreadLocal<LDACmdOption> curOption = new ThreadLocal<LDACmdOption>();

	/** Specify whether we want to estimate model from scratch，是否开始训练模型 */
	public boolean est = false;

	/**
	 * Specify whether we want to continue the last
	 * estimation，决定是否是基于先前已有的模型基础上继续用新数据训练模型
	 */
	public boolean estc = false;

	/** Specify whether we want to do inference，是否使用先前已经训练好的模型进行推断 */
	public boolean inf = true;

	/** Specify directory，数据结果（模型数据）保存位置 */
	public String dir = "";

	/** Specify the model name，选择使用哪一个迭代的模型结果来进行推断 */
	public String modelName = "model-final";

	/** Specify alpha，平滑系数 */
	public double alpha = 1.0;

	/** Specify beta */
	public double beta = 1.0;

	/** Specify the number of topics，类簇数目，谨慎设置 */
	public int K = 100;

	/** Specify the number of iterations，迭代数目，谨慎设置 */
	public int niters = 1000;

	/**
	 * Specify the number of steps to save the model since the last
	 * save，指定把迭代结果模型保存到硬盘上的迭代跨度，表示每次步多少之后保存一次，即每迭代10次保存一次。
	 */
	public int savestep = -1;

	/**
	 * Specify the number of most likely words to be printed for each
	 * topic，对每一个类别（话题）选择前多少个最大概率词项
	 */
	public int twords = 100;

	/** Specify whether we include raw data in the input */
	public boolean withrawdata = false;

	/** Specify the wordmap file，生成的副产品的文件名 */
	public String wordMapFileName = "wordmap.txt";
	/** 每篇文章中各个词的出现次数 */
	public String eachwords = "doc_eachwords.txt";
	/** 每篇文章的顺序及id */
	public String docIdFile = "doc_ids.txt";
	/** 读写文件的编码 */
	public String fileEncoding = "UTF-8";
}
