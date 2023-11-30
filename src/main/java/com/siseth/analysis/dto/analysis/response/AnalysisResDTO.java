package com.siseth.analysis.dto.analysis.response;

import com.siseth.analysis.entity.Analysis;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResDTO {

    private Long id;
    private String name;

    private String desc;

    private Analysis.AnalysisType type;

    private Long sourceId;

    private String algorithmName;

    private Analysis.AnalysisStatus status;

    private LocalDateTime createdAt;

    private LocalDate dateFrom;

    private LocalDate dateTo;

    private Boolean hasRoi;

    private Long finishedTasks;

    private Long totalTasks;

    private Boolean incremental;

    public AnalysisResDTO(Analysis dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.desc = dto.getDesc();
        this.type = dto.getType();
        this.sourceId = dto.getSourceId();
        this.algorithmName = dto.getAlgorithmName();
        this.status = dto.getStatus();
        this.createdAt = dto.getCreatedAt().toLocalDateTime();
        this.dateFrom = dto.getDateFrom();
        this.dateTo = dto.getDateTo();
        this.hasRoi = dto.getRoiId() != null;
        this.finishedTasks = dto.getFinished();
        this.totalTasks = dto.getCount();
        this.incremental = dto.getIncremental();
    }

}

