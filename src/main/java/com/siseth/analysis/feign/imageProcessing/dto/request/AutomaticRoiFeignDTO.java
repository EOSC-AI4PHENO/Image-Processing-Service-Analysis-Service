package com.siseth.analysis.feign.imageProcessing.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AutomaticRoiFeignDTO {

    private byte[] img;
    private String name;

}
