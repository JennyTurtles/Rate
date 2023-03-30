package org.sys.rate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.Collator;
import java.util.Comparator;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisplayItem implements Comparable<DisplayItem> {
    private Integer ID;
    private Integer activityID;
    private String name;
    private String source;
    private String sourceName;
    private Integer displaySequence;
    private Integer fullScore;

    public DisplayItem(String sourceName, String source) {
        this.sourceName = sourceName;
        this.source = source;
    }

    @Override
    public int compareTo(DisplayItem o) {
        Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
        return cmp.compare(this.sourceName,o.sourceName);
    }
}
