package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.sys.rate.model.ProjectBase;
import org.sys.rate.model.XinProject;
import org.sys.rate.model.XinProjectVo;

import java.util.List;
import java.util.Map;

/**
 * xin_project Mapper接口
 *
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface XinProjectMapper
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
                                              @Param(value = "pointBack")   String pointBack);


    List<XinProject> selectXinProjectList(XinProjectVo xinProjectVo);
}
