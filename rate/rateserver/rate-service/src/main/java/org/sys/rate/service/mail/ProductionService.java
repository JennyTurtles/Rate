package org.sys.rate.service.mail;

import org.springframework.stereotype.Service;
import org.sys.rate.mapper.PublicationMapper;
import org.sys.rate.mapper.productionMapper;
import org.sys.rate.model.Production;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @param: $
 * @return: $
 */
@Service
public class ProductionService {

    @Resource
    private productionMapper productionMapper;

    @Resource
    private PublicationMapper publicationMapper;

    private static final Map<String, String> typeToTableMap = new HashMap<>();

    static {
        typeToTableMap.put("授权专利", "i_patent");
        typeToTableMap.put("科研获奖", "i_award");
        typeToTableMap.put("纵向科研项目", "i_project");
        typeToTableMap.put("制定标准", "i_standard");
        typeToTableMap.put("决策咨询", "i_decision");
        typeToTableMap.put("学术专著和教材", "i_book");
        typeToTableMap.put("制造或设计的产品", "i_application");
        typeToTableMap.put("学科竞赛", "i_competition");
    }

    public Production checkProductionById(int productionId, String type) {
        if ("纵向科研项目".equals(type)) {
            return productionMapper.checkProjectById(productionId);
        } else if (typeToTableMap.containsKey(type)) {
            return productionMapper.checkProductionById(typeToTableMap.get(type), productionId);
        } else {
            throw new IllegalArgumentException("不支持的类型: " + type);
        }
    }

    public int editState(int productionId, String type, String state) {
        if (typeToTableMap.containsKey(type)) {
            return productionMapper.editState(typeToTableMap.get(type), productionId, state);
        } else {
            throw new IllegalArgumentException("不支持的类型: " + type);
        }
    }

    public String getPublicationName(Long publicationID) {
        return publicationMapper.getPublicationNameById(publicationID);
    }
}






