package com.siseth.analysis.controller;

import com.siseth.analysis.dto.analysis.request.AnalysisCreateReqDTO;
import com.siseth.analysis.service.AnalysisService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/image-processing/analysis")
@RequiredArgsConstructor
public class AnalysisInternalController {

    private final AnalysisService service;

    @GetMapping("/incremental")
    public ResponseEntity<Boolean> create(@RequestParam Long sourceId,
                                    @RequestHeader String id,
                                    @RequestHeader String realm) {
        return ResponseEntity.ok(service.isIncremental(sourceId, id, realm));
    }
}
