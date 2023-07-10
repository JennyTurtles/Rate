package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.PublicationSubmission;

import java.util.List;


@Mapper
public interface PublicationSubmissionMapper {
    // 搜索所有的提交记录
    @Select("select s.name as studentName, CONCAT(i.order, i.name) as indicatorName,publication_id, indicator_id, p.year, student_id, date, state, publication_name, publication_abbr, publisher_name , publication_url, publication_proof_url from publication_submission p left join student s on p.student_id = s.ID left join indicator i on p.indicator_id = i.id")
    List<PublicationSubmission> getAllSubmission();

    // 同意或拒绝某个提交记录，修改记录状态
    // 同意某个提交记录时，顺便对publication和indicator_publication表进行插入操作
    @Update("update publication_submission set state = #{state} where id = #{id}")
    void editState(Integer id, String state);

    @Select("select s.name as studentName, CONCAT(i.order, i.name) as indicatorName, publication_id, indicator_id, p.year, student_id, date, state, publication_name, publication_abbr, publisher_name , publication_url, publication_proof_url from publication_submission p left join student s on p.student_id = s.ID left join indicator i on p.indicator_id = i.id where state=#{state}")
    List<PublicationSubmission> getSubmissionByState(String state);

    @Insert("INSERT INTO publication_submission (publication_id, indicator_id, year, student_id, date, state, publication_name, publication_abbr, publisher_name, publication_url, publication_proof_url) " +
            "VALUES (#{publicationId}, #{indicatorId}, #{year}, #{studentId}, #{date}, #{state}, #{publicationName}, #{publicationAbbr}, #{publisherName}, #{publicationUrl}, #{publicationProofUrl})")
    int insert(PublicationSubmission submission);

}