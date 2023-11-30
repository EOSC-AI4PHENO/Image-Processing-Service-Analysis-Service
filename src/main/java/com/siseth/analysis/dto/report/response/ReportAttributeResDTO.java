package com.siseth.analysis.dto.report.response;

import com.siseth.analysis.reports.constant.ReportType;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ReportAttributeResDTO {

    private List<AttributeResDTO> types;

    public ReportAttributeResDTO() {
        this.types = ReportType.stream()
                .map(x -> new AttributeResDTO(x.toString(), x.getDesc()))
                .collect(Collectors.toList());
    }

}
