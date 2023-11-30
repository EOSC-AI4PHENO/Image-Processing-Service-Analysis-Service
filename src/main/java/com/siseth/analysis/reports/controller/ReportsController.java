package com.siseth.analysis.reports.controller;

import com.siseth.analysis.dto.analysis.request.respond.AnalysisParameterResDTO;
import com.siseth.analysis.dto.analysis.request.respond.RawDataResDTO;
import com.siseth.analysis.dto.analysis.request.respond.SourcePreVerificationResDTO;
import com.siseth.analysis.dto.report.request.ReportAttributeReqDTO;
import com.siseth.analysis.dto.report.response.ReportAttributeResDTO;
import com.siseth.analysis.reports.service.ReportService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/image-processing/analysis/report")
@RequiredArgsConstructor
public class ReportsController {

    private final ReportService service;

    @GetMapping("/attribute")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ReportAttributeResDTO> getAttribute() {
        return ResponseEntity.ok(new ReportAttributeResDTO());
    }
    @PostMapping("/create")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<Map<String, Object>>> create(@RequestBody ReportAttributeReqDTO api) {
        return ResponseEntity.ok(service.getReport(api));
    }

    @PostMapping("/create/csv")
    @SecurityRequirement(name = "Bearer Authentication")
    @SneakyThrows
    public ResponseEntity<?> createCSV(@RequestBody ReportAttributeReqDTO api) {
        File file = service.getReportCSV(api);
        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(new FileInputStream(file)));
    }

    @PostMapping("/create/excel")
    @SecurityRequirement(name = "Bearer Authentication")
    @SneakyThrows
    public ResponseEntity<?> getReportExcel(@RequestBody ReportAttributeReqDTO api) {
        File file = service.getReportExcel(api);
        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(new FileInputStream(file)));
    }

    @PostMapping("/getRawData2/excel")
    @SecurityRequirement(name = "Bearer Authentication")
    @SneakyThrows
    public ResponseEntity<?> getAnalysisExcelExtend(@RequestParam Long analysisId) {
        File file = service.getAnalysisExcelExtend(analysisId);
        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(new FileInputStream(file)));
    }

    @PostMapping("/getRawData2/csv")
    @SecurityRequirement(name = "Bearer Authentication")
    @SneakyThrows
    public ResponseEntity<?> getAnalysisCSVExtend(@RequestParam Long analysisId) {
        File file = service.getAnalysisCSVExtend(analysisId);
        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(new FileInputStream(file)));
    }

    @GetMapping("/getRawData")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<Map<String, Object>>> getRawData(@RequestParam Long analysisId,
                                                                @Parameter(hidden = true) @RequestHeader String id,
                                                                @Parameter(hidden = true)  @RequestHeader String realm) {
        return ResponseEntity.ok(service.getRawData(analysisId));
    }

    @GetMapping("/getRawData/inTimeRange")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<Map<String, Object>>> getRawData(@RequestParam Long analysisId,
                                                                @RequestParam(required = false, name = "startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                                                                @RequestParam(required = false, name = "endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
                                                                @Parameter(hidden = true) @RequestHeader String id,
                                                                @Parameter(hidden = true)  @RequestHeader String realm) {
        return ResponseEntity.ok(service.getRawDataInTimeRange(analysisId, startTime, endTime));
    }

    @GetMapping("/getRawData2")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getRawData2(@RequestParam Long analysisId,
                                                           @RequestParam(required = false) boolean addPreviewPhoto,
                                                           @RequestParam(required = false, defaultValue = "") String sort) {
        return ResponseEntity.ok(service._getRawData(analysisId, addPreviewPhoto, sort));
    }

    @GetMapping("/getRawData/inPages")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getRawDataInPages(@RequestParam Long analysisId,
                                                           @Parameter(hidden = false) @RequestHeader(required = false) String id,
                                                           @Parameter(hidden = false)  @RequestHeader(required = false) String realm,
                                               @RequestParam(required = false) boolean addPreviewPhoto,
                                                           @RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                                           @RequestParam(name = "size", defaultValue = "1000") int pageSize,
                                               @RequestParam(required = false, defaultValue = "") String sort) {
        return ResponseEntity.ok(service.getRawData(analysisId, addPreviewPhoto, pageNumber, pageSize,sort));
    }

    @GetMapping("/getRawData/byImageId")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<Map<String, Object>>> getRawDataForImageId(@RequestParam Long imageId,
                                                                @Parameter(hidden = true) @RequestHeader String id,
                                                                @Parameter(hidden = true)  @RequestHeader String realm) {
        return ResponseEntity.ok(service.getRawDataByImageId(imageId));
    }

    @GetMapping("/getParameters")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<AnalysisParameterResDTO>> getParameters(@Parameter(hidden = true) @RequestHeader String id,
                                                                       @Parameter(hidden = true)  @RequestHeader String realm) {
        return ResponseEntity.ok(service.getParameters());
    }

    @GetMapping("/getSourcePreVer/byImageId")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<SourcePreVerificationResDTO> getSourcePreVerForImageId(@RequestParam Long imageId,
                                                                                 @Parameter(hidden = true) @RequestHeader String id,
                                                                                 @Parameter(hidden = true)  @RequestHeader String realm) {
        return ResponseEntity.ok(service.getSourcePreVerByImageId(imageId));
    }

    @GetMapping("/getColorContribution")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<byte[]> getColorContribution(@RequestParam Long analysisId,
                                                       @Parameter(hidden = true) @RequestHeader String id,
                                                       @Parameter(hidden = true)  @RequestHeader String realm){
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(service.getColorContribution(analysisId));
    }
}
