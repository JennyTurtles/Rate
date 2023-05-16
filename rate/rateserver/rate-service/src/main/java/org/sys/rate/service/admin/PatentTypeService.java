package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.PatentTypeMapper;
import org.sys.rate.model.PatentType;

@Service
public class PatentTypeService {

    @Autowired
    private PatentTypeMapper patentTypeMapper;

    public PatentType getById(Integer id) {
        return patentTypeMapper.getById(id);
    }

}
