package com.siseth.analysis.dto.analysis.request;

import com.siseth.analysis.entity.Analysis;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisCreateReqDTO {

    private String name;

    private Long sourceId;

    private String algorithmName;

    private Analysis.AnalysisType type;

    private LocalDate dateFrom;

    private Long roiId;

    private LocalDate dateTo;

    private Boolean incremental;

}
