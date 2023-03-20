package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.apache.ibatis.annotations.Select;
import org.sys.rate.model.Admin;
import org.sys.rate.model.Experts;
import org.sys.rate.model.Role;
import org.sys.rate.model.Student;

import java.util.List;

@Mapper
public interface StudentMapper {
    List<Student> getTotal();

    Student loadUserByUsername(String username);
//    List<Role> getAdminRolesById(Integer id);

    Student getById(Integer ID);

    int insert(Student record);

    int delete(Student record);

    int update(Student record);

    int check(@Param("IDNumber") String record);

    int insertFROMImport(Student record);

    int updateFROMImport(Student record);
    public List<Student> selectList();
    int deleteStudent(Integer ID);
}
