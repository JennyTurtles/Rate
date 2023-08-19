package org.sys.rate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecisionType {
    private Integer id;
    private String name;
    private Integer indicatorId;
    private String year;
}
