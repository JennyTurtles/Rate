package org.sys.rate.service.admin;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.StandardMapper;
import org.sys.rate.mapper.StandardMapper;
import org.sys.rate.model.Standard;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.List;

@Service
public class StandardService {
    @Resource
    private StandardMapper standardMapper;

    public Standard selectStandardById(Long ID){
        return standardMapper.selectStandardById(ID);
    }

    /**
     * 通过ID寻找paper信息
     * @param ID
     * @return paper
     */
    public Standard getById(Integer ID){
        Standard paper = standardMapper.getById(ID);
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
    public List<Standard> selectStandardList(Standard paper){
        return standardMapper.selectStandardList(paper);
    }

    public List<Standard> selectListById(@Param("studentID") Integer studentID, @Param("page") Integer page, @Param("size") Integer size){
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Standard> p=standardMapper.selectListById(studentID,page,size);
//        System.out.println(p);
        return p;
    }
    public List<Standard> selectListByIds(@Param("studentID") Integer studentID){
        return standardMapper.selectListByIds(studentID);
    }

    /**
     * 新增论文成果
     *
     * @param standard 论文成果
     * @return 结果
     */
    public int insertStandard(Standard standard){
        return standardMapper.insertStandard(standard);
    }

    /**
     * 修改论文成果
     *
     * @param paper 论文成果
     * @return 结果
     */
    public int updateStandard(Standard paper){
        return standardMapper.updateStandard(paper);
    }

    /**
     * 删除论文成果
     *
     * @param ID 论文成果ID
     * @return 结果
     */
    public int deleteStandardById(Long ID){
        return standardMapper.deleteStandardById(ID);
    }

    //    老师界面调用paper
    public List<Standard> selectList(){
        List<Standard> res = standardMapper.selectList();
        return res;
    }

    //    修改论文状态
    public int editState(String state, Long ID) throws MessagingException {
        //mailToStuServicei.sendStuMail(state, paper, "论文");
        return standardMapper.editState(state,ID);
    }
    public List<Standard> searchStandardByConditions(String studentName, String state, String projectName, String pointFront, String pointBack) {
        List<Standard> list = standardMapper.searchStandardByConditions(studentName, state, projectName, pointFront, pointBack);
        return list;
    }
}
