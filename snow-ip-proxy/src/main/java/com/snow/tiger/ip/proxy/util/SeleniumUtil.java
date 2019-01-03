package com.snow.tiger.ip.proxy.util;


import com.snow.tiger.ip.proxy.download.PhantomJSDownloaderSetting;
import com.snow.tiger.ip.proxy.proxy.IpObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Selenium的工具方法
 * Created by chenheyou on 2017/4/20.
 */
public class SeleniumUtil {
    public static final Logger LOG = LoggerFactory.getLogger(SeleniumUtil.class);
    private final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
    @Deprecated
    public static WebDriver getPhantomJSDriver(boolean isProxy) {
        PhantomJSDownloaderSetting setting = new PhantomJSDownloaderSetting();
        setting.setOpenProxy(isProxy);
        return getPhantomJSDriver(setting);
    }

    public static WebDriver getPhantomJSDriver(PhantomJSDownloaderSetting setting) {
        return getPhantomJSDriver(setting, null);
    }

    @Deprecated
    public static WebDriver getPhantomJSDriver(boolean isProxy, Map<String, String> properties) {
        PhantomJSDownloaderSetting setting = new PhantomJSDownloaderSetting();
        setting.setOpenProxy(isProxy);
        return getPhantomJSDriver(setting, properties);
    }

    public static WebDriver getPhantomJSDriver(PhantomJSDownloaderSetting setting, Map<String, String> properties) {
        WebDriver driver = null;
        try {
            LOG.info("start init PhantomJSDriver");
            DesiredCapabilities cc = DesiredCapabilities.phantomjs();
            cc.setCapability("phantomjs.page.settings.loadImages", "false");
            cc.setCapability("phantomjs.page.settings.resourceTimeout", "15000");
            cc.setCapability("phantomjs.page.settings.javascriptCanOpenWindows", "false");
            cc.setCapability("takesScreenshot", "false");
            cc.setCapability("phantomjs.page.settings.userAgent", UserAgentUtil.getUserAgent());
            if (properties != null) {
                for (Map.Entry<String, String> kv : properties.entrySet()) {
                    cc.setCapability(kv.getKey(), kv.getValue());
                }
            }
            //使用代理
            if (setting.isOpenProxy()) {
                cc.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCapabilities(setting));
            }
            driver = new PhantomJSDriver(cc);
            driver.manage().window().maximize();
            driver.manage().timeouts().pageLoadTimeout(60L, TimeUnit.SECONDS);
            LOG.info("init PhantomJSDriver success");
        } catch (Exception e) {
            LOG.info("init PhantomJSDriver fail");
            LOG.error(e.getMessage(), e);
        }
        return driver;
    }

    /**
     * 设置PhantomJs Driver使用指定代理ip
     *
     * @return
     */
    public static List<String> cliArgsCapabilities(PhantomJSDownloaderSetting setting) {
        List<String> cliArgsCap = new ArrayList<>();
        List<IpObject> ipObject = null;
        //可以自定义proxy manager
        int idx = 0;
        if (setting.isOpenProxy() && setting.getProxyList() != null) {
            ipObject = setting.getProxyList();
            idx = RandomUtils.nextInt(0, setting.getProxyList().size());
        }

        //todo 先强行设置,避免使用URL获取代理
        if (ipObject == null) {
            LOG.warn("get proxy ip fail!");
            return null;
        }
        System.out.println("ipObject:" + ipObject.get(idx).getIpString());
        cliArgsCap.add("--proxy=" + ipObject.get(idx).getIpString());
        cliArgsCap.add("--proxy-type=http");
        return cliArgsCap;
    }

    /**
     * 截图
     *
     * @param drivername PhantomJSDriver
     * @param filename   login.png
     */
    public static void snapshot(TakesScreenshot drivername, String filename) {
        //get current work folder
        String currentPath = System.getProperty("user.dir");
        LOG.info("current path is: " + currentPath);
        File scrFile = drivername.getScreenshotAs(OutputType.FILE);
        // Now you can do whatever you need to do with it, for example copy somewhere
        try {
            LOG.info("save snapshot path is:" + currentPath + "/" + filename);
            FileUtils.copyFile(scrFile, new File(currentPath + "/" + filename));
        } catch (IOException e) {
            LOG.error("Can't save screenshot\n" + e.getMessage(), e);
        }
    }
}
