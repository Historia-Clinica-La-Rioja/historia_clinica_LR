package net.pladema.reports.application.reportqueueprocess;

import java.time.LocalDateTime;

import net.pladema.reports.domain.InstitutionReportType;
import net.pladema.reports.application.ports.GenerationReportNotificationPort;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.reports.application.ProcessInstitutionExcelReport;
import net.pladema.reports.infrastructure.output.repository.InstitutionReportQueuedRepository;
import net.pladema.reports.infrastructure.output.repository.entity.ReportQueue;

@Slf4j
@RequiredArgsConstructor
@Service
public class GenerateReportFile {
	private final InstitutionReportQueuedRepository institutionReportQueuedRepository;
	private final ProcessInstitutionExcelReport processInstitutionMonthlyExcelReport;
	private final GenerationReportNotificationPort generationReportNotificationPort;

	public FileInfo run(ReportQueue reportQueue) throws InstitutionReportQueuedNotFound, InstitutionExcelReportGeneratorNotFound {
		var reportToProcessOpt = institutionReportQueuedRepository.findById(reportQueue.getId())
				.orElseThrow(InstitutionReportQueuedNotFound::new);
		var result = processInstitutionMonthlyExcelReport.run(reportToProcessOpt);
		this.notify(reportQueue.getCreatedBy(), reportQueue.getCreatedOn(), reportToProcessOpt.getReportType());
		return result;
	}

	private void notify(Integer createdBy, LocalDateTime createdOn, InstitutionReportType reportType) {
        try {
            generationReportNotificationPort.send(createdBy, createdOn, reportType);
        } catch (Exception e) {
            // catch exceptions from notification only and do not propagate them
        }
    }

}
