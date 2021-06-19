package net.pladema.clinichistory.requests.servicerequests.service.impl;

import net.pladema.clinichistory.requests.servicerequests.repository.DiagnosticReportFileRepository;
import net.pladema.clinichistory.requests.servicerequests.service.ServeDiagnosticReportFileService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.StoredFileBo;
import ar.lamansys.sgx.shared.files.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ServeDiagnosticReportFileServiceImpl implements ServeDiagnosticReportFileService {

    private final DiagnosticReportFileRepository diagnosticReportFileRepository;
    private final FileService fileService;

    private static final Logger LOG = LoggerFactory.getLogger(ServeDiagnosticReportFileServiceImpl.class);
    private final String OUTPUT = "Output -> {}";

    public ServeDiagnosticReportFileServiceImpl(DiagnosticReportFileRepository diagnosticReportFileRepository, FileService fileService) {
        this.diagnosticReportFileRepository = diagnosticReportFileRepository;
        this.fileService = fileService;
    }

    @Override
    public StoredFileBo run(Integer fileId) {
        LOG.debug("input -> fileId {}", fileId);
        StoredFileBo result = diagnosticReportFileRepository.findById(fileId).stream().map(drf ->
                new StoredFileBo(
                        fileService.loadFile(drf.getPath()),
                        drf.getContentType(),
                        drf.getSize())).findFirst().orElse(null);
        LOG.debug(OUTPUT, result);
        return result;
    }
}
