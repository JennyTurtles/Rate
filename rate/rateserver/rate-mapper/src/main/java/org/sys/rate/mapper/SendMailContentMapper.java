package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.sys.rate.model.SendMailContent;

/**
 * ClassName: SendMailContentMapper
 * Package: org.sys.rate.mapper
 * Description:
 *
 * @Author ZYK
 * @Create 2023/9/11 9:51
 * @Version 1.0
 */
@Mapper
public interface SendMailContentMapper {
    @Select("SELECT s.`name` AS studentName, t.`name` AS teacherName, t.email AS teacherEmail FROM teacher t, graduatestudent g, student s WHERE g.studentID = #{studentId} AND g.studentID = s.ID AND g.tutorID = t.ID ORDER BY g.ID desc LIMIT 1")
    SendMailContent getSendMailContent(Integer studentId);

}
