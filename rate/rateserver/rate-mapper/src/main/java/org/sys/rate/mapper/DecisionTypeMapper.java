package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.DecisionType;

import java.util.List;

/**
 * 专利成果Mapper接口
 *
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface DecisionTypeMapper {
    public List<DecisionType> getIndicatorByYearAndType(String year, String type);

    @Select("select a.id, a.name, a.year, i.rankN from i_decision_type a, indicator i where a.indicator_id = #{indicatorId} and year = #{year} and a.indicator_id = i.id")
    List<DecisionType> selectDecisionTypeListByYear(Integer indicatorId, Integer year);

    @Insert("insert into i_decision_type (name, indicator_id, year) values(#{name}, #{indicatorId}, #{year}) ")
    void addDecisionType(DecisionType decisionType);

    @Update("update i_decision_type set name=#{name},year = #{year} where id = #{id}")
    void editDecisionType(DecisionType decisionType);

    @Update("update indicator set rankN = #{rankN} where id = #{indicatorId}")
    void editIndicatorDecisionRankN(DecisionType decisionType);

    int deleteByYearIndicatorID(Integer year,Integer indicatorID);

    @Delete("delete from i_decision_type where id = #{id}")
    int deleteById(Integer id);

}

