package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.Indicator;

import java.util.List;

@Mapper
public interface IndicatorMapper{
    /**
     * 获取indicator的分数
     * @param id:
     * @Return Integer
     */
    @Select("SELECT score FROM indicator WHERE id = #{id}")
    Integer getScore(Integer id);

    @Select("select * from indicator")
    public List<Indicator> getAll();

    @Select("select `order` from indicator where id = #{id}")
    public String selectOrder(Integer id);

    @Insert("insert into indicator values(#{id},#{name},#{type},#{order},#{score},#{father})")
    public int save(Indicator indicator);

    //需要获取被删除记录的order,并调整其他记录的order（难点）,通过sql语句来实现有些困难，因此采用java来进行逻辑判断并Update
    @Delete("delete from indicator where id = #{id}")
    public int delete(Integer id);

    @Update("update indicator set name = #{name} where id = #{id}")
    public int update(Indicator indicator);

    @Update("update indicator set `order` = #{order} where id = #{id}")
    public int updateOrder(Indicator indicator);

    @Update("update indicator set type = #{type} where id = #{id}")
    public int updateType(Indicator indicator);

    @Update("update indicator set name = #{name}, score = #{score} where id = #{id}")
    public int updateScoreName(Indicator indicator);

    @Update("update indicator set type = #{type},`order` = #{order}, score = #{score}, father = #{father} where id = #{id}")
    public int updateAllField(Indicator indicator);

    @Select("select score from indicator where id = #{id}")
    public Integer selectScoreById(Integer id);

    @Select("SELECT MAX(year) FROM indicator i, indicator_publication ip\n" +
            "WHERE i.id = ip.indicator_id AND `year` <= #{year} AND i.id = #{id}")
    public Integer getMaxYear(Integer id, Integer year);

    @Select("select id from indicator i  where i.order =#{order} limit 1")
    Integer getIndicatorId(String order);

    @Select("select * from indicator i where id = #{id}")
    Indicator getIndicatorById(Integer id);
}
