package org.sys.rate.service.admin;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.sys.rate.mapper.IndicatorMapper;
import org.sys.rate.mapper.PublicationMapper;
import org.sys.rate.model.Indicator;
import org.sys.rate.model.Publication;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PublicationService {

    @Resource
    private PublicationMapper publicationMapper;

    @Resource
    private IndicatorMapper indicatorMapper;

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
     * @param publicationName :
     * @param year
     * @Return List<String>
     */
    public List<String> selectPublicationListByName(String publicationName, Integer year) {
        List<String> res = publicationMapper.getPublicationNamesByNameYear(publicationName);
        return res;
    }

    public List<String> selectPublicationListByName(String publicationName) {
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
        if(publication.getId()==null) {
            publicationMapper.insertPublication(publication);
        }
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

    public Indicator chooseBestIndicator(Publication publication , Integer year){
        if (publication == null)
            return null;
        List<Indicator> indicatorList = publication.getIndicatorList();
        Set<Integer> indicatorIDSet = new HashSet<>();
        for (Indicator indicator : indicatorList) {
            Integer indicatorID = indicator.getId();
            if (indicatorIDSet.contains(indicatorID)) // 已经按年份降序排列。对于同一个指标点，只判断最大的年份即可。
                continue;
            indicatorIDSet.add(indicatorID);
            Integer maxYear = indicatorMapper.getMaxYear(indicatorID, year);
            if (maxYear <= indicator.getYear()) // 已经按照积分降序排列，找到第一个满足条件的指标点即可。
                return indicator;
        }
        return null;
    }
}
