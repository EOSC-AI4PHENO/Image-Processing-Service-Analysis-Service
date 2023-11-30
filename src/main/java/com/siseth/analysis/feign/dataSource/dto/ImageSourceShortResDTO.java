package com.siseth.analysis.feign.dataSource.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageSourceShortResDTO {

    private Long id;
    private Double longitude;
    private Double latitude;
    private String name;
    private String userId;
    private Boolean isOwner;
    private String desc;
    private SourceRecorded recorded;


    public enum SourceRecorded {
        LINDEN,
        APPLE
    }

    public Map<String, Object> getMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("Name", this.name);
        map.put("Latitude", this.latitude);
        map.put("Longitude", this.longitude);
        return map;
    }

}
