package org.sys.rate.service.admin;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.ProductMapper;
import org.sys.rate.model.Product;
import org.sys.rate.model.XinProject;
import org.sys.rate.service.mail.MailToStuService;
import org.sys.rate.utils.ProjectTypeEnums;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.List;

@Service
public class ProductService {
    @Resource
    private ProductMapper productMapper;
    @Resource
    private MailToStuService mailToStuService;
    @Resource
    private XinProjectService xinProjectService;

    private void dealXinProject(Product dto, int type){
        XinProject xinProject = new XinProject(dto.getName(), dto.getPoint(), dto.getAuthor(),
                dto.getState(), dto.getRemark(), dto.getId(), ProjectTypeEnums.PRODUCT_APPLICATION.getDisplayName(), dto.getStudentId());
        if(type ==1) {
            xinProjectService.insertXinProject(xinProject);
        }else if(type == 2){
            xinProjectService.updateXinProject(xinProject);
        }else if(type == 3){
            xinProjectService.updateXinProject(xinProject.getMid(), xinProject.getType(), xinProject.getState());
        }else if(type == 4){
            xinProjectService.deleteXinProject(xinProject.getMid(), xinProject.getType());
        }
    }
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
        int rlt = productMapper.insertProduct(product);
        dealXinProject(product, 1);
        return rlt;
    }

    /**
     * 修改论文成果
     *
     * @param product 论文成果
     * @return 结果
     */
    public int updateProduct(Product product){
        dealXinProject(product, 2);
        return productMapper.updateProduct(product);
    }

    /**
     * 删除论文成果
     *
     * @param ID 论文成果ID
     * @return 结果
     */
    public int deleteProductById(Long ID){
        Product product = new Product();
        product.setId(Math.toIntExact(ID));
        dealXinProject(product, 4);
        return productMapper.deleteProductById(ID);
    }

    //    老师界面调用paper
    public List<Product> selectList(){
        List<Product> res = productMapper.selectList();
        return res;
    }

    //    修改论文状态
    public int editState(String state, Long ID) throws MessagingException {
        Product product = productMapper.getById(Math.toIntExact(ID));
        mailToStuService.sendStuMail(state, product, null, "产品应用");
        product.setState(state);
        dealXinProject(product, 3);
        return productMapper.editState(state,ID);
    }
    public List<Product> searchProductByConditions(String studentName, String state, String projectName, String pointFront, String pointBack) {
        List<Product> list = productMapper.searchProductByConditions(studentName, state, projectName, pointFront, pointBack);
        return list;
    }
}
