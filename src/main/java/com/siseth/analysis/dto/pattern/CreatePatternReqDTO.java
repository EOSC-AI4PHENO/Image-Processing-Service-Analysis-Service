package com.siseth.analysis.dto.pattern;

import com.siseth.analysis.reports.constant.ReportType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePatternReqDTO {

    private String name;
    private String desc;
    private String formula;
}
