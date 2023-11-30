package com.siseth.analysis.dto.pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePatternReqDTO extends CreatePatternReqDTO{

    private Long id;
}
