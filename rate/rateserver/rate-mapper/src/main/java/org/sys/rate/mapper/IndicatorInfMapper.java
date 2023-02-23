package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.*;

import java.util.List;

@Mapper
public interface IndicatorInfMapper {
    //获取最大id
    @Select("select max(ID) from ${formName}")
    public Integer getMaxID(String formName);

    //期刊
    @Select("select * from publication where indicatorID = #{id}")
    public List<IndicatorPublication> getPublication(Integer id);

    @Insert("insert into publication values(#{ID},#{name},#{abbr},#{publisher},#{url},#{level},#{indicatorID},#{year})")
    public int savePublication(IndicatorPublication indicatorPublication);

    @Insert({
            "<script>",
            "insert into publication (name, abbr, publisher,url,level,indicatorID,year) values ",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.name}, #{item.abbr}, #{item.publisher},#{item.url}, #{item.level}, #{item.indicatorID},#{item.year})",
            "</foreach>",
            "</script>"
    })
    public int savePublications(@Param("list") List<IndicatorPublication> publications);

    @Select("select ID from indicator where name = #{name} AND type = '论文'")
    public Integer getIndicatorID(String name);

    @Delete("delete from publication where ID = #{id}")
    public int deletePublication(Integer id);

    @Update("update publication set name = #{name}, abbr = #{abbr}, publisher = #{publisher}, url = #{url}, level = #{level}, year = #{year} where ID = #{ID}")
    public int updatePublicationById(IndicatorPublication indicatorPublication);

    @Select("SELECT * FROM publication \n" +
            "WHERE indicatorID = #{indicatorID} AND `year` = (SELECT MAX(year) FROM publication WHERE indicatorID = #{indicatorID} AND `year` <= #{year})")
    public List<IndicatorPublication> getPublicationByIndicatorAndYear(Integer indicatorID,Integer year);


    @Select("select ID from publication where name = #{name} AND year = #{year}")
    public Integer getIdByNameAndYear(String name, Integer year);

    @Select("select MAX(year) from publication where indicatorID = #{indicatorId}")
    public Integer getMaxYearByIndicatorId(Integer indicatorId);

    //科技奖
    @Select("select * from award where indicatorID = #{id}")
    public List<IndicatorAward> getAward(Integer id);

    @Insert("insert into award values(#{ID},#{name},#{indicatorID})")
    public int saveAward(IndicatorAward indicatorAward);

    @Delete("delete from award where ID = #{id}")
    public int deleteAward (Integer id);

    @Update("update award set name = #{name} where ID = #{ID}")
    public int updateAward(IndicatorAward indicatorAward);

    //纵向科研项目
    @Select("select * from program where indicatorID = #{id}")
    public List<IndicatorProgram> getProgram(Integer id);

    @Insert("insert into program values(#{ID},#{name},#{indicatorID})")
    public int saveProgram(IndicatorProgram indicatorProgram);

    @Delete("delete from program where ID = #{id}")
    public int deleteProgram (Integer id);

    @Update("update program set name = #{name} where ID = #{ID}")
    public int updateProgram(IndicatorProgram indicatorProgram);

    //决策咨询成果
    @Select("select * from decision where indicatorID = #{id}")
    public List<IndicatorDecision> getDecision(Integer id);

    @Insert("insert into decision values(#{ID},#{name},#{indicatorID})")
    public int saveDecision(IndicatorDecision indicatorDecision);

    @Delete("delete from decision where ID = #{id}")
    public int deleteDecision (Integer id);

    @Update("update decision set name = #{name} where ID = #{ID}")
    public int updateDecision(IndicatorDecision indicatorDecision);

    //查询四个表内所有的ID,name,indicatorID
    @Select("select ID,name,indicatorID from publication")
    public List<IndicatorSearchResult> getSearchPublication();

    @Select("select ID,name,indicatorID from award")
    public List<IndicatorSearchResult> getSearchAward();

    @Select("select ID,name,indicatorID from program")
    public List<IndicatorSearchResult> getSearchProgram();

    @Select("select ID,name,indicatorID from decision")
    public List<IndicatorSearchResult> getSearchDecision();

    //查询indicator表内特定ID的信息
    @Select("select * from indicator where ID = #{id}")
    public List<Indicator> getSearchIndicator(Integer id);

    //查询四个表内特定ID的信息
    @Select("select * from publication where ID = #{id}")
    public List<IndicatorPublication> getSearchPublicationById(Integer id);

    @Select("select * from award where ID = #{id}")
    public List<IndicatorAward> getSearchAwardById(Integer id);

    @Select("select * from program where ID = #{id}")
    public List<IndicatorProgram> getSearchProgramById(Integer id);

    @Select("select * from decision where ID = #{id}")
    public List<IndicatorDecision> getSearchDecisionById(Integer id);
}
