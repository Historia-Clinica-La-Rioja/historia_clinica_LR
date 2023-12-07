package ar.lamansys.refcounterref.infraestructure.output.repository.referenceobservation;

import ar.lamansys.refcounterref.application.port.ReferenceObservationStorage;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class ReferenceObservationStorageImpl implements ReferenceObservationStorage {

	private final ReferenceObservationRepository referenceObservationRepository;

	@Override
	public void save(Integer referenceId, String observation) {
		log.debug("Imput parameters -> referenceId {}, observation {} ", referenceId, observation);
		referenceObservationRepository.save(new ReferenceObservation(referenceId, observation));
	}

}
