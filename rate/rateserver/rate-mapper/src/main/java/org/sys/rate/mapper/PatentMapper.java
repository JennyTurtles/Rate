package org.sys.rate.mapper;

import java.util.List;

import org.sys.rate.model.Award;
import org.sys.rate.model.Patent;

/**
 * 著作Mapper接口
 * 
 * @author system
 * @date 2022-03-13
 */
public interface PatentMapper 
{
    /**
     * 查询著作
     * 
     * @param ID 著作ID
     * @return 著作
     */
    public Patent selectPatentById(Long ID);

    /**
     * 查询著作列表
     * 
     * @param patent 著作
     * @return 著作集合
     */
    public List<Patent> selectPatentList(Patent patent);

    /**
     * 新增著作
     * 
     * @param patent 著作
     * @return 结果
     */
    public int insertPatent(Patent patent);

    /**
     * 修改著作
     * 
     * @param patent 著作
     * @return 结果
     */
    public int updatePatent(Patent patent);

    /**
     * 删除著作
     * 
     * @param ID 著作ID
     * @return 结果
     */
    public int deletePatentById(Long ID);

    /**
     * 批量删除著作
     * 
     * @param IDs 需要删除的数据ID
     * @return 结果
     */
    public int deletePatentByIds(String[] IDs);

    public List<Patent> selectList();
}
