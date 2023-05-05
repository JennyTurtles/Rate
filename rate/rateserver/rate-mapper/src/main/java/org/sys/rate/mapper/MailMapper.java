package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.sys.rate.model.Mail;

@Mapper
public interface MailMapper {
    @Update("update mail set emailAddress = #{emailAddress}, IMAPVerifyCode = #{IMAPVerifyCode}, IMAPHost = #{IMAPHost}, SMTPHost = #{SMTPHost} where id = 0")
    int updateMail(Mail mail);

    @Select("select * from mail")
    Mail getMail();
}