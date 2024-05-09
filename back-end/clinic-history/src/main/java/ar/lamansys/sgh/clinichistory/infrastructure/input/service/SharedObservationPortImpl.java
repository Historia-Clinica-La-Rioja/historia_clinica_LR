package ar.lamansys.sgh.clinichistory.infrastructure.input.service;

import ar.lamansys.sgh.clinichistory.application.observation.ObservationMapper;
import ar.lamansys.sgh.clinichistory.application.observation.ObservationService;
import ar.lamansys.sgh.clinichistory.domain.ips.FhirObservationGroupBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.observation.FhirObservationGroupInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.observation.SharedObservationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SharedObservationPortImpl implements SharedObservationPort {

	private final ObservationService observationService;
	private final ObservationMapper observationMapper;

	@Override
	public void save(FhirObservationGroupInfoDto fhirObservationGroupInfoDto) {
		observationService.save(observationMapper.toFhirObservationGroupBo(fhirObservationGroupInfoDto));
	}
}
