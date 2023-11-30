package com.siseth.analysis.service;

import com.siseth.analysis.entity.SourcePreVerification;
import com.siseth.analysis.feign.api.request.ImageWellExposedReqDTO;
import com.siseth.analysis.feign.api.response.ImageWellExposedResDTO;
import com.siseth.analysis.feign.api.response.TaskResultResDTO;
import com.siseth.analysis.feign.fedora.FedoraService;
import com.siseth.analysis.feign.imageProcessing.ImageProcessingAdapterService;
import com.siseth.analysis.repository.SourcePreVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final SourcePreVerificationRepository repository;
    private final FedoraService fedoraFeign;
    private final ImageProcessingAdapterService imageProcessingAdapterFeign;


    public List<SourcePreVerification> getImagesForVerification() {
        return repository.findAllByStatus(SourcePreVerification.VerificationStatus.ADDED)
                .orElseThrow( () -> new RuntimeException("There are no image that are ADDED"));
    }

    public List<SourcePreVerification> getImagesThatAreBeingProcessed() {
        return repository.findAllByStatus(SourcePreVerification.VerificationStatus.PROCESSING)
                .orElseThrow( () -> new RuntimeException("There are no image under processing"));
    }

    public List<SourcePreVerification> getImagesThatAreFinished() {
        return repository.findAllByStatusAndTaskIdIsNotNull(SourcePreVerification.VerificationStatus.FINISHED)
                .orElseThrow( () -> new RuntimeException("There are no image that are finished"));
    }

    public byte[] downloadImageFromFedora(Long id) {
        try {
            return fedoraFeign.getImage(id);
        }catch (Exception e) {
            return null;
        }
    }

    public TaskResultResDTO isImageWellExposed(byte[] byteImage, SourcePreVerification sourcePreVerification) {
        return imageProcessingAdapterFeign.isImageWellExposed(
                new ImageWellExposedReqDTO(
                        byteImage,
                        UUID.randomUUID().toString(),
                        sourcePreVerification.getLat(),
                        sourcePreVerification.getLon(),
                        sourcePreVerification.getCreatedAt().toString()));
    }

    public ImageWellExposedResDTO isImageWellExposedResult(String taskId) {
        return imageProcessingAdapterFeign.isImageWellExposedResult(taskId);
    }

    public void addTaskIdForSourcePreVerification(SourcePreVerification sourcePreVerification, String taskId) {
        sourcePreVerification.addTask(taskId);
        repository.save(sourcePreVerification);
    }

    public void taskFailed(SourcePreVerification sourcePreVerification) {
        sourcePreVerification.cancel();
        repository.save(sourcePreVerification);
    }

    public void saveResultsImageWellExposed(SourcePreVerification sourcePreVerification, ImageWellExposedResDTO dto) {
        sourcePreVerification.saveResults(dto);
        repository.save(sourcePreVerification);
    }

    public void deleteTask(SourcePreVerification sourcePreVerification) {
        try {
            imageProcessingAdapterFeign.deleteTask(sourcePreVerification.getTaskId());
            log.debug("Deleted task from the AI");
        } catch (Exception e) {
            log.debug("Failed to delete task from the AI");
        }
        sourcePreVerification.clearTaskId();
        repository.save(sourcePreVerification);
    }
}
