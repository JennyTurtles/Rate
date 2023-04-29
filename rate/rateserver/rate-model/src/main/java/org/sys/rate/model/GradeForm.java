package org.sys.rate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class GradeForm {
    private Integer studentID;
    private String name;
    private String stuNumber;
    private String specialty;
    private String className;
    private String thesisName;
    private Map<Integer,List<Comment>> comments; // 类型-评语(可能有多个老师，因此为List)
    private Map<Integer,List<ScoreItem>> scoreItems; // 类型-评分项

    public enum Type{
        INSTRUCTOR,REVIEWER,DEFENSE,OTHER // 分别对应：指导教师，评阅教师，答辩，其他
    }
}
