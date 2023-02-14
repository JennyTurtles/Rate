package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.sys.rate.model.Activities;
import org.sys.rate.model.Log;

import java.util.List;

@Mapper
public interface LogMapper {

    int insert(Log record);

    List<Log> getLogByPage(@Param("page") Integer page, @Param("size") Integer size,@Param("institutionID") Integer institutionID);

    Long getTotal(@Param("emp") Log employee, @Param("institutionID") Integer institutionID);
}
