package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.apache.ibatis.annotations.Select;
import org.sys.rate.model.*;

import java.util.List;

@Mapper
public interface StudentMapper {
    List<Student> getTotal();

    Student loadUserByUsername(String username);
//    List<Role> getAdminRolesById(Integer id);
    Student getStuByIDNumber(String IDNumber);
    Student getById(Integer ID);

    int updatePassword(Integer ID,String pass);
    int insert(Student record);

    int delete(Student record);

    int update(Student record);

    int check(@Param("IDNumber") String record);
    List<Student> checkAndReturnID(List<Student> stuList);

    int updatePasswordAndUsername(Student record);
    int insertStuFromRegister(Student record);//注册添加
    int insertFROMImport(Student record);
    int insertStuFromExcel(Student record);
    int updateFROMImport(Student record);
    public List<Student> selectList();
    int deleteStudent(Integer ID);

    int insertFromAdminExcel(List<Student> record);
    int updateFromAdminExcel(List<Student> record);


}
