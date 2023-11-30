package com.siseth.analysis.reports.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public enum ReportType {

    ANALYSIS_BASIC ("Report about result from analysis"),

    ANALYSIS_EXTEND ("Report about result from analysis with photo data"),
    ANALYSIS_MEDIAN ("Analysis median"),
    ANALYSIS_90_PERCENTILE ("Analysis 90 percentile"),

    ANALYSIS_FROM_FORMULA ("Analysis from formula");


    @Getter
    private String desc;

    ReportType(String desc) {
        this.desc = desc;
    }

    public static Stream<ReportType> stream() {
        return Stream.of(ReportType.values());
    }




}
