package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.EmailErrorLog;

import java.sql.Timestamp;
import java.util.List;

/**
 * ClassName: EmailErrorLogMapper
 * Package: org.sys.rate.mapper
 * Description:
 *
 * @Author ZYK
 * @Create 2023/9/18 15:24
 * @Version 1.0
 */
@Mapper
public interface EmailErrorLogMapper {
    @Insert("INSERT INTO exception_log (error_type, error_description, timestamp) " +
            "VALUES (#{errorType}, #{errorDescription}, #{timestamp})")
    void insert(EmailErrorLog emailErrorLog);


    @Delete("DELETE FROM exception_log WHERE id=#{id}")
    void delete(Long id);

    void deleteMulti(@Param("data") List<EmailErrorLog> data);

    void deleteAll(Timestamp startTime, Timestamp endTime);

    @Select("SELECT id, error_type, error_description, timestamp FROM exception_log WHERE id=#{id}")
    EmailErrorLog getById(Long id);

    @Select("SELECT id, error_type, error_description, timestamp FROM exception_log")
    List<EmailErrorLog> getAll();

    List<EmailErrorLog> filterByDate(Timestamp startTime, Timestamp endTime);
}
