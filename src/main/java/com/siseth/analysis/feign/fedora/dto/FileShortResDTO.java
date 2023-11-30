package com.siseth.analysis.feign.fedora.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileShortResDTO {

    private Long id;

    private Long sourceId;

    private String name;

    private String path;

    private String directory;

    private LocalDateTime originCreatedAt;

    public String getName() {
        return Optional.ofNullable(this.name)
                                .orElse("");
    }

    public String getDirectory() {
        return Optional.ofNullable(this.directory)
                .orElse("");
    }

}

