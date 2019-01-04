package com.snow.tiger.ip.proxy.util;

/**
 * @Author: xyc
 * @Date: 2019/1/2 17:55
 * @Version 1.0
 */

import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ConnectTimeoutException;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.selector.Html;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 代理测试类
 */
public class ProxyTestUtil {
    private static final Integer TRY_TIME = 5;


    private static List<String> breakCondition = new LinkedList<>();
    private static List<String> sucCondition = new LinkedList<>();

    static {
        breakCondition.add("503 - Connect failed");
        breakCondition.add("ERROR: The requested URL could not be retrieved");
        breakCondition.add("400");
        breakCondition.add("Server error");
        breakCondition.add("403 Forbidden");
        ////////////////////////////////////////////
        sucCondition.add("Twitter");
        sucCondition.add("百度");
    }

    /**
     * 判定是否符合推出条件
     *
     * @param info
     * @return
     */
    private static boolean isBreak(String info) {
        for (String item : breakCondition) {
            if (info.contains(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param info
     * @return
     */
    private static boolean isSuc(String info) {
        for (String item : sucCondition) {
            if (info.contains(item)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 成功条件
     * @param host
     * @param port
     * @return
     */
    public static Tuple2<Boolean, Integer> testTwProxy(String host, String port) {
        return testProxy(host, port, "http://twitter.com");
    }

    /**
     * @param host
     * @param port
     * @return
     */
    public static Tuple2<Boolean, Integer> testBdProxy(String host, String port) {
        return testProxy(host, port, "https://www.baidu.com/");
    }
    /**
     * @param host
     * @param port
     * @return
     */
    public static Tuple2<Boolean, Integer> testProxy(String host, String port, String url) {
        HttpClientDownloader proxyDownload = getProxyDownload(host, port);
        int hadlerTime = 0;
        long speed = 0;
        while (hadlerTime < TRY_TIME) {
            long start = System.currentTimeMillis();
            Page page = proxyDownload.download(new Request(url), Site.me().setCharset("utf-8").toTask());
            long end = System.currentTimeMillis();
            speed = (end - start) / 1000;
            if (page.isDownloadSuccess()) {
                Html html = page.getHtml();
                String rawText = html.xpath("//title/text()").get();
                System.err.println(".............................................." + rawText);
                if (!StringUtils.isBlank(rawText) && isBreak(rawText)) {
                    return new Tuple2<>(false, (int) speed);
                } else if (StringUtils.isNotBlank(rawText) && isSuc(rawText)) {
                    return new Tuple2<>(true, (int) speed);
                }
                rawText = html.xpath("//body/text()").get();
                if (rawText.contains("Maximum number")) {
                    hadlerTime++;
                    continue;
                }
                int length = page.getRawText().length();
                if (length >= 10000) {
                    hadlerTime++;
                    continue;
                }
                break;
            } else {
                // 请求超时返回
                if (page.getException() instanceof ConnectTimeoutException) {
                    return new Tuple2<>(false, (int) speed);
                }
                if (page.getException() instanceof SocketTimeoutException) {
                    return new Tuple2<>(false, (int) speed);
                }
                if (page.getException() instanceof SocketException) {
                    return new Tuple2<>(false, (int) speed);
                }
                hadlerTime++;
                continue;
            }
        }
        return new Tuple2<>(false, (int) speed);
    }


    public static void main(String[] args) {
        testProxy("1.32.57.157", "58556", "http://twitter.com");
    }


    public static HttpClientDownloader getProxyDownload(String host, String port) {
        HttpClientDownloader clientDownloader = new HttpClientDownloader();
        Proxy proxy = new Proxy(host, Integer.valueOf(port));
        List<Proxy> list = new ArrayList<>();
        list.add(proxy);
        SimpleProxyProvider provider = new SimpleProxyProvider(list);
        clientDownloader.setProxyProvider(provider);
        return clientDownloader;
    }
}
