package org.sys.rate.model;

import lombok.Data;

import java.util.List;

@Data
public class GradeForm {
    private Integer studentID;
    private String name;
    private String stuNumber;
    private String specialty;
    private String className;
    private String thesisName;
    private List<Comment> comments;
    private List<ScoreItem> scoreItems;
    public enum Type{
        INSTRUCTOR,REVIEWER,DEFENSE,OTHER // 分别对应：指导教师，评阅教师，答辩，其他
    }


}
