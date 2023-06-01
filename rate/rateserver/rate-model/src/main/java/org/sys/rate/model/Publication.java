package org.sys.rate.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 刊物对象 publication
 *
 * @author system
 * @date 2022-03-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Publication {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Integer ID;

    /**
     * 刊物全称
     */
    private String name;

    /**
     * 简称
     */
    private String abbr;

    /**
     * 出版社
     */
    private String publisher;

    /**
     * 网址
     */
    private String url;

    /**
     * 类型
     */
    private String type;

    /**
     * 级别
     */
    private String level;

    private Integer score;
    private Integer year;

    /**
     * 指标点id
     */
    private Integer indicatorID;

    private Indicator indicator;
    private String indicatorName;

}
