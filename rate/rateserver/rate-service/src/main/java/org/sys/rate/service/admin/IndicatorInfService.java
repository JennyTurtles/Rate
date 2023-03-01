package org.sys.rate.service.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.bind.annotation.PathVariable;
import org.sys.rate.mapper.IndicatorInfMapper;
import org.sys.rate.model.*;

import java.util.List;

@Service
@Transactional
public class IndicatorInfService {
    @Autowired
    private IndicatorInfMapper indicatorInfMapper;

    public Integer getMaxID(String formName){return  indicatorInfMapper.getMaxID(formName);}

    //期刊
    public List<IndicatorPublication> getPublication (Integer indicatorID){return indicatorInfMapper.getPublication(indicatorID);}

    public boolean savePublication(IndicatorPublication indicatorPublication){
//        Integer id = getIdByNameAndYear(indicatorPublication.getName(),indicatorPublication.getYear());
//        if (id != null) // 已经存在重复的记录，直接在重复记录上编辑
//        {
//            indicatorPublication.setID(id);
//            return updatePublicationById(indicatorPublication);
//        }
//        else // 不存在重复记录，添加记录
//            return indicatorInfMapper.savePublication(indicatorPublication) > 0;
        return indicatorInfMapper.savePublication(indicatorPublication) > 0;
    }

    public int savePublications(List<IndicatorPublication> indicatorPublications){
        return indicatorInfMapper.savePublications(indicatorPublications);
    }

    public Integer getIdByNameAndYear(String name, Integer year){
        return indicatorInfMapper.getIdByNameAndYear(name,year);
    }

    public boolean deletePublication(Integer id){return indicatorInfMapper.deletePublication(id) > 0;}

    public boolean updatePublicationById(IndicatorPublication indicatorPublication){
        Integer id = getIdByNameAndYear(indicatorPublication.getName(),indicatorPublication.getYear());
        if (id != null && !id.equals(indicatorPublication.getID()))
            return false;
        return indicatorInfMapper.updatePublicationById(indicatorPublication) > 0;
    }

    public List<IndicatorPublication> getPublicationByIndicatorAndYear(Integer indicatorID,Integer year){
        return indicatorInfMapper.getPublicationByIndicatorAndYear(indicatorID,year);
    }

    public List<IndicatorPublication> getPublicationByIndicatorAndYear2(Integer indicatorID,Integer year,Integer pageNum,Integer pageSize){
        Page page = PageHelper.startPage(pageNum, pageSize);
        PageInfo info = new PageInfo<>(page.getResult());
        return indicatorInfMapper.getPublicationByIndicatorAndYear(indicatorID,year);
    }

    public Integer getMaxYearByIndicatorId(Integer indicatorId){
        return indicatorInfMapper.getMaxYearByIndicatorId(indicatorId);
    }

    //科技奖
    public List<IndicatorAward> getAward(Integer indicatorID){return indicatorInfMapper.getAward(indicatorID);}

    public boolean saveAward(IndicatorAward indicatorAward){return indicatorInfMapper.saveAward(indicatorAward) > 0;}

    public boolean deleteAward (Integer id){return indicatorInfMapper.deleteAward(id) > 0;}

    public boolean updateAward(IndicatorAward indicatorAward){return indicatorInfMapper.updateAward(indicatorAward) > 0;}

    //纵向科研项目
    public List<IndicatorProgram> getProgram(Integer id){return indicatorInfMapper.getProgram(id);}

    public boolean saveProgram(IndicatorProgram indicatorProgram){return indicatorInfMapper.saveProgram(indicatorProgram) > 0;}

    public boolean deleteProgram (Integer id){return indicatorInfMapper.deleteProgram(id) > 0;}

    public boolean updateProgram(IndicatorProgram indicatorProgram){return indicatorInfMapper.updateProgram(indicatorProgram) > 0;}

    //决策咨询成果
    public List<IndicatorDecision> getDecision(Integer id){return indicatorInfMapper.getDecision(id);}

    public boolean saveDecision(IndicatorDecision indicatorDecision){return indicatorInfMapper.saveDecision(indicatorDecision) > 0;}

    public boolean deleteDecision (Integer id){return indicatorInfMapper.deleteDecision(id) > 0;}

    public boolean updateDecision(IndicatorDecision indicatorDecision){return indicatorInfMapper.updateDecision(indicatorDecision) > 0;}

    //查询四个表内所有的ID,name,indicatorID
    public List<IndicatorSearchResult> getSearchPublication(){return indicatorInfMapper.getSearchPublication();}

    public List<IndicatorSearchResult> getSearchAward(){return indicatorInfMapper.getSearchAward();}

    public List<IndicatorSearchResult> getSearchProgram(){return indicatorInfMapper.getSearchProgram();}

    public List<IndicatorSearchResult> getSearchDecision(){return indicatorInfMapper.getSearchDecision();}

    public List<Indicator> getSearchIndicator(Integer id){return indicatorInfMapper.getSearchIndicator(id);}

    //查询四个表内单条信息通过id
    public List<IndicatorPublication> getSearchPublicationById(Integer id){return indicatorInfMapper.getSearchPublicationById(id);}

    public List<IndicatorAward> getSearchAwardById(Integer id){return indicatorInfMapper.getSearchAwardById(id);}

    public List<IndicatorProgram> getSearchProgramById(Integer id){return indicatorInfMapper.getSearchProgramById(id);}

    public List<IndicatorDecision> getSearchDecisionById(Integer id){return indicatorInfMapper.getSearchDecisionById(id);}

}
