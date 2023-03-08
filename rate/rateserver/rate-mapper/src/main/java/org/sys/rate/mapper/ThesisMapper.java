package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.sys.rate.model.Thesis;

import java.util.List;

@Mapper
public interface ThesisMapper {
    // 通过学生ID获取学生所有的毕业论文或设计
    @Select("select * from thesis where studentID=#{studentID}")
    public List<Thesis> getAll(int studentID);

    // 通过thesisID获取thesis
    @Select("select * from thesis where ID =  #{thesisID}")
    Thesis getByID(int thesisID);


}
