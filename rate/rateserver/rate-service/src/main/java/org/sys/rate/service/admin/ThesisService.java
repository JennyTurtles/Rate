package org.sys.rate.service.admin;

import lombok.var;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.ThesisMapper;
import org.sys.rate.model.Thesis;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ThesisService {
    @Resource
    private ThesisMapper thesisMapper;

    public Integer upsert(List<Thesis> thesisList, Integer year, Integer month, String type) {
        int rows = 0;
        for (var thesis : thesisList) {
            if ("teacher".equals(type)) {
                thesis.setGrade(0d);
            }
            thesisMapper.upsert(thesis, year, month);
            if (thesis.getID() != null) {
                ++rows;
            }
        }
        return rows;
    }

    public void editThesisName(Integer thesisId, String thesisName) {
        thesisMapper.editThesisName(thesisId, thesisName);
    }

    public Integer notExistOrUpdate(Thesis thesis) {
        return thesisMapper.notExistOrUpdate(thesis);
    }

    public Integer getStuId(Integer studentID) {
        return thesisMapper.getStuId(studentID);
    }
}
