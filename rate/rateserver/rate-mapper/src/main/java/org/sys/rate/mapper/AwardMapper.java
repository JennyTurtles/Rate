package org.sys.rate.mapper;

import org.sys.rate.model.Award;

import java.util.List;

/**
 * 回答Mapper接口
 * 
 * @author system
 * @date 2022-03-13
 */
public interface AwardMapper 
{
    /**
     * 查询回答
     * 
     * @param ID 回答ID
     * @return 回答
     */
    public Award selectAwardById(Long ID);

    /**
     * 查询回答列表
     * 
     * @param award 回答
     * @return 回答集合
     */
    public List<Award> selectAwardList(Award award);

    /**
     * 新增回答
     * 
     * @param award 回答
     * @return 结果
     */
    public int insertAward(Award award);

    /**
     * 修改回答
     * 
     * @param award 回答
     * @return 结果
     */
    public int updateAward(Award award);

    /**
     * 删除回答
     * 
     * @param ID 回答ID
     * @return 结果
     */
    public int deleteAwardById(Long ID);

    /**
     * 批量删除回答
     * 
     * @param IDs 需要删除的数据ID
     * @return 结果
     */
    public int deleteAwardByIds(String[] IDs);

    public List<Award> selectList();
}
