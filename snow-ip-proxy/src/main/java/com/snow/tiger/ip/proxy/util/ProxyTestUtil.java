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

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * 代理测试类
 */
public class ProxyTestUtil {
    private static final Integer TRY_TIME = 5;

    /**
     * @param host
     * @param port
     * @return
     */
    public static boolean testTwProxy(String host, String port) {
        HttpClientDownloader proxyDownload = getProxyDownload(host, port);
        int hadlerTime = 0;
        while (hadlerTime < TRY_TIME) {
            try {
                proxyDownload.download("http://twitter.com");
                return true;
            } catch (Exception E) {
                hadlerTime++;
            }
        }
        return false;
    }

    /**
     * @param host
     * @param port
     * @return
     */
    public static boolean testProxy(String host, String port, String url) {
        HttpClientDownloader proxyDownload = getProxyDownload(host, port);
        int hadlerTime = 0;
        while (hadlerTime < TRY_TIME) {
            Page page = proxyDownload.download(new Request(url), Site.me().setCharset("utf-8").toTask());
            if (page.isDownloadSuccess()) {
                String rawText = page.getRawText();
                if (!StringUtils.isBlank(rawText) && (rawText.contains("Maximum number")
                        || rawText.contains("503 - Connect failed")
                        || rawText.contains("The requested URL could not be retrieved"))) {
                    hadlerTime++;
                    continue;
                }
                return true;
            } else {
                // 请求超时返回
                if (page.getException() instanceof ConnectTimeoutException) {
                    break;
                }
                if (page.getException() instanceof SocketTimeoutException) {
                    break;
                }
                hadlerTime++;
                continue;
            }
        }
        return false;
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
