package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.DictMapper;

@Service
public class DictService {
    @Autowired
    private DictMapper dictMapper;

    public String getDictValue(String item) {
        return dictMapper.getDictValue(item);
    }
}
