package org.sys.rate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecisionType {
    private Integer id;
    private Integer indicatorId;
    @NotBlank(message = "决策名称不为空")
    private String name;
    @NotBlank(message = "年份不为空")
    @Min(value = 1900, message = "年份不合法")
    private String year;
}
