package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.sys.rate.model.*;

@Mapper
public interface LoginMapper {
    @Select("select * from admin where username = #{username}")
    public Admin loginAdmin(LoginInf loginInf);

    @Select("select * from student where username = #{username}")
    public Student loginStudent(LoginInf loginInf);

    @Select("select * from teacher where username = #{username}")
    public Teacher loginTeacher(LoginInf loginInf);

    @Update("update ${table} set password = #{password} where ID = #{ID}")
    public int updatePassword(String table, Integer ID, String password);

    @Select("select count(ID) from ${table} where password = #{password} and ID = #{id}")
    int checkPassword(String table, Integer id, String password);
}
