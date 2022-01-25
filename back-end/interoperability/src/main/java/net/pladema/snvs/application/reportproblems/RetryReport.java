package net.pladema.snvs.application.reportproblems;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snvs.application.ports.event.exceptions.SnvsStorageException;
import net.pladema.snvs.application.ports.report.ReportPort;
import net.pladema.snvs.application.ports.report.ReportStorage;
import net.pladema.snvs.application.ports.report.exceptions.ReportPortException;
import net.pladema.snvs.application.reportproblems.exceptions.ReportProblemException;
import net.pladema.snvs.domain.event.exceptions.SnvsEventInfoBoException;
import net.pladema.snvs.domain.problem.exceptions.SnvsProblemBoException;
import net.pladema.snvs.domain.report.ReportCommandBo;
import net.pladema.snvs.domain.report.SnvsReportBo;
import net.pladema.snvs.infrastructure.configuration.SnvsCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Conditional(SnvsCondition.class)
public class RetryReport {

	private final ReportStorage reportStorage;

	private final ReportPort reportPort;

	private final ReportBuilder reportBuilder;

	public RetryReport(ReportStorage reportStorage, ReportPort reportPort, ReportBuilder reportBuilder) {
		this.reportStorage = reportStorage;
		this.reportPort = reportPort;
		this.reportBuilder = reportBuilder;
	}

	public SnvsReportBo run(Integer snvsReportId) throws ReportPortException, SnvsProblemBoException, SnvsStorageException, ReportProblemException, SnvsEventInfoBoException {
		SnvsReportBo queryResult = reportStorage.findById(snvsReportId).orElseThrow(
				() -> new NotFoundException("Report-not-found", "Report not found"));
		ReportCommandBo toRun = new ReportCommandBo(queryResult);
		SnvsReportBo response = reportPort.run(reportBuilder.buildReport(toRun));
		response.setProblemBo(toRun.getProblemBo());
		response = reportStorage.save(response);
		return response;
	}


}
