package net.pladema.clinichistory.requests.servicerequests.service.impl;

import net.pladema.clinichistory.requests.servicerequests.repository.DiagnosticReportFileRepository;
import net.pladema.clinichistory.requests.servicerequests.service.UpdateDiagnosticReportFileService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpdateDiagnosticReportFileServiceImpl implements UpdateDiagnosticReportFileService {

    private final DiagnosticReportFileRepository diagnosticReportFileRepository;

    public UpdateDiagnosticReportFileServiceImpl(DiagnosticReportFileRepository diagnosticReportFileRepository) {
        this.diagnosticReportFileRepository = diagnosticReportFileRepository;
    }

    @Override
    public void run(Integer diagnosticReportId, List<Integer> fileIds) {
        diagnosticReportFileRepository.updateDiagnosticReportFile(diagnosticReportId, fileIds);
    }
}
