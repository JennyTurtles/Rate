package org.sys.rate.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Indicator {
    private Integer ID;
    private String name;
    private String type;
    private String order;
    private Integer score;
    private Integer father;
}
