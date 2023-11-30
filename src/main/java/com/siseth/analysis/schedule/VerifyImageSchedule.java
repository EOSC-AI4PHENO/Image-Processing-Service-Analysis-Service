package com.siseth.analysis.schedule;

import com.siseth.analysis.entity.SourcePreVerification;
import com.siseth.analysis.feign.api.response.ImageWellExposedResDTO;
import com.siseth.analysis.feign.api.response.TaskResultResDTO;
import com.siseth.analysis.service.ScheduleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.siseth.analysis.constant.AppProperty.VERIFY_IMAGE_SCHEDULE;

@Component
@AllArgsConstructor
@Slf4j
public class VerifyImageSchedule {

    private final ScheduleService service;

    @Scheduled(initialDelay = 200L, fixedRate = 60000L)
    public void schedule() {
        log.debug("Check VerifyImageSchedule");
        if(!VERIFY_IMAGE_SCHEDULE)
            return;
        log.debug("Start VerifyImageSchedule");
        // ***********************
        // START PROCESSING IMAGES
        // ***********************
        log.debug("Start processing images");

        List<SourcePreVerification> sourcePreVerificationList = service.getImagesForVerification();

        sourcePreVerificationList.forEach(sourcePreVerification -> {
            byte[] byteImage = service.downloadImageFromFedora(sourcePreVerification.getImageId());
            if (byteImage != null) {
                TaskResultResDTO taskResultResDTO = service.isImageWellExposed(byteImage, sourcePreVerification);
                service.addTaskIdForSourcePreVerification(sourcePreVerification, taskResultResDTO.getTask_id());
                log.debug("started task: " + taskResultResDTO.getTask_id());
            }else {
                service.taskFailed(sourcePreVerification);
            }
        });

        // ***********************
        // START GET RESULTS FROM PROCESSED IMAGES
        // ***********************
        log.debug("Start to get results from processed images");

        List<SourcePreVerification> processedImage = service.getImagesThatAreBeingProcessed();

        processedImage.forEach(sourcePreVerification -> {
            try {
                ImageWellExposedResDTO imageWellExposedResDTO = service.isImageWellExposedResult(sourcePreVerification.getTaskId());
                service.saveResultsImageWellExposed(sourcePreVerification, imageWellExposedResDTO);
                log.debug("getting results for task: " + sourcePreVerification.getTaskId());
            } catch (Exception e) {
                log.debug("FAILED TO GET FEIGN RESPONSE for the task: "  + sourcePreVerification.getTaskId());
            }
        });

        // ***********************
        // START TO DELETE TASKS
        // ***********************
        log.debug("Start to delete tasks from the AI");

        List<SourcePreVerification> finishedImages = service.getImagesThatAreFinished();

        finishedImages.forEach(service::deleteTask);
    }

}
