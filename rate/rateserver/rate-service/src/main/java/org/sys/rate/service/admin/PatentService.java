package org.sys.rate.service.admin;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.PatentMapper;
import org.sys.rate.model.Operation;
import org.sys.rate.model.Patent;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class PatentService {
    @Resource
    private PatentMapper patentMapper;

    public Patent selectPatentById(Long ID){
        return patentMapper.selectPatentById(ID);
    }

    /**
     * 通过ID寻找paper信息
     * @param ID
     * @return paper
     */
    public Patent getById(Integer ID){
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
     * @param paper 论文成果
     * @return 结果
     */
    public int insertPatent(Patent paper){
        return patentMapper.insertPatent(paper);
    }

    /**
     * 修改论文成果
     *
     * @param paper 论文成果
     * @return 结果
     */
    public int updatePatent(Patent paper){
        return patentMapper.updatePatent(paper);
    }

    /**
     * 删除论文成果
     *
     * @param ID 论文成果ID
     * @return 结果
     */
    public int deletePatentById(Long ID){
        return patentMapper.deletePatentById(ID);
    }

    //    老师界面调用paper
    public List<Patent> selectList(){
        return patentMapper.selectList();
    }
    public List<Patent> setPatentOperation(List<Patent> list) {
        for (int i = 0; i < list.size(); i++) {
            Patent patent = list.get(i);
            List<Operation> patentoperList = patent.getOperationList();
            if (patentoperList == null || patentoperList.size() == 0) {
                continue;
            }
            if (patentoperList.size() == 1) {//只有一个提交
                patent.setDate(patentoperList.get(0).getTime());
                continue;
            }
            int indexReject = -1;
            Timestamp timeCommit = new Timestamp(new Date().getTime());
            Timestamp timeReject = patentoperList.get(0).getTime();
            for (int patentOper = 0; patentOper < patentoperList.size(); patentOper++) {
                if (patentoperList.get(patentOper).getOperationName().equals("提交专利") && patentoperList.get(patentOper).getOperatorRole().equals("student")) {
//                    因为可能有多次提交（如老师驳回、再次提交），所以找到时间最早的一条记录
                    if (timeCommit.getTime() > patentoperList.get(patentOper).getTime().getTime()) {
                        timeCommit = patentoperList.get(patentOper).getTime();
                    }
                }
                if (patentoperList.get(patentOper).getOperationName().equals("审核驳回") && (patentoperList.get(patentOper).getOperatorRole().equals("teacher") || patentoperList.get(patentOper).getOperatorRole().equals("admin"))) {
//                    可能有多次驳回，所以找到时间最晚的一条记录
                    if (timeReject.getTime() < patentoperList.get(patentOper).getTime().getTime()) {
                        indexReject = patentOper; //有可能该专利没有被驳回过，所以后续通过indexReject判断是否为初始值-1
                        timeReject = patentoperList.get(patentOper).getTime();
                    }
                }
            }
            if (indexReject != -1) { //说明有驳回记录
                if (patent.getState().equals("commit") || patent.getState().equals("tea_pass") || patent.getState().equals("adm_pass")) {
                    //但是如果当前的专利处于通过或再次被提交状态就不返回驳回理由
                    patent.setRemark(" ");
                } else {
                    patent.setRemark(patentoperList.get(indexReject).getRemark());
                }
            }
            patent.setDate(timeCommit);
        }
        return list;
    }

    //    修改论文状态
    public int editState(String state, Long ID) throws MessagingException {
        //mailToStuServicei.sendStuMail(state, paper, "论文");
        return patentMapper.editState(state,ID);
    }
}
