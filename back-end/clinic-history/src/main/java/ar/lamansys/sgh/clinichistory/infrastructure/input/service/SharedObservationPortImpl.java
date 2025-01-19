package ar.lamansys.sgh.clinichistory.infrastructure.input.service;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.observation.FhirObservationMapper;
import ar.lamansys.sgh.clinichistory.application.observation.SaveFhirObservationService;
import ar.lamansys.sgh.shared.infrastructure.input.service.observation.FhirObservationGroupInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.observation.SharedObservationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SharedObservationPortImpl implements SharedObservationPort {

	private final SaveFhirObservationService saveFhirObservationService;
	private final FhirObservationMapper fhirObservationMapper;

	@Override
	public void save(FhirObservationGroupInfoDto fhirObservationGroupInfoDto) {
		saveFhirObservationService.save(fhirObservationMapper.toFhirObservationGroupBo(fhirObservationGroupInfoDto));
	}
}
