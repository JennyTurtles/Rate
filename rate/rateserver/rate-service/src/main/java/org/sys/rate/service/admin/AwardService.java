package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.AwardMapper;
import org.sys.rate.mapper.AwardMapper;
import org.sys.rate.model.Award;
import org.sys.rate.model.AwardOper;
import org.sys.rate.model.Paper;
import org.sys.rate.model.PatentOper;
import org.sys.rate.service.mail.MailToStuService;

import javax.mail.MessagingException;
import java.util.List;

@Service
public class AwardService {

    @Autowired
    private AwardMapper awardtypeMapper;

    public Award selectAwardById(Long ID){
        return awardtypeMapper.selectAwardById(ID);
    }

    /**
     * 查询类别列表
     *
     * @param awardtype 类别
     * @return 类别集合
     */
    public List<Award> selectAwardList(Award awardtype){
        return awardtypeMapper.selectAwardList(awardtype);
    }

    /**
     * 新增类别
     *
     * @param awardtype 类别
     * @return 结果
     */
    public int insertAward(Award awardtype){
        return awardtypeMapper.insertAward(awardtype);
    }

    /**
     * 修改类别
     *
     * @param awardtype 类别
     * @return 结果
     */
    public int updateAward(Award awardtype){
        return awardtypeMapper.insertAward(awardtype);
    }

    /**
     * 删除类别
     *
     * @param ID 类别ID
     * @return 结果
     */
    public int deleteAwardById(Long ID){
        return awardtypeMapper.deleteAwardById(ID);
    }

    public List<Award> selectList(){
        return awardtypeMapper.selectList();
    }

    @Autowired
    private MailToStuService mailToStuService;

    //    修改论文状态
    public int editState(String state, Long ID) throws MessagingException {
        Award paper = awardtypeMapper.selectAwardById(ID);
//        mailToStuService.sendStuMail(state, paper);
        // 管理员通过的时候需要处理2分论文的情况，还要计算student的活动总分
        if (state.equals("adm_pass")){
            int stuID = paper.getStudentID();
            int score = paper.getPoint();

            if (score == 2){ // 2分的时候检查是否已经发表过2分的论文
                if (awardtypeMapper.checkScore(Long.parseLong(String.valueOf(stuID))) != null){ // 已经发表过2分论文,将该论文的have_score设置为0
                    return awardtypeMapper.editState2(state,ID,0);
                }else {
                    awardtypeMapper.editState2(state,ID,1);
                    return awardtypeMapper.updateScore(stuID,score);
                }
            }
            awardtypeMapper.updateScore(stuID,score);
        }
        return awardtypeMapper.editState(state,ID);
    }

    public int insertPaperOper(AwardOper paperoper){
        return awardtypeMapper.insertPaperoper(paperoper);
    }
}
