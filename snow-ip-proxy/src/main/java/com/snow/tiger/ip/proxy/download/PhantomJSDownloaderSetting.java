package com.snow.tiger.ip.proxy.download;

import com.snow.tiger.ip.proxy.proxy.IpObject;

import java.util.List;

/**
 * Created by wcting on 2017/7/6.
 */
public class PhantomJSDownloaderSetting {
    /**
     * 是下一级需要获得的子框架的selector列表（iframe可以有多个层级，目前支持获取第一层的），用"|"作为分隔符。
     */
    private String subFrame = "";
    /**
     * 代表出现了该字符串便抛出异常，即抓取失败
     */

    private String errorStr = "";
    /**
     * 是forceErrorStr的selector。
     */

    private String forceErrorLocation = "";

    /**
     * 代表返回结果是否包含主框架内容
     */
    private boolean mainFrame = true;
    /**
     * 是否开启代理
     */
    private boolean isOpenProxy = false;

    /**
     * 代理候选列表
     */
    private List<IpObject> proxyList = null;


    public PhantomJSDownloaderSetting() {
    }

    public PhantomJSDownloaderSetting(PhantomJSDownloaderSetting setting) {
        setOpenProxy(setting.isOpenProxy());
        setMainFrame(setting.isMainFrame());
        setForceErrorLocation(setting.getForceErrorLocation());
        setErrorStr(setting.getErrorStr());
        setSubFrame(setting.getSubFrame());
        setProxyList(setting.getProxyList());
    }


    public boolean isOpenProxy() {
        return isOpenProxy;
    }

    public List<IpObject> getProxyList() {
        return proxyList;
    }

    public void setProxyList(List<IpObject> proxyList) {
        this.proxyList = proxyList;
    }

    public void setOpenProxy(boolean openProxy) {
        isOpenProxy = openProxy;
    }

    public boolean isMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(boolean mainFrame) {
        this.mainFrame = mainFrame;
    }

    public String getForceErrorLocation() {
        return forceErrorLocation;
    }

    public void setForceErrorLocation(String forceErrorLocation) {
        this.forceErrorLocation = forceErrorLocation;
    }

    public String getErrorStr() {
        return errorStr;
    }

    public void setErrorStr(String errorStr) {
        this.errorStr = errorStr;
    }

    public String getSubFrame() {
        return subFrame;
    }

    public void setSubFrame(String subFrame) {
        this.subFrame = subFrame;
    }

}
