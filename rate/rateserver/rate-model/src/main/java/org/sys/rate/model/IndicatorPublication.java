package org.sys.rate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 指标点和期刊的中间表
 * @param: $
 * @return: $
 */
public class IndicatorPublication {

    private Integer id;

    private Integer indicatorId;

    private Integer publicationId;

    @DateTimeFormat(pattern = "yyyy")
    @JsonFormat(pattern = "yyyy", timezone = "GMT+8")
    private Date year;

    private Integer flag;
}
