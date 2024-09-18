package ar.lamansys.refcounterref.application.modifyreferencebymanagerrole;

import ar.lamansys.refcounterref.application.modifyReference.exceptions.ModifyReferenceException;
import ar.lamansys.refcounterref.application.modifyReference.exceptions.ModifyReferenceExceptionEnum;
import ar.lamansys.refcounterref.application.port.CounterReferenceStorage;
import ar.lamansys.refcounterref.application.port.ReferenceAppointmentStorage;
import ar.lamansys.refcounterref.application.port.ReferenceCounterReferenceFileStorage;
import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class ModifyReferenceByManagerRole {

	private final ReferenceStorage referenceStorage;

	private final ReferenceCounterReferenceFileStorage referenceCounterReferenceFileStorage;

	private final CounterReferenceStorage counterReferenceStorage;

	private final ReferenceAppointmentStorage referenceAppointmentStorage;

	@Transactional
	public void run(Integer referenceId, Integer destinationInstitutionId, List<Integer> fileIds) {
		log.debug("Input parameter -> referenceId {},  destinationInstitutionId {}, fileIds {} ", referenceId, destinationInstitutionId, fileIds);
		assertValid(referenceId, destinationInstitutionId);
		var oldDestinationInstitutionId = referenceStorage.getDestinationInstitutionId(referenceId);
		if (!Objects.equals(oldDestinationInstitutionId, destinationInstitutionId))
			referenceStorage.updateDestinationInstitution(referenceId, destinationInstitutionId);
		if (fileIds != null && !fileIds.isEmpty())
			referenceCounterReferenceFileStorage.updateReferenceCounterReferenceId(referenceId, fileIds);
		log.debug("reference successfully modified");
	}

	private void assertValid(Integer referenceId, Integer institutionId) {
		boolean referenceIsClosed = counterReferenceStorage.existsCounterReference(referenceId);
		if (referenceIsClosed)
			throw new ModifyReferenceException(ModifyReferenceExceptionEnum.CLOSED_REFERENCE, "No es posible modificar la referencia porque la misma se encuentra cerrada");

		if (institutionId != null) {
			var appointments = referenceAppointmentStorage.getReferenceAppointmentsIdsWithoutCancelledStateId(Collections.singletonList(referenceId));
			if (!appointments.isEmpty())
				throw new ModifyReferenceException(ModifyReferenceExceptionEnum.HAS_APPOINTMENT, "No es posible modificar la institucion de la referencia porque la misma tiene un turno asignado");
		}
	}
}
