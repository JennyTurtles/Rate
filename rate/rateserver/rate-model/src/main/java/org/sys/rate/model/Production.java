package org.sys.rate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Production {
    Integer id;

    String name;

    Integer studentId;

    Student student;

    Indicator indicator;

    Integer indicatorId;

    String indicatorName;

    Integer year;

    Integer score;

    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    Date date;

    String author;

    String state;

    String url;

    List<Operation> operationList;

    // 排名
    Integer rank;

    // 总人数
    Integer total;

    Integer point;

}


