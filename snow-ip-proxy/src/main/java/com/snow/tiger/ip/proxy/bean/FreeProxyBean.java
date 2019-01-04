package com.snow.tiger.ip.proxy.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: xyc
 * @Date: 2018/12/29 15:28
 * @Version 1.0
 */
public class FreeProxyBean implements Serializable {
    private Integer id;
    private String host;// ip
    private Integer port;// 端口
    //1:高匿 2:匿名 3:透明
    private Integer anonymity;// 是否高匿
    // 1 yes 2 no
    private Integer type;// 是否为https
    private Integer speed;// 访问速度
    private String source;// 来源
    private Integer foreign;// 是否可翻墙1：可   2：不可
    private Integer failTime;// 失败次数
    private Integer sucTime;// 成功次数
    private Date creatTime;
    private Date updateTime;

    public Integer getFailTime() {
        return failTime;
    }

    public void setFailTime(Integer failTime) {
        this.failTime = failTime;
    }

    public Integer getSucTime() {
        return sucTime;
    }

    public void setSucTime(Integer sucTime) {
        this.sucTime = sucTime;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

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

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getForeign() {
        return foreign;
    }

    public void setForeign(Integer foreign) {
        this.foreign = foreign;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
