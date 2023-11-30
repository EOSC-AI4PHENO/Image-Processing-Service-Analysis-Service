package com.siseth.analysis.component.chart;
public enum AggregateTypeEnum {
    PERCENTILE("90th percentile (in 3-days moving window) z GI,RI,BI value"),
    MEDIAN("median (in 1-day window) - GCC,RCC,BCC");

    private final String description;

    AggregateTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static AggregateTypeEnum findByValue(String description) {
        for (AggregateTypeEnum aggregateEnum : AggregateTypeEnum.values()) {
            if (aggregateEnum.getDescription().equals(description)) {
                return aggregateEnum;
            }

        }
        return null;
    }
}

