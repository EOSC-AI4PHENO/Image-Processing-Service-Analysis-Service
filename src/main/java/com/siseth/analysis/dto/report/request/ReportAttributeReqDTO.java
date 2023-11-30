package com.siseth.analysis.dto.report.request;

import com.siseth.analysis.reports.constant.ReportType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ReportAttributeReqDTO {

    private ReportType type;

    private LocalDate dateFrom;

    private LocalDate dateTo;

    private Long analysisId;

    private Long patternId;

    private String sort;

    public ReportAttributeReqDTO(ReportType type, Long analysisId) {
        this.type = type;
        this.analysisId = analysisId;
    }

    public ReportAttributeReqDTO(ReportType type, Long analysisId, String sort) {
        this.type = type;
        this.analysisId = analysisId;
        this.sort = sort;
    }

    public LocalDate getDateFrom() {
        return Optional.ofNullable(this.dateFrom)
                .orElse(LocalDate.of(1900, 1, 1));
    }

    public LocalDate getDateTo() {
        return Optional.ofNullable(this.dateTo)
                .orElse(LocalDate.of(2200, 1, 1));
    }


}
