package org.sys.rate.service.admin;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.PatentMapper;
import org.sys.rate.model.Patent;

import javax.annotation.Resource;
import javax.mail.MessagingException;
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

    //    修改论文状态
    public int editState(String state, Long ID) throws MessagingException {
        //mailToStuServicei.sendStuMail(state, paper, "论文");
        return patentMapper.editState(state,ID);
    }
}
