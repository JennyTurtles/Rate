package org.sys.rate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.sys.rate.model.ProjectBase;
import org.sys.rate.model.XinProject;
import org.sys.rate.model.XinProjectVo;

import java.util.List;

/**
 * xin_project Mapper接口
 *
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface XinProjectMapper extends BaseMapper<XinProject>
{

    public XinProject selectXinProject(XinProject xinProject);

    public int insertXinProject(XinProject xinProject);

    public int updateXinProject(XinProject xinProject);

    public int deleteXinProject(Integer mid, String type);

    public List<XinProject> selectByStudentId(XinProject xinProject);


    List<ProjectBase> searchPaperByConditions(@Param(value = "studentName") String studentName,
                                              @Param(value = "name")  String name,
                                              @Param(value = "state")  String state,
                                              @Param(value = "pointFront")  String pointFront,
                                              @Param(value = "pointBack")   String pointBack,
                                              @Param(value = "teacherId")   String teacherId);

    List<XinProject> selectXinProjectList(XinProjectVo xinProjectVo);

    XinProject selectById(@Param(value = "operatorId")Integer operatorId);

    @Update("UPDATE paper SET have_score = #{have_score} WHERE ID = #{id}")
    public int updatePaperScore(Integer have_score,Integer id);

    @Update("UPDATE i_patent SET have_score = #{have_score} WHERE ID = #{id}")
    public int updatePatentScore(Integer have_score,Integer id);

    @Update("UPDATE i_book SET have_score = #{have_score} WHERE ID = #{id}")
    public int updateBookScore(Integer have_score,Integer id);

    @Update("UPDATE i_award SET have_score = #{have_score} WHERE ID = #{id}")
    public int updateAwardScore(Integer have_score,Integer id);

    @Update("UPDATE programresults SET have_score = #{have_score} WHERE ID = #{id}")
    public int updateProgramScore(Integer have_score,Integer id);

    @Update("UPDATE i_project SET have_score = #{have_score} WHERE ID = #{id}")
    public int updateProjectScore(Integer have_score,Integer id);

    @Update("UPDATE i_competition SET have_score = #{have_score} WHERE ID = #{id}")
    public int updateCompetitionScore(Integer have_score,Integer id);

    @Update("UPDATE i_decision SET have_score = #{have_score} WHERE ID = #{id}")
    public int updateDecisionScore(Integer have_score,Integer id);

    @Update("UPDATE i_application SET have_score = #{have_score} WHERE ID = #{id}")
    public int updateApplicationScore(Integer have_score,Integer id);

    @Update("UPDATE i_standard SET have_score = #{have_score} WHERE ID = #{id}")
    public int updateStandardScore(Integer have_score,Integer id);

    @Update("UPDATE graduatestudent SET point = point - #{score} WHERE studentID = #{id}")
    public int removeStudentScore(Integer score,Integer id);

    @Update("UPDATE graduatestudent SET point = point + #{score} WHERE studentID = #{id}")
    public int addStudentScore(Integer score,Integer id);

    List<XinProject> selectProjectDataListByIds(XinProjectVo xinProjectVo);
}
