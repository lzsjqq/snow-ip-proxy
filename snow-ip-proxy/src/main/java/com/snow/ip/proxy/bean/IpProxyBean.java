package com.snow.ip.proxy.bean;

import java.io.Serializable;

/**
 * @Author: xyc
 * @Date: 2018/12/29 15:28
 * @Version 1.0
 */
public class IpProxyBean implements Serializable {
    private String host;// ip
    private Integer port;// 端口
    //1:高匿 2:匿名 3:透明
    private Integer anonymity;// 是否高匿
    // 1 yes 2 no
    private Integer https;// 是否为https
    private Double speed;// 访问速度
    private String source;// 来源

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getAnonymity() {
        return anonymity;
    }

    public void setAnonymity(Integer anonymity) {
        this.anonymity = anonymity;
    }

    public Integer getHttps() {
        return https;
    }

    public void setHttps(Integer https) {
        this.https = https;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
