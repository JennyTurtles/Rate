package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.DisplayItem;

import java.util.List;

@Mapper
public interface DisplayItemMapper {

    @Select("select * from displayitem where activityID = #{activityID} order by displaySequence")
    List<DisplayItem> getAllDisplayItem(Integer activityID);

    @Insert("insert into displayitem (activityID, name, source, displaySequence, passScore) values (#{activityID}, #{name}, #{source}, #{displaySequence}, #{passScore})")
    int insert(DisplayItem displayItems);

    @Update("update displayitem set activityID = #{activityID},name = #{name}, source = #{source}, displaySequence = #{displaySequence}, passScore = #{passScore} where ID = #{ID}")
    int update(DisplayItem displayItems);

    @Delete("delete from displayitem where ID = #{ID}")
    int delete(Integer id);

    @Select("select count(*) from displayitem where activityID = #{activityID}")
    Long getAll(Integer activityID);

    @Select("select name from displayitem where ID = #{ID}")
    String getNameByID(Integer id);

    @Select("select * from displayitem where ID = #{ID}")
    DisplayItem getDisplayItemByID(Integer id);

    @Update("update displayitem set displaySequence=displaySequence-1 " +
            "where activityID=#{activityID} and displaySequence > #{small}  and ID!='0' " +
            "and displaySequence <= #{big}")
    Integer subDisplaySequence(@Param("activityID") Integer activityID, @Param("small") Integer small,@Param("big") Integer big);

    @Update("UPDATE displayitem set displaySequence=#{newP} where activityID=#{activityID} and ID=#{ID}")
    Integer saveDisplaySequence(@Param("activityID") Integer activityID, @Param("newP") Integer newP,@Param("ID") Integer ID);

    @Update("UPDATE displayitem set displaySequence=displaySequence+1 " +
            "where activityID=#{activityID} and displaySequence < #{big} and displaySequence >= #{small} and ID!='0'")
    Integer addDisplaySequence(@Param("activityID") Integer activityID, @Param("small") Integer small,@Param("big") Integer big);
}
