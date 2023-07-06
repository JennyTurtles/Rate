package org.sys.rate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisplayItemSource {
    private double coefficient;
    private String type;
    private Integer id;
    private boolean isBasicSource; // 基础类型为编号，组名，姓名。基础类型无coefficient和id

    public DisplayItemSource(String type) {
        this.type = type;
        this.isBasicSource = true;
    }

}
