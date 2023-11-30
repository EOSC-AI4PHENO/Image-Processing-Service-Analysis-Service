package com.siseth.analysis.feign.imageProcessing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LindenAutomaticRoisWithIndicatorsResultDTO {

    private String task_id;
    private String status;
    private String filename;
    private String jsonBase64LindenROIs;
    private BigDecimal r_av;
    private BigDecimal g_av;
    private BigDecimal b_av;
    private BigDecimal r_sd;
    private BigDecimal g_sd;
    private BigDecimal b_sd;
    private BigDecimal bri_av;
    private BigDecimal bri_sd;
    private BigDecimal gi_av;
    private BigDecimal gei_av;
    private BigDecimal gei_sd;
    private BigDecimal ri_av;
    private BigDecimal ri_sd;
    private BigDecimal bi_av;
    private BigDecimal bi_sd;
    private BigDecimal avg_width;
    private BigDecimal avg_height;
    private BigDecimal avg_area;
    private BigDecimal number_of_lindens;

    public Map<String, BigDecimal> getParameters() {
        Map<String, BigDecimal> rawMap = new HashMap<>();
        rawMap.put("r_av", r_av);
        rawMap.put("g_av", g_av);
        rawMap.put("b_av", b_av);
        rawMap.put("r_sd", r_sd);
        rawMap.put("g_sd", g_sd);
        rawMap.put("b_sd", b_sd);
        rawMap.put("bri_av", bri_av);
        rawMap.put("bri_sd", bri_sd);
        rawMap.put("gi_av", gi_av);
        rawMap.put("gei_av", gei_av);
        rawMap.put("gei_sd", gei_sd);
        rawMap.put("ri_av", ri_av);
        rawMap.put("ri_sd", ri_sd);
        rawMap.put("bi_av", bi_av);
        rawMap.put("bi_sd", bi_sd);
        rawMap.put("avg_width", avg_width);
        rawMap.put("avg_height", avg_height);
        rawMap.put("avg_area", avg_area);
        rawMap.put("number_of_lindens", number_of_lindens);
        return  rawMap;
    }
}
