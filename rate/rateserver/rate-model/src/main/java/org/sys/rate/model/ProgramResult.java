package org.sys.rate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProgramResult extends Production{
    private Double workHours;
    private String remark;
}
