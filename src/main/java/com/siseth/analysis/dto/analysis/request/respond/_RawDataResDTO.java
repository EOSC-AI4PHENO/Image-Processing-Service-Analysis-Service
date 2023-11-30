package com.siseth.analysis.dto.analysis.request.respond;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class _RawDataResDTO {
    private List<Map<String, Object>> rawData;

    private String content;
}

