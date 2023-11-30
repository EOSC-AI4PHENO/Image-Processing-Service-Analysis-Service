package com.siseth.analysis.entity;

import com.siseth.analysis.component.entity.BaseEntity;
import com.siseth.analysis.dto.analysis.request.AnalysisCreateReqDTO;
import com.siseth.analysis.dto.pattern.CreatePatternReqDTO;
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
@Table(schema = "public", name = "`Pattern`")
@Where(clause = "`isActive`")
public class Pattern extends BaseEntity {

    @Column(name="`name`")
    private String name;

    @Column(name="`desc`")
    private String desc;

    @Column(name="`realm`", nullable = false)
    private String realm;

    @Column(name="`userId`")
    private String userId;

    @Column(name="`formula`")
    private String formula;

    @Column(name="`isActive`")
    private Boolean isActive;


    public Pattern(CreatePatternReqDTO dto, String userId, String realm) {
        this.name = dto.getName();
        this.desc = dto.getDesc();
        this.realm = realm;
        this.userId = userId;
        this.formula = dto.getFormula();
        this.isActive = true;
        valid();
    }

    public Pattern(String userId, String realm) {
        this.realm = realm;
        this.userId = userId;
        this.isActive = true;
    }

    public Pattern update(CreatePatternReqDTO dto) {
        this.name = dto.getName();
        this.desc = dto.getDesc();
        this.formula = dto.getFormula();
        valid();
        return this;
    }

    private void valid() {
        if(this.formula == null || this.userId == null || this.realm == null)
            throw new RuntimeException("Not valid!");
    }

}
