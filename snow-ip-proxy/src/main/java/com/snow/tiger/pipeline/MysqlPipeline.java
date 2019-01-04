package com.snow.tiger.pipeline;

import com.alibaba.fastjson.JSONObject;
import com.snow.tiger.ip.proxy.bean.FreeProxyBean;
import com.snow.tiger.ip.proxy.mapper.FreeProxyMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * @Author: xyc
 * @Date: 2019/1/2 20:02
 * @Version 1.0
 */
@Service
public class MysqlPipeline implements Pipeline {
    @Autowired
    private FreeProxyMapper ipProxyDao;

    @Override
    public void process(ResultItems resultItems, Task task) {
        try {
            String proxys = resultItems.get("proxy").toString();
            if (StringUtils.isNotBlank(proxys)) {
                List<FreeProxyBean> list = JSONObject.parseArray(proxys, FreeProxyBean.class);
                if(CollectionUtils.isNotEmpty(list)){
                    ipProxyDao.insertList(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
