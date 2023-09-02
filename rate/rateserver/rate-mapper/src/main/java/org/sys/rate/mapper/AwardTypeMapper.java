package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.Award;
import org.sys.rate.model.AwardType;
import org.sys.rate.model.ProjectType;

import java.util.List;

/**
 * 专利成果Mapper接口
 *
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface AwardTypeMapper {
    public List<AwardType> getIndicatorByYearAndType(String year, String type);

    @Select("select a.id, a.name, a.year, i.rankN from i_award_type a, indicator i where a.indicator_id = #{indicatorId} and year = #{year} and a.indicator_id = i.id")
    List<AwardType> selectAwardTypeListByYear(Integer indicatorId, Integer year);

    @Insert("insert ignore into i_award_type (name, indicator_id, year) values(#{name}, #{indicatorId}, #{year}) ")
    Integer addAwardType(AwardType awardType);

    @Update("update i_award_type set name=#{name}, year = #{year} where id = #{id}")
    void editAwardType(AwardType awardType);

    @Update("update indicator set rankN = #{rankN} where id = #{indicatorId}")
    void editIndicatorAwardRankN(AwardType awardType);

    int deleteByYearIndicatorID(Integer year,Integer indicatorID);

    @Delete("delete from i_award_type where id = #{id}")
    int deleteById(Integer id);

    @Select("SELECT a.id awardTypeId, a.`name` awardTypeName, a.year, CONCAT(i.`order`, i.`name`) indicatorName, i.score FROM i_award_type a, indicator i WHERE a.`name` = #{fullName} AND i.id = a.indicator_id")
    List<Award> getAwardByName(String fullName);
}

