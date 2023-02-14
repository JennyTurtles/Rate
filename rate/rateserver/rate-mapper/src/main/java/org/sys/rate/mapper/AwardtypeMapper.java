package org.sys.rate.mapper;


import org.sys.rate.model.Award;
import org.sys.rate.model.Awardtype;

import java.util.List;

/**
 * 类别Mapper接口
 * 
 * @author system
 * @date 2022-03-13
 */
public interface AwardtypeMapper 
{
    /**
     * 查询类别
     * 
     * @param ID 类别ID
     * @return 类别
     */
    public Awardtype selectAwardtypeById(Long ID);

    /**
     * 查询类别列表
     * 
     * @param awardtype 类别
     * @return 类别集合
     */
    public List<Awardtype> selectAwardtypeList(Awardtype awardtype);

    /**
     * 新增类别
     * 
     * @param awardtype 类别
     * @return 结果
     */
    public int insertAwardtype(Awardtype awardtype);

    /**
     * 修改类别
     * 
     * @param awardtype 类别
     * @return 结果
     */
    public int updateAwardtype(Awardtype awardtype);

    /**
     * 删除类别
     * 
     * @param ID 类别ID
     * @return 结果
     */
    public int deleteAwardtypeById(Long ID);

    /**
     * 批量删除类别
     * 
     * @param IDs 需要删除的数据ID
     * @return 结果
     */
    public int deleteAwardtypeByIds(String[] IDs);

    public List<Awardtype> selectList();
}
