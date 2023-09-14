package org.sys.rate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Indicator {

    private Integer id;

    private String name;

    private String type;

    private String order;

    private Integer score;

    private Integer father;

    private Integer year;

    private Integer rankN;

    private String level;

    /**
     * 一个指标点可以对应多个期刊
     */
    private List<Publication> publicationList;

    public Indicator(Integer id, String name, String type, String order, Integer score, Integer father) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.order = order;
        this.score = score;
        this.father = father;
    }

    public Indicator(Integer id, String name, String type, String order, Integer score, Integer father,Integer rankN){
        this.id = id;
        this.name = name;
        this.type = type;
        this.order = order;
        this.score = score;
        this.father = father;
        this.rankN = rankN;
    }
}
