package com.siseth.analysis.feign.imageProcessing.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomRoiFeignDTO extends AutomaticRoiFeignDTO{

    private byte[] roi;

    public CustomRoiFeignDTO(byte[] img, String name, byte[] roi) {
        super(img, name);
        this.roi = roi;
    }
}
