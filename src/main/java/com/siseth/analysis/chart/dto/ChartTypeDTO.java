package com.siseth.analysis.chart.dto;

import com.siseth.analysis.component.chart.ChartTypesEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChartTypeDTO {

    private ChartTypesEnum chartType;
    private String description;

    public ChartTypeDTO(ChartTypesEnum chartTypesEnum) {
        this.chartType = chartTypesEnum;
        this.description = chartTypesEnum.getDescription();
    }
}
