package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.sys.rate.model.Production;

/**
 * ClassName: productionMapper
 * Package: org.sys.rate.mapper
 * Description:
 *
 * @Author ZYK
 * @Create 2023/9/11 18:15
 * @Version 1.0
 */
@Mapper
public interface productionMapper {

    // ${}是拼接符。#{}将传入的数据都当成一个字符串，会对自动传入的数据加一个双引号
    @Select("select id, name, student_id, date, author, state, url, `rank`, total, point from ${table} where id = #{productionId}")
    Production checkProductionById(@Param("table") String table, @Param("productionId") int productionId);

    @Update("update ${table} set state = #{state} where id = #{productionId}")
    int editState(@Param("table") String table, @Param("productionId") int productionId, @Param("state") String state);

    @Select("select id, name, student_id, start_date, end_date, author, state, url, `rank`, total, point from i_project where id = #{productionId}")
    Production checkProjectById(int productionId);
}
