package com.siseth.analysis.feign.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ImageWellExposedResDTO {
    private String task_id;
    private String status;
    @JsonProperty("WellExposedStatusFlag")
    private String WellExposedStatusFlag;
    @JsonProperty("WellExposedStatusDesc")
    private String WellExposedStatusDesc;
    private String filename;

}
