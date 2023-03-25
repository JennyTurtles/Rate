package org.sys.rate.model;

import lombok.Data;

@Data
public class FinalValue {
    private Integer studentID;

    private String code;

    private Integer participantID;

    private Integer groupID;

    private String groupName;

    private String studentName;

    private String infoItemName;

    private Double activityScore;

    private String content;

    private Integer displaySequence; // 暂且放在这
}
