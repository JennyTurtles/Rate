package org.sys.rate.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

// 该类不是DAO层的mapper,仅用来保存前端设置的成绩评定表映射。
@Data
public class ExportGradeMapper {
    private Integer activityID;
    private Integer teacherID;
    private Integer groupID;
    public Integer instructorCommentActID; // 指导教师评语对应的活动ID
    public Integer defenseCommentActID; // 评阅教师评语对应的活动ID
    public Integer reviewCommentActID; // 答辩评语对应的活动ID
    public Map<Integer, Double> instructorScoreItems; // 指导教师评分项ID和系数
    public Map<Integer, Double> reviewScoreItems; // 评阅教师评分项ID和系数
    public Map<Integer, Double> defenseScoreItems; // 答辩评分项ID和系数
    public Map<String, Integer> instructorScoreName2ID; // 显示的名字到评分项ID的映射
    public Map<String, Integer> reviewScoreName2ID;
    public Map<String, Integer> defenseScoreName2ID;
    public List<String> orderList;

}
