package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sys.rate.mapper.IndicatorMapper;
import org.sys.rate.model.Indicator;

import java.util.List;

@Service
@Transactional
public class IndicatorService {
    @Autowired
    private IndicatorMapper indicatorMapper;

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
}
