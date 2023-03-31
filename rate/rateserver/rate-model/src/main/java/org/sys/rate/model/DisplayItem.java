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
    private String content;

    public DisplayItem(String sourceName, String source) {
        this.sourceName = sourceName;
        this.source = source;
    }

    public DisplayItem(DisplayItem displayItem) {
        this.ID = displayItem.getID();
        this.activityID = displayItem.getActivityID();
        this.name = displayItem.getName();
        this.source = displayItem.getSource();
        this.sourceName = displayItem.getSourceName();
        this.displaySequence = displayItem.getDisplaySequence();
        this.fullScore = displayItem.getFullScore();
        this.content = displayItem.getContent();
    }

    @Override
    public int compareTo(DisplayItem o) {
        Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
        return cmp.compare(this.sourceName,o.sourceName);
    }
}