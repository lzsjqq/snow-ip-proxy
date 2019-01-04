package com.snow.tiger.ip.proxy.mapper;


import com.snow.tiger.ip.proxy.bean.FreeProxyBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface FreeProxyMapper extends BaseMapper<FreeProxyBean> {
    /**
     * 批量更新
     *
     * @param list
     * @return
     */
    int insertList(@Param("list") List<FreeProxyBean> list);
}
