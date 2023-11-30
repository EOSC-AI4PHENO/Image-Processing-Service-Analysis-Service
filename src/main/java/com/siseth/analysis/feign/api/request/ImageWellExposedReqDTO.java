package com.siseth.analysis.feign.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageWellExposedReqDTO {
        private byte[] img;
        private String name;
        private Double lat;
        private Double lon;
        private String createdAt;

}
