package net.pladema.reports.application.reportqueueprocess;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.reports.application.ProcessInstitutionExcelReport;
import net.pladema.reports.infrastructure.output.repository.InstitutionReportQueuedRepository;
import net.pladema.reports.infrastructure.output.repository.entity.ReportQueue;

@Slf4j
@AllArgsConstructor
@Service
public class GenerateReportFile {
	private final InstitutionReportQueuedRepository institutionReportQueuedRepository;
	private final ProcessInstitutionExcelReport processInstitutionMonthlyExcelReport;

	public FileInfo run(ReportQueue reportQueue) throws InstitutionReportQueuedNotFound, InstitutionExcelReportGeneratorNotFound {
		var reportToProcessOpt = institutionReportQueuedRepository.findById(reportQueue.getId())
				.orElseThrow(InstitutionReportQueuedNotFound::new);
		return processInstitutionMonthlyExcelReport.run(reportToProcessOpt);
	}

}
