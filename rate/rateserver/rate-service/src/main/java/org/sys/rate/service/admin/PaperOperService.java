package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.PaperOperMapper;
import org.sys.rate.model.PaperOper;

import java.util.List;

@Service
public class PaperOperService {
    @Autowired
    private PaperOperMapper paperoperMapper;


    public int insertPaperOper(PaperOper paperoper){
        return paperoperMapper.insertPaperoper(paperoper);
    }
    public List<PaperOper> selectList(Long ID){
        return paperoperMapper.selectList(ID);
    }
}
