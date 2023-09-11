package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;
import org.sys.rate.model.Indicator;
import org.sys.rate.model.Project;

import java.util.ArrayList;
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

    @Select("select * from indicator where type=#{type} or type is null")
    public List<Indicator> getAllByType(String type);

    @Select("select `order` from indicator where id = #{id}")
    public String selectOrder(Integer id);

    @Insert("insert into indicator(name, type, `order`, score, father_id, rankN,level) values(#{name},#{type},#{order},#{score},#{father},#{rankN},#{level})")
    public int save(Indicator indicator);

    //需要获取被删除记录的order,并调整其他记录的order（难点）,通过sql语句来实现有些困难，因此采用java来进行逻辑判断并Update
    @Delete("delete from indicator where id = #{id}")
    public int delete(Integer id);

    @Update("update indicator set name = #{name}, rankN = #{rankN}, level = #{level} where id = #{id}")
    public int update(Indicator indicator);

    @Update("update indicator set `order` = #{order} where id = #{id}")
    public int updateOrder(Indicator indicator);

    @Update("update indicator set type = #{type} where id = #{id}")
    public int updateType(Indicator indicator);

    @Update("update indicator set name = #{name}, score = #{score} where id = #{id}")
    public int updateScoreName(Indicator indicator);

    @Update("update indicator set type = #{type},`order` = #{order}, score = #{score}, father_id = #{father} where id = #{id}")
    public int updateAllField(Indicator indicator);

    @Select("select score from indicator where id = #{id}")
    public Integer selectScoreById(Integer id);

    @Select("SELECT MAX(year) FROM indicator i, indicator_publication ip\n" +
            "WHERE i.id = ip.indicator_id AND `year` <= #{year} AND i.id = #{id}")
    public Integer getMaxYear(Integer id, Integer year);

    @Select("select id from indicator i  where i.order =#{order} limit 1")
    Integer getIndicatorId(String order);

    @Select("select id from indicator where name =#{name}")
    Integer getIdByName(String name);

    @Select("select * from indicator i where id = #{id}")
    Indicator getIndicatorById(Integer id);


    ArrayList<Integer> getAllYearByIdType(@Param("indicatorId") Integer indicatorId, @Param("indicatorType") String indicatorType);

    @Insert("INSERT INTO indicator_publication (indicator_id, publication_id, YEAR) " +
            "SELECT indicator_id, publication_id, #{toYear} FROM indicator_publication " +
            "WHERE indicator_id = #{indicatorId} AND YEAR = #{fromYear} AND publication_id NOT IN " +
            "(SELECT publication_id FROM indicator_publication WHERE indicator_id = #{indicatorId} AND YEAR = #{toYear})")
    Integer clonePublication(Integer fromYear, Integer toYear, Long indicatorId);
    @Insert("INSERT INTO i_project_type (indicator_id, `name`, `year`) " +
            "SELECT indicator_id, `name`, #{toYear} " +
            "FROM i_project_type WHERE indicator_id = #{indicatorId} " +
            "AND `year` = #{fromYear} AND `name` " +
            "NOT IN (SELECT `name` FROM i_project_type WHERE indicator_id = #{indicatorId} AND `year` = #{toYear})")
    Integer cloneProject(Integer fromYear, Integer toYear, Long indicatorId);


    List<String> getProductNamesByTypeName(String indicatorType, String name);

    @Select("SELECT pt.id projectTypeId, pt.`name` projectTypeName, pt.year, CONCAT(i.`order`, i.`name`) indicatorName, i.score FROM i_project_type pt, indicator i WHERE pt.`name` = #{fullName} AND i.id = pt.indicator_id")
    List<Project> getProjectByName(String fullName);


//    @Insert("INSERT INTO i_award_type (indicator_id, `name`, `year`) " +
//            "SELECT indicator_id, `name`, #{toYear} " +
//            "FROM i_award_type WHERE indicator_id = #{indicatorId} " +
//            "AND `year` = #{fromYear} AND `name` " +
//            "NOT IN (SELECT `name` FROM i_award_type WHERE indicator_id = #{indicatorId} AND `year` = #{toYear})")
//    void cloneAward(Integer fromYear, Integer toYear, Long indicatorId);

    @Insert("INSERT INTO i_award_type (level, `name`, `year`) " +
            "SELECT level, `name`, #{toYear} " +
            "FROM i_award_type WHERE level = #{level} " +
            "AND `year` = #{fromYear} AND `name` " +
            "NOT IN (SELECT `name` FROM i_award_type WHERE level = #{level} AND `year` = #{toYear})")
    void cloneAward(Integer fromYear, Integer toYear, String level);

    @Insert("INSERT INTO i_decision_type (indicator_id, `name`, `year`) " +
            "SELECT indicator_id, `name`, #{toYear} " +
            "FROM i_decision_type WHERE indicator_id = #{indicatorId} " +
            "AND `year` = #{fromYear} AND `name` " +
            "NOT IN (SELECT `name` FROM i_decision_type WHERE indicator_id = #{indicatorId} AND `year` = #{toYear})")
    void cloneDecision(Integer fromYear, Integer toYear, Long indicatorId);

//    @Insert("INSERT INTO i_competition_type (indicator_id, `name`, `year`) " +
//            "SELECT indicator_id, `name`, #{toYear} " +
//            "FROM i_competition_type WHERE indicator_id = #{indicatorId} " +
//            "AND `year` = #{fromYear} AND `name` " +
//            "NOT IN (SELECT `name` FROM i_competition_type WHERE indicator_id = #{indicatorId} AND `year` = #{toYear})")
//    void cloneCompetition(Integer fromYear, Integer toYear, Long indicatorId);

    @Insert("INSERT INTO i_competition_type ( `name`, `year`) " +
            "SELECT `name`, #{toYear} " +
            "FROM i_competition_type WHERE `year` = #{fromYear} AND `name` " +
            "NOT IN (SELECT `name` FROM i_competition_type WHERE `year` = #{toYear})")
    void cloneCompetition(Integer fromYear, Integer toYear);

    @Select("SELECT DISTINCT `year` FROM i_award_type WHERE `level` = #{level} ORDER BY `year` DESC")
    List<Integer> getAllYearForAward(String level);

    @Select("SELECT DISTINCT `year` FROM i_competition_type ORDER BY `year` DESC")
    List<Integer> getAllYearForCompetition();
}
