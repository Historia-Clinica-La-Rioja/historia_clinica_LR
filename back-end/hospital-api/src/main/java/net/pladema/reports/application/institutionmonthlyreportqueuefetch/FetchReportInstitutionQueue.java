package net.pladema.reports.application.institutionmonthlyreportqueuefetch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;
import net.pladema.reports.application.ReportInstitutionQueryBo;
import net.pladema.reports.domain.InstitutionReportType;
import net.pladema.reports.domain.InstitutionReportQueueItemBo;
import net.pladema.reports.infrastructure.output.InstitutionReportQueueStorage;

@AllArgsConstructor
public class FetchReportInstitutionQueue {
	private final InstitutionReportQueueStorage storage;

	public Page<InstitutionReportQueueItemBo> run(
			InstitutionReportType reportType,
			ReportInstitutionQueryBo filterParams,
			Pageable pageable
	) {
		return storage.findQueueReports(reportType, filterParams, pageable);
	}
}
