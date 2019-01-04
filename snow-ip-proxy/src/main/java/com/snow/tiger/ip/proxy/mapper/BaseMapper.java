package com.snow.tiger.ip.proxy.mapper;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;


public interface BaseMapper<T> {


    /**
     * insert a object to database
     *
     * @param obj
     * @return
     * @throws Exception
     */
    int insert(T obj) throws Exception;

    /**
     * update a row
     *
     * @param obj
     * @return
     * @throws Exception
     */
    int update(T obj) throws Exception;

    /**
     * select a row from database
     *
     * @param id
     * @return
     * @throws Exception
     */
    T selectById(Serializable id) throws Exception;

    /**
     * delete a row
     *
     * @param id
     * @throws Exception
     */
    void delete(@Param("id") Serializable id) throws Exception;

    /**
     * query by param
     *
     * @param param
     * @return
     * @throws Exception
     */
    List<T> queryByParam(@Param("obj") T param, @Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("size") Integer size, @Param("offset") Integer offset, @Param("state") String state) throws Exception;


    List<T> queryByParam(@Param("obj") T param) throws Exception;

    List<T> queryLimitByParam(@Param("obj") T param, @Param("limit") Integer limit) throws Exception;

    /**
     * count by param
     *
     * @param param
     * @return
     * @throws Exception
     */
    Long countByParam(@Param("obj") T param, @Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("state") String state) throws Exception;

}
