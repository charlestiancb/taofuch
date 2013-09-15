package com.cloudtech.site.popocapp.utils;

import com.cloudtech.site.popocapp.entity.weixin.Msg;

//TODO 完善微信xml与实体之间转换的工具类
public class WeixinMsgUtils {
	/**
	 * 将Xml内容转换为对应的实体类！
	 * 
	 * @param xml
	 * @param msgClass
	 * @return
	 */
	public static <T> T parseToMsg(String xml, Class<T> msgClass) {
		return null;
	}

	/**
	 * 将具体的消息信息转换为回复微信的xml格式
	 * 
	 * @param msg
	 * @return
	 */
	public static String parseToString(Msg msg) {
		return "";
	}
}
