package com.siseth.analysis.dto.analysis.request.respond;

import com.siseth.analysis.entity.SourcePreVerification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SourcePreVerificationResDTO {

    private Long imageId;

    private LocalDateTime imageCreatedAt;

    private Double lat;

    private Double lon;

    private Boolean isCorrect;

    private String algorithmName;

    private String desc;

    public SourcePreVerificationResDTO(SourcePreVerification sourcePreVerification) {
        this.imageId = sourcePreVerification.getImageId();
        this.imageCreatedAt = sourcePreVerification.getImageCreatedAt();
        this.lat = sourcePreVerification.getLat();
        this.lon = sourcePreVerification.getLon();
        this.isCorrect = sourcePreVerification.getIsCorrect();
        this.algorithmName = sourcePreVerification.getAlgorithmName();
        this.desc = sourcePreVerification.getDesc();
    }

}
