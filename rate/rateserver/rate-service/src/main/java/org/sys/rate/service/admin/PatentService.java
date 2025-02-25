package org.sys.rate.service.admin;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.PatentMapper;
import org.sys.rate.model.Operation;
import org.sys.rate.model.Patent;
import org.sys.rate.model.Project;
import org.sys.rate.model.XinProject;
import org.sys.rate.service.mail.MailToStuService;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class PatentService {
    @Resource
    private PatentMapper patentMapper;
    @Resource
    private MailToStuService mailToStuService;
    @Resource
    private XinProjectService xinProjectService;

    public Patent selectPatentById(Long ID){
        return patentMapper.getById(ID);
    }

    /**
     * 通过ID寻找paper信息
     * @param ID
     * @return paper
     */
    public Patent getById(Long ID){
        Patent paper = patentMapper.getById(ID);
        if(paper != null){
            return paper;
        }
        return null;
    }

    /**
     * 查询论文成果列表
     *
     * @param paper 论文成果
     * @return 论文成果集合
     */
    public List<Patent> selectPatentList(Patent paper){
        return patentMapper.selectPatentList(paper);
    }

    public List<Patent> selectListById(@Param("studentID") Integer studentID, @Param("page") Integer page, @Param("size") Integer size){
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Patent> p=patentMapper.selectListById(studentID,page,size);
//        System.out.println(p);
        return p;
    }
    public List<Patent> selectListByIds(@Param("studentID") Integer studentID){
        return patentMapper.selectListByIds(studentID);
    }

    /**
     * 新增论文成果
     *
     * @param patent 论文成果
     * @return 结果
     */
    public int insertPatent(Patent patent){
        int rlt = patentMapper.insertPatent(patent);

        XinProject xinProject = new XinProject(patent.getName(), patent.getPoint(), patent.getAuthor(),
                patent.getState(), patent.getRemark(), patent.getId(), "授权专利", patent.getStudentId());
        xinProjectService.insertXinProject(xinProject);
        return rlt;
    }

    /**
     * 修改论文成果
     *
     * @param patent 论文成果
     * @return 结果
     */
    public int updatePatent(Patent patent){
        XinProject xinProject = new XinProject(patent.getName(), patent.getPoint(), patent.getAuthor(),
                patent.getState(), patent.getRemark(), patent.getId(), "授权专利", patent.getStudentId());
        xinProjectService.updateXinProject(xinProject);
        return patentMapper.updatePatent(patent);
    }

    /**
     * 删除论文成果
     *
     * @param ID 论文成果ID
     * @return 结果
     */
    public int deletePatentById(Long ID){
        xinProjectService.deleteXinProject(ID.intValue(), "授权专利");
        return patentMapper.deletePatentById(ID);
    }

    //    老师界面调用paper
    public List<Patent> selectList(){
        List<Patent> res = patentMapper.selectList();
        return res;
    }

    //    修改论文状态
    public int editState(String state, Long ID) throws MessagingException {
        Patent patent = patentMapper.getById(ID);
        mailToStuService.sendStuMail(state, patent, null, "授权专利");

        xinProjectService.updateXinProject(Integer.parseInt(ID+""), "授权专利", state);
        return patentMapper.editState(state,ID);
    }
    public List<Patent> searchPatentByConditions(String studentName, String state, String projectName, String pointFront, String pointBack) {
        List<Patent> list = patentMapper.searchPatentByConditions(studentName, state, projectName, pointFront, pointBack);
        return list;
    }
}
