package org.sys.rate.service.admin;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.ProductMapper;
import org.sys.rate.model.Product;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.List;

@Service
public class ProductService {
    @Resource
    private ProductMapper productMapper;

    public Product selectProductById(Long ID){
        return productMapper.selectProductById(ID);
    }

    /**
     * 通过ID寻找paper信息
     * @param ID
     * @return paper
     */
    public Product getById(Integer ID){
        Product paper = productMapper.getById(ID);
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
    public List<Product> selectProductList(Product paper){
        return productMapper.selectProductList(paper);
    }

    public List<Product> selectListById(@Param("studentID") Integer studentID, @Param("page") Integer page, @Param("size") Integer size){
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Product> p=productMapper.selectListById(studentID,page,size);
//        System.out.println(p);
        return p;
    }
    public List<Product> selectListByIds(@Param("studentID") Integer studentID){
        return productMapper.selectListByIds(studentID);
    }

    /**
     * 新增论文成果
     *
     * @param product 论文成果
     * @return 结果
     */
    public int insertProduct(Product product){
        return productMapper.insertProduct(product);
    }

    /**
     * 修改论文成果
     *
     * @param paper 论文成果
     * @return 结果
     */
    public int updateProduct(Product paper){
        return productMapper.updateProduct(paper);
    }

    /**
     * 删除论文成果
     *
     * @param ID 论文成果ID
     * @return 结果
     */
    public int deleteProductById(Long ID){
        return productMapper.deleteProductById(ID);
    }

    //    老师界面调用paper
    public List<Product> selectList(){
        List<Product> res = productMapper.selectList();
        return res;
    }

    //    修改论文状态
    public int editState(String state, Long ID) throws MessagingException {
        //mailToStuServicei.sendStuMail(state, paper, "论文");
        return productMapper.editState(state,ID);
    }
    public List<Product> searchProductByConditions(String studentName, String state, String projectName, String pointFront, String pointBack) {
        List<Product> list = productMapper.searchProductByConditions(studentName, state, projectName, pointFront, pointBack);
        return list;
    }
}
