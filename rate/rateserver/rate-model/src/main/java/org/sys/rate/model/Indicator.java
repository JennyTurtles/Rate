package org.sys.rate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Indicator {

    private Integer id;

    private String name;

    private String type;

    private String order;

    private Integer score;

    private Integer father;

    /**
     * 一个指标点可以对应多个期刊
     */
    private List<Publication> publicationList;

}
