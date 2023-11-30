package com.siseth.analysis.feign.roi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UploadRoiDTO {

    private Object roi;

    private Long sourceId;

}
