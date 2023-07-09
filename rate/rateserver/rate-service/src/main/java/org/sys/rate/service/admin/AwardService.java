package org.sys.rate.service.admin;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.AwardMapper;
import org.sys.rate.mapper.AwardMapper;
import org.sys.rate.model.Award;
import org.sys.rate.model.Operation;
import org.sys.rate.model.Award;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class AwardService {
    @Resource
    private AwardMapper awardMapper;

//    public Award selectAwardById(Long ID){
//        return awardMapper.selectAwardById(ID);
//    }

    /**
     * 通过ID寻找paper信息
     * @param ID
     * @return paper
     */
//    public Award getById(Integer ID){
//        Award award = awardMapper.getById(ID);
//        if(award != null){
//            return award;
//        }
//        return null;
//    }

//    public List<Award> selectAwardList(Award paper){
//        return awardMapper.selectAwardList(paper);
//    }

//    public List<Award> selectListById(@Param("studentID") Integer studentID, @Param("page") Integer page, @Param("size") Integer size){
//        if (page != null && size != null) {
//            page = (page - 1) * size;
//        }
//        List<Award> p=awardMapper.selectListById(studentID,page,size);
////        System.out.println(p);
//        return p;
//    }
    public List<Award> selectAwardListById(@Param("studentID") Integer studentID){
        List<Award> list = awardMapper.selectAwardListById(studentID);
        return setAwardOperation(list);
    }

    /**
     * 新增科研奖励成果
     *
     * @param award 科研奖励成果
     * @return 结果
     */
    public int insertAward(Award award){
        return awardMapper.insertAward(award);
    }
    public int updateAward(Award award){
        return awardMapper.updateAward(award);
    }

    /**
     * 删除科研奖励成果
     *
     * @param ID 科研奖励成果ID
     * @return 结果
     */
    public int deleteAwardById(Long ID){
        return awardMapper.deleteAwardById(ID);
    }

    public List<Award> selectAllAwardList(){
        List<Award> list = awardMapper.selectAllAwardList();
        return setAwardOperation(list);
    }
    public List<Award> setAwardOperation(List<Award> list) {
        for (int i = 0; i < list.size(); i++) {
            Award award = list.get(i);
            List<Operation> awardoperList = award.getOperationList();
            if (awardoperList == null || awardoperList.size() == 0) {
                continue;
            }
            if (awardoperList.size() == 1) {//只有一个提交
                award.setDate(awardoperList.get(0).getTime());
                continue;
            }
            int indexReject = -1;
            Timestamp timeCommit = new Timestamp(new Date().getTime());
            Timestamp timeReject = awardoperList.get(0).getTime();
            for (int awardOper = 0; awardOper < awardoperList.size(); awardOper++) {
                if (awardoperList.get(awardOper).getOperationName().equals("提交奖励") && awardoperList.get(awardOper).getOperatorRole().equals("student")) {
//                    因为可能有多次提交（如老师驳回、再次提交），所以找到时间最早的一条记录
                    if (timeCommit.getTime() > awardoperList.get(awardOper).getTime().getTime()) {
                        timeCommit = awardoperList.get(awardOper).getTime();
                    }
                }
                if (awardoperList.get(awardOper).getOperationName().equals("审核驳回") && (awardoperList.get(awardOper).getOperatorRole().equals("teacher") || awardoperList.get(awardOper).getOperatorRole().equals("admin"))) {
//                    可能有多次驳回，所以找到时间最晚的一条记录
                    if (timeReject.getTime() < awardoperList.get(awardOper).getTime().getTime()) {
                        indexReject = awardOper; //有可能该专利没有被驳回过，所以后续通过indexReject判断是否为初始值-1
                        timeReject = awardoperList.get(awardOper).getTime();
                    }
                }
            }
            if (indexReject != -1) { //说明有驳回记录
                if (award.getState().equals("commit") || award.getState().equals("tea_pass") || award.getState().equals("adm_pass")) {
                    //但是如果当前的专利处于通过或再次被提交状态就不返回驳回理由
                    award.setRemark("");
                } else {
                    award.setRemark(awardoperList.get(indexReject).getRemark());
                }
            }
//            这里是返回里最早提交时间，显示详情中应该是申请的时间
//            award.setDate(timeCommit);
        }
        return list;
    }

    //    修改科研奖励状态
    public int editState(String state, Long ID) throws MessagingException {
        //mailToStuServicei.sendStuMail(state, paper, "科研奖励");
        return awardMapper.editState(state,ID);
    }
}
