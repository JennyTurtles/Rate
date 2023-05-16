package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sys.rate.model.ProjectsOper;

import java.util.List;


@Mapper
public interface ProjectsOperMapper
{

        public int insertProjectOper(ProjectsOper projectsOper);
        public List<ProjectsOper> selectList(Long ID);

}
