package com.siseth.analysis.feign.roi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoiResDTO {

    private Long sourceId;

    private Long fileId;

    private Object roi;

}
