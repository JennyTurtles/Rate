package org.sys.rate.service.admin;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.OperationMapper;
import org.sys.rate.mapper.PaperMapper;
import org.sys.rate.model.Monograph;
import org.sys.rate.model.Operation;
import org.sys.rate.model.Paper;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaperService {

    @Resource
    private PaperMapper paperMapper;
    @Resource
    private OperationMapper operationMapper;
    //@Resource
    //MailToStuService mailToStuService;

    public Paper selectPaperById(Long ID){
        return paperMapper.selectPaperById(ID);
    }

    /**
     * 通过ID寻找paper信息
     * @param ID
     * @return paper
     */
    public Paper getById(Integer ID){
        Paper paper = paperMapper.getById(ID);
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
    public List<Paper> selectPaperList(Paper paper){
        return paperMapper.selectPaperList(paper);
    }

    public List<Paper> selectListById(@Param("studentID") Integer studentID, @Param("page") Integer page, @Param("size") Integer size){
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Paper> p=paperMapper.selectListById(studentID,page,size);
//        System.out.println(p);
        return p;
    }
    public List<Paper> selectListByIds(@Param("studentID") Integer studentID){
        return paperMapper.selectListByIds(studentID);
    }

    /**
     * 新增论文成果
     *
     * @param paper 论文成果
     * @return 结果
     */
    public int insertPaper(Paper paper){
        return paperMapper.insertPaper(paper);
    }

    /**
     * 修改论文成果
     *
     * @param paper 论文成果
     * @return 结果
     */
    public int updatePaper(Paper paper){
        return paperMapper.updatePaper(paper);
    }

    /**
     * 删除论文成果
     *
     * @param ID 论文成果ID
     * @return 结果
     */
    public int deletePaperById(Long ID){
        return paperMapper.deletePaperById(ID);
    }

    //    老师界面调用paper
    public List<Paper> selectList(){
        return paperMapper.selectList();
    }

    //    修改论文状态
    public int editState(String state, Long ID) throws MessagingException {
        Paper paper = paperMapper.selectByID(ID);
        //mailToStuServicei.sendStuMail(state, paper, "论文");
        // 管理员通过的时候需要处理2分论文的情况，还要计算student的活动总分
        if (state.equals("adm_pass")){
            Long stuID = paper.getStudentID();
            Long score = paper.getPoint();

            if (score == 2){ // 2分的时候检查是否已经发表过2分的论文
                if (paperMapper.checkScore(stuID) != null){ // 已经发表过2分论文,将该论文的have_score设置为0
                    return paperMapper.editState2(state,ID,0);
                }else {
                    paperMapper.editState2(state,ID,1);
                    return paperMapper.updateScore(stuID,score);
                }
            }
            paperMapper.updateScore(stuID,score);
        }
        return paperMapper.editState(state,ID);
    }
    public List<Paper> searchPaperByConditions(String studentName, String state, String name, String pointFront, String pointBack, String pub) {
        List<Paper> list = paperMapper.searchPaperByConditions(studentName, state, name, pointFront, pointBack, pub);
//        List<Operation> operationList = operationMapper.selectTypeAllOperationList("学术论文");
//        List<Operation> monographList = new ArrayList<>();
//        //可优化
//        for (int i = 0;i < list.size(); i++) {
//            monographList = new ArrayList<>();
//            for (int j = 0;j < operationList.size(); j++) {
//                if(operationList.get(j).getProdId() == (int)list.get(i).getID()) {
//                    monographList.add(operationList.get(j));
//                }
//            }
//            list.get(i).s(monographList);
//        }
//        return setMonographOperation(list);
        return list;
    }
}
