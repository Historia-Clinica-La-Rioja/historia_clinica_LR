package net.pladema.clinichistory.requests.servicerequests.service.impl;

import net.pladema.clinichistory.requests.servicerequests.repository.DiagnosticReportFileRepository;
import net.pladema.clinichistory.requests.servicerequests.service.DiagnosticReportDanglingFilesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DiagnosticReportDanglingFilesServiceServiceImplTest implements DiagnosticReportDanglingFilesService {

    private final DiagnosticReportFileRepository diagnosticReportFileRepository;

    private static final Logger LOG = LoggerFactory.getLogger(DiagnosticReportDanglingFilesServiceServiceImplTest.class);
    private final String OUTPUT = "Output -> {}";

    public DiagnosticReportDanglingFilesServiceServiceImplTest(DiagnosticReportFileRepository diagnosticReportFileRepository) {
        this.diagnosticReportFileRepository = diagnosticReportFileRepository;
    }

    @Override
    public List<String> run() {
        var result = diagnosticReportFileRepository.getDanglingFiles();
        LOG.debug(OUTPUT, result);
        return result;
    }
}
