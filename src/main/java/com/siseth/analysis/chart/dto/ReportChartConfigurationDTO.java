package com.siseth.analysis.chart.dto;

import com.siseth.analysis.component.chart.ColorIndexEnum;
import com.siseth.analysis.component.chart.DateRangeEnum;
import com.siseth.analysis.component.chart.ReportChartConfiguration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportChartConfigurationDTO {

    private List<ChartTypeDTO> chartTypes;
    private DateRangeEnum dateRangeEnum;
    private ColorIndexEnum colorIndexEnum;
    private AggregateTypeDTO aggregateType;

    public ReportChartConfigurationDTO(ReportChartConfiguration reportChartConfiguration) {
        this.chartTypes = reportChartConfiguration.getChartTypes() != null ? reportChartConfiguration.getChartTypes().stream().map(ChartTypeDTO::new).collect(Collectors.toList()) : null;
        this.dateRangeEnum = reportChartConfiguration.getDateRangeEnum();
        this.colorIndexEnum = reportChartConfiguration.getColorIndexEnum();
        this.aggregateType = reportChartConfiguration.getAggregateType() != null ? new AggregateTypeDTO(reportChartConfiguration.getAggregateType()) : null;
    }

}
