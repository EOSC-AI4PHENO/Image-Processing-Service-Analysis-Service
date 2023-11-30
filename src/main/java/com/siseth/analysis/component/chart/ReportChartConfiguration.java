package com.siseth.analysis.component.chart;

import java.util.Arrays;
import java.util.List;

public enum ReportChartConfiguration {
    AVERAGED_FOR_ROI(Arrays.asList(ChartTypesEnum.R_G_B, ChartTypesEnum.RI, ChartTypesEnum.GI, ChartTypesEnum.BI, ChartTypesEnum.BRI),
            DateRangeEnum.DateTime,
            null,
            null);

    private final List<ChartTypesEnum> chartTypes;
    private final DateRangeEnum dateRangeEnum;
    private final ColorIndexEnum colorIndexEnum;
    private final AggregateTypeEnum aggregateType;

    ReportChartConfiguration(List<ChartTypesEnum> chartTypes, DateRangeEnum dateRangeEnum, ColorIndexEnum colorIndexEnum, AggregateTypeEnum aggregateType) {
        this.chartTypes = chartTypes;
        this.dateRangeEnum = dateRangeEnum;
        this.colorIndexEnum = colorIndexEnum;
        this.aggregateType = aggregateType;
    }

    public List<ChartTypesEnum> getChartTypes() {
        return chartTypes;
    }

    public DateRangeEnum getDateRangeEnum() {
        return dateRangeEnum;
    }

    public ColorIndexEnum getColorIndexEnum() {
        return colorIndexEnum;
    }

    public AggregateTypeEnum getAggregateType() {
        return aggregateType;
    }

}

