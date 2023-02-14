package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.sys.rate.model.*;

@Mapper
public interface LoginMapper{
    @Select("select * from admin where username = #{username}")
    public Admin loginAdmin(LoginInf loginInf);

    @Select("select * from student where username = #{username}")
    public Student loginStudent(LoginInf loginInf);

    @Select("select * from teacher where username = #{username}")
    public Teacher loginTeacher(LoginInf loginInf);
}
