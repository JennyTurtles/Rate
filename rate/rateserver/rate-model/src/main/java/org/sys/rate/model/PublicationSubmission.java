package org.sys.rate.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * ClassName: PublicationSubmission
 * Package: org.sys.rate.model
 * Description:
 *
 * @Author ZYK
 * @Create 2023/7/7 19:49
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("publication_submission")
public class PublicationSubmission {
    private Integer id;
    private Integer indicatorId;
    private Integer publicationId;
    private Integer year;
    private Integer studentId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date;
    private String state;
    private String publicationName;
    private String publisherName;
    private String publicationAbbr;
    private String publicationUrl;
    private String publicationProofUrl;
    @TableField(exist = false)
    private String studentName;
    @TableField(exist = false)
    private String indicatorName;
    private String comment;
}
