package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.DisplayItem;

import java.util.List;

@Mapper
public interface DisplayItemMapper {

    @Select("select * from displayitem where activityID = #{activityID} order by displaySequence")
    List<DisplayItem> getAllDisplayItem(Integer activityID);

    @Insert("insert into displayitem (activityID, name, source, displaySequence, fullScore) values (#{activityID}, #{name}, #{source}, #{displaySequence}, #{fullScore})")
    int insert(DisplayItem displayItems);

    @Update("update displayitem set activityID = #{activityID},name = #{name}, source = #{source}, displaySequence = #{displaySequence}, fullScore = #{fullScore} where ID = #{ID}")
    int update(DisplayItem displayItems);

    @Delete("delete from displayitem where ID = #{ID}")
    int delete(Integer id);

    @Select("select name from displayitem where ID = #{ID}")
    String getNameByID(Integer id);
}
