package org.sys.rate.service.admin;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.PublicationMapper;
import org.sys.rate.model.Publication;

import java.util.List;

@Service
public class PublicationService {

    @Autowired
    private PublicationMapper publicationMapper;

    /**
     * 查询刊物
     *
     * @param ID 刊物ID
     * @return 刊物
     */
    public Publication selectPublicationById(Long ID){
       return publicationMapper.selectPublicationById(ID);
    }

    /**
     * 查询刊物列表
     *
     * @param publication 刊物
     * @return 刊物集合
     */
    public List<Publication> selectPublicationList(Publication publication){
        return publicationMapper.selectPublicationList(publication);
    }

    //模糊查询 返回pub
    public List<Publication> selectPublicationListByName(@Param("publicationName") String publicationName){
        List<Publication> res=publicationMapper.selectListByPubName(publicationName);
//        List<String> res=paperMapper.selectListByPubName(publicationName);
//        System.out.println("res:");
//        System.out.println(res);
        return res;
    }

    /**
     * 新增刊物
     *
     * @param publication 刊物
     * @return 结果
     */
    public int insertPublication(Publication publication){
        return publicationMapper.insertPublication(publication);
    }

    /**
     * 修改刊物
     *
     * @param publication 刊物
     * @return 结果
     */
    public int updatePublication(Publication publication){
        return publicationMapper.updatePublication(publication);
    }

    /**
     * 删除刊物
     *
     * @param ID 刊物ID
     * @return 结果
     */
    public int deletePublicationById(Long ID){
        return publicationMapper.deletePublicationById(ID);
    }

    public List<Publication> selectList(){
        return publicationMapper.selectList();
    }

    public Long selectScoreById(long id)
    {
        return publicationMapper.selectScoreById(id);
    }

    public Publication selectPublicationByNameYear(String name, Integer year){return publicationMapper.selectPublicationByNameYear(name,year);}

    public List<Publication> selectPublicationListByYear(Integer year){
        return publicationMapper.selectPublicationByYear(year);
    }
}
