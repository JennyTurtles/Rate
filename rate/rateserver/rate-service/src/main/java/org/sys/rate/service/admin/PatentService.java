package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.PatentMapper;
import org.sys.rate.model.Paper;
import org.sys.rate.model.PaperOper;
import org.sys.rate.model.Patent;
import org.sys.rate.model.PatentOper;
import org.sys.rate.service.mail.MailToStuService;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.List;

@Service
public class PatentService {
    @Autowired
    private PatentMapper patentMapper;

    @Resource
    MailToStuService mailToStuService;

    /**
     * 查询著作
     *
     * @param ID 著作ID
     * @return 著作
     */
    public Patent selectPatentById(Long ID){
       return patentMapper.selectPatentById(ID);
    }

    /**
     * 查询著作列表
     *
     * @param patent 著作
     * @return 著作集合
     */
    public List<Patent> selectPatentList(Patent patent){
        return patentMapper.selectPatentList(patent);
    }

    /**
     * 新增著作
     *
     * @param patent 著作
     * @return 结果
     */
    public int insertPatent(Patent patent){
        return patentMapper.insertPatent(patent);
    }

    /**
     * 修改著作
     *
     * @param patent 著作
     * @return 结果
     */
    public int updatePatent(Patent patent){
        return patentMapper.updatePatent(patent);
    }

    /**
     * 删除著作
     *
     * @param ID 著作ID
     * @return 结果
     */
    public int deletePatentById(Long ID){
        return patentMapper.deletePatentById(ID);
    }

    public List<Patent> selectList(Integer id){
        return patentMapper.selectList(id);
    }

    //    修改论文状态
    public int editState(String state, Long ID) throws MessagingException {
        Paper paper = patentMapper.selectByID(ID);
        mailToStuService.sendStuMail(state, paper, "专利");
        // 管理员通过的时候需要处理2分论文的情况，还要计算student的活动总分
        if (state.equals("adm_pass")){
            Long stuID = paper.getStudentID();
            Long score = paper.getPoint();

            if (score == 2){ // 2分的时候检查是否已经发表过2分的论文
                if (patentMapper.checkScore(stuID) != null){ // 已经发表过2分论文,将该论文的have_score设置为0
                    return patentMapper.editState2(state,ID,0);
                }else {
                    patentMapper.editState2(state,ID,1);
                    return patentMapper.updateScore(stuID,score);
                }
            }
            patentMapper.updateScore(stuID,score);
        }
        return patentMapper.editState(state,ID);
    }

    /**
     * 新增论文成果
     *
     * @param paper 论文成果
     * @return 结果
     */
    public int insertPaper(Paper paper){
        return patentMapper.insertPaper(paper);
    }

    public int insertPaperOper(PatentOper paperoper){
        return patentMapper.insertPaperoper(paperoper);
    }
}
