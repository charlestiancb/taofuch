package com.scoop.crawler.weibo.repository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.csvreader.CsvWriter;
import com.scoop.crawler.weibo.entity.OneWeiboInfo;
import com.scoop.crawler.weibo.entity.WeiboComment;
import com.scoop.crawler.weibo.entity.WeiboPersonInfo;
import com.scoop.crawler.weibo.repository.mysql.FailedRequest;
import com.scoop.crawler.weibo.repository.mysql.Weibo;

public class CsvDataSource implements DataSource {
	/** csv文件的编码 */
	public static String CSV_ENC = "GBK";
	/** 空字符串 */
	public static final String[] EMPTY_ARRAY = new String[] {};
	private CsvWriter csvWriter;
	private String csvFile = "F:/tmp/sina_weibo.csv";
	private List<String> line = new ArrayList<String>();// 每一行数据

	/**
	 * 使用指定文件路径的方式
	 * 
	 * @param csvFile
	 */
	public CsvDataSource(String csvFile) {
		if (csvFile != null && csvFile.trim().length() > 0) {
			this.csvFile = csvFile;
		}
		try {
			csvWriter = new CsvWriter(new FileOutputStream(csvFile), ',', Charset.forName(CSV_ENC));
			String[] titles = new String[] { "消息ID", "消息内容", "消息URL", "消息来源", "转发数", "评论数", "发布时间" };
			csvWriter.writeRecord(titles);
			csvWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 使用默认的路径文件
	 */
	public CsvDataSource() {
		this(null);
	}

	public void saveWeibo(OneWeiboInfo weibo) {
		List<String> line = new ArrayList<String>();
		line.add(StringUtils.trim(weibo.getId()));// 解析消息ID
		line.add(StringUtils.trim(weibo.getMsg()));// 解析消息内容
		line.add(StringUtils.trim(weibo.getUrl()));// 解析消息URL
		line.add(StringUtils.trim(weibo.getOrigin()));// 解析消息来源
		line.add(StringUtils.trim(weibo.getRwNum()));// 解析转发数
		line.add(StringUtils.trim(weibo.getCommentNum()));// 解析评论数
		line.add(StringUtils.trim(weibo.getPublishTime()));// 解析发布时间
		// 将该条微博写入文件
		try {
			csvWriter.writeRecord(line.toArray(EMPTY_ARRAY));
		} catch (IOException e) {
			e.printStackTrace();
		}
		csvWriter.flush();
	}

	public void setCsvFile(String csvFile) {
		this.csvFile = csvFile;
	}

	public String getCsvFile() {
		return csvFile;
	}

	public void close() {
		if (csvWriter != null) {
			csvWriter.close();
		}
	}

	public void saveComment(WeiboComment comment) {
		line.clear();
		line.add(comment.getContent());// 获取评论内容
		line.add(comment.getPublishTime());// 获取评论时间
		line.add(comment.getPerson().getUrl());// 评论者主页URL
		line.add(comment.getPerson().getId());// 评论者ID
		line.add(comment.getPerson().getName());// 评论者姓名
		line.add(comment.getPerson().getInfo());// 评论者所在地
		line.add(comment.getPerson().getFavorite());// 评论者兴趣爱好
		line.add(comment.getPerson().getIntro());// 评论者简介
		line.add(comment.getPerson().getFollowNum());// 评论者关注数
		line.add(comment.getPerson().getFansNum());// 评论者粉丝数
		line.add(comment.getPerson().getPublishNum());// 评论者发布微博数
		try {
			csvWriter.writeRecord(line.toArray(EMPTY_ARRAY));
			csvWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void savePerson(WeiboPersonInfo person) {
		// 这里面不需要保存，因为在Comment里面已经一起保存了！
	}

	public boolean isWeiboExists(String weiboId) {
		return false;
	}

	public boolean isCommentExists(String commentId) {
		return false;
	}

	public boolean isUserExists(String userId) {
		return false;
	}

	public void saveFailedRequest(FailedRequest request) {
	}

	public FailedRequest pop() {
		return null;
	}

	public Weibo getOneUnfetchedWeibo() {
		// TODO Auto-generated method stub
		return null;
	}
}
