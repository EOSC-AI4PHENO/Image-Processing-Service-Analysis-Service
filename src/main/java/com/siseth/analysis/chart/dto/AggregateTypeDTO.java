package com.siseth.analysis.chart.dto;

import com.siseth.analysis.component.chart.AggregateTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AggregateTypeDTO {

    private AggregateTypeEnum aggregateType;
    private String description;

    public AggregateTypeDTO(AggregateTypeEnum aggregateType) {
        this.aggregateType = aggregateType;
        this.description = aggregateType.getDescription();
    }
}
