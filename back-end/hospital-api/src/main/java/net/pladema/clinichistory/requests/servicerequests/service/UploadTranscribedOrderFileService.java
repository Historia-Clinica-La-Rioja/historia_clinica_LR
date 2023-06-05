package net.pladema.clinichistory.requests.servicerequests.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadTranscribedOrderFileService {
    List<Integer> execute(MultipartFile[] files, Integer orderId, Integer patientId);
}
