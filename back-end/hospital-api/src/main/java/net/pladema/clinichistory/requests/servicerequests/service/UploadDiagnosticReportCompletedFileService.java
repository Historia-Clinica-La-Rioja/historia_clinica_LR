package net.pladema.clinichistory.requests.servicerequests.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadDiagnosticReportCompletedFileService {
    List<Integer> execute(List<MultipartFile> files, Integer diagnosticReportId, Integer patientId);
}
