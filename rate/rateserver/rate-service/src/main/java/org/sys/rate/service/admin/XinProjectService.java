package org.sys.rate.service.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.XinProjectMapper;
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

        return xinProjectMapper.searchPaperByConditions(studentName,
                name,
                state,
                pointFront,
                pointBack);
    }

    public List<XinProject> selectList(XinProjectVo xinProjectVo) {
        return     xinProjectMapper.selectXinProjectList(xinProjectVo);
    }

    public int updatePointType(Integer mid,Integer type) {
        XinProject xinProject = xinProjectMapper.selectOne(new QueryWrapper<XinProject>().eq("mid",mid));
        if (null!= xinProject){
            xinProject.setPointtype(type);
            xinProjectMapper.updateById(xinProject);
            //TODO 看是否改其余的表,待定
        }else {
            return 1;
        }

        return 0;
    }
}