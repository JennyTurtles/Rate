package org.sys.rate.model;

import lombok.Data;

import java.util.Map;

// 该类不是DAO层的mapper,仅用来保存前端设置的成绩评定表映射。
@Data
public class ExportGradeMapper {
    private Integer activityID;
    private Integer teacherID;
    private Integer instructorCommentActID; // 指导教师评语对应的活动ID
    private Integer defenseCommentActID; // 评阅教师评语对应的活动ID
    private Integer reviewCommentActID; // 答辩评语对应的活动ID
    private Map<Integer, Double> instructorScoreItems; // 指导教师评分项ID和系数
    private Map<Integer, Double> reviewScoreItems; // 评阅教师评分项ID和系数
    private Map<Integer, Double> defenseScoreItems; // 答辩评分项ID和系数
}
