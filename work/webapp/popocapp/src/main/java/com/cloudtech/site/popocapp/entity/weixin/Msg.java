package com.cloudtech.site.popocapp.entity.weixin;

/**
 * 消息
 * 
 * @author taofucheng
 * 
 */
public abstract class Msg {
	/** 消息类型：文本 */
	public static final String MSG_TYPE_TEXT = "text";
	/** 消息类型：图片 */
	public static final String MSG_TYPE_IMAGE = "image";
	/** 消息类型：地理信息 */
	public static final String MSG_TYPE_LOCATION = "location";
	/** 消息类型：链接 */
	public static final String MSG_TYPE_LINK = "link";
	/** 消息类型：事件 */
	public static final String MSG_TYPE_EVENT = "event";

	/** 开发者微信号 */
	private String toUserName;
	/** 发送方帐号（一个OpenID） */
	private String fromUserName;
	/** 消息创建时间 （整型） */
	private long createTime;
	/** 消息类型，目前有：text、image、location、link、event */
	private String msgType;

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
}
