package org.sys.rate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMailContent {
    private String studentName;
    private String studentEmail;
    private String teacherEmail;
    private String teacherName;
    private String publicationMedium;

}
