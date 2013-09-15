package com.cloudtech.site.popocapp.entity.weixin;

/**
 * 微信推送过来的事件。<br>
 * 
 * <pre>
 * 事件类型，subscribe(订阅)、unsubscribe(取消订阅)、CLICK(自定义菜单点击事件)
 * </pre>
 * 
 * @author taofucheng
 * 
 */
public class ReceiveEvent extends Msg {
	/** 订阅事件 */
	public static final String EVENT_SUBSCRIBE = "subscribe";
	/** 取消订阅事件 */
	public static final String EVENT_UNSUBSCRIBE = "unsubscribe";
	/** 点击事件 */
	public static final String EVENT_CLICK = "CLICK";
	private String event;
	private String eventKey;

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
}
