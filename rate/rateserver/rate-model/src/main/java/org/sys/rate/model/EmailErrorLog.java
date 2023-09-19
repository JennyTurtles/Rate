package org.sys.rate.model;/**
 * ClassName: EmailErrorLog
 * Package: org.sys.rate.model
 * Description:
 *
 * @Author ZYK
 * @Create 2023/9/18 15:23
 * @Version 1.0
 */


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @Description:
 * @param: $
 * @return: $
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailErrorLog {
    private Long id;
    private String errorType;
    private String errorDescription;
    private String senderEmail;
    private String recipientEmail;
    private String subject;
    private String body;
    private Timestamp timestamp;

    public EmailErrorLog(String errorType, String errorDescription, String senderEmail, String recipientEmail, String subject, String body, Timestamp timestamp) {
        this.errorType = errorType;
        this.errorDescription = errorDescription;
        this.senderEmail = senderEmail;
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.body = body;
        this.timestamp = timestamp;
    }
}
