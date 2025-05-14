package org.sys.rate.utils;

public enum ProjectTypeEnums {
    ACADEMIC_PAPER("学术论文"),
    AUTHORIZED_PATENT("授权专利"),
    RESEARCH_AWARD("科研获奖"),
    VERTICAL_RESEARCH_PROJECT("纵向科研项目"),
    HORIZONTAL_RESEARCH_PROJECT("横向科研项目"),
    STANDARD_DEVELOPMENT("制定标准"),
    DECISION_CONSULTING("决策咨询"),
    ACADEMIC_MONOGRAPH_AND_TEXTBOOK("学术专著和教材"),
    PRODUCT_APPLICATION("产品应用"),
    ACADEMIC_COMPETITION("学科竞赛");

    private final String displayName;

    // 构造函数，用于初始化枚举常量的显示名称
    ProjectTypeEnums(String displayName) {
        this.displayName = displayName;
    }

    // 获取枚举常量的显示名称
    public String getDisplayName() {
        return displayName;
    }

    // 从显示名称获取对应的枚举常量
    public static ProjectTypeEnums fromDisplayName(String displayName) {
        for (ProjectTypeEnums type : ProjectTypeEnums.values()) {
            if (type.getDisplayName().equals(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown display name: " + displayName);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
