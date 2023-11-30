package com.siseth.analysis.feign.imageProcessing;

import com.siseth.analysis.feign.api.request.ImageWellExposedReqDTO;
import com.siseth.analysis.feign.api.response.ImageWellExposedResDTO;
import com.siseth.analysis.feign.api.response.TaskResultResDTO;
import com.siseth.analysis.feign.imageProcessing.dto.request.AutomaticRoiFeignDTO;
import com.siseth.analysis.feign.imageProcessing.dto.request.CustomRoiFeignDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "ai-image-processing-service")
public interface ImageProcessingAdapterService {

    @PostMapping("/api/image-processing/ai-adapter/isImageWellExposed")
    TaskResultResDTO isImageWellExposed(@RequestBody ImageWellExposedReqDTO dto);

    @GetMapping("/api/image-processing/ai-adapter/isImageWellExposedResult/{taskId}")
    ImageWellExposedResDTO isImageWellExposedResult(@PathVariable String taskId);

    @GetMapping("/api/image-processing/ai-adapter/deleteTask/{taskId}")
    Boolean deleteTask(@PathVariable String taskId);


    @PostMapping("/api/image-processing/ai-adapter/lindenAutomaticRoisWithIndicators")
    TaskResultResDTO lindenAutomaticRoisWithIndicators(@RequestBody AutomaticRoiFeignDTO dto);

    @GetMapping("/api/image-processing/ai-adapter/lindenAutomaticRoisWithIndicatorsResult/{taskId}")
    Map<String, Object> lindenAutomaticRoisWithIndicatorsResult(@PathVariable String taskId);




    @PostMapping("/api/image-processing/ai-adapter/appleAutomaticRoisWithIndicators")
    TaskResultResDTO appleAutomaticRoisWithIndicators(@RequestBody AutomaticRoiFeignDTO dto);

    @GetMapping("/api/image-processing/ai-adapter/appleAutomaticRoisWithIndicatorsResult/{taskId}")
    Map<String, Object> appleAutomaticRoisWithIndicatorsResult(@PathVariable String taskId);










    @PostMapping("/api/image-processing/ai-adapter/lindenWithIndicators")
    TaskResultResDTO lindenWithIndicators(@RequestBody CustomRoiFeignDTO dto);

    @GetMapping("/api/image-processing/ai-adapter/lindenWithIndicatorsResult/{taskId}")
    Map<String, Object> lindenWithIndicatorsResult(@PathVariable String taskId);



    @PostMapping("/api/image-processing/ai-adapter/lindenBasic")
    Map<String, Object> lindenBasic(@RequestBody CustomRoiFeignDTO dto);

    @PostMapping("/api/image-processing/ai-adapter/lindenExtended")
    Map<String, Object> lindenExtended(@RequestBody CustomRoiFeignDTO dto);

    @PostMapping("/api/image-processing/ai-adapter/lindenExtended")
    Map<String, Object> lindenExtended(@RequestBody AutomaticRoiFeignDTO dto);

    @PostMapping("/api/image-processing/ai-adapter/apple")
    Map<String, Object> apple(@RequestBody CustomRoiFeignDTO dto);

    @PostMapping("/api/image-processing/ai-adapter/apple")
    Map<String, Object> apple(@RequestBody AutomaticRoiFeignDTO dto);

}


