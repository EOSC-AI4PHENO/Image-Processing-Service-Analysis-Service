package com.siseth.analysis.chart.dto;

import com.siseth.analysis.component.chart.ReportChartConfiguration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportChartDTO {

    List<ReportChartConfigurationDTO> chartConfigurationDTOList = new ArrayList<>();

    public ReportChartDTO(ReportChartConfiguration[] reportChartConfigurations) {
        this.chartConfigurationDTOList = Arrays.stream(reportChartConfigurations).map(ReportChartConfigurationDTO::new).collect(Collectors.toList());
    }
}
