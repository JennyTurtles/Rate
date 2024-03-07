package org.sys.rate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ProgramRecord {

    private Integer ID;
    private Integer studentID;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date startDateStu;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date endDateStu;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date dateTea;
    private String preSum;
    private String nextPlan;
    private String tutorComment;
    private int num;
    private String isPass;
    private Double workHours;

}
