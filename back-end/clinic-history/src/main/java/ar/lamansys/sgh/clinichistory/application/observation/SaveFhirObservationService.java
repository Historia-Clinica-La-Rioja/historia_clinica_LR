package ar.lamansys.sgh.clinichistory.application.observation;

import ar.lamansys.sgh.clinichistory.domain.ips.FhirObservationGroupBo;

public interface SaveFhirObservationService {
	void save(FhirObservationGroupBo observationGroupBo);
}
