package org.sys.rate.service.admin;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.DecisionMapper;
import org.sys.rate.mapper.DecisionTypeMapper;
import org.sys.rate.model.Decision;
import org.sys.rate.model.DecisionType;
import org.sys.rate.model.Operation;
import org.sys.rate.service.mail.MailToStuService;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class DecisionService {
    @Resource
    private DecisionMapper decisionMapper;
    @Resource
    private DecisionTypeMapper decisionTypeMapper;
    @Resource
    private MailToStuService mailToStuService;

    public List<Decision> selectDecisionListById(@Param("studentID") Integer studentID){
        List<Decision> list = decisionMapper.selectDecisionListById(studentID);
        return list;
    }

    /**
     * 新增科研奖励成果
     *
     * @param decision 科研奖励成果
     * @return 结果
     */
    public int insertDecision(Decision decision){
        return decisionMapper.insertDecision(decision);
    }
    public int updateDecision(Decision decision){
        return decisionMapper.updateDecision(decision);
    }

    /**
     * 删除科研奖励成果
     *
     * @param ID 科研奖励成果ID
     * @return 结果
     */
    public int deleteDecisionById(Long ID){
        return decisionMapper.deleteDecisionById(ID);
    }

    public List<Decision> selectAllDecisionList(){
        List<Decision> list = decisionMapper.selectAllDecisionList();
        return list;
    }
    //  修改状态
    public int editState(String state, Long ID) throws MessagingException {
        Decision decision = decisionMapper.getById(Math.toIntExact(ID));
        mailToStuService.sendStuMail(state, decision,null, "决策咨询");
        return decisionMapper.editState(state,ID);
    }
    public List<DecisionType> getIndicatorByYearAndType(String year, Integer indicatorId) {
        List<DecisionType> list = decisionTypeMapper.getIndicatorByYearAndType(year, indicatorId);
        return list;
    }
    public List<Decision> searchDecisionByConditions(String studentName, String state, String decisionName, String pointFront, String pointBack) {
        List<Decision> list = decisionMapper.searchDecisionByConditions(studentName, state, decisionName, pointFront, pointBack);
        return list;
    }

    public void addDecisionType(DecisionType decisionType) {
        decisionTypeMapper.addDecisionType(decisionType);
    }

    public void editDecisionType(DecisionType decisionType) {
        // 这里不好判断之前的rankN是否修改了，那就都修改一下
        decisionTypeMapper.editDecisionType(decisionType);
        decisionTypeMapper.editIndicatorDecisionRankN(decisionType);
    }
}
