package org.sys.rate.service.admin;

import cn.hutool.core.util.ObjectUtil;
import com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.OperationMapper;
import org.sys.rate.mapper.PaperMapper;
import org.sys.rate.model.Monograph;
import org.sys.rate.model.Operation;
import org.sys.rate.model.Paper;
import org.sys.rate.model.XinProject;
import org.sys.rate.service.mail.MailToStuService;
import org.sys.rate.utils.ProjectTypeEnums;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaperService {

    @Resource
    private PaperMapper paperMapper;
    @Resource
    private OperationMapper operationMapper;
    @Resource
    MailToStuService mailToStuService;

    @Resource
    private XinProjectService xinProjectService;

    private void dealXinProject(Paper dto, int type){
        XinProject xinProject = new XinProject(dto.getName(), dto.getPoint() == null ? null : Math.toIntExact(dto.getPoint()), dto.getAuthor(),
                dto.getState(), dto.getRemark(), Math.toIntExact(dto.getID()), ProjectTypeEnums.ACADEMIC_PAPER.getDisplayName(), Math.toIntExact(dto.getStudentID()));
//        XinProject xinProject = new XinProject(dto.getName(), Math.toIntExact(dto.getPoint()), dto.getAuthor(),
//                dto.getState(), dto.getRemark(), Math.toIntExact(dto.getID()), ProjectTypeEnums.ACADEMIC_PAPER.getDisplayName(),Math.toIntExact(dto.getStudentID());
        if(type ==1) {
            xinProjectService.insertXinProject(xinProject);
        }else if(type == 2){
            xinProjectService.updateXinProject(xinProject);
        }else if(type == 3){
            xinProjectService.updateXinProject(xinProject.getMid(), xinProject.getType(), dto.getState());
        }else if(type == 4){
            xinProjectService.deleteXinProject(xinProject.getMid(), xinProject.getType());
        }
    }

    public Paper selectPaperById(Long ID) {
        return paperMapper.selectPaperById(ID);
    }

    /**
     * 通过ID寻找paper信息
     *
     * @param ID
     * @return paper
     */
    public Paper getById(Integer ID) {
        Paper paper = paperMapper.getById(ID);
        if (paper != null) {
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
    public List<Paper> selectPaperList(Paper paper) {
        return paperMapper.selectPaperList(paper);
    }

    public List<Paper> selectListById(@Param("studentID") Integer studentID, @Param("page") Integer page, @Param("size") Integer size) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Paper> p = paperMapper.selectListById(studentID, page, size);
//        System.out.println(p);
        return p;
    }

    public List<Paper> selectListByIds(@Param("studentID") Integer studentID) {
        return paperMapper.selectListByIds(studentID);
    }

    /**
     * 新增论文成果
     *
     * @param paper 论文成果
     * @return 结果
     */

    public int insertPaper(Paper paper) {

        int rlt = paperMapper.insertPaper(paper);
        dealXinProject(paper, 1);
        return rlt;
    }

    /**
     * 修改论文成果
     *
     * @param paper 论文成果
     * @return 结果
     */
    public int updatePaper(Paper paper) {
        dealXinProject(paper, 2);
        return paperMapper.updatePaper(paper);
    }

    /**
     * 删除论文成果
     *
     * @param ID 论文成果ID
     * @return 结果
     */
    public int deletePaperById(Long ID) {
//        Paper paper = new Paper();
//        paper.setID(ID);
        Paper paper = paperMapper.selectByID(ID);
        if (ObjectUtil.isEmpty(paper)) {
            throw new RuntimeException("该数据不存在");
        }
        dealXinProject(paper, 4);
        return paperMapper.deletePaperById(ID);
    }

    //    老师界面调用paper
    public List<Paper> selectList() {
        return paperMapper.selectList();
    }

    //    修改论文状态
    public int editState(String state, Long ID) throws MessagingException {
        Paper paper = paperMapper.selectByID(ID);
        // 管理员通过的时候需要处理2分论文的情况，还要计算student的活动总分
        paper.setState(state);
        dealXinProject(paper, 3);
        if (state.equals("adm_pass")) {
            Long stuID = paper.getStudentID();
            Long score = paper.getPoint();

            if (score == 2) { // 2分的时候检查是否已经发表过2分的论文
                if (paperMapper.checkScore(stuID) != null) { // 已经发表过2分论文,将该论文的have_score设置为0
                    return paperMapper.editState2(state, ID, 0);
                } else {
                    paperMapper.editState2(state, ID, 1);
                    return paperMapper.updateScore(stuID, score);
                }
            }
            paperMapper.editState2(state, ID, 1); // 不为2分的论文的have_score直接设置为1

            paper.setState(state);
            dealXinProject(paper, 3);
            paperMapper.updateScore(stuID, score);
        }
        int res = paperMapper.editState(state, ID);

        mailToStuService.sendStuMail(state, null, paper, "学术论文");
        return res;
    }

    public List<Paper> searchPaperByConditions(String studentName, String state, String name, String pointFront, String pointBack, String pub) {
        List<Paper> list = paperMapper.searchPaperByConditions(studentName, state, name, pointFront, pointBack, pub);
        return list;
    }

    public String getEmailByPaperId(Long studentId) {
        return paperMapper.getEmailByPaperId(studentId);
    }
}
