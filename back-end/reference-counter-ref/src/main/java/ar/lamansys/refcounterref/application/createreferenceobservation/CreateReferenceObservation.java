package ar.lamansys.refcounterref.application.createreferenceobservation;


import ar.lamansys.refcounterref.application.createreferenceobservation.exceptions.ReferenceObservationException;
import ar.lamansys.refcounterref.application.createreferenceobservation.exceptions.ReferenceObservationExceptionEnum;
import ar.lamansys.refcounterref.application.port.ReferenceObservationStorage;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateReferenceObservation {

	private final ReferenceObservationStorage referenceObservationStorage;

	public void run(Integer referenceId, String observation) {
		assertContextValid(referenceId, observation);
		referenceObservationStorage.save(referenceId, observation);
	}

	private void assertContextValid(Integer referenceId, String observation) {
		if (referenceId == null)
			throw new ReferenceObservationException(ReferenceObservationExceptionEnum.NULL_REFRENCE_ID, "El id de la referencia es obligatorio");
		if (observation == null || observation.isBlank())
			throw new ReferenceObservationException(ReferenceObservationExceptionEnum.NULL_OBSERVATION, "El texto de observación es obligatorio");
	}

}
