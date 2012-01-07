/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.spring.velocity.tools;

import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Assert;

import com.tfc.evolve.common.utils.StringUtils;

/**
 * Url工具类，从HttpServletRequest中构建访问URL
 * 
 * @author taofucheng
 * @see Spring-utils
 */
public class UrlUtils {

    public static String buildFullRequestUrl(HttpServletRequest r) {
        return buildFullRequestUrl(r.getScheme(), r.getServerName(), r.getServerPort(), r.getRequestURI(),
                r.getQueryString());
    }

    /**
     * Obtains the full URL the client used to make the request.
     * <p>
     * Note that the server port will not be shown if it is the default server port for HTTP or HTTPS (80 and 443
     * respectively).
     * 
     * @return the full URL, suitable for redirects (not decoded).
     */
    public static String buildFullRequestUrl(String scheme, String serverName, int serverPort, String requestURI,
            String queryString) {

        scheme = scheme.toLowerCase();

        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);

        // Only add port if not default
        if ("http".equals(scheme)) {
            if (serverPort != 80) {
                url.append(":").append(serverPort);
            }
        }
        else if ("https".equals(scheme)) {
            if (serverPort != 443) {
                url.append(":").append(serverPort);
            }
        }

        // Use the requestURI as it is encoded (RFC 3986) and hence suitable for redirects.
        url.append(requestURI);

        if (queryString != null) {
            url.append("?").append(queryString);
        }

        return url.toString();
    }

    /**
     * Obtains the web application-specific fragment of the request URL.
     * <p>
     * Under normal spec conditions,
     * 
     * <pre>
     * requestURI = contextPath + servletPath + pathInfo
     * </pre>
     * 
     * But the requestURI is not decoded, whereas the servletPath and pathInfo are (SEC-1255). This method is typically
     * used to return a URL for matching against secured paths, hence the decoded form is used in preference to the
     * requestURI for building the returned value. But this method may also be called using dummy request objects which
     * just have the requestURI and contextPatth set, for example, so it will fall back to using those.
     * 
     * @return the decoded URL, excluding any server name, context path or servlet path
     */
    public static String buildRequestUrl(HttpServletRequest r) {
        return buildRequestUrl(r.getServletPath(), r.getRequestURI(), r.getContextPath(), r.getPathInfo(),
                r.getQueryString());
    }

    /**
     * Obtains the web application-specific fragment of the URL.
     */
    private static String buildRequestUrl(String servletPath, String requestURI, String contextPath, String pathInfo,
            String queryString) {

        StringBuilder url = new StringBuilder();

        if (servletPath != null) {
            url.append(servletPath);
            if (pathInfo != null) {
                url.append(pathInfo);
            }
        }
        else {
            url.append(requestURI.substring(contextPath.length()));
        }

        if (queryString != null) {
            url.append("?").append(queryString);
        }

        return url.toString();
    }

    public static Map<String, Object> getParameters(ServletRequest request) {
        return getParametersStartingWith(request, null);

    }

    @SuppressWarnings({"rawtypes"})
    public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
        Assert.notNull(request, "Request must not be null");
        Enumeration paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<String, Object>();
        if (prefix == null) {
            prefix = "";
        }
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if ("".equals(prefix) || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());
                String[] values = request.getParameterValues(paramName);

                if (values != null && values.length > 0) {
                    if (values.length > 1) {
                        params.put(unprefixed, values);
                    }
                    else {
                        params.put(unprefixed, values[0]);
                    }
                }
            }
        }
        return params;
    }

    /**
     * Returns true if the supplied URL starts with a "/" or "http".
     */
    public static boolean isValidRedirectUrl(String url) {
        return url != null && (url.startsWith("/") || url.toLowerCase().startsWith("http"));
    }

    /**
     * 获取请求的真正IP地址。也就是屏蔽代理这一层的。<br>
     * 目前网上流行的所谓“取真实IP地址”的方法，都有bug，没有考虑到多层透明代理的情况。 多数代码类似：<br>
     * <code>
     * string IpAddress = (HttpContext.Current.Request.ServerVariables["HTTP_X_FORWARDED_FOR"]!=null &&
     * HttpContext.Current.Request.ServerVariables["HTTP_X_FORWARDED_FOR"] !=String.Empty)
     * ?HttpContext.Current.Request.ServerVariables["HTTP_X_FORWARDED_FOR"]
     * :HttpContext.Current.Request.ServerVariables["REMOTE_ADDR"];</code><br>
     * 事实上，上面的代码只试用与用户只使用了1层代理，如果用户有2层，3层HTTP_X_FORWARDED_FOR 的值是：“本机真实IP,1层代理IP,2层代理IP,.....” ，
     * 如果这个时候你的数据中保存IP字段的长度很小（15个字节），数据库就报错了。 所以取“真正”IP地址的方式，还应该判断 “HTTP_X_FORWARDED_FOR” 中是否有“,”逗号，或者长度是否超长（超过15字节
     * xxx.xxx.xxx.xxx）。
     * 
     * @param request 请求数据。
     * @return 返回真正的IP
     */
    public static String getRealIp(HttpServletRequest request) {
        String result = request.getHeader("x-forwarded-for");
        if ((result != null) && (result.trim().length() > 0)) {
            // 可能有代理
            if (result.indexOf(".") == -1) {
                // 没有“.”肯定是非IPv4格式
                result = null;
            }
            else {
                if (result.indexOf(",") != -1) {
                    // 有“,”，估计多个代理。取第一个不是内网的IP。
                    result = result.trim().replace("'", "");
                    String[] temparyip = result.split(",");
                    for (int i = 0; i < temparyip.length; i++) {
                        // 找到不是内网的地址
                        if (!isV4IPAddress(temparyip[i])) {
                            continue;
                        }
                        if (!temparyip[i].substring(0, 3).equals("10.")
                                && !temparyip[i].substring(0, 7).equals("192.168")
                                && !temparyip[i].substring(0, 7).equals("172.16.")) {
                            return temparyip[i];
                        }
                    }
                }
                else if (isV4IPAddress(result)) {
                    // 代理即是IP地址
                    return result;
                }
                else {
                    result = null; // 代理中的内容 非IP，取IP
                }
            }
        }
        if ((result == null) || (result.trim().length() == 0) || "unknown".equalsIgnoreCase(result)) {
            result = request.getHeader("Proxy-Client-IP");
        }
        if ((result == null) || (result.trim().length() == 0) || "unknown".equalsIgnoreCase(result)) {
            result = request.getHeader("WL-Proxy-Client-IP");
        }
        if ((result == null) || (result.trim().length() == 0) || "unknown".equalsIgnoreCase(result)) {
            result = request.getRemoteAddr();
        }
        return result;
    }

    /**
     * 获取用于SSO的IP地址。因为HTTP_X_FORWARDED_FOR 的值是：“本机真实IP,1层代理IP,2层代理IP,……”
     * ，为了获取正确的完整的地址，因此，我们只要获取“本机真实IP,1层代理IP,2层代理IP,.....”直到不再是代理地址即可。<br>
     * 如： "192.168.0.5,172.16.12.25,10.221.0.2,221.45.0.110,192.168.0.5"得到的是：
     * "192.168.0.5,172.16.12.25,10.221.0.2,221.45.0.110"
     * 
     * @param request 请求数据
     * @return
     */
    public static String getRealIp4SSO(HttpServletRequest request) {
        String result = request.getHeader("x-forwarded-for");
        if ((result != null) && (result.trim().length() > 0)) {
            // 可能有代理
            if (result.indexOf(".") == -1) {
                // 没有“.”肯定是非IPv4格式
                result = null;
            }
            else {
                if (result.indexOf(",") != -1) {
                    // 有“,”，估计多个代理。取第一个不是内网的IP。
                    result = result.trim().replace("'", "");
                    String[] temparyip = result.split(",");
                    String ip4sso = "";
                    for (int i = 0; i < temparyip.length; i++) {
                        // 找到不是内网的地址
                        if (!isV4IPAddress(temparyip[i])) {
                            continue;
                        }
                        if (!temparyip[i].substring(0, 3).equals("10.")
                                && !temparyip[i].substring(0, 7).equals("192.168")
                                && !temparyip[i].substring(0, 7).equals("172.16.")) {
                            return (ip4sso + temparyip[i]);
                        }
                        else {
                            ip4sso += temparyip[i] + ",";
                        }
                    }
                    if (StringUtils.isNotEmpty(ip4sso)
                            && StringUtils.isNotEmpty(StringUtils.trim(ip4sso.replaceAll(",", "")))) {
                        return StringUtils.truncateComma(ip4sso);
                    }
                }
                else if (isV4IPAddress(result)) {
                    // 代理即是IP地址
                    return result;
                }
                else {
                    result = null; // 代理中的内容 非IP，取IP
                }
            }
        }
        if ((result == null) || (result.trim().length() == 0) || "unknown".equalsIgnoreCase(result)) {
            result = request.getHeader("Proxy-Client-IP");
        }
        if ((result == null) || (result.trim().length() == 0) || "unknown".equalsIgnoreCase(result)) {
            result = request.getHeader("WL-Proxy-Client-IP");
        }
        if ((result == null) || (result.trim().length() == 0) || "unknown".equalsIgnoreCase(result)) {
            result = request.getRemoteAddr();
        }
        return result;
    }

    /**
     * 判断是否是第四版的IP地址，如果ip为空或空字符串，或全是空格的字符串，则返回false。
     * 
     * @param ip 需要验证的值
     * @return
     */
    public static boolean isV4IPAddress(String ip) {
        ip = StringUtils.trim(ip);
        if (StringUtils.isEmpty(ip)) {
            return false;
        }
        String[] elements = ip.split("\\.");
        if (elements.length != 4) {
            return false;
        }
        for (String element : elements) {
            if (!StringUtils.isInteger(element)) {
                return false;
            }
            else {
                int e = Integer.parseInt(element);
                if ((e < 0) || (e > 255)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 从HTTP请求中获取除域名信息外的完整的请求路径，如：<b>action.do?method=test&param=1%26param1=2</b><br>
     * <font color='red'><b>注意：这里获取的请求链接前面是没有“/”符号的！</b></font>如果有汉字等字符时，会转换编码。
     * 
     * @param request HTTP请求信息
     * @return 返回请求的信息。如果request为空，则返回""。
     */
    @SuppressWarnings("deprecation")
    public static String getRequestUrlByRequest(HttpServletRequest request) {
        if (request == null) {
            return "";
        }
        Enumeration<?> enumeration = request.getParameterNames();
        String dispatchPath = request.getRequestURI().substring(1);
        StringBuffer temp = new StringBuffer("");
        for (enumeration.hasMoreElements(); enumeration.hasMoreElements();) {
            String field = enumeration.nextElement().toString();
            String[] values = request.getParameterValues(field);
            for (String value : values) {
                temp.append("&");
                temp.append(field);
                temp.append("=");
                temp.append(URLEncoder.encode(value));
            }
        }
        if (StringUtils.isNotEmpty(temp.toString())) {
            dispatchPath = dispatchPath + "?" + temp.toString().substring(1);
        }
        return dispatchPath;
    }

    /**
     * 完善url网址指定的url的协议。<br>
     * fillProtocol(null)==null;<br>
     * fillProtocol("")=="";<br>
     * fillProtocol(" ")=="";<br>
     * fillProtocol("abiz.com")=="http://abiz.com";<br>
     * fillProtocol(" abiz.com ")=="http://abiz.com";<br>
     * fillProtocol("http://abiz.com")=="http://abiz.com";<br>
     * 
     * @param url
     */
    public static String fillProtocol(String url) {
        url = StringUtils.trim(url);
        if (StringUtils.isEmpty(url)) {
            return url;
        }
        if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("/")) {
            return url;
        }
        else {
            return "http://" + url;
        }
    }

    /**
     * 将传入的内容使用UTF-8编码解析为url中可以传递的内容。
     * 
     * @param text
     * @return
     */
    public static String escape(String text) {
        return escape(text, "UTF-8");
    }

    /**
     * 将传入的内容解析为url中可以传递的内容。
     * 
     * @param text
     * @return
     */
    public static String escape(String text, String encoding) {
        if (text == null) {
            return "";
        }
        try {
            return URLEncoder.encode(text, encoding);
        }
        catch (Exception e) {
            return text;
        }
    }

    /**
     * 将MIC产品名称转义为URL中的参数形式
     * 
     * @param prodName
     * @return
     */
    public static String escapeProdName(String prodName) {
        return escapeProdName(prodName, "UTF-8");
    }

    /**
     * 将MIC产品名称转义为URL中的参数形式
     * 
     * @param prodName
     * @return
     */
    public static String escapeProdName(String prodName, String encoding) {
        String tmp = escape(prodName, encoding);
        if (StringUtils.isEmpty(tmp)) {
            return prodName;
        }
        return tmp.replaceAll("%2F", "/");
    }

}
