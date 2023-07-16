package org.sys.rate.service.admin;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.MonographMapper;
import org.sys.rate.model.Monograph;
import org.sys.rate.model.Operation;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class MonographService {
    @Resource
    private MonographMapper monographMapper;

//    public Monograph selectMonographById(Long ID){
//        return monographMapper.selectMonographById(ID);
//    }

    /**
     * 通过ID寻找paper信息
     * @param ID
     * @return paper
     */
//    public Monograph getById(Integer ID){
//        Monograph monograph = monographMapper.getById(ID);
//        if(monograph != null){
//            return monograph;
//        }
//        return null;
//    }

//    public List<Monograph> selectMonographList(Monograph paper){
//        return monographMapper.selectMonographList(paper);
//    }

//    public List<Monograph> selectListById(@Param("studentID") Integer studentID, @Param("page") Integer page, @Param("size") Integer size){
//        if (page != null && size != null) {
//            page = (page - 1) * size;
//        }
//        List<Monograph> p=monographMapper.selectListById(studentID,page,size);
////        System.out.println(p);
//        return p;
//    }
    public List<Monograph> selectMonographListById(@Param("studentID") Integer studentID){
        List<Monograph> list = monographMapper.selectMonographListById(studentID);
        return setMonographOperation(list);
    }

    /**
     * 新增科研专著教材成果
     *
     * @param monograph 科研专著教材成果
     * @return 结果
     */
    public int insertMonograph(Monograph monograph){
        return monographMapper.insertMonograph(monograph);
    }
    public int updateMonograph(Monograph monograph){
        return monographMapper.updateMonograph(monograph);
    }

    /**
     * 删除科研专著教材成果
     *
     * @param ID 科研专著教材成果ID
     * @return 结果
     */
    public int deleteMonographById(Long ID){
        return monographMapper.deleteMonographById(ID);
    }

    public List<Monograph> selectAllMonographList(){
        List<Monograph> list = monographMapper.selectAllMonographList();
        return setMonographOperation(list);
    }
    public List<Monograph> setMonographOperation(List<Monograph> list) {
        for (int i = 0; i < list.size(); i++) {
            Monograph monograph = list.get(i);
            List<Operation> monographoperList = monograph.getOperationList();
            if (monographoperList == null || monographoperList.size() == 0) {
                continue;
            }
            if (monographoperList.size() == 1) {//只有一个提交
                monograph.setDate(monographoperList.get(0).getTime());
                continue;
            }
            int indexReject = -1;
            Timestamp timeCommit = new Timestamp(new Date().getTime());
            Timestamp timeReject = monographoperList.get(0).getTime();
            for (int monographOper = 0; monographOper < monographoperList.size(); monographOper++) {
                if (monographoperList.get(monographOper).getOperationName().equals("提交专著教材") && monographoperList.get(monographOper).getOperatorRole().equals("student")) {
//                    因为可能有多次提交（如老师驳回、再次提交），所以找到时间最早的一条记录
                    if (timeCommit.getTime() > monographoperList.get(monographOper).getTime().getTime()) {
                        timeCommit = monographoperList.get(monographOper).getTime();
                    }
                }
                if (monographoperList.get(monographOper).getOperationName().equals("审核驳回") && (monographoperList.get(monographOper).getOperatorRole().equals("teacher") || monographoperList.get(monographOper).getOperatorRole().equals("admin"))) {
//                    可能有多次驳回，所以找到时间最晚的一条记录
                    if (timeReject.getTime() < monographoperList.get(monographOper).getTime().getTime()) {
                        indexReject = monographOper; //有可能该专利没有被驳回过，所以后续通过indexReject判断是否为初始值-1
                        timeReject = monographoperList.get(monographOper).getTime();
                    }
                }
            }
            if (indexReject != -1) { //说明有驳回记录
                if (monograph.getState().equals("commit") || monograph.getState().equals("tea_pass") || monograph.getState().equals("adm_pass")) {
                    //但是如果当前的专利处于通过或再次被提交状态就不返回驳回理由
                    monograph.setRemark("");
                } else {
                    monograph.setRemark(monographoperList.get(indexReject).getRemark());
                }
            }
//            这里是返回里最早提交时间，显示详情中应该是申请的时间
//            monograph.setDate(timeCommit);
        }
        return list;
    }

    //    修改科研专著教材状态
    public int editState(String state, Long ID) throws MessagingException {
        //mailToStuServicei.sendStuMail(state, paper, "科研专著教材");
        return monographMapper.editState(state,ID);
    }
}
