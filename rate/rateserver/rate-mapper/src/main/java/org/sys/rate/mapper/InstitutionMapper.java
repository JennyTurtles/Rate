package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Param;
import org.sys.rate.model.Institution;

import java.util.List;

public interface InstitutionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Institution record);

    int delete(Institution record);

    int update(Institution record);

    int insertSelective(Institution record);


    List<Institution> getInstitutionByPage(@Param("page") Integer page, @Param("size") Integer size, @Param("emp") Institution employee);

    List<Institution> getInstitutionByPageAndCompany(@Param("page") Integer page, @Param("size") Integer size, @Param("company") String company);

    Long getTotal(@Param("emp") Institution employee);

    Long getTotalByCompany(@Param("company") String company);

    Integer maxWorkID();


    Integer updateEmployeeSalaryById(@Param("eid") Integer eid, @Param("sid") Integer sid);
}
