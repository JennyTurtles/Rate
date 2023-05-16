package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.sys.rate.model.Award;
import org.sys.rate.model.AwardOper;
import org.sys.rate.model.Projects;
import org.sys.rate.model.ProjectsOper;

import java.util.List;

/**
 * 项目Mapper接口
 *
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface ProjectsMapper
{

    /**
     * 通过ID获取Paper
     *
     * @param ID
     * @return
     */
    @Select("select * from project where ID = #{ID}")
    Projects getById(Integer ID);
    /**
     * 查询项目
     *
     * @param ID 项目ID
     * @return 项目
     */
    public Projects selectProjectById(Long ID);

    /**
     * 查询项目列表
     *
     * @param projects 项目
     * @return 项目集合
     */
    public List<Projects> selectProjectList(Projects projects);

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

    @Select("select * from project")
    public List<Projects> selectList(Integer id);

    public List<Projects> selectListById(@Param("studentID") Integer studentID);
    public List<Projects> selectListByIds(@Param("studentID") Integer studentID);


    @Select("SELECT ID FROM award WHERE studentID = #{stuID} AND point = 2 AND state = 'adm_pass' LIMIT 1")
    public Integer checkScore(Long stuID);

    @Update("UPDATE award SET state = #{state},have_score = #{valid} WHERE ID = #{ID}")
    public Integer editState2(String state, Long ID, Integer valid);

    int editState(@Param("state") String state, @Param("ID") Long id);

    @Update("UPDATE student SET score = score + #{score} WHERE ID = #{stuID}")
    public int updateScore(int stuID,int score);

    int insertPaperoper(ProjectsOper paperoper);
}
