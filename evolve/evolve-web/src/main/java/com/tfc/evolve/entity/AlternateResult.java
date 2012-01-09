package com.tfc.evolve.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <li>与“进化者”交互的结果</li>
 * 
 * @author taofucheng
 * 
 */
public class AlternateResult implements Serializable {
    private static final long serialVersionUID = -5311506842103768643L;
    /** 聊天历史记录 */
    private List<String> history;
    /** “进化者”讲的话 */
    private String reply;

    public void addHistory(String historyContent) {
        if (history == null) {
            history = new ArrayList<String>();
        }
        history.add(historyContent);
    }

    public List<String> getHistory() {
        return history;
    }

    public void setHistory(List<String> history) {
        this.history = history;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
