package com.siseth.analysis.dto.analysis.request.respond;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RawDataResDTO {
    Page<Map<String, Object>> rawData;

    private String content;
}
