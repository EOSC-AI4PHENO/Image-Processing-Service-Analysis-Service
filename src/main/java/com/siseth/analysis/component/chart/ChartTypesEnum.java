package com.siseth.analysis.component.chart;

public enum ChartTypesEnum {
    R_G_B("R+G+B"),
    RI("RI"),
    GI("GI"),
    BI("BI"),
    BRI("BRI");

    private final String description;

    ChartTypesEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ChartTypesEnum findByValue(String description) {
        for (ChartTypesEnum chartTypeEnum : ChartTypesEnum.values()) {
            if (chartTypeEnum.getDescription().equals(description)) {
                return chartTypeEnum;
            }
        }
        return null;
    }
}
