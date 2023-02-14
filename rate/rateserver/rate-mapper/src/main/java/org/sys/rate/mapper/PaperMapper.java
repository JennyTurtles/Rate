package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.sys.rate.model.Award;
import org.sys.rate.model.Paper;

import org.w3c.dom.Text;
import java.util.List;

/**
 * 论文成果Mapper接口
 * 
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface PaperMapper 
{
    /**
     * 查询论文成果
     * 
     * @param ID 论文成果ID
     * @return 论文成果
     */
    public Paper selectPaperById(Long ID);

    /**
     * 查询论文成果列表
     * 
     * @param paper 论文成果
     * @return 论文成果集合
     */
    public List<Paper> selectPaperList(Paper paper);

    /**
     * 新增论文成果
     * 
     * @param paper 论文成果
     * @return 结果
     */
    public int insertPaper(Paper paper);

    /**
     * 修改论文成果
     * 
     * @param paper 论文成果
     * @return 结果
     */
    public int updatePaper(Paper paper);

    public int editState(String state, Long ID);
    /**
     * 删除论文成果
     * 
     * @param ID 论文成果ID
     * @return 结果
     */
    public int deletePaperById(Long ID);

    /**
     * 批量删除论文成果
     * 
     * @param IDs 需要删除的数据ID
     * @return 结果
     */
    public int deletePaperByIds(String[] IDs);

    public List<Paper> selectList();
    public List<Paper> selectListById(@Param("studentID") Integer studentID, @Param("page") Integer page, @Param("size") Integer size);
    public List<Paper> selectListByIds(@Param("studentID") Integer studentID);
}
