package net.pladema.reports.application.institutionreportqueueadd;

import lombok.AllArgsConstructor;
import net.pladema.reports.application.ReportInstitutionQueryBo;
import net.pladema.reports.domain.InstitutionReportType;
import net.pladema.reports.domain.InstitutionReportQueueItemBo;
import net.pladema.reports.infrastructure.output.InstitutionReportQueueStorage;

@AllArgsConstructor
public class AddReportInstitutionToQueue {
	private final InstitutionReportQueueStorage storage;

	public InstitutionReportQueueItemBo run(
			InstitutionReportType reportType,
			ReportInstitutionQueryBo filterParams
	) {
		return storage.addReportToQueue(reportType, filterParams);
	}
}
