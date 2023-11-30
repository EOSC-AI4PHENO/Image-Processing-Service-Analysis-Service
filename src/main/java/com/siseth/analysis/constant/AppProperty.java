package com.siseth.analysis.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AppProperty {

    public static Boolean DOWNLOAD_FILE_FROM_FEDORA_SCHEDULE;

    public static Boolean SEND_DETAIL_TO_ANALYSIS_SCHEDULE;

    public static Boolean DOWNLOAD_DETAIL_TO_ANALYSIS_SCHEDULE;

    public static Boolean VERIFY_IMAGE_SCHEDULE;

    @Value("${app.schedule.download-file-from-fedora-schedule}")
    public void setDownloadFileFromFedoraSchedule(Boolean schedule) {
        DOWNLOAD_FILE_FROM_FEDORA_SCHEDULE = schedule;
    }

    @Value("${app.schedule.send-detail-to-analysis-schedule}")
    public void setSendDetailToAnalysisSchedule(Boolean schedule) {
        SEND_DETAIL_TO_ANALYSIS_SCHEDULE = schedule;
    }

    @Value("${app.schedule.download-detail-to-analysis-schedule}")
    public void setDownloadDetailToAnalysisSchedule(Boolean schedule) {
        DOWNLOAD_DETAIL_TO_ANALYSIS_SCHEDULE = schedule;
    }

    @Value("${app.schedule.verify-image-schedule}")
    public void setVerifyImageSchedule(Boolean schedule) {
        VERIFY_IMAGE_SCHEDULE = schedule;
    }

}
