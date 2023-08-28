package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.CompetitionType;
import org.sys.rate.model.DecisionType;
import org.sys.rate.model.ProjectType;

import java.util.List;

/**
 * 专利成果Mapper接口
 *
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface CompetitionTypeMapper {
    @Select("SELECT p.* FROM i_competition_type p where p.name like concat('%', #{type}, '%') and p.year = #{year}")
    List<CompetitionType> getIndicatorByYearAndType(String year, String type);

    @Select("select a.id, a.name, a.year, i.rankN from i_competition_type a, indicator i where a.indicator_id = #{indicatorId} and year = #{year} and a.indicator_id = i.id")
    List<CompetitionType> selectCompetitionTypeListByYear(Integer indicatorId, Integer year);

    @Insert("insert into i_competition_type (name, indicator_id, year) values(#{name}, #{indicatorId}, #{year}) ")
    void addCompetitionType(CompetitionType competitionType);

    @Delete("delete from i_competition_type where id = #{id}")
    void deleteById(Integer id);
}

