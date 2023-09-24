package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.EmailErrorLog;

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
    @Insert("INSERT INTO exception_log (error_type, error_description, body, timestamp) " +
            "VALUES (#{errorType}, #{errorDescription}, #{body}, #{timestamp})")
    void insert(EmailErrorLog emailErrorLog);

//    @Update("UPDATE exception_log SET error_type=#{errorType}, error_description=#{errorDescription}, " +
//            "sender_email=#{senderEmail}, recipient_email=#{recipientEmail}, subject=#{subject}, body=#{body}, timestamp=#{timestamp} " +
//            "WHERE id=#{id}")
//    void update(EmailErrorLog emailErrorLog);

    @Delete("DELETE FROM exception_log WHERE id=#{id}")
    void delete(Long id);

    @Select("SELECT id, error_type, error_description, body, timestamp FROM exception_log WHERE id=#{id}")
    EmailErrorLog getById(Long id);

    @Select("SELECT id, error_type, error_description, body, timestamp FROM exception_log")
    List<EmailErrorLog> getAll();
}
