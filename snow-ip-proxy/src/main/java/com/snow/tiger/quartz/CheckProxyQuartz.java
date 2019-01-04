package com.snow.tiger.quartz;

import com.snow.tiger.ip.proxy.bean.FreeProxyBean;
import com.snow.tiger.ip.proxy.mapper.FreeProxyMapper;
import com.snow.tiger.ip.proxy.util.ProxyTestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: xyc
 * @Date: 2019/1/4 11:13
 * @Version 1.0
 */
@Component
public class CheckProxyQuartz {
    private static final Logger log = LoggerFactory.getLogger(CheckProxyQuartz.class);

    // 初始化线程池
    ExecutorService threadPool = Executors.newFixedThreadPool(3);
    @Autowired
    private FreeProxyMapper proxyDao;

    @Scheduled(cron = "0 1/10 * * * ?")
    public void run() {
        FreeProxyBean bean = new FreeProxyBean();
        bean.setForeign(1);
        try {
            List<FreeProxyBean> list = proxyDao.queryByParam(bean);
            // 线程池
            long taskCount = ((ThreadPoolExecutor) threadPool).getActiveCount();
            // 线程池满不执行任务
            if (taskCount != 0) {
                return;
            }
            for (FreeProxyBean proxy : list) {
                threadPool.execute(() -> {
                    try {
                        boolean b = ProxyTestUtil.testTwProxy(proxy.getHost(), String.valueOf(proxy.getPort()))._1();
                        if (!b) {
                            proxyDao.delete(proxy.getId());
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                });
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
