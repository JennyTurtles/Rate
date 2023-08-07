package org.sys.rate.service.admin;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sys.rate.mapper.IndicatorMapper;
import org.sys.rate.mapper.PublicationMapper;
import org.sys.rate.model.Indicator;
import org.sys.rate.model.Production;
import org.sys.rate.model.Project;
import org.sys.rate.model.Publication;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IndicatorService {
    @Autowired
    private IndicatorMapper indicatorMapper;
    @Resource
    private PublicationMapper publicationMapper;


    public List<Indicator> getAll (){return indicatorMapper.getAll();}

    public String selectOrder(Integer id){return indicatorMapper.selectOrder(id);}

    public boolean save(Indicator indicator){return indicatorMapper.save(indicator) > 0;}

    public boolean delete(Integer id){return indicatorMapper.delete(id) > 0;}

    public boolean update(Indicator indicator){return  indicatorMapper.update(indicator) > 0;}

    public boolean updateScoreName(Indicator indicator){return indicatorMapper.updateScoreName(indicator) > 0;}

    public boolean updateType(Indicator indicator){return indicatorMapper.updateType(indicator) > 0;}

    public boolean updateOrder(Indicator indicator){return indicatorMapper.updateOrder(indicator) > 0;}

    public boolean updateAllField(Indicator indicator){return indicatorMapper.updateAllField(indicator) > 0;}

    public Integer selectScoreById(Integer id){return indicatorMapper.selectScoreById(id);}

    public Integer getMaxYear(Integer id,Integer year){return indicatorMapper.getMaxYear(id,year);}

    public Integer getIndicatorId(String order) {
        return indicatorMapper.getIndicatorId(order);
    }

    public ArrayList<Integer> getAllYearById(Integer indicatorId, String indicatorType) {
        return indicatorMapper.getAllYearByIdType(indicatorId, indicatorType);
    }

    public void clone(Integer fromYear, Integer toYear, Long indicatorId, String indicatorType) {
        if("publication".equals(indicatorType)){
            indicatorMapper.clonePublication(fromYear, toYear, indicatorId);
        }else if("project".equals(indicatorType)){
            indicatorMapper.cloneProject(fromYear, toYear, indicatorId);
        }
    }

    public List<String> getProductNamesByTypeName(String indicatorType, String name) {
        List<String> productNamesByTypeName = indicatorMapper.getProductNamesByTypeName(indicatorType, name);
        return productNamesByTypeName;
    }

    public <T> List<T> getProductByTypeName(String indicatorType, String fullName) {
        if ("publication".equals(indicatorType)) {
            List<Publication> publications = publicationMapper.getPublicationInfByName(fullName);
            return castList(publications);
        } else if ("project".equals(indicatorType)) {
            List<Project> projects = indicatorMapper.getProjectByName(fullName);
            return castList(projects);
        }
        return new ArrayList<>();
    }

    private <T> List<T> castList(List<?> list) {
        return list.stream()
                .map(element -> (T) element)
                .collect(Collectors.toList());
    }

}
