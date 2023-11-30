package com.siseth.analysis.feign.imageProcessing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LindenWithIndicatorsResultDTO extends LindenAutomaticRoisWithIndicatorsResultDTO{

    private List<BigDecimal> isFlowering;
    private List<BigDecimal> isFloweringConfidence;

    public Map<String, BigDecimal> getParameters() {
        Map<String, BigDecimal> params = super.getParameters();
        params.put("isFlowering", isFlowering.get(0));
        params.put("isFloweringConfidence", isFloweringConfidence.get(0));
        return params;
    }

}
