package org.sys.rate.service.admin;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.MonographMapper;
import org.sys.rate.mapper.OperationMapper;
import org.sys.rate.model.Monograph;
import org.sys.rate.model.Operation;
import org.sys.rate.model.XinProject;
import org.sys.rate.service.mail.MailToStuService;
import org.sys.rate.utils.ProjectTypeEnums;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MonographService {
    @Resource
    private MonographMapper monographMapper;
    @Resource
    private OperationMapper operationMapper;
    @Resource
    private MailToStuService mailToStuService;

    @Resource
    private XinProjectService xinProjectService;

    private void dealXinProject(Monograph dto, int type){
        XinProject xinProject = new XinProject(dto.getName(), dto.getPoint(), dto.getAuthor(),
                dto.getState(), dto.getRemark(), dto.getId(),
                ProjectTypeEnums.ACADEMIC_MONOGRAPH_AND_TEXTBOOK.getDisplayName(),
                dto.getStudentId());
        if(type ==1) {
            xinProjectService.insertXinProject(xinProject);
        }else if(type == 2){
            xinProjectService.updateXinProject(xinProject);
        }else if(type == 3){
            xinProjectService.updateXinProject(xinProject.getMid(), xinProject.getType(), xinProject.getState());
        }else if(type == 4){
            xinProjectService.deleteXinProject(xinProject.getMid(), xinProject.getType());
        }
    }
    public List<Monograph> selectMonographListById(@Param("studentID") Integer studentID) {
        List<Monograph> list = monographMapper.selectMonographListById(studentID);
        return list;
    }

    /**
     * 新增科研学术专著和教材成果
     *
     * @param monograph 科研学术专著和教材成果
     * @return 结果
     */
    public int insertMonograph(Monograph monograph) {
        int rlt = monographMapper.insertMonograph(monograph);
        dealXinProject(monograph, 1);
        return rlt;
    }

    public int updateMonograph(Monograph monograph) {
        dealXinProject(monograph, 2);
        return monographMapper.updateMonograph(monograph);
    }

    /**
     * 删除科研学术专著和教材成果
     *
     * @param ID 科研学术专著和教材成果ID
     * @return 结果
     */
    public int deleteMonographById(Long ID) {

        Monograph monograph = new Monograph();
        monograph.setId(Math.toIntExact(ID));
        dealXinProject(monograph, 4);
        return monographMapper.deleteMonographById(ID);
    }

    public List<Monograph> selectAllMonographList() {
        List<Monograph> list = monographMapper.selectAllMonographList();
        return list;
    }

    //    修改科研学术专著和教材状态
    public int editState(String state, Long ID)  {
        Monograph monograph = monographMapper.getById(Math.toIntExact(ID));
        mailToStuService.sendStuMail(state, monograph, null, "学术专著和教材");
        monograph.setState(state);
        dealXinProject(monograph, 3);
        return monographMapper.editState(state, ID);
    }

    public List<Monograph> searchMonographByConditions(String studentName, String state, String monoName, String pointFront, String pointBack) {
        List<Monograph> list = monographMapper.searchMonographByConditions(studentName, state, monoName, pointFront, pointBack);
        return list;
    }
}
