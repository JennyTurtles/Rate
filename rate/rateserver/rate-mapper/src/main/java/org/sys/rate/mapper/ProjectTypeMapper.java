package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.Project;
import org.sys.rate.model.ProjectType;

import java.util.List;

/**
 * 专利成果Mapper接口
 *
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface ProjectTypeMapper {
    public List<ProjectType> getIndicatorByYearAndType(String year, String type);

    @Select("SELECT id, name, indicator_id, year FROM i_project_type where indicator_id = #{indicatorID} and year = #{year}")
    List<ProjectType> selectProjectTypeListByYear(Integer indicatorID, Integer year);

    @Insert("insert into i_project_type (name, indicator_id, year) VALUES (#{name}, #{indicatorId}, #{year})")
    Integer addProjectType(ProjectType projectType);

    @Update("update i_project_type set name = #{name}, year = #{year} where id = #{id}")
    Integer editProjectType(ProjectType projectType);

    @Delete("delete from i_project_type where id = #{id}")
    Integer deleteById(Integer id);
}

