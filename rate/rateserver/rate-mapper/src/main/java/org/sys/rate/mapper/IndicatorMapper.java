package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.Indicator;

import java.util.List;

@Mapper
public interface IndicatorMapper{
    @Select("select * from indicator")
    public List<Indicator> getAll();

    @Select("select `order` from indicator where ID = #{id}")
    public String selectOrder(Integer id);

    @Insert("insert into indicator values(#{ID},#{name},#{type},#{order},#{score},#{father})")
    public int save(Indicator indicator);

    //需要获取被删除记录的order,并调整其他记录的order（难点）,通过sql语句来实现有些困难，因此采用java来进行逻辑判断并Update
    @Delete("delete from indicator where ID = #{id}")
    public int delete(Integer id);

    @Update("update indicator set name = #{name} where ID = #{ID}")
    public int update(Indicator indicator);

    @Update("update indicator set `order` = #{order} where ID = #{ID}")
    public int updateOrder(Indicator indicator);

    @Update("update indicator set type = #{type} where ID = #{ID}")
    public int updateType(Indicator indicator);

    @Update("update indicator set name = #{name}, score = #{score} where ID = #{ID}")
    public int updateScoreName(Indicator indicator);

    @Update("update indicator set type = #{type},`order` = #{order}, score = #{score}, father = #{father} where ID = #{ID}")
    public int updateAllField(Indicator indicator);

    @Select("select score from indicator where ID = #{id}")
    public Integer selectScoreById(Integer id);
}
