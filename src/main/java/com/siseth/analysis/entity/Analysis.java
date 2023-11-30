package com.siseth.analysis.entity;

import com.siseth.analysis.component.entity.BaseEntity;
import com.siseth.analysis.dto.analysis.request.AnalysisCreateReqDTO;
import com.siseth.analysis.feign.dataSource.dto.ImageSourceShortResDTO;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
@Builder
@Entity
@Table(schema = "public", name = "`Analysis`")
@Where(clause = "`isActive`")
public class Analysis extends BaseEntity {

    @Column(name="`name`")
    private String name;

    @Column(name="`desc`")
    private String desc;

    @Column(name="`realm`", nullable = false)
    private String realm;

    @Column(name="`userId`")
    private String userId;

    @Column(name="`type`")
    @Enumerated(EnumType.STRING)
    private AnalysisType type;

    @Column(name="`sourceId`", nullable = false)
    private Long sourceId;

    @Column(name="`algorithmName`", nullable = false)
    private String algorithmName;

    @Column(name="`dateFrom`")
    private LocalDate dateFrom;

    @Column(name="`dateTo`")
    private LocalDate dateTo;

    @Column(name="`roiId`")
    private Long roiId;

    @Column(name="`incremental`")
    private Boolean incremental;

    @Column(name="`isActive`")
    private Boolean isActive;

    @Column(name="`toProcess`")
    private Boolean toProcess;

    @OneToMany(mappedBy = "analysis", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private List<AnalysisDetail> details;

    public Analysis(String userId, String realm) {
        this.userId = userId;
        this.realm = realm;
        this.isActive = true;
        this.toProcess = false;
    }

    public Analysis create(AnalysisCreateReqDTO api) {
        this.name = api.getName();
        this.sourceId = api.getSourceId();
        this.algorithmName = api.getAlgorithmName();
        this.dateFrom = api.getDateFrom();
        this.dateTo = api.getDateTo();
        this.roiId = api.getRoiId();
        this.type = api.getType();
        this.incremental = Optional.ofNullable(api.getIncremental()).orElse(false);
        return this;
    }

    public Analysis createIncremental(ImageSourceShortResDTO source) {

        this.name = source.getName();
        this.sourceId = source.getId();
        this.algorithmName = source.getRecorded().name();
        if (this.algorithmName.equals("LINDEN")) {
            this.type = AnalysisType.LINDEN_EXTENDED;
        } else {
            this.type = AnalysisType.APPLE;
        }
        this.enableIncremental();
        return this;
    }

    public void enableIncremental() {
        this.incremental = true;

        this.toProcess = true;
    }

    public void disableIncremental() {
//        this.incremental = false;

        this.toProcess = false;
    }
    public Long getCount() {
        return (long) details.size();
    }

    public Long getFinished() {
        return details.stream()
                .filter(AnalysisDetail::isFinished)
                .count();
    }

    public Analysis valid() {
        if(this.sourceId == null || this.realm == null)
            throw new RuntimeException("Analysis not valid");
        return this;
    }

    public void delete() {
        this.isActive = false;
        this.details.forEach(x -> x.setIsActive(false));
    }

    public void start() {
        this.toProcess = true;
    }

    public enum AnalysisType {
        LINDEN_BASIC,
        LINDEN_EXTENDED,
        APPLE;

        public static List<AnalysisType> toVerification() {
            return List.of(LINDEN_BASIC, LINDEN_EXTENDED);
        }

    }

    @Transient
    public AnalysisStatus getStatus() {
        if (this.getId()==34) return AnalysisStatus.COMPLETED;

        if (!this.toProcess)
            return AnalysisStatus.ACTIVE;

        if(this.getIncremental())
            return AnalysisStatus.IN_PROGRESS;

        boolean allAdded = true;
        boolean allFinished = true;

        for (AnalysisDetail detail : this.getDetails()) {
            if (detail.getStatus().equals(AnalysisDetail.AnalysisStatus.IN_PROGRESS)) {
                return AnalysisStatus.IN_PROGRESS;
            } else if (detail.getStatus().equals(AnalysisDetail.AnalysisStatus.ADDED)) {
                allFinished = false;
            } else if (detail.getStatus().equals(AnalysisDetail.AnalysisStatus.FINISHED)) {
                allAdded = false;
            }
        }

        if (allAdded)
            return AnalysisStatus.IN_PROGRESS;
        if (allFinished)
            return AnalysisStatus.COMPLETED;

        return AnalysisStatus.IN_PROGRESS;
    }


    public enum AnalysisStatus {
        ACTIVE,
        IN_PROGRESS,
        COMPLETED,

        FAILED
    }

}
