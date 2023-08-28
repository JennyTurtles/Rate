package org.sys.rate.service.admin;

import lombok.var;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.PaperCommentMapper;
import org.sys.rate.mapper.ThesisMapper;
import org.sys.rate.model.PaperComment;
import org.sys.rate.model.Student;
import org.sys.rate.model.Thesis;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ThesisService {
    @Resource
    private ThesisMapper thesisMapper;

    public Integer upsert(List<Thesis> thesisList, Integer year, Integer month) {
        int rows = 0;
        for(var thesis:thesisList){
            if(thesis.getStudentID()==null||thesis.getStudentID().equals(-1)||thesis.getTutorID().equals(-1)){
                continue;
            }
            thesisMapper.upsert(thesis, year, month);
            if(thesis.getID()!=null){
                ++rows;
            }
        }
        return rows;
    }

}
