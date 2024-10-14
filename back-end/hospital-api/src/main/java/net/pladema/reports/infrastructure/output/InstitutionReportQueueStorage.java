package net.pladema.reports.infrastructure.output;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.pladema.reports.application.ReportInstitutionQueryBo;
import net.pladema.reports.domain.InstitutionReportType;
import net.pladema.reports.domain.InstitutionReportQueueItemBo;

public interface InstitutionReportQueueStorage {

	Page<InstitutionReportQueueItemBo> findQueueReports(InstitutionReportType reportType, ReportInstitutionQueryBo reportInstitutionMonthlyQueue, Pageable pageable);
	InstitutionReportQueueItemBo addReportToQueue(InstitutionReportType reportType, ReportInstitutionQueryBo reportInstitutionMonthly);
}
