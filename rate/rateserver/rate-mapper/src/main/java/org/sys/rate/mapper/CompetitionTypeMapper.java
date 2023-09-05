package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.*;

import java.util.List;

/**
 * 专利成果Mapper接口
 *
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface CompetitionTypeMapper {
    @Select("SELECT * FROM i_competition_type where year = (select max(year) from i_competition_type where  year <= #{year})")
    List<CompetitionType> getIndicatorByYearAndType(String year);

    @Select("select a.id, a.name, a.year, i.rankN from i_competition_type a, indicator i where a.indicator_id = #{indicatorId} and year = #{year} and a.indicator_id = i.id")
    List<CompetitionType> selectCompetitionTypeListByYear(Integer indicatorId, Integer year);

    @Insert("insert ignore into i_competition_type (name, year) values(#{name}, #{year}) ")
    Integer addCompetitionType(CompetitionType competitionType);

    @Delete("delete from i_competition_type where id = #{id}")
    void deleteById(Integer id);

    @Update("update i_competition_type set name=#{name},year = #{year} where id = #{id}")
    void editCompetitionType(CompetitionType competitionType);

    @Select("SELECT a.id competitionTypeId, a.`name` competitionTypeName, a.year, CONCAT(i.`order`, i.`name`) indicatorName, i.score FROM i_competition_type a, indicator i WHERE a.`name` = #{fullName} AND i.id = a.indicator_id")
    List<Competition> getCompetitionByName(String fullName);

    @Select("SELECT * from i_competition_type WHERE year = #{year}")
    List<CompetitionType> getByLevelCompetition(Integer year);
}

