package org.sys.rate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.sql.Timestamp;


import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Operation {

    private Integer id;

    private String operatorRole;

    private Integer operatorId;

    private String operatorName;

    private String prodType;

    private Integer prodId;

    private Timestamp time;

    private String operationName;

    private String state;

    private String remark;


}
