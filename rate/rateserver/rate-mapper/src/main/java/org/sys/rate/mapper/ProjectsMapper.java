package org.sys.rate.mapper;

import org.sys.rate.model.Award;
import org.sys.rate.model.Projects;

import java.util.List;

/**
 * 项目Mapper接口
 * 
 * @author system
 * @date 2022-03-13
 */
public interface ProjectsMapper 
{
    /**
     * 查询项目
     * 
     * @param ID 项目ID
     * @return 项目
     */
    public Projects selectProjectsById(Long ID);

    /**
     * 查询项目列表
     * 
     * @param projects 项目
     * @return 项目集合
     */
    public List<Projects> selectProjectsList(Projects projects);

    /**
     * 新增项目
     * 
     * @param projects 项目
     * @return 结果
     */
    public int insertProjects(Projects projects);

    /**
     * 修改项目
     * 
     * @param projects 项目
     * @return 结果
     */
    public int updateProjects(Projects projects);

    /**
     * 删除项目
     * 
     * @param ID 项目ID
     * @return 结果
     */
    public int deleteProjectsById(Long ID);

    /**
     * 批量删除项目
     * 
     * @param IDs 需要删除的数据ID
     * @return 结果
     */
    public int deleteProjectsByIds(String[] IDs);

    public List<Projects> selectList();
}
