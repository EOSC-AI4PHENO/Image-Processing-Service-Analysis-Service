package com.siseth.analysis.feign.roi;

import com.siseth.analysis.feign.roi.dto.RoiResDTO;
import com.siseth.analysis.feign.roi.dto.UploadRoiDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@FeignClient(name = "roi-service")
public interface ROIService {

    @GetMapping("/api/internal/image-processing/roi/all")
    RoiResDTO getRoiToSource(@RequestParam Long sourceId,
                             @RequestParam LocalDateTime dateTime,
                             @RequestParam String userId,
                             @RequestParam String realm);


    @PostMapping("/api/internal/image-processing/roi/results")
    RoiResDTO addRoi(@RequestBody UploadRoiDTO roi,
                  @RequestParam(required = false)  String id,
                  @RequestParam String realm);
}
