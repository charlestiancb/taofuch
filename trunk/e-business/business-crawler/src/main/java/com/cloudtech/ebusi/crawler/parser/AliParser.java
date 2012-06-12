package com.cloudtech.ebusi.crawler.parser;

import java.util.List;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.Url;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.Bullet;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.cloudtech.ebusi.crawler.parser.ali.CredibilityParser;
import com.cloudtech.ebusi.crawler.parser.ali.ProfileParser;

/**
 * 解析阿里的公司信息的数据，并将其分字段进行建立相应的索引。
 * 
 * @author taofucheng
 * 
 */
public class AliParser extends AbstractParser {

	@Override
	public boolean followUrl(Url url) {
		return url.link().startsWith("http://search.china.alibaba.com/selloffer/offer_search.htm");
	}

	@Override
	public void doParse(Page page) {
		// 得到所有的链接，然后得到所有的链接内容，然后解析每个详细页面中的内容！
		List<String> links = page.getLinks();
		if (links != null && !links.isEmpty()) {
			for (String link : links) {
				if (link.startsWith("http://detail.china.alibaba.com/buyer/offerdetail")) {
					try {
						Parser parser = new Parser(link);
						NodeList nl = parser.parse(new HasAttributeFilter("data-page-type"));
						if (CredibilityParser.accept(getBulletLink(nl.elementAt(2)))) {// 判断这个用户的信用档案是否合格
							ProfileParser.indexComInfo(nl);// nl是所有的链接Tab信息。
						}
					} catch (ParserException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private String getBulletLink(Node node) {
		if (!(node instanceof Bullet)) {
			return null;
		}
		node = node.getChildren().extractAllNodesThatMatch(new HasAttributeFilter("href")).elementAt(0);
		LinkTag t = (LinkTag) node;
		return t.extractLink();
	}
}
