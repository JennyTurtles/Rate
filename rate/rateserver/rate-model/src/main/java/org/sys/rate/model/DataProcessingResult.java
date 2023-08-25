package org.sys.rate.model;/**
 * ClassName: DataProcessingResult
 * Package: org.sys.rate.model
 * Description:
 *
 * @Author ZYK
 * @Create 2023/8/24 9:17
 * @Version 1.0
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataProcessingResult {
    private Integer successfulRowsCount;
    private Integer failedRowsCount;
    private Integer duplicateInsertRowsCount;
    private HashMap<Integer, String> failureReasons = new HashMap<>();
    private Integer total;

    public void setFailReasonForRowIndex(int rowIndex, String reason) {
        failureReasons.put(rowIndex, reason);
    }
}
