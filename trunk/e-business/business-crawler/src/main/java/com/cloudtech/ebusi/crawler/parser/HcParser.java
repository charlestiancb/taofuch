package com.cloudtech.ebusi.crawler.parser;

import java.util.List;
import java.util.regex.Pattern;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.Url;

import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.cloudtech.ebusi.crawler.index.Indexer;
import com.cloudtech.ebusi.crawler.parser.hc.CredibilityParser;
import com.cloudtech.ebusi.crawler.parser.hc.ProfileParser;

/**
 * 解析慧聪的公司信息的数据，并将其分字段进行建立相应的索引。
 * 
 * @author taofucheng
 * 
 */
public class HcParser extends AbstractParser {
	/**
	 * 构造一个指定索引器的解析
	 * 
	 * @param indexer
	 */
	public HcParser(Indexer indexer) {
		super(indexer);
	}

	@Override
	public boolean followUrl(Url url) {
		return url.link().startsWith("http://b2b.hc360.com/companylist/");
	}

	@Override
	public void doParse(Page page, Indexer indexer) {
		// 得到所有的链接，然后得到所有的链接内容，然后解析每个详细页面中的内容！
		List<String> links = page.getLinks();
		if (links != null && !links.isEmpty()) {
			for (String link : links) {
				if (Pattern.matches("^http://([a-zA-Z0-9\\-_]+).b2b.hc360.com/$", link)) {
					try {
						Parser parser = new Parser(link + "shop/show.html");// 公司介绍页面
						NodeList nl = parser.parse(new HasAttributeFilter("class", "mainbox"));// 总体信息
						if (CredibilityParser.accept(nl)) {// 判断这个用户的信用档案是否合格
							System.out.println(link);
							CompanyInfo com = ProfileParser.getIndexComInfo(nl);// nl是公司的所有信息页面。
							if (com != null && indexer != null) {
								indexer.indexCom(com);
							}
						}
					} catch (ParserException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
