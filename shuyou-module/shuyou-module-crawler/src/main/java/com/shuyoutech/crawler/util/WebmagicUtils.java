package com.shuyoutech.crawler.util;

import cn.hutool.http.HtmlUtil;
import com.alibaba.fastjson2.JSONObject;
import com.shuyoutech.common.core.util.*;
import com.shuyoutech.common.redis.util.RedisUtils;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.selector.Selectable;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author YangChao
 * @date 2025-07-07 12:32
 **/
public class WebmagicUtils extends HtmlUtil {

    public static final String REDIS_KEY_WEBMAGIC_PROXY_IP = "WEBMAGIC_PROXY_IP";


    /**
     * 把标签形如<span class='class'>123</span> 替换为123
     */
    public static String getText(Selectable selectable) {
        if (selectable == null || StringUtils.isEmpty(selectable.get())) {
            return "";
        }
        return selectable.get().replaceAll("<script.*?</script>", "").replaceAll("<.*?>", "").replaceAll("</.*?>", "");
    }

    /**
     * 把标签形如<span class='class'>123</span> 替换为123
     */
    public static String getTextTrim(Selectable selectable) {
        String result = getText(selectable);
        return replace(result);
    }

    /**
     * 把标签形如<span class='class'>123</span> 替换为123
     */
    public static String formatTextByTag(Selectable selectable, String... tagNames) {
        return formatTextByTag(selectable.get(), tagNames);
    }

    /**
     * 把标签形如<span class='class'>123</span> 替换为123
     */
    public static String formatTextByTag(String nodeStr, String... tagNames) {
        if (StringUtils.isBlank(nodeStr)) {
            return "";
        }
        for (String tagName : tagNames) {
            nodeStr = nodeStr.replaceAll(StringUtils.format("<{}.*?>", tagName), "").replaceAll(StringUtils.format("</{}>", tagName), "");
        }
        return replace(nodeStr);
    }

    /**
     * 去掉非法空格以及指定字符串
     */
    public static String replace(String nodeStr, String... trimNames) {
        if (StringUtils.isBlank(nodeStr)) {
            return "";
        }
        nodeStr = nodeStr.replaceAll(" ", "");
        nodeStr = nodeStr.replaceAll("<br>", "");
        nodeStr = nodeStr.replaceAll("&nbsp;", "");
        nodeStr = nodeStr.replaceAll(" ", "");
        nodeStr = nodeStr.replaceAll("　", "");
        nodeStr = nodeStr.replaceAll(" ", "");
        for (String trimName : trimNames) {
            nodeStr = nodeStr.trim().replaceAll(trimName, "");
        }
        return nodeStr.trim();
    }

    /**
     * 截取报文体
     */
    public static Map<String, String> getParam(String url) {
        Map<String, String> result = MapUtils.newHashMap();
        if (StringUtils.isEmpty(url)) {
            return result;
        }
        String paramStr = StringUtils.subAfter(url, '?', false);
        List<String> paramList = StringUtils.split(paramStr, '&');
        if (CollectionUtils.isEmpty(paramList)) {
            return result;
        }
        for (String param : paramList) {
            result.put(StringUtils.subBefore(param, "=", true).trim(), StringUtils.subAfter(param, "=", true));
        }
        return result;
    }

    public static synchronized HttpClientDownloader getAbYProxy() {
        // 代理服务器
        String proxyHost = "http-dyn.abuyun.com";
        int proxyPort = 9020;

        // 代理隧道验证信息
        String proxyUser = "HX59410BP8D5878D";
        String proxyPass = "6A2B9E88C34FD95A";

        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy(proxyHost, proxyPort, proxyUser, proxyPass)));
        return httpClientDownloader;
    }

    /**
     * 按量获取IP
     */
    public static synchronized HttpClientDownloader getQingGuoIpProxy() {
        JSONObject jsonObject = HttpClientUtils.sendGet("https://share.proxy.qg.net/get?key=DSI0M9HT&num=1&pool=1", JSONObject.class);
        if (null == jsonObject || !"SUCCESS".equalsIgnoreCase(jsonObject.getString("code"))) {
            return null;
        }
        String server = jsonObject.getJSONArray("data").getJSONObject(0).getString("server");
        if (StringUtils.isNotBlank(server)) {
            List<String> arrList = StringUtils.split(server, ":");
            return getQingGuoIpProxy(arrList.get(0), Integer.parseInt(arrList.get(1)));
        }
        return null;
    }

    public static HttpClientDownloader getQingGuoIpProxy(String proxyHost, Integer proxyPort) {
        // 代理隧道验证信息
        String proxyUser = "DSI0M9HT";
        String proxyPass = "C3BCDB198DEC";

        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy(proxyHost, proxyPort, proxyUser, proxyPass)));
        return httpClientDownloader;
    }

    /**
     * 按月获取IP
     */
    public static synchronized HttpClientDownloader getQingGuoTimeProxy() {
        String proxyIp = RedisUtils.getString(REDIS_KEY_WEBMAGIC_PROXY_IP);
        if (StringUtils.isNotBlank(proxyIp)) {
            List<String> arrList = StringUtils.split(proxyIp, ":");
            return getQingGuoTimeProxy(arrList.get(0), Integer.parseInt(arrList.get(1)));
        }
        JSONObject jsonObject = HttpClientUtils.sendGet("https://share.proxy.qg.net/pool?key=E616003F&num=1&area=&isp=&format=json&distinct=true&pool=1", JSONObject.class);
        if (null == jsonObject || !"SUCCESS".equalsIgnoreCase(jsonObject.getString("code"))) {
            return null;
        }
        String server = jsonObject.getJSONArray("data").getJSONObject(0).getString("server");
        String deadline = jsonObject.getJSONArray("data").getJSONObject(0).getString("deadline");
        List<String> arrList = StringUtils.split(server, ":");
        RedisUtils.set(REDIS_KEY_WEBMAGIC_PROXY_IP, server);
        RedisUtils.expireAt(REDIS_KEY_WEBMAGIC_PROXY_IP, new Date(DateUtils.parseDateTime(deadline).toJdkDate().getTime() - 5000));
        return getQingGuoTimeProxy(arrList.get(0), Integer.parseInt(arrList.get(1)));
    }

    public static HttpClientDownloader getQingGuoTimeProxy(String proxyHost, Integer proxyPort) {
        // 代理隧道验证信息
        String proxyUser = "E616003F";
        String proxyPass = "75447DEF685C";

        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy(proxyHost, proxyPort, proxyUser, proxyPass)));
        return httpClientDownloader;
    }

}
