package org.sys.rate.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 刊物对象 publication
 *
 * @author system
 * @date 2022-03-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class  Publication {

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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private List<Date> dateList;

    private List<Integer> flag;
}
