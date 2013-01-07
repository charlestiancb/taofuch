package com.scoop.crawler.weibo.runnable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.csvreader.CsvWriter;
import com.scoop.crawler.weibo.entity.OneWeiboInfo;
import com.scoop.crawler.weibo.entity.WeiboComment;
import com.scoop.crawler.weibo.entity.WeiboPersonInfo;
import com.scoop.crawler.weibo.repository.CsvDataSource;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.util.ThreadUtils;

public class CsvRunnable extends WeiboCommentRunnable {
	public CsvRunnable(DataSource dataSource, OneWeiboInfo weibo) {
		super(dataSource, weibo);
	}

	@Override
	public void run() {
		DefaultHttpClient client = ThreadUtils.allocateHttpClient();
		CsvWriter csvWriter = null;
		DataSource ds;
		boolean hasComment = false;
		File commentFile = null;
		try {
			System.out.println("解析评论信息……");
			CsvDataSource csv = (CsvDataSource) dataSource;
			// 先生成对应的评论csv文件！
			commentFile = new File(new File(csv.getCsvFile()).getParentFile(), parseToFileName(weibo.getUrl()));
			if (!commentFile.isFile()) {
				FileUtils.forceMkdir(commentFile.getParentFile());
				commentFile.createNewFile();
			} else {
				long l = commentFile.lastModified() - new Date().getTime();
				if (l > 0 && l < 30 * 60 * 1000) {// 30分钟之内不再抓取
					System.out.println("该微博的评论已经抓取过，放弃此次抓取……");
					return;
				}
			}
			// 将解析出来的评论内容写入csv文件中！
			csvWriter = new CsvWriter(new FileOutputStream(commentFile), ',', Charset.forName(CsvDataSource.CSV_ENC));
			String[] titles = new String[] { "评论内容", "评论时间", "评论者主页", "评论者id", "评论者姓名", "评论者相关信息", "兴趣爱好", "评论者简介",
					"关注数", "粉丝数", "发布微博数" };
			csvWriter.writeRecord(titles);
			csvWriter.flush();
			ds = new CsvDataSource(commentFile.getAbsolutePath());
			// 获取所有评论信息，并进行循环处理。
			Elements eles = weibo.getDetailDoc().getElementsByAttributeValue("class", "comment_lists").select("dd");
			Comments comments = new Comments(weibo.getDetail());
			while (eles != null && eles.size() > 0) {
				Element tmp = null;
				for (int i = 0; i < eles.size(); i++) {
					System.out.println("解析其中一条评论信息……");
					tmp = eles.get(i);
					if (tmp != null) {
						// 获取对应的评论者主页URL。
						try {
							String userInfoUrl = parseToUrl(tmp, weibo.getUrl());
							WeiboPersonInfo person = new WeiboPersonInfo(userInfoUrl, client);
							person.setHandler(weibo.getHandler());
							WeiboComment comment = new WeiboComment(tmp);
							comment.setHandler(weibo.getHandler());
							comment.setWeiboId(weibo.getId());
							comment.setPerson(person);
							ds.saveComment(comment);
							ds.savePerson(person);
							hasComment = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				// 加载下一页评论，并进行分析
				eles = loadNextPage(comments, client);
			}
		} catch (Exception e) {
			System.err.println("解析微博[" + weibo + "]的评论失败！");
			e.printStackTrace();
		} finally {
			if (csvWriter != null) {
				csvWriter.close();
			}
			// 如果没有微博评论，则删除对应的文件！
			if (!hasComment && commentFile != null) {
				try {
					FileUtils.forceDelete(commentFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 将线程释放
			ThreadUtils.freeThread();
		}
	}

	/**
	 * 将URL转换成文件名。即，将“/”转换为“-”即可！
	 * 
	 * @param weiboUrl
	 * @return
	 */
	protected String parseToFileName(String weiboUrl) {
		String tmp = weiboUrl.startsWith("http://") ? weiboUrl.substring(7) : weiboUrl;
		String[] paths = tmp.split("/");
		tmp = paths[paths.length - 2] + "-" + paths[paths.length - 1];
		return "评论/" + tmp + ".csv";
	}
}
