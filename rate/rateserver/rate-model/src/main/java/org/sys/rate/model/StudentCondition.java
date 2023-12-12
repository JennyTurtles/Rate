package org.sys.rate.model;/**
 * ClassName: StudentCondition
 * Package: org.sys.rate.model
 * Description:
 *
 * @Author ZYK
 * @Create 2023/9/1 20:36
 * @Version 1.0
 */

import lombok.Data;

/**
 * @Description:
 * @param: $
 * @return: $
 */
@Data
public class StudentCondition {
    private String stuNumber;
    private String name;
    private String specialty;
    private String className;
    private Integer year;
    private String tutorJobNumber;
    private String tutorName;
    private Integer institutionID;
    private Integer startYear;
    private Integer month;
    private Integer startThesisID;
    private Integer pageNum;
    private Integer pageSize;
}
