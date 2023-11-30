package com.siseth.analysis.feign.dataSource;

import com.siseth.analysis.feign.dataSource.dto.ImageSourceShortResDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataSourceAdapterServiceImpl {

    private final DataSourceAdapterService dataSourceAdapterService;

    public ImageSourceShortResDTO getSource(Long sourceId) {
        try {
            return dataSourceAdapterService.getSource(sourceId);
        } catch (Exception e) {
            log.info("Failed to connect to DataSourceAdapter, message {}", e.getMessage());
            return null;
        }
    }

}
