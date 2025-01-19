package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.ips.FhirObservationGroupBo;

import java.util.Optional;

public interface FhirObservationStorage {
	void save(FhirObservationGroupBo observationGroupBo);

	Optional<FhirObservationGroupBo> findByDiagnosticReportId(Integer diagnosticReportId);
}
