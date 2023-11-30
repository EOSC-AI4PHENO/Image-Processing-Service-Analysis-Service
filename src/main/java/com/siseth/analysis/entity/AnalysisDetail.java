package com.siseth.analysis.entity;

import com.siseth.analysis.component.entity.BaseEntity;
import com.siseth.analysis.feign.fedora.dto.FileShortResDTO;
import com.siseth.analysis.feign.roi.dto.RoiResDTO;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
@Builder
@Entity
@Table(schema = "public", name = "`AnalysisDetail`")
@Where(clause = "`isActive`")
public class AnalysisDetail extends BaseEntity {


    @ManyToOne
    @JoinColumn(name = "`analysisId`")
    private Analysis analysis;

    @Column(name="`imageId`", nullable = false)
    private Long imageId;

    @Column(name="`roiId`")
    private Long roiId;

    @Column(name="`roiResultId`")
    private Long roiResultId;

    @Column(name="`imageCreatedAt`")
    private LocalDateTime imageCreatedAt;

    @Column(name="`status`")
    @Enumerated(EnumType.STRING)
    private AnalysisStatus status;

    @Column(name="`taskId`")
    private String taskId;

    @Column(name="`isActive`")
    private Boolean isActive;

    @Column(name="`isCashed`")
    private Boolean isCashed;

    @OneToMany(mappedBy = "detail", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<AnalysisResult> results;

    public enum AnalysisStatus {
        ADDED,
        IN_PROGRESS,
        FINISHED,
        ABORTED,

        FAILED;
        
        public static List<AnalysisStatus> getFinished() {
            return List.of(FINISHED);
        }

    }

    public Boolean isFinished() {
        return AnalysisStatus.getFinished().contains(status);
    }

    public void deleteCash() {
        this.isCashed = false;
    }

    public AnalysisDetail(Analysis analysis) {
        this.analysis = analysis;
        this.isActive = true;
        this.status = AnalysisStatus.ADDED;
        this.results = new ArrayList<>();
    }

    public void setToInProgress(String taskId) {
        this.status = AnalysisStatus.IN_PROGRESS;
        this.taskId = taskId;
        this.isCashed = true;
    }

    public void finished() {
        this.status = AnalysisStatus.FINISHED;
    }

    public void failed() {
        this.status = AnalysisStatus.FAILED;
    }

    public AnalysisDetail create(FileShortResDTO file, Long roiId) {
        this.imageId = file.getId();
        this.imageCreatedAt = file.getOriginCreatedAt();
        this.roiId = roiId;
        return this;
    }
    public AnalysisDetail create(Long imageId, Long roiId) {
        this.imageId = imageId;
        this.roiId = roiId;
        return this;
    }
}
