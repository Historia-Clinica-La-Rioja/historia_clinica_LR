package net.pladema.clinichistory.requests.servicerequests.service.impl;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.files.FileService;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.repository.DiagnosticReportFileRepository;
import net.pladema.clinichistory.requests.servicerequests.service.ServeDiagnosticReportFileService;

@Slf4j
@AllArgsConstructor
@Service
public class ServeDiagnosticReportFileServiceImpl implements ServeDiagnosticReportFileService {

    private final DiagnosticReportFileRepository diagnosticReportFileRepository;
	private final FileService fileService;
    private final String OUTPUT = "Output -> {}";


    @Override
    public StoredFileBo run(Integer fileId) {
        log.debug("input -> fileId {}", fileId);
        StoredFileBo result = diagnosticReportFileRepository.findById(fileId).map(drf ->
                new StoredFileBo(
                        fileService.loadFileRelativePath(drf.getPath()),
                        drf.getContentType(),
						drf.getName()
				)
		).orElse(null);
        log.debug(OUTPUT, result);
        return result;
    }
}
