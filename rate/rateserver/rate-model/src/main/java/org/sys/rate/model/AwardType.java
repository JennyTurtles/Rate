package org.sys.rate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AwardType {
    private Integer id;
    @NotBlank(message = "奖项名称不为空")
    private String name;
    private Integer indicatorId;
    @NotBlank(message = "年份不为空")
    @Min(value = 1900, message = "年份不合法")
    private String year;
    private Integer rankN;
}
