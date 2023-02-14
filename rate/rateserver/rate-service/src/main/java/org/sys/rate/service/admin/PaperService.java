package org.sys.rate.service.admin;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.PaperMapper;
import org.sys.rate.model.Paper;
import org.sys.rate.mapper.PublicationMapper;
import org.sys.rate.model.Publication;

import org.w3c.dom.Text;
import java.util.List;

@Service
public class PaperService {

    @Autowired
    private PaperMapper paperMapper;

    public Paper selectPaperById(Long ID){
        return paperMapper.selectPaperById(ID);
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
//        System.out.println(studentID);
//        System.out.println("...");
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
        List<Paper> papers = paperMapper.selectList();
        return paperMapper.selectList();
    }

//    修改论文状态
    public int edit_state(String state, Long ID){
        return paperMapper.editState(state,ID);
    }
}
