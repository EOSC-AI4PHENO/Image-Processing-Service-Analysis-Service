package com.siseth.analysis.controller;

import com.siseth.analysis.component.analysisModel.AnalysisModelClass;
import com.siseth.analysis.dto.analysis.request.AnalysisCreateReqDTO;
import com.siseth.analysis.dto.analysis.request.AnalysisDetailReqDTO;
import com.siseth.analysis.dto.analysis.request.AnalysisIdReqDTO;
import com.siseth.analysis.dto.analysis.request.AnalysisUpdateReqDTO;
import com.siseth.analysis.dto.analysis.response.AnalysisDetailsResDTO;
import com.siseth.analysis.dto.analysis.response.AnalysisResDTO;
import com.siseth.analysis.entity.Analysis;
import com.siseth.analysis.entity.AnalysisDetail;
import com.siseth.analysis.entity.AnalysisParameter;
import com.siseth.analysis.service.AnalysisService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/image-processing/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService service;

    @GetMapping("/attribute")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getAttribute() {

        return ResponseEntity.ok(AnalysisModelClass.map);
    }


    @PostMapping("/create")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> create(@RequestBody AnalysisCreateReqDTO api,
                                    @Parameter(hidden = true) @RequestHeader String id,
                                    @Parameter(hidden = true) @RequestHeader String realm) {
        return ResponseEntity.ok(service.create(api, id, realm));
    }

    @PostMapping("/enableIncremental")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> enableIncremental(@RequestParam Long sourceId,
                                    @Parameter(hidden = true) @RequestHeader String id,
                                    @Parameter(hidden = true) @RequestHeader String realm) {
        return ResponseEntity.ok(service.enableIncremental(sourceId, id, realm));
    }

    @PutMapping("/disableIncremental")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> disableIncremental(@RequestParam Long sourceId,
                                               @Parameter(hidden = true) @RequestHeader String id,
                                               @Parameter(hidden = true) @RequestHeader String realm) {
        return ResponseEntity.ok(service.disableIncremental(sourceId, id, realm));
    }


    @PutMapping("/startAnalysis")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> create(@RequestBody AnalysisIdReqDTO api,
                                    @Parameter(hidden = true) @RequestHeader String id,
                                    @Parameter(hidden = true) @RequestHeader String realm) {
        return ResponseEntity.ok(service.startAnalysis(api, id, realm));
    }

    @PutMapping("/update")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> update(@RequestBody AnalysisUpdateReqDTO api,
                                    @Parameter(hidden = true) @RequestHeader String id,
                                    @Parameter(hidden = true) @RequestHeader String realm) {
        return ResponseEntity.ok(service.update(api, id, realm));
    }


    @GetMapping("/getUserAnalysis")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<AnalysisResDTO>> getUserAnalysis(@RequestParam(required = false, defaultValue = "0") Long sourceId,
                                                                @RequestParam(required = false, defaultValue = "") String sort,
                                                                @RequestParam(required = false, defaultValue = "") Analysis.AnalysisType type,
                                                                @RequestParam(required = false) Analysis.AnalysisStatus status,
                                                                @RequestParam(required = false, defaultValue = "1900-01-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                                                                @RequestParam(required = false, defaultValue = "2100-01-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate dateTo,
                                                            @Parameter(hidden = true) @RequestHeader String id,
                                                            @Parameter(hidden = true) @RequestHeader String realm) {
        return ResponseEntity.ok(service.getUserAnalysis(id, realm, sourceId, sort, type, status, dateFrom, dateTo));
    }

    @GetMapping("/getAnalysisDetails")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<AnalysisDetailsResDTO>> getAnalysisDetails(@RequestParam Long analysisId,
                                                                          @Parameter(hidden = true) @RequestHeader String id,
                                                                          @Parameter(hidden = true) @RequestHeader String realm) {
        return ResponseEntity.ok(service.getAnalysisDetails(analysisId));
    }


    @GetMapping("/getAnalysis")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<AnalysisResDTO> getAnalysis(@RequestParam Long analysisId,
                                                            @Parameter(hidden = true) @RequestHeader String id,
                                                            @Parameter(hidden = true) @RequestHeader String realm) {
        return ResponseEntity.ok(service.getAnalysis(analysisId, realm, id));
    }

    @DeleteMapping("/delete")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> deleteAnalysis(@RequestBody AnalysisIdReqDTO dto,
                                               @Parameter(hidden = true) @RequestHeader String id,
                                               @Parameter(hidden = true) @RequestHeader String realm) {
        service.deleteAnalysis(dto.getAnalysisId(), realm, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addDetail")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> addDetail(@RequestBody AnalysisDetailReqDTO api,
                                    @RequestHeader String id,
                                    @RequestHeader String realm) {
        return ResponseEntity.ok(service.addDetail(api, id, realm));
    }
}
