package com.snow.tiger.quartz;

import com.snow.tiger.ip.proxy.processer.Ip66Processor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: xyc
 * @Date: 2019/1/2 20:38
 * @Version 1.0
 */
@Component
public class FreeProxyCrawlQuartz {


    //    @Scheduled(cron = "0 0/10 * * * ?")
//    @Scheduled(fixedDelay = 1000)
    public void runIp66() {
        String[] args = {};
        Ip66Processor.main(args);
        while (true) {

        }
    }

}
