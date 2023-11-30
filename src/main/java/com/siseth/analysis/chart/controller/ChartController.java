package com.siseth.analysis.chart.controller;

import com.siseth.analysis.chart.dto.ReportChartDTO;
import com.siseth.analysis.chart.service.ChartService;
import com.siseth.analysis.component.chart.ReportChartConfiguration;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/image-processing/analysis/chart")
@RequiredArgsConstructor
public class ChartController {

    private final ChartService service;
    @GetMapping("/getChartTypes")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ReportChartDTO> getReportChartTypes() {
        return ResponseEntity.ok(service.getReportChartTypes());
    }

}
