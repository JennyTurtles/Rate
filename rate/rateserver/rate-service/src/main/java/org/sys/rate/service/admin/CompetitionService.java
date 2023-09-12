package org.sys.rate.service.admin;

import org.springframework.stereotype.Service;
import org.sys.rate.mapper.CompetitionTypeMapper;
import org.sys.rate.mapper.OperationMapper;
import org.sys.rate.mapper.CompetitionMapper;
import org.sys.rate.mapper.ProjectTypeMapper;
import org.sys.rate.model.CompetitionType;
import org.sys.rate.model.Operation;
import org.sys.rate.model.Competition;
import org.sys.rate.model.ProjectType;
import org.sys.rate.service.mail.MailToStuService;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class CompetitionService {
    @Resource
    private CompetitionMapper competitionMapper;
    @Resource
    private CompetitionTypeMapper competitionTypeMapper;
    @Resource
    private OperationMapper operationMapper;
    @Resource
    private MailToStuService mailToStuService;

    public List<Competition> selectCompetitionListById(Integer studentID) {
        List<Competition> list = competitionMapper.selectCompetitionListById(studentID);
        return list;
    }

    /**
     * 新增科研专著教材成果
     *
     * @param competition 科研专著教材成果
     * @return 结果
     */
    public int insertCompetition(Competition competition) {
        return competitionMapper.insertCompetition(competition);
    }

    public int updateCompetition(Competition competition) {
        return competitionMapper.updateCompetition(competition);
    }

    /**
     * 删除科研专著教材成果
     *
     * @param ID 科研专著教材成果ID
     * @return 结果
     */
    public int deleteCompetitionById(Long ID) {
        return competitionMapper.deleteCompetitionById(ID);
    }

    public List<Competition> selectAllCompetitionList() {
        List<Competition> list = competitionMapper.selectAllCompetitionList();
        return list;
    }

    public List<Competition> setCompetitionOperation(List<Competition> list) {
        for (int i = 0; i < list.size(); i++) {
            Competition competition = list.get(i);
            List<Operation> competitionoperList = competition.getOperationList();
            if (competitionoperList == null || competitionoperList.size() == 0) {
                continue;
            }
            if (competitionoperList.size() == 1) {//只有一个提交
                competition.setDate(competitionoperList.get(0).getTime());
                continue;
            }
            int indexReject = -1;
            Timestamp timeCommit = new Timestamp(new Date().getTime());
            Timestamp timeReject = competitionoperList.get(0).getTime();
            for (int competitionOper = 0; competitionOper < competitionoperList.size(); competitionOper++) {
                if (competitionoperList.get(competitionOper).getOperationName().equals("提交著作") && competitionoperList.get(competitionOper).getOperatorRole().equals("student")) {
//                    因为可能有多次提交（如老师驳回、再次提交），所以找到时间最早的一条记录
                    if (timeCommit.getTime() > competitionoperList.get(competitionOper).getTime().getTime()) {
                        timeCommit = competitionoperList.get(competitionOper).getTime();
                    }
                }
                if (competitionoperList.get(competitionOper).getOperationName().equals("审核驳回") && (competitionoperList.get(competitionOper).getOperatorRole().equals("teacher") || competitionoperList.get(competitionOper).getOperatorRole().equals("admin"))) {
//                    可能有多次驳回，所以找到时间最晚的一条记录
                    if (timeReject.getTime() < competitionoperList.get(competitionOper).getTime().getTime()) {
                        indexReject = competitionOper; //有可能该专利没有被驳回过，所以后续通过indexReject判断是否为初始值-1
                        timeReject = competitionoperList.get(competitionOper).getTime();
                    }
                }
            }
            if (indexReject != -1) { //说明有驳回记录
                if (competition.getState().equals("commit") || competition.getState().equals("tea_pass") || competition.getState().equals("adm_pass")) {
                    //但是如果当前的专利处于通过或再次被提交状态就不返回驳回理由
                    competition.setRemark("");
                } else {
                    competition.setRemark(competitionoperList.get(indexReject).getRemark());
                }
            }
//            这里是返回里最早提交时间，显示详情中应该是申请的时间
//            competition.setDate(timeCommit);
        }
        return list;
    }

    //    修改科研专著教材状态
    public int editState(String state, Long ID) throws MessagingException {
        Competition competition = competitionMapper.getById(Math.toIntExact(ID));
//        mailToStuService.sendStuMail(state, competition, null, "学科竞赛");
        return competitionMapper.editState(state, ID);
    }

    public List<Competition> searchCompetitionByConditions(String studentName, String state, String competitionName, String pointFront, String pointBack) {
        List<Competition> list = competitionMapper.searchCompetitionByConditions(studentName, state, competitionName, pointFront, pointBack);
        return list;
    }

    public List<CompetitionType> getIndicatorByYearAndType(String year) {
        List<CompetitionType> list = competitionTypeMapper.getIndicatorByYearAndType(year);
        return list;
    }

}
