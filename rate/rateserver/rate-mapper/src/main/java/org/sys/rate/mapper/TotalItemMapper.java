package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sys.rate.model.TotalItem;

import java.util.List;

@Mapper
public interface TotalItemMapper {

    @Select("select * from totalitem where activityID=#{activityID}  order by displaySequence")
    List<TotalItem> findbyActivityID(Integer activityID);

    @Select("select count(*) from totalitem where activityID=#{activityID}")
    Long getTotal(Integer activityID);

    @Select("select name from totalitem where ID=#{ID}")
    String getNameByID(Integer ID);
}
