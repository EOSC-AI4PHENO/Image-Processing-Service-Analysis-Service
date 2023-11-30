package com.siseth.analysis.component.analysisModel;

import com.siseth.analysis.entity.Analysis;

import java.util.List;
import java.util.Map;

import static com.siseth.analysis.entity.Analysis.AnalysisType.*;

public final class AnalysisModelClass {

    public static Map<String, List<Analysis.AnalysisType>> map =
            Map.of(
                    "APPLE", List.of(APPLE),
                    "LINDEN", List.of(LINDEN_BASIC, LINDEN_EXTENDED)
            );







}
