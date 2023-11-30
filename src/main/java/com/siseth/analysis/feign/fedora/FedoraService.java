package com.siseth.analysis.feign.fedora;

import com.siseth.analysis.feign.fedora.dto.FileShortResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "fedora-management-service")
public interface FedoraService {


    @GetMapping("/api/internal/assets/fedora/file/source/{sourceId}")
    List<FileShortResDTO> getFileImageToSource(@PathVariable Long sourceId,
                                               @RequestParam LocalDate dateFrom,
                                               @RequestParam LocalDate dateTo,
                                               @RequestParam String realm);

    @GetMapping("/api/internal/assets/fedora/file/{fileId}/image")
    byte[] getImage(@PathVariable Long fileId);

    @GetMapping("/api/internal/assets/fedora/roi/byte")
    byte[] getRoi(@RequestParam Long fileId);

    @GetMapping("/api/internal/assets/fedora/file/source/{imageId}/previewPhoto")
    String getPreviewPhotoForImageId(@PathVariable Long imageId);

    @GetMapping(value ="/api/internal/assets/fedora/file/img/{imgId}")
    FileShortResDTO getFilesImageDataById(@PathVariable Long imgId);

}

