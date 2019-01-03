package com.snow.tiger.ip.proxy.download;

import com.snow.tiger.ip.proxy.util.SeleniumUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;
import us.codecraft.webmagic.selector.PlainText;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by wcting on 2017/7/6.
 */
public class PhantomJSDownloader extends AbstractDownloader {
    private static final Logger LOG = LoggerFactory.getLogger(PhantomJSDownloader.class);
    private PhantomJSDownloaderSetting setting;
    public PhantomJSDownloader(PhantomJSDownloaderSetting setting) {
        this.setting = setting;
    }


    public PhantomJSDownloaderSetting getSetting() {
        return setting;
    }

    public void setSetting(PhantomJSDownloaderSetting setting) {
        this.setting = setting;
    }

    public static void main(String[] args) {
        PhantomJSDownloader downloader = new PhantomJSDownloader(new PhantomJSDownloaderSetting());
//        IProxyManager manager = ProxyManagerFactory.newInstance(RhinoFrameworkConfig.getInstance());
//        List<IpObject> ips = manager.getIpService().getIps(3, "rhino");
//        downloader.getSetting().setOpenProxy(true);
//        downloader.getSetting().setProxyList(ips);
        Request request = new Request("http://www.66ip.cn/");

        Page download = downloader.download(request, null);
//        System.out.println(phantomJSDownloader._2());
    }


    protected Page handleResponse(Request request, String pageSource) throws IOException {
        Page page = new Page();
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        page.setStatusCode(200);
        String charset = "UTF-8";
        page.setCharset(charset);
        page.setRawText(new String(pageSource.getBytes(), charset));
        page.setDownloadSuccess(true);
//      page.setHeaders(HttpClientUtils.convertHeaders(httpResponse.getAllHeaders()));
        return page;
    }

    @Override
    public Page download(Request request, Task task) {
        WebDriver driver = null;
        Page page = Page.fail();
        String url = request.getUrl();
        try {
            //是否开启代理
            driver = SeleniumUtil.getPhantomJSDriver(setting);
            Object pid = ((PhantomJSDriver) driver).executePhantomJS("return require('system').pid");
            LOG.info("start phantomjs driver, pid: " + pid);
            driver.manage().window().maximize();
            driver.get(url);
            //获取要抓取的元素,并设置等待时间,超出抛异常
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            //设置设置线程休眠时间等待页面加载完成
            Thread.sleep(1000L);
            String pageSource = driver.getPageSource();
            Document sourceDoc = Jsoup.parse(pageSource);
            Document resultDoc = sourceDoc;
            String subFrame = setting.getSubFrame();
            //需要渲染子窗口
            if (!StringUtils.isBlank(subFrame)) {
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

                    String subFrameHtml = pageSource;
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
            return handleResponse(request, resultDoc.toString());
        } catch (Exception e) {
            LOG.error(String.format("crawler fail , url: %s , msg:%s", url, e.getMessage()), e);
            return page;
        } finally {
            if (driver != null) {
                driver.close();
                driver.quit();
            }
        }
    }

    @Override
    public void setThread(int threadNum) {
    }
}
