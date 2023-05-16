package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Select;
import org.sys.rate.model.PatentType;

public interface PatentTypeMapper {
    @Select("select * from patenttype where ID = #{id}")
    PatentType getById(Integer id);
}
