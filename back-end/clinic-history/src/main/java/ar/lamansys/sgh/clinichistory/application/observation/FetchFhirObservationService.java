package ar.lamansys.sgh.clinichistory.application.observation;

import ar.lamansys.sgh.clinichistory.domain.ips.FhirObservationGroupBo;

import java.util.Optional;

public interface FetchFhirObservationService {
	Optional<FhirObservationGroupBo> run(Integer diagnosticReportId);
}
