package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.sys.rate.model.Product;

import java.util.List;

/**
 * 专利成果Mapper接口
 *
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface ProductMapper
{
    /**
     * 通过ID获取Product
     *
     * @param ID
     * @return
     */
    @Select("select * from i_application where ID = #{ID}")
    Product getById(Integer ID);

    /**
     * 查询专利成果
     *
     * @param ID 专利成果ID
     * @return 专利成果
     */
    public Product selectProductById(Long ID);

    /**
     * 查询专利成果列表
     *
     * @param i_application 专利成果
     * @return 专利成果集合
     */
    public List<Product> selectProductList(Product product);

    /**
     * 新增专利成果
     *
     * @param product 专利成果
     * @return 结果
     */
    public int insertProduct(Product product);

    /**
     * 修改专利成果
     */
    public int updateProduct(Product product);

    public int editState(String state, Long ID);
    /**
     * 删除专利成果
     *
     * @param ID 专利成果ID
     * @return 结果
     */
    public int deleteProductById(Long ID);

    /**
     * 批量删除专利成果
     *
     * @param IDs 需要删除的数据ID
     * @return 结果
     */
    public int deleteProductByIds(String[] IDs);

    public List<Product> selectList();
    public List<Product> selectListById(@Param("studentID") Integer studentID, @Param("page") Integer page, @Param("size") Integer size);
    public List<Product> selectListByIds(@Param("studentID") Integer studentID);


    @Select("SELECT ID FROM i_application WHERE studentID = #{stuID} AND point = 2 AND state = 'adm_pass' LIMIT 1")
    public Integer checkScore(Long stuID);

    @Update("UPDATE i_application SET state = #{state},have_score = #{valid} WHERE ID = #{ID}")
    public Integer editState2(String state, Long ID, Integer valid);

    @Update("UPDATE graduatestudent SET point = point + #{score} WHERE ID = #{stuID}")
    public int updateScore(Long stuID,Long score);

    @Select("SELECT * FROM i_application WHERE  ID = #{ID}")
    public Product selectByID(Long ID);

    public List<Product> searchProductByConditions(String studentName, String state, String productName, String pointFront, String pointBack);
    public Integer selectProductNumberOfPendingMessing(String state);

}
