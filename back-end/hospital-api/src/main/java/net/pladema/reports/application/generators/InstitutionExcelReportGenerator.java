package net.pladema.reports.application.generators;

import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import net.pladema.reports.application.ReportInstitutionQueryBo;

public interface InstitutionExcelReportGenerator {
	StoredFileBo run(ReportInstitutionQueryBo institutionMonthlyReportParams);
}
