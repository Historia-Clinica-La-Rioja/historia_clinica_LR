package ar.lamansys.refcounterref.infraestructure.output.repository.referenceobservation;

import ar.lamansys.refcounterref.application.port.ReferenceObservationStorage;

import ar.lamansys.refcounterref.domain.reference.ReferenceObservationBo;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReferenceObservationStorageImpl implements ReferenceObservationStorage {

	private final ReferenceObservationRepository referenceObservationRepository;

	private final SharedPersonPort sharedPersonPort;

	@Override
	public void save(Integer referenceId, String observation) {
		log.debug("Imput parameters -> referenceId {}, observation {} ", referenceId, observation);
		referenceObservationRepository.save(new ReferenceObservation(referenceId, observation));
	}

	@Override
	public Optional<ReferenceObservationBo> getReferenceObservation(Integer referenceId) {
		log.debug("Imput parameter -> referenceId {} ", referenceId);
		var observations = referenceObservationRepository.getObservationsByReferenceId(referenceId);
		return observations
				.stream().findFirst()
				.map(this::setCreatedBy);
	}

	private ReferenceObservationBo setCreatedBy(ReferenceObservationBo observation) {
		observation.setCreatedBy(sharedPersonPort.getCompletePersonNameById(observation.getPersonId()));
		return observation;
	}

}
