package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.ReferenceReportBo;

import java.time.LocalDate;
import java.util.List;

public interface ReferenceReportStorage {

	List<ReferenceReportBo> fetchReceivedReferencesReport(Integer institutionId, LocalDate from, LocalDate to);

	List<ReferenceReportBo> fetchRequestedReferencesReport(Integer institutionId, Integer healthcareProfessionalId, LocalDate from, LocalDate to);

}
