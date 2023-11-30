package com.siseth.analysis.reports.controller;

import com.siseth.analysis.dto.analysis.request.respond.AnalysisParameterResDTO;
import com.siseth.analysis.dto.analysis.request.respond.RawDataResDTO;
import com.siseth.analysis.dto.analysis.request.respond.SourcePreVerificationResDTO;
import com.siseth.analysis.dto.report.request.ReportAttributeReqDTO;
import com.siseth.analysis.dto.report.response.ReportAttributeResDTO;
import com.siseth.analysis.reports.service.ReportInternalService;
import com.siseth.analysis.reports.service.ReportService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/internal/image-processing/analysis/report")
@RequiredArgsConstructor
public class ReportsInternalController {

    private final ReportInternalService service;

    @GetMapping("/getParameters")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<AnalysisParameterResDTO>> getParameters(@Parameter(hidden = true) @RequestHeader String id,
                                                                       @Parameter(hidden = true)  @RequestHeader String realm) {
        return ResponseEntity.ok(service.getParameters());
    }
}
