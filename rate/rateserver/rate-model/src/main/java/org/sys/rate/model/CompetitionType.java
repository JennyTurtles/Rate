package org.sys.rate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionType {
    private Integer id;
    @NotBlank(message = "竞赛类型不为空")
    private String name;
    private Integer indicatorId;
    @NotBlank(message = "年份不为空")
    @Min(value = 1900, message = "年份不合法")
    private String year;
}
