package org.sys.rate.model;

import lombok.Data;

import java.util.Date;

@Data
public class Oper {
    private Integer id;
    private String operatorRole;
    private Integer operatorId;
    private String prodType;
    private Integer prodId;
    private Date time;
    private String operationName;
    private String state;
    private String remark;
}
