package ar.lamansys.sgh.clinichistory.application.observation;

import ar.lamansys.sgh.clinichistory.application.ports.FhirObservationStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.FhirObservationGroupBo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SaveFhirObservationServiceImpl implements SaveFhirObservationService {
	private final FhirObservationStorage fhirObservationStorage;

	@Override
	public void save(FhirObservationGroupBo observationGroupBo) {
		fhirObservationStorage.save(observationGroupBo);
	}
}
