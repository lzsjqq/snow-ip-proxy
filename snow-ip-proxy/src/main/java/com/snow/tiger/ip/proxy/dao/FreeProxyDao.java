package com.snow.tiger.ip.proxy.dao;


import com.snow.tiger.ip.proxy.bean.FreeProxyBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FreeProxyDao extends BaseDao<FreeProxyBean> {
    /**
     * 批量更新
     *
     * @param list
     * @return
     */
    int insertList(@Param("list") List<FreeProxyBean> list);
}
