package org.sys.rate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisplayItem implements Comparable<DisplayItem> { // 列的信息
    private Integer ID;
    private Integer activityID;
    private String name;
    private String source;
    private String sourceName;
    private Integer displaySequence;
    private Integer passScore;

    public DisplayItem(String sourceName, String source) {
        this.sourceName = sourceName;
        this.source = source;
    }

    public DisplayItem(Integer ID, String name, String sourceName, String source) {
        this.ID = ID;
        this.name = name;
        this.sourceName = sourceName;
        this.source = source;
    }

    public DisplayItem(String sourceName, String source, Integer activityID) {
        this.activityID = activityID;
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
        this.passScore = displayItem.getPassScore();
    }

    @Override
    public int compareTo(DisplayItem o) {
        Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
        return cmp.compare(this.sourceName,o.sourceName);
    }

    // 将字符串形式的source转换为DisplayItemSource列表
    public List<DisplayItemSource> source2list() {
        if (this.source == null || !this.source.contains(".")) { // 不包含'.'，则为基础类型
            return null;
        }
        List<DisplayItemSource> res = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?\\*)?(\\w+)\\.(\\d+)");
        Matcher matcher = pattern.matcher(this.source);
        while (matcher.find()) {
            double coefficient = matcher.group(1) != null ? Double.parseDouble(matcher.group(1).replace("*", "")) : 1.0;
            String type = matcher.group(3);
            int id = Integer.parseInt(matcher.group(4));
            res.add(new DisplayItemSource(coefficient, type, id, false));
        }
        return res;
    }

    // 将DisplayItemSource列表转换为字符串形式的source
    public static String list2source(List<DisplayItemSource> list) {
        if (list == null) {
            return null;
        }
        StringBuilder res = new StringBuilder();
        for (DisplayItemSource displayItemSource : list) {
            res.append(displayItemSource.getCoefficient()).append("*").append(displayItemSource.getType()).append(".").append(displayItemSource.getId()).append("+");
        }
        return res.substring(0, res.length() - 1);
    }
}
