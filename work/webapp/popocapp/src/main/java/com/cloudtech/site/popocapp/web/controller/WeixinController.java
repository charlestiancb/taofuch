package com.cloudtech.site.popocapp.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/weixin")
public class WeixinController extends BaseController {
	/**
	 * 响应微信认证的。<br>
	 * 
	 * <pre>
	 * 开发者通过检验signature对请求进行校验（下面有校验方式）。若确认此次GET请求来自微信服务器，请原样返回echostr参数内容，则接入生效，否则接入失败。
	 * 
	 * signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
	 * 
	 * 加密/校验流程：
	 * 1. 将token、timestamp、nonce三个参数进行字典序排序
	 * 2. 将三个参数字符串拼接成一个字符串进行sha1加密
	 * 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
	 * </pre>
	 * 
	 * @param signature
	 *            微信加密签名
	 * @param timestamp
	 *            时间戳
	 * @param nonce
	 *            随机数
	 * @param echostr
	 *            随机字符串
	 * @param model
	 */
	@RequestMapping(value = "/resp", method = RequestMethod.GET)
	public void wx(String signature, String timestamp, String nonce,
			String echostr, Model model) {
		model.addAttribute("key", "hello");
	}

	/**
	 * 接收微信推送的消息，并作相应的回复！
	 * 
	 * @param ToUserName
	 *            开发者微信号
	 * @param FromUserName
	 *            发送方帐号（一个OpenID）
	 * @param CreateTime
	 *            消息创建时间 （整型）
	 * @param MsgType
	 *            消息类型，目前有：location
	 * @param request
	 * @param model
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/resp", method = RequestMethod.POST)
	public void wx(String ToUserName, String FromUserName, String CreateTime,
			String MsgType, HttpServletRequest request, Model model,
			HttpServletResponse response) throws IOException {

	}
}
