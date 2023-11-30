package com.siseth.analysis.chart.service;

import com.siseth.analysis.chart.dto.ReportChartDTO;
import com.siseth.analysis.component.chart.ReportChartConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChartService {
    public ReportChartDTO getReportChartTypes() {
        return new ReportChartDTO(ReportChartConfiguration.values());
    }

}
