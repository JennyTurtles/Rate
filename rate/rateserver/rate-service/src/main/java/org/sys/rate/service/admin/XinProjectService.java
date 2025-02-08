package org.sys.rate.service.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.XinProjectMapper;
import org.sys.rate.model.XinProject;


@Service
public class XinProjectService {

    @Autowired
    private XinProjectMapper xinProjectMapper;


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
}