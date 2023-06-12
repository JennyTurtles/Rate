package org.sys.rate.service.admin;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.PublicationMapper;
import org.sys.rate.model.Indicator;
import org.sys.rate.model.Publication;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class PublicationService {

    @Resource
    private PublicationMapper publicationMapper;

    /**
     * 查询刊物
     *
     * @param ID 刊物ID
     * @return 刊物
     */
    public Publication selectPublicationById(Integer id) {
        return publicationMapper.selectPublicationById(id);
    }

    /**
     * 模糊查询期刊，获取相关期刊全称
     *
     * @param publicationName:
     * @Return List<String>
     */
    public List<String> selectPublicationListByName(@Param("publicationName") String publicationName) {
        List<String> res = publicationMapper.getPublicationNamesByName(publicationName);
        return res;
    }

    /**
     * 新增刊物
     *
     * @param publication 刊物
     * @return 结果
     */
    public void insertPublication(Publication publication) {
        publicationMapper.insertPublication(publication);
        for (int i = 0; i < publication.getIndicatorList().size(); ++i) {
            publicationMapper.insertIndicatorPublication(publication.getIndicatorList().get(i).getId(), publication.getId(), publication.getDateList().get(i));
        }
    }

    /**
     * 修改刊物
     *
     * @param publication 刊物
     * @return 结果
     */
    public Integer updatePublication(Publication publication) {
        return publicationMapper.updatePublication(publication);
    }

    /**
     * 删除刊物
     *
     * @param ID 刊物ID
     * @return 结果
     */
    public Integer deletePublicationById(List<Integer> ids) {
        return publicationMapper.deletePublicationByIds(ids);
    }

}
