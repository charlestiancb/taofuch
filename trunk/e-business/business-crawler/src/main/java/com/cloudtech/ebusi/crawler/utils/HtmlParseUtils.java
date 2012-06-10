package com.cloudtech.ebusi.crawler.utils;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * 对Html页面进行相应的初步解析功能，如：将Html页面解析成一个易操作的数据结构。
 * 
 * @author taofucheng
 * 
 */
public class HtmlParseUtils {
	public static void main(String[] args) throws ParserException {
		parseDemo();
	}

	public static void parseDemo() throws ParserException {
		Parser parser = new Parser("http://search.china.alibaba.com/selloffer/offer_search.htm?keywords=&categoryId=1046622&pageSize=40&country=&province=&showProvince=&city=&sortType=booked&descendOrder=true&onlineStatus=&isOnlyAlipay=&biztype=&memberlevel=&pmType=&feature=&postTime=&showStyle=img&priceStart=&priceEnd=&quantityBegin=&tradeType=&isCreditFlag=&creditBalance=&filtExcludeKeywords=&partFeature=&filtResultkeywords=&uniqfield=&companyName=&memberId=&spuId=&fromOfferId=&spuCategoryId=&sprId=&imageFeatures=&defaultMerge=&retailWholesale=&mixWholesale=&isPop=&isRealPrice=&earseDirect=&fromPop=&toPop=&filt=y&lessThanQuantityBegin=true&previousPriceStart=0.0&n=y#listViewBar");
		NodeList list = parser.parse(null);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Node node = list.elementAt(i);
				if (node instanceof Html) {
					System.out.println(node.getChildren().size());
					break;
				}
			}
		}
	}
}
