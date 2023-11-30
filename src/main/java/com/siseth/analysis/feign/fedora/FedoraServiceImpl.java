package com.siseth.analysis.feign.fedora;

import com.siseth.analysis.feign.fedora.dto.FileShortResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FedoraServiceImpl {

    private final FedoraService fedoraService;

    public List<FileShortResDTO> getFileImageToSource( Long sourceId,
                                                       LocalDate dateFrom,
                                                       LocalDate dateTo,
                                                       String realm) {
        try {
            return fedoraService.getFileImageToSource(sourceId, dateFrom, dateTo, realm);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public byte[] getImage(Long fileId) {
        try {
            return fedoraService.getImage(fileId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public byte[] getRoi(Long fileId) {
        try {
            return fedoraService.getRoi(fileId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public FileShortResDTO getFilesImageDataById(Long imgId) {
        try {
            return fedoraService.getFilesImageDataById(imgId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new FileShortResDTO();
        }
    }

    public String getPreviewPhotoForImageId(Long imgId) {
        try {
            return fedoraService.getPreviewPhotoForImageId(imgId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

}
