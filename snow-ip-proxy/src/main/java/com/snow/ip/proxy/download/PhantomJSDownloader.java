package com.snow.ip.proxy.download;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wcting on 2017/7/6.
 */
public class PhantomJSDownloader {
    private static final Logger LOG = LoggerFactory.getLogger(PhantomJSDownloader.class);
    private PhantomJSDownloaderSetting setting;

    public PhantomJSDownloader(PhantomJSDownloaderSetting setting) {
        this.setting = setting;
    }

    public Tuple2<Document, String> getHtml(String url) {
        WebDriver driver = null;
        try {
            //是否开启代理
            driver = SeleniumUtil.getPhantomJSDriver(setting);
            Object pid = ((PhantomJSDriver) driver).executePhantomJS("return require('system').pid");
            LOG.info("start phantomjs driver, pid: " + pid);
            driver.manage().window().maximize();
            driver.get(url);
            Document sourceDoc = Jsoup.parse(driver.getPageSource());
            Document resultDoc = sourceDoc;
            String subFrame = setting.getSubFrame();
            //不需要渲染子窗口
            if (StringUtil.isNullOrEmpty(subFrame)) {
                return new Tuple2<>(resultDoc, driver.getPageSource());
            } else {
                String[] subFrameSelectors = subFrame.split("\\|");
                //是否需要渲染主框架
                boolean mainFrame = setting.isMainFrame();
                if (!mainFrame) {
                    resultDoc = Jsoup.parse("<html><body></body></html>");
                }
                for (String subFrameSelector : subFrameSelectors) {
                    try {
                        driver.switchTo().frame(driver.findElement(By.cssSelector(subFrameSelector.trim())));
                    } catch (Exception e) {
                        LOG.error("phantomJs swichToFrame fail,fail url:" + url);
                        return null;
                    }
                    String subFrameHtml = driver.getPageSource();
                    Element subFrameElement = sourceDoc.select(subFrameSelector).first();
                    subFrameElement.appendChild(Jsoup.parse(subFrameHtml));
                    if (!mainFrame) {
                        //不需要主框架，添加子框架节点对象到resultDoc的body中
                        resultDoc.getElementsByTag("body").first().appendChild(subFrameElement);
                    }
                    //切回主框架
                    driver.switchTo().defaultContent();
                }
            }
            if (!StringUtils.isEmpty(setting.getErrorStr()) && resultDoc.select(setting.getForceErrorLocation()).html().contains(setting.getErrorStr())) {
                return null;
            }
            //拿到html
            return new Tuple2<>(resultDoc, resultDoc.toString());
        } catch (Exception e) {
            LOG.error(String.format("crawler fail , url: %s , msg:%s", url, e.getMessage()), e);
//            throw e;
            return null;
        } finally {
            if (driver != null) {
                driver.close();
                driver.quit();
            }
        }
    }

    public PhantomJSDownloaderSetting getSetting() {
        return setting;
    }

    public void setSetting(PhantomJSDownloaderSetting setting) {
        this.setting = setting;
    }

    public static void main(String[] args) throws IpServiceException {
        PhantomJSDownloader downloader = new PhantomJSDownloader(new PhantomJSDownloaderSetting());
//        IProxyManager manager = ProxyManagerFactory.newInstance(RhinoFrameworkConfig.getInstance());
//        List<IpObject> ips = manager.getIpService().getIps(3, "rhino");
//        downloader.getSetting().setOpenProxy(true);
//        downloader.getSetting().setProxyList(ips);
        Tuple2<Document, String> phantomJSDownloader = downloader.getHtml("http://www.51tv.com/#/movie");
        System.out.println(phantomJSDownloader.toString());
//        System.out.println(phantomJSDownloader._2());
    }
}
