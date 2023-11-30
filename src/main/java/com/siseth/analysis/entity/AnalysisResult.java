package com.siseth.analysis.entity;

import com.siseth.analysis.component.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Setter
@Builder
@Entity
@Table(schema = "public", name = "`AnalysisResult`")
@Where(clause = "`isActive`")
public class AnalysisResult extends BaseEntity {


    @ManyToOne
    @JoinColumn(name = "`detailsId`")
    private AnalysisDetail detail;

    @ManyToOne
    @JoinColumn(name = "`parameterId`")
    private AnalysisParameter parameter;

    @Column(name="`value`")
    private BigDecimal value;

    @Column(name="`isActive`")
    private Boolean isActive;

    public AnalysisResult() {

    }


    public String getParamName() {
        return this.parameter.getName();
    }


}
