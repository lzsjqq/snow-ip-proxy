package com.snow.tiger.ip.proxy.util;

import com.snow.tiger.ip.proxy.bean.FreeProxyBean;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author: xyc
 * @Date: 2019/1/4 17:30
 * @Version 1.0
 */
public class CrtProxyBeanUtil {

    public static List<FreeProxyBean> getProxyBean(Page page, List<Selectable> hosts, List<Selectable> ports, List<Selectable> anonymitys) {
        List<FreeProxyBean> proxys = new LinkedList<>();
        for (int i = 0; i < hosts.size(); i++) {
            String host = hosts.get(i).get();
            String port = ports.get(i).get();
            String anonymity = anonymitys.get(i).get();
            boolean bd = false;
            boolean tw;
            Integer speed;
            Tuple2<Boolean, Integer> twProxy = ProxyTestUtil.testTwProxy(host, port);
            tw = twProxy._1();
            speed = twProxy._2();
            if (!tw) {
                Tuple2<Boolean, Integer> bdProxy = ProxyTestUtil.testBdProxy(host, port);
                bd = bdProxy._1();
                speed = bdProxy._2();
            }
            if (tw || bd) {
                FreeProxyBean proxyBean = new FreeProxyBean();
                proxyBean.setHost(host);
                proxyBean.setSource(page.getRequest().getUrl());
                if (anonymity.contains("高匿")) {
                    proxyBean.setAnonymity(1);
                } else if (anonymity.contains("透明")) {
                    proxyBean.setAnonymity(3);
                } else {
                    proxyBean.setAnonymity(2);
                }
                proxyBean.setSpeed(speed);
                proxyBean.setPort(Integer.valueOf(port));
                proxyBean.setType(1);
                if (tw) {
                    proxyBean.setForeign(1);
                } else if (bd) {
                    proxyBean.setForeign(2);
                }
                proxys.add(proxyBean);
            }
        }
        return proxys;
    }
}
