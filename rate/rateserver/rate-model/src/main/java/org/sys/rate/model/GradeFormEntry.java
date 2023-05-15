package org.sys.rate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeFormEntry implements Comparable<GradeFormEntry>{
    private Integer ID;
    private Integer activityID;
    private Integer typeID; // 1：指导教师评语 2：评阅教师评语 3：答辩评语 4-6：指导教师评分项 7-10：评阅教师评分项 11-13：答辩评分项
    private Integer targetID;
    private Double coef;

    public GradeFormEntry(Integer typeID){
        this.typeID = typeID;
    }

    public GradeFormEntry(Integer activityID, Integer typeID, Integer targetID, Double coef) {
        this.activityID = activityID;
        this.typeID = typeID;
        this.targetID = targetID;
        this.coef = coef;
    }

    public GradeFormEntry(Integer activityID, Integer typeID, Integer targetID) {
        this.activityID = activityID;
        this.typeID = typeID;
        this.targetID = targetID;
    }

    @Override
    public int compareTo(GradeFormEntry g) {
        return this.getTypeID() - g.getTypeID();
    }

}
