package org.sys.rate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreAverage {
    private Integer activityID;
    private Integer participantID;
    private Integer scoreItemID;
    private Double score;
    private Double coef;
}
