package com.siseth.analysis.entity;

import com.siseth.analysis.component.entity.BaseEntity;
import com.siseth.analysis.feign.api.response.ImageWellExposedResDTO;
import com.siseth.analysis.feign.dataSource.dto.ImageSourceShortResDTO;
import com.siseth.analysis.feign.fedora.dto.FileShortResDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Setter
@Builder
@Entity
@Table(schema = "public", name = "`SourcePreVerification`")
@Where(clause = "`isActive`")
public class SourcePreVerification extends BaseEntity {

    @Column(name="`imageId`")
    private Long imageId;

    @Column(name="`imageCreatedAt`")
    private LocalDateTime imageCreatedAt;

    @Column(name="`lat`")
    private Double lat;

    @Column(name="`lon`")
    private Double lon;

    @Column(name="`isCorrect`")
    private Boolean isCorrect;

    @Column(name="`taskId`")
    private String taskId;

    @Column(name="`algorithmName`")
    private String algorithmName;

    @Column(name="`desc`")
    private String desc;

    @Column(name="`isActive`")
    private Boolean isActive;

    @Column(name="`status`")
    @Enumerated(EnumType.STRING)
    private VerificationStatus status;

    public void addTask(String task) {
        this.taskId = task;
        this.status = VerificationStatus.PROCESSING;
    }

    public void clearTaskId() {
        this.taskId = null;
    }

    public void cancel() {
        this.status = VerificationStatus.CANCELLED;
    }

    public void saveResults(ImageWellExposedResDTO dto) {
        this.isCorrect = Boolean.valueOf(dto.getWellExposedStatusFlag());
        this.desc = dto.getWellExposedStatusDesc();
        this.status = VerificationStatus.FINISHED;
    }
    @Transient
    public boolean isCorrect() {
        return this.isCorrect != null && this.isCorrect;
    }
    public SourcePreVerification(){
        this.isActive = true;
        this.status = VerificationStatus.ADDED;
    }

    public SourcePreVerification create(FileShortResDTO file) {
        this.imageId = file.getId();
        this.imageCreatedAt = file.getOriginCreatedAt();
        this.algorithmName = "";
        return this;
    }

    public SourcePreVerification addSource(ImageSourceShortResDTO source) {
        this.lat = source.getLatitude();
        this.lon = source.getLongitude();
        return this;
    }

    public enum VerificationStatus {
        ADDED,
        PROCESSING,
        FINISHED,
        CANCELLED
    }

}
