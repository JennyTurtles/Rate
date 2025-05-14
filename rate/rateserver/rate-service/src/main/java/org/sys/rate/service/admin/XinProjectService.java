package org.sys.rate.service.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.*;
import org.sys.rate.model.ProgramResult;
import org.sys.rate.model.ProjectBase;
import org.sys.rate.model.XinProject;
import org.sys.rate.model.XinProjectVo;

import java.util.List;
import java.util.Map;


@Service
public class XinProjectService {

    @Autowired
    private XinProjectMapper xinProjectMapper;

    public List<XinProject> selectListByIds(Integer studentID) {
        XinProject xinProject = new XinProject();
        xinProject.setSid(studentID);
        return xinProjectMapper.selectByStudentId(xinProject);
    }
    public int insertXinProject(XinProject xinProject) {
        return xinProjectMapper.insertXinProject(xinProject);
    }

    public int updateXinProject(Integer mid, String type, String state) {
        XinProject project = new XinProject();
        project.setMid(mid);
        project.setType(type);
        XinProject base = xinProjectMapper.selectXinProject(project);
        base.setState(state);
        return updateXinProject(base);
    }
    public int updateXinProject(XinProject xinProject) {
        XinProject base = xinProjectMapper.selectXinProject(xinProject);
        if(base == null){
            return insertXinProject(xinProject);
        }else {
            return xinProjectMapper.updateXinProject(xinProject);
        }
    }

    public int deleteXinProject(Integer mid, String type) {
        return xinProjectMapper.deleteXinProject(mid, type);
    }

    public List<ProjectBase> searchPaperByConditions(Map<String, String> params) {
        String studentName ="";
        String name ="";
        String state ="";
        String pointFront ="";
        String pointBack ="";
        String teacherId ="";


        if (!StringUtils.isEmpty(params.get("studentName"))){
            studentName=params.get("studentName");
        };
        if (!StringUtils.isEmpty(params.get("name"))){
            name=params.get("name");
        };
        if (!StringUtils.isEmpty(params.get("state"))){
            state=params.get("state");
        };
        if (!StringUtils.isEmpty(params.get("pointFront"))){
            pointFront=params.get("pointFront");
        };
        if (!StringUtils.isEmpty(params.get("pointBack"))){
            pointBack=params.get("pointBack");
        };
        if (!StringUtils.isEmpty(params.get("teacherId"))){
            teacherId=params.get("teacherId");
        };

        return xinProjectMapper.searchPaperByConditions(studentName,
                name,
                state,
                pointFront,
                pointBack,
                teacherId);
    }

    public List<XinProject> selectList(XinProjectVo xinProjectVo) {
        return     xinProjectMapper.selectXinProjectList(xinProjectVo);
    }

    public int updatePointType(Integer mid, String type, Integer pointtype) {
        XinProject xinProject = xinProjectMapper.selectOne(new QueryWrapper<XinProject>().eq("mid", mid).eq("type", type));
        if (null != xinProject) {
            xinProject.setPointtype(pointtype);
            xinProjectMapper.updateById(xinProject);
            // TODO 看是否改其余的表,待定
        } else {
            return 1;
        }
        if (pointtype == 1) {
            if (type.equals("学术论文")) {
                int i = xinProjectMapper.updatePaperScore(1, mid);
            } else if (type.equals("学术专著和教材")) {
                int i = xinProjectMapper.updateBookScore(1, mid);
            } else if (type.equals("授权专利")) {
                int i = xinProjectMapper.updatePatentScore(1, mid);
            } else if (type.equals("科研获奖")) {
                int i = xinProjectMapper.updateAwardScore(1, mid);
            } else if (type.equals("纵向科研项目")) {
                int i = xinProjectMapper.updateProjectScore(1, mid);
            } else if (type.equals("项目开发")) {
                int i = xinProjectMapper.updateProgramScore(1, mid);
            } else if (type.equals("学科竞赛")) {
                int i = xinProjectMapper.updateCompetitionScore(1, mid);
            } else if (type.equals("决策咨询")) {
                int i = xinProjectMapper.updateDecisionScore(1, mid);
            } else if (type.equals("指定标准")) {
                int i = xinProjectMapper.updateStandardScore(1, mid);
            } else if (type.equals("撰写项目文档")) {
                int i = xinProjectMapper.updateApplicationScore(1, mid);
            }
            xinProjectMapper.addStudentScore(xinProject.getPoint(),xinProject.getSid());
        } else if (pointtype == 2) {
            if (type.equals("学术论文")) {
                int i = xinProjectMapper.updatePaperScore(0, mid);
            } else if (type.equals("学术专著和教材")) {
                int i = xinProjectMapper.updateBookScore(0, mid);
            } else if (type.equals("授权专利")) {
                int i = xinProjectMapper.updatePatentScore(0, mid);
            } else if (type.equals("科研获奖")) {
                int i = xinProjectMapper.updateAwardScore(0, mid);
            } else if (type.equals("纵向科研项目")) {
                int i = xinProjectMapper.updateProjectScore(0, mid);
            } else if (type.equals("项目开发")) {
                int i = xinProjectMapper.updateProgramScore(0, mid);
            } else if (type.equals("学科竞赛")) {
                int i = xinProjectMapper.updateCompetitionScore(0, mid);
            } else if (type.equals("决策咨询")) {
                int i = xinProjectMapper.updateDecisionScore(0, mid);
            } else if (type.equals("指定标准")) {
                int i = xinProjectMapper.updateStandardScore(0, mid);
            } else if (type.equals("撰写项目文档")) {
                int i = xinProjectMapper.updateApplicationScore(0, mid);
            }
            xinProjectMapper.removeStudentScore(xinProject.getPoint(),xinProject.getSid());
        }

        return 0;
    }

    public List<XinProject> selectProjectDataListByIds(XinProjectVo xinProjectVo) {
        return xinProjectMapper.selectProjectDataListByIds(xinProjectVo);
    }
}