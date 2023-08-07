package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.CompetitionType;
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
}

