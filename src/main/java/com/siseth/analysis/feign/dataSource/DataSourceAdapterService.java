package com.siseth.analysis.feign.dataSource;

import com.siseth.analysis.feign.dataSource.dto.ImageSourceLongResDTO;
import com.siseth.analysis.feign.dataSource.dto.ImageSourceShortResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "source-adapter-service")
public interface DataSourceAdapterService {

    @GetMapping("/api/internal/digital/source-adapter/{sourceId}")
    ImageSourceShortResDTO getSource(@PathVariable Long sourceId);

    @GetMapping("/api/internal/digital/source-adapter/{sourceId}/get")
    ImageSourceLongResDTO getSourceInternal(@PathVariable Long sourceId);

}
