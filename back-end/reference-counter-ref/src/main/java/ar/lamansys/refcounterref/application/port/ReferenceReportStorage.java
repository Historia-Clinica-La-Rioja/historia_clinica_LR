package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.report.ReferenceReportBo;
import ar.lamansys.refcounterref.domain.report.ReferenceReportFilterBo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReferenceReportStorage {

	Page<ReferenceReportBo> fetchReferencesReport(ReferenceReportFilterBo filter, Pageable pageable);

}
