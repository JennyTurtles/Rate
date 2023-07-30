package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.sys.rate.model.Project;

import java.util.List;

/**
 * 专利成果Mapper接口
 *
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface ProjectMapper
{
    /**
     * 通过ID获取Project
     *
     * @param ID
     * @return
     */
    @Select("select * from i_project where ID = #{ID}")
    Project getById(Integer ID);

    /**
     * 查询专利成果
     *
     * @param ID 专利成果ID
     * @return 专利成果
     */
    public Project selectProjectById(Long ID);

    /**
     * 查询专利成果列表
     *
     * @param project 专利成果
     * @return 专利成果集合
     */
    public List<Project> selectProjectList(Project project);

    public int insertProject(Project project);

    public int updateProject(Project project);

    public int editState(String state, Long ID);
    /**
     * 删除专利成果
     *
     * @param ID 成果ID
     * @return 结果
     */
    public int deleteProjectById(Long ID);

    /**
     * 批量删除专利成果
     *
     * @param IDs 需要删除的数据ID
     * @return 结果
     */
    public int deleteProjectByIds(String[] IDs);

    public List<Project> selectAllProjectList();
    public List<Project> selectListById(@Param("studentID") Integer studentID, @Param("page") Integer page, @Param("size") Integer size);
    public List<Project> selectProjectListById(Integer studentID);


    @Select("SELECT ID FROM i_project WHERE studentID = #{stuID} AND point = 2 AND state = 'adm_pass' LIMIT 1")
    public Integer checkScore(Long stuID);

    @Update("UPDATE i_project SET state = #{state},have_score = #{valid} WHERE ID = #{ID}")
    public Integer editState2(String state, Long ID, Integer valid);

    @Update("UPDATE graduatestudent SET point = point + #{score} WHERE ID = #{stuID}")
    public int updateScore(Long stuID,Long score);

    @Select("SELECT * FROM i_project WHERE  ID = #{ID}")
    public Project selectByID(Long ID);

    public List<Project> searchProjectByConditions(String studentName, String state, String projectName, String pointFront, String pointBack);


}
