package ar.lamansys.refcounterref.application.modifyreferencebymanagerrole;

import ar.lamansys.refcounterref.application.modifyReference.exceptions.ModifyReferenceException;
import ar.lamansys.refcounterref.application.modifyReference.exceptions.ModifyReferenceExceptionEnum;
import ar.lamansys.refcounterref.application.port.CounterReferenceStorage;
import ar.lamansys.refcounterref.application.port.HistoricReferenceAdministrativeStateStorage;
import ar.lamansys.refcounterref.application.port.HistoricReferenceRegulationStorage;
import ar.lamansys.refcounterref.application.port.ReferenceAppointmentStorage;
import ar.lamansys.refcounterref.application.port.ReferenceCounterReferenceAppointmentStorage;
import ar.lamansys.refcounterref.application.port.ReferenceCounterReferenceFileStorage;
import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import ar.lamansys.refcounterref.domain.enums.EReferenceAdministrativeState;
import ar.lamansys.refcounterref.domain.enums.EReferenceRegulationState;
import ar.lamansys.refcounterref.infraestructure.output.repository.reference.Reference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ModifyReferenceByManagerRole {

	private final ReferenceStorage referenceStorage;

	private final ReferenceCounterReferenceFileStorage referenceCounterReferenceFileStorage;

	private final CounterReferenceStorage counterReferenceStorage;

	private final ReferenceAppointmentStorage referenceAppointmentStorage;

	private final ReferenceCounterReferenceAppointmentStorage referenceCounterReferenceAppointmentStorage;

	private final HistoricReferenceAdministrativeStateStorage historicReferenceAdministrativeStateStorage;

	private final HistoricReferenceRegulationStorage historicReferenceRegulationStorage;

	@Transactional
	public void run(Integer referenceId, Integer destinationInstitutionId, List<Integer> fileIds) {
		log.debug("Input parameter -> referenceId {},  destinationInstitutionId {}, fileIds {} ", referenceId, destinationInstitutionId, fileIds);
		assertValid(referenceId, destinationInstitutionId);
		updateDestinationInstitution(referenceId, destinationInstitutionId);
		updateReferenceAdministrativeState(referenceId);
		if (fileIds != null && !fileIds.isEmpty())
			referenceCounterReferenceFileStorage.updateReferenceCounterReferenceId(referenceId, fileIds);
		log.debug("reference successfully modified");
	}

	private void updateDestinationInstitution(Integer referenceId, Integer destinationInstitutionId) {
		var isDestinationInstitutionUpdated  = referenceStorage.updateDestinationInstitution(referenceId, destinationInstitutionId);
		if (isDestinationInstitutionUpdated) {
			Optional<Integer> absentAppointmentId = referenceAppointmentStorage.getAbsentAppointmentIdByReferenceId(referenceId);
			absentAppointmentId.ifPresent(aptId -> referenceCounterReferenceAppointmentStorage.cancelAbsentAppointment(aptId, "Turno cancelado por cambio de institución destino en solicitud de referencia"));
		}
	}

	private void assertValid(Integer referenceId, Integer institutionId) {
		boolean referenceIsClosed = counterReferenceStorage.existsCounterReference(referenceId);
		if (referenceIsClosed)
			throw new ModifyReferenceException(ModifyReferenceExceptionEnum.CLOSED_REFERENCE, "No es posible modificar la referencia porque la misma se encuentra cerrada");

		if (institutionId != null) {
			var referenceHasAppointment = referenceAppointmentStorage.referenceHasAppointment(referenceId);
			if (referenceHasAppointment)
				throw new ModifyReferenceException(ModifyReferenceExceptionEnum.HAS_APPOINTMENT, "No es posible modificar la institucion de la referencia porque la misma tiene un turno asignado");
		}
	}

	private void updateReferenceAdministrativeState(Integer referenceId){
		referenceStorage.findById(referenceId).map(Reference::getAdministrativeStateId)
				.ifPresent(administrativeStateId -> {
					if (administrativeStateId.equals(EReferenceAdministrativeState.SUGGESTED_REVISION.getId())){
						historicReferenceRegulationStorage.updateReferenceRegulationState(referenceId, EReferenceRegulationState.AUDITED.getId(), null);
						historicReferenceAdministrativeStateStorage.updateReferenceAdministrativeState(referenceId, EReferenceAdministrativeState.WAITING_APPROVAL.getId(), null);
					}
					historicReferenceAdministrativeStateStorage.updateReferenceAdministrativeState(referenceId, EReferenceAdministrativeState.WAITING_APPROVAL.getId(), null);
			});
	}

}
