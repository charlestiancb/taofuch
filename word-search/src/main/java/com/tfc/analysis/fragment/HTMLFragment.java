/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.analysis.fragment;

import org.apache.commons.lang.StringUtils;

import com.tfc.analysis.entity.Keyword;

/**
 * 默认高亮的方式。即使用
 * 
 * @author taofucheng
 */
public class HTMLFragment extends AbstractFragment {
    /** 开口标记 */
    private String open;
    /** 封闭标记 */
    private String close;

    /**
     * 初始化开闭标签
     * 
     * @param open 开始标签，如< b >、< font >等。
     * @param close 结束标签，如< /b >、< /font >等。
     */
    public HTMLFragment(String open, String close) {
        this.open = StringUtils.trimToEmpty(open);
        this.close = StringUtils.trimToEmpty(close);
    }

    @Override
    public String format(Keyword word) {
        return open + word.getWord() + close;
    }
}
