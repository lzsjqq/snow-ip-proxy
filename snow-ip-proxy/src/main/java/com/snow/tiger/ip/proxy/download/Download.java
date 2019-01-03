package com.snow.tiger.ip.proxy.download;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;

/**
 * @Author: xyc
 * @Date: 2018/12/29 17:21
 * @Version 1.0
 */
public class Download {

    public static void main(String[] args) {


        //设置必要参数
        DesiredCapabilities dcaps = new DesiredCapabilities();
        //ssl证书支持
        dcaps.setCapability("acceptSslCerts", true);
        //截屏支持
        dcaps.setCapability("takesScreenshot", true);
        //css搜索支持
        dcaps.setCapability("cssSelectorsEnabled", true);
        //js支持
        dcaps.setJavascriptEnabled(true);
        //驱动支持
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "D:\\devTool\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        //创建无界面浏览器对象
        PhantomJSDriver driver = new PhantomJSDriver(dcaps);

        try {
            // 让浏览器访问空间主页
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            driver.get("http://www.66ip.cn/");
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            Thread.sleep(1000L);
            Document sourceDoc = Jsoup.parse(driver.getPageSource());


        } catch (Exception e) { //
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally { //关闭并退出浏览器
            driver.close();
            driver.quit();
        }
    }
}

