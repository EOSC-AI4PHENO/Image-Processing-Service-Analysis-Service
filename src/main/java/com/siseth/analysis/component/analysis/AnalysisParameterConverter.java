package com.siseth.analysis.component.analysis;

import com.siseth.analysis.entity.AnalysisParameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnalysisParameterConverter {

    public static Map<String, AnalysisParameter> toMap(List<AnalysisParameter> analysisParameters) {
        return Optional.ofNullable(analysisParameters)
                .map(x -> x.stream()
                        .collect(Collectors.toMap(AnalysisParameter::getName, Function.identity()))
                ).orElse(new HashMap<>());
    }


}
