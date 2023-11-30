package com.siseth.analysis.dto.analysis.request.respond;

import com.siseth.analysis.entity.AnalysisParameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisParameterResDTO {

    private String name;

    private String desc;

    public AnalysisParameterResDTO(AnalysisParameter analysisParameter) {
        this.name = analysisParameter.getName();
        this.desc = analysisParameter.getDesc();
    }

}
