package com.siseth.analysis.entity;

import com.siseth.analysis.component.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
@Builder
@Entity
@Table(schema = "public", name = "`AnalysisParameter`")
@Where(clause = "`isActive`")
public class AnalysisParameter extends BaseEntity {

    @Column(name="`name`")
    private String name;

    @Column(name="`desc`")
    private String desc;

    @Column(name="`value`")
    private String value;

    @Column(name="`isActive`")
    private Boolean isActive;


    @Column(name="`orderNumber`")
    private @Builder.Default Integer orderNumber = 0;

    @OneToMany(mappedBy = "parameter") //Raczej niepotrzebne z tej strony
    private List<AnalysisResult> results;
}
