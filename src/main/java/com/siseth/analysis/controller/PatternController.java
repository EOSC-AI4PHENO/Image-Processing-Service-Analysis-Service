package com.siseth.analysis.controller;

import com.siseth.analysis.component.analysisModel.AnalysisModelClass;
import com.siseth.analysis.dto.analysis.request.AnalysisCreateReqDTO;
import com.siseth.analysis.dto.analysis.request.AnalysisDetailReqDTO;
import com.siseth.analysis.dto.analysis.request.AnalysisIdReqDTO;
import com.siseth.analysis.dto.analysis.request.AnalysisUpdateReqDTO;
import com.siseth.analysis.dto.analysis.response.AnalysisDetailsResDTO;
import com.siseth.analysis.dto.analysis.response.AnalysisResDTO;
import com.siseth.analysis.dto.pattern.CreatePatternReqDTO;
import com.siseth.analysis.dto.pattern.PatternIdReqDTO;
import com.siseth.analysis.dto.pattern.UpdatePatternReqDTO;
import com.siseth.analysis.entity.Pattern;
import com.siseth.analysis.service.AnalysisService;
import com.siseth.analysis.service.PatternService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/image-processing/analysis/pattern")
@RequiredArgsConstructor
public class PatternController {

    private final PatternService service;


    @PostMapping("/create")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> create(@RequestBody CreatePatternReqDTO api,
                                    @Parameter(hidden = true) @RequestHeader String id,
                                    @Parameter(hidden = true) @RequestHeader String realm) {
        return ResponseEntity.ok(service.create(api, id, realm));
    }

    @PostMapping("/update")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> create(@RequestBody UpdatePatternReqDTO api,
                                    @Parameter(hidden = true) @RequestHeader String id,
                                    @Parameter(hidden = true) @RequestHeader String realm) {
        return ResponseEntity.ok(service.update(api, id, realm));
    }

    @GetMapping()
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Pattern> getPattern(@RequestParam Long patternId,
                                          @Parameter(hidden = true) @RequestHeader String id,
                                          @Parameter(hidden = true) @RequestHeader String realm) {
        return ResponseEntity.ok(service.getPattern(patternId, id, realm));
    }
    @GetMapping("/all")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getAllPattern(@Parameter(hidden = true) @RequestHeader String id,
                                        @Parameter(hidden = true) @RequestHeader String realm) {
        return ResponseEntity.ok(service.getAllPattern(id, realm));
    }

    @DeleteMapping("/delete")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> delete(@RequestBody PatternIdReqDTO patternId,
                                    @Parameter(hidden = true) @RequestHeader String id,
                                    @Parameter(hidden = true) @RequestHeader String realm) {
        service.delete(patternId.getId(), id, realm);
        return ResponseEntity.ok().build();
    }
}
