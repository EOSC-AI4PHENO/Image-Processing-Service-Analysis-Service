package com.siseth.analysis.dto.analysis.response;

import com.siseth.analysis.entity.Analysis;
import com.siseth.analysis.entity.AnalysisDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisDetailsResDTO {

    private Long id;
    private Long imageId;
    private String taskId;
    private AnalysisDetail.AnalysisStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime modifiedAt;


    public AnalysisDetailsResDTO(AnalysisDetail dto) {
        this.id = dto.getId();
        this.imageId = dto.getImageId();
        this.taskId = dto.getTaskId();
        this.status = dto.getStatus();
        this.createdAt = dto.getCreatedAt();
        this.modifiedAt = dto.getModifiedAt();
    }
}

