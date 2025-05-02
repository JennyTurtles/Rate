package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DictMapper {

    @Select("select val from dict where item = #{item}")
    String getDictValue(String item) ;
}
