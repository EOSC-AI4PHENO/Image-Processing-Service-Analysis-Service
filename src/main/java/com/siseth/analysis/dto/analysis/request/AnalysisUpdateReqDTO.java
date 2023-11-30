package com.siseth.analysis.dto.analysis.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisUpdateReqDTO extends AnalysisCreateReqDTO{
    private Long analysisId;
}
