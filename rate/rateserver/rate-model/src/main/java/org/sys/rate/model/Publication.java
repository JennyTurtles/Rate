package org.sys.rate.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    private Integer id;

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
     * 一个期刊可以对应多个指标点
     */
    private List<Indicator> indicatorList;

}
