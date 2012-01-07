/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.spring.velocity.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * LinkTool.java
 * 
 * @author taofucheng
 */
public class LinkTool extends org.apache.velocity.tools.view.LinkTool {
    /**
     * 获取当前页面的完整url，包含query string等
     * 
     * @return
     */
    public String getRequestUrl() {
        return UrlUtils.buildFullRequestUrl(this.request);
    }

    /**
     * 获取当前页面的完整url，但是根据参数排除掉一部分query string<br>
     * 例如url为/vo/demands?page=1&sort=1&xx=yy<br>
     * 则getExcludeUrl("sort","order")返回/vo/demands?page=1&xx=yy
     */
    public String getExcludeUrl(String... exdQueryArr) {
        if (exdQueryArr != null && exdQueryArr.length > 0) {
            String queryString = getQueryString();
            if (StringUtils.isEmpty(queryString)) {
                queryString = "";
            }
            String[] currQueryArr = queryString.split("&");
            List<String> resultQueryList = new ArrayList<String>();
            // 将不在exclude中的参数放到result里面
            for (String currQuery : currQueryArr) {
                List<String> exdQueryList = Arrays.asList(exdQueryArr);
                String currQueryName = currQuery.split("=")[0];
                if (!exdQueryList.contains(currQueryName)) {
                    resultQueryList.add(currQuery);
                }
            }
            String[] resultQueryArr = (String[]) resultQueryList.toArray(new String[resultQueryList.size()]);
            return getRequestPath() + "?" + StringUtils.join(resultQueryArr, "&");
        }
        else {
            return getRequestUrl();
        }

    }

    /**
     * 将当前页面的完整url加上页数的query string，如果已有了则替换
     * 
     * @return
     */
    public String getPagerUrl(Object page) {
        String pageStr = Integer.valueOf(String.valueOf(page)).toString();
        String url = UrlUtils.buildRequestUrl(this.request);
        String p = this.request.getParameter("page");
        if (p == null) {
            if (url.contains("?")) {
                url += "&page=" + pageStr;
            }
            else {
                url += "?page=" + pageStr;
            }
        }
        else {
            url = url.replaceAll("page=[\\d]*", "page=" + pageStr);
        }
        url = url.replaceAll("&", "&amp;");
        return url;
    }

    /**
     * 将当前页面的完整url加上每页个数的query string，如果已有了则替换
     * 
     * @return
     */
    public String getPerPageUrl(Object perPage) {
        String perPageStr = Integer.valueOf(String.valueOf(perPage)).toString();
        String url = getPagerUrl(1).replaceAll("&amp;", "&");
        String p = this.request.getParameter("perPage");
        if (p == null) {
            if (url.contains("?")) {
                url += "&perPage=" + perPageStr;
            }
            else {
                url += "?perPage=" + perPageStr;
            }
        }
        else {
            url = url.replaceAll("perPage=[\\d]*", "perPage=" + perPageStr);
        }
        url = url.replaceAll("&", "&amp;");
        return url;
    }

    /**
     * 获得页面上的查询字符串
     */
    public String getQueryString() {
        return this.request.getQueryString();
    }

}
