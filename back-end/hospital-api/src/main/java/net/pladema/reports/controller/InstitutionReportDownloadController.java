package net.pladema.reports.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.files.FileService;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.reports.infrastructure.output.repository.ReportQueueRepository;
import net.pladema.reports.infrastructure.output.repository.entity.ReportQueue;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/institutions/{institutionId}/reports/queue/{reportId}")
public class InstitutionReportDownloadController {

    public static final String OUTPUT = "Output -> {}";

	private final FileService fileService;
	private final ReportQueueRepository reportQueueRepository;

    @GetMapping
    public ResponseEntity<Resource> getExcelReport(
            @PathVariable Integer institutionId,
			@PathVariable Integer reportId
    ) {
		Long fileId = reportQueueRepository.findById(reportId).map(ReportQueue::getFileId).orElse(-99999L);
		StoredFileBo generatedReport = fileService.loadStoredFile(fileId);

		return StoredFileResponse.sendFile(
				generatedReport
		);
    }

}
