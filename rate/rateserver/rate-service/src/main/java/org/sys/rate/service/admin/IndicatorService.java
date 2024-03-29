package org.sys.rate.service.admin;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sys.rate.mapper.*;
import org.sys.rate.model.*;

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

    @Resource
    private ProjectTypeMapper projectTypeMapper;

    @Resource
    private AwardTypeMapper awardTypeMapper;

    @Resource
    private DecisionTypeMapper decisionTypeMapper;

    @Resource
    private CompetitionTypeMapper competitionTypeMapper;

    public List<Indicator> getAll (){return indicatorMapper.getAll();}
    public List<Indicator> getAllByType (String type){return indicatorMapper.getAllByType(type);}

    public List<TreeNode> removeType(List<TreeNode> list, String type){
        List<TreeNode> res = new ArrayList<>();
        for(TreeNode node : list){
            List<TreeNode> children = node.getChildren();
            node.setChildren(null);
            List<TreeNode> newChildren = new ArrayList<>();
            if(children != null){
               for (TreeNode child : children){
                   if (type.equals(child.getType())){
                       newChildren.add(child);
                   }
               }
            }
            if (!newChildren.isEmpty()){
                node.setChildren(newChildren);
                res.add(node);
            }
        }
        return res;
    }

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
        }else if("award".equals(indicatorType)) {
//            indicatorMapper.cloneAward(fromYear, toYear, indicatorId);
        }else if("decision".equals(indicatorType)){
            indicatorMapper.cloneDecision(fromYear, toYear, indicatorId);
        }else if("competition".equals(indicatorType)){
//            indicatorMapper.cloneCompetition(fromYear, toYear, indicatorId);
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
            // 这里需要传入indicator的相关信息，所以用project来作为返回类型
            List<Project> projects = indicatorMapper.getProjectByName(fullName);
            return castList(projects);
        } else if ("award".equals(indicatorType)){
            List<Award> awards = awardTypeMapper.getAwardByName(fullName);
            return castList(awards);
        } else if ("decision".equals(indicatorType)){
            List<Decision> decisions = decisionTypeMapper.getDecisionByName(fullName);
            return castList(decisions);
        } else if ("competition".equals(indicatorType)){
            List<Competition> competitions = competitionTypeMapper.getCompetitionByName(fullName);
            return castList(competitions);
        }
        return new ArrayList<>();
    }

    private <T> List<T> castList(List<?> list) {
        return list.stream()
                .map(element -> (T) element)
                .collect(Collectors.toList());
    }

    public <T> List<T> selectProductListByYear(Integer indicatorId, Integer year, String type) {
        if ("publication".equals(type)) {
            List<Publication> publications = publicationMapper.selectPublicationListByYear(indicatorId, year);
            return castList(publications);
        } else if ("project".equals(type)) {
            List<ProjectType> projectTypes = projectTypeMapper.selectProjectTypeListByYear(indicatorId,year);
            return castList(projectTypes);
        }else if ("award".equals(type)) {
            List<AwardType> awardTypes = awardTypeMapper.selectAwardTypeListByYear(indicatorId,year);
            return castList(awardTypes);
        }else if ("decision".equals(type)){
            List<DecisionType> decisionTypes = decisionTypeMapper.selectDecisionTypeListByYear(indicatorId,year);
            return castList(decisionTypes);
        }else if ("competition".equals(type)){
            List<CompetitionType> competitions = competitionTypeMapper.selectCompetitionTypeListByYear(indicatorId,year);
            return castList(competitions);
        }
        return new ArrayList<>();
    }
}
