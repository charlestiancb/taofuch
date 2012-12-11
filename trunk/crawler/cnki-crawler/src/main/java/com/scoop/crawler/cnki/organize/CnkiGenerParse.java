package com.scoop.crawler.cnki.organize;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.scoop.crawler.cnki.entity.RefStatistic;
import com.scoop.crawler.cnki.parser.CnkiArticleParser;

public class CnkiGenerParse {
	public int year;
	public int volume;
	public String journal = "";// 输入爬取的杂志名
	public String db = "";// 输入爬取杂志的对应编号
	// 辅助变量：每一期期刊对应的url，1994-1998年是特殊格式，ZGTS706.001
	public int leftyear;
	public int rightyear;
	// 辅助变量：//不同年份，一年内期刊发行数也不同，
	public int voltyear;// 一年期刊数变化的年份
	public int leftvol;// 1993年以前一年4期，
	public int rightvol;// 2010-1994一年可有6期
	public BufferedWriter rw;

	public void JournalWholeParse(int startyear) throws IOException {
		CnkiArticleParser c = new CnkiArticleParser();
		c.journal = journal;
		year = startyear;
		URLgenerate(c, year);
	}

	public void JouranlWholeParse(int startyear, int endyear) throws IOException {
		CnkiArticleParser c = new CnkiArticleParser();
		c.journal = journal;
		for (int i = startyear; i < endyear; i++) {
			year = i;
			URLgenerate(c, year);
		}

	}

	// 根据输入年份不同，通过分析不同年份的url变化，生成某一年份各期杂志的url
	public void URLgenerate(CnkiArticleParser c, int year) throws IOException {
		String journalcode = "http://www.cnki.net/kcms/detail/detail.aspx?dbCode=cjfd&QueryID=318&CurRec=3&filename="
				+ db;
		String dbcode = "";// 期刊数据库编号
		dbcode = "&dbname=CJFD7993";// 默认1957-1993的编号
		// 不同年份数据库不同,期刊数据库编号也不同
		if (year == 2010 || year == 2011 || year == 2012)
			dbcode = "&dbname=CJFDLAST" + Integer.toString(year);
		if (year == 2009)
			dbcode = "&dbname=CJFD0911";
		if (2006 <= year && year <= 2008)
			dbcode = "&dbname=CJFD0608";
		if (2003 <= year && year <= 2005)
			dbcode = "&dbname=CJFD0305";
		if (1999 <= year && year <= 2002)
			dbcode = "&dbname=CJFD9902";
		if (1994 <= year && year <= 1998)
			dbcode = "&dbname=CJFD9498";
		if (1994 <= year && year <= 1998)
			dbcode = "&dbname=CJFD9498";
		// 不同年份，一年内期刊发行数也不同，1993年以前一年4期，且1979年只有两期
		int volumenum = 0;// 一年共几期
		int papernum = 21;// 一期共几篇文章
		if (year <= voltyear) // 突变的前一年，以期刊少的那年为例
		{
			volumenum = leftvol;

		} else {
			volumenum = rightvol;
		}

		// 生成每一期期刊对应的url，
		if (leftyear <= year && year <= rightyear) { // 1994-1998年是特殊格式，ZGTS706.001

			for (int i = 1; i <= volumenum; i++) {
				for (int j = 1; j <= papernum; j++) {
					volume = i;
					String article = null;
					if (j < 10)
						article = ".00" + String.valueOf(j);
					else
						article = ".0" + String.valueOf(j);
					String url = journalcode + Integer.toString(year - 1990) + "0" + Integer.toString(volume) + article
							+ dbcode;
					// System.out.println(url);
					parsearticles(c, url);
					sqlgenerator(c);
				}
			}
		} else {
			for (int i = 1; i <= volumenum; i++) {
				for (int j = 2; j <= papernum; j++) {

					volume = i;
					String article = null;
					if (j < 10)
						article = "00" + String.valueOf(j);
					else
						article = "0" + String.valueOf(j);
					String url = journalcode + Integer.toString(year) + "0" + Integer.toString(volume) + article
							+ dbcode;
					parsearticles(c, url);
					sqlgenerator(c);
				}
			}

		}

	}

	// 本函数用于获取某一年份对应期刊的所有信息
	// 主要包括各期的时间，标题，作者，摘要，引证文献的各年份次数
	public void parsearticles(CnkiArticleParser c, String linkurl) throws IOException {
		c.getHtml(linkurl);
		c.year = (Integer.toString(year)); // 年
		c.volume = (Integer.toString(volume));// 期刊号
		// 获取引文相关信息

	}

	// 生成对应的sql语句
	public void sqlgenerator(CnkiArticleParser c) throws IOException {
		String processsql = "";// sql处理语句，可对应增删改查
		String sqlend = ";";
		if (!c.titleCn.equals("") && (c.author.indexOf("【摘要】") == -1)) {
			processsql = "insert informationscience set Title=" + "\'" + c.titleCn.trim() + "\'" + ",author=" + "\'"
					+ c.author.trim() + "\'" + ",Abstract=" + "\'" + c.abstr.trim() + "\'" + ",keywords=" + "\'"
					+ c.keywords.trim() + "\'" + ",JournalName=" + "\'" + c.journal.trim() + "\'" + ",year=" + "\'"
					+ c.year.trim() + "\'" + ",vol=" + "\'" + c.volume.trim() + "\'" + ",citedsum=" + "\'"
					+ c.citedsum.trim() + "\'" + ",downsum=" + "\'" + c.downsum.trim() + "\'";
		}

		List<String> ciationsqllist = new ArrayList<String>();
		for (int i = year; i <= 2012; i++) {

			for (RefStatistic rs : c.citationlist) {
				if (rs.getYear() == i) {

					String ciationsql = ",c" + Integer.toString(i) + "=\'" + "引证文献数量:" + rs.getCount() + "|详细信息:"
							+ rs.toString() + "\'";
					ciationsqllist.add(ciationsql);
				}
			}
		}
		String citationclasue = "";
		for (String cs : ciationsqllist) {

			citationclasue += cs;
		}
		String sqlclaue = processsql + citationclasue + sqlend;
		System.out.println(sqlclaue);// 批量写入文件
		rw.write(sqlclaue);
		rw.flush();
		System.out.println("请耐心等待");
	}
}
