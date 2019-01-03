package com.snow.tiger.ip.proxy.proxy;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class IpObject {
    private static Logger LOG = LoggerFactory.getLogger(IpObject.class);
    private static final long TIME_SAFA_DETA = 30000;//还有30秒就过期
    private static Gson GSON = new Gson();

    private String host;
    private int port;
    private String location = "unknown";
    private long usefulPeriod = 0;// -1 表示不限期
    private long findTime = 0;
    private String username = "";
    private String password = "";
    private AtomicInteger useCount = new AtomicInteger(0);

    public int getUseCount() {
        return useCount.get();
    }

    public void incrOne() {
        this.useCount.incrementAndGet();
    }

    public boolean isExpired() {
        return usefulPeriod != -1 && (findTime + usefulPeriod - System.currentTimeMillis()) < TIME_SAFA_DETA;
    }

    public IpObject() {
        super();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIpString() {
        return host + ":" + port;
    }

    public long getFindTime() {
        return findTime;
    }

    public void setFindTime(long findTime) {
        this.findTime = findTime;
    }

    public long getUsefulPeriod() {
        return usefulPeriod;
    }

    public void setUsefulPeriod(long usefulPeriod) {
        this.usefulPeriod = usefulPeriod;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String toJson() {
        return GSON.toJson(this);
    }

    public static IpObject parseJson(String json) {
        try {
            return GSON.fromJson(json, IpObject.class);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String toString() {
        return "IpObject{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", location='" + location + '\'' +
                ", usefulPeriod=" + usefulPeriod +
                ", findTime=" + findTime +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", useCount=" + useCount +
                '}';
    }
}
