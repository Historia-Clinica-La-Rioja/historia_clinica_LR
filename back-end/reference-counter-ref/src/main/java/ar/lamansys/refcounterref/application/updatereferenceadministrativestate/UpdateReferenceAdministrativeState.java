package ar.lamansys.refcounterref.application.updatereferenceadministrativestate;

import ar.lamansys.refcounterref.application.port.CounterReferenceStorage;
import ar.lamansys.refcounterref.application.port.HistoricReferenceAdministrativeStateStorage;
import ar.lamansys.refcounterref.application.port.HistoricReferenceRegulationStorage;
import ar.lamansys.refcounterref.application.port.ReferenceAppointmentStorage;
import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import ar.lamansys.refcounterref.application.updatereferenceadministrativestate.exceptions.UpdateReferenceAdministrativeStateException;
import ar.lamansys.refcounterref.application.updatereferenceadministrativestate.exceptions.UpdateReferenceAdministrativeStateExceptionEnum;
import ar.lamansys.refcounterref.domain.enums.EReferenceAdministrativeState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateReferenceAdministrativeState {

	private final HistoricReferenceAdministrativeStateStorage historicReferenceAdministrativeStateStorage;
	private final HistoricReferenceRegulationStorage historicReferenceRegulationStorage;
	private final CounterReferenceStorage counterReferenceStorage;
	private final ReferenceAppointmentStorage referenceAppointmentStorage;

	public void run (Integer referenceId, Short administrativeStateId, String reason){
		log.debug("Input parameters -> referenceId {}, administrativeStateId {}, reason {}", reason, administrativeStateId, reason);
		assertContextValid(referenceId);
		validateStateChange(administrativeStateId, reason);
		historicReferenceAdministrativeStateStorage.updateReferenceAdministrativeState(referenceId, administrativeStateId, reason);
		if (administrativeStateId.equals(EReferenceAdministrativeState.SUGGESTED_REVISION.getId()))
			historicReferenceRegulationStorage.updateReferenceRegulationState(referenceId, EReferenceAdministrativeState.WAITING_APPROVAL.getId(), null);
	}

	private void assertContextValid(Integer referenceId) {
		var referenceHasClosure = counterReferenceStorage.existsCounterReference(referenceId);
		if (referenceHasClosure)
			throw new UpdateReferenceAdministrativeStateException(UpdateReferenceAdministrativeStateExceptionEnum.CLOSED_REFERENCE, "No es posible cambiar el estado de aprobación ya que la referencia se encuentra cerrada");
		boolean referenceHasAppointment = referenceAppointmentStorage.referenceHasAppointment(referenceId);
		if (referenceHasAppointment)
			throw new UpdateReferenceAdministrativeStateException(UpdateReferenceAdministrativeStateExceptionEnum.REFERENCE_HAS_APPOINTMENT, "No es posible cambiar el estado de aprobación de la referencia ya que la misma posee un turno asociado");
	}

	private void validateStateChange(Short stateId, String reason){
		if (stateId.equals(EReferenceAdministrativeState.SUGGESTED_REVISION.getId()) && reason == null)
			throw new UpdateReferenceAdministrativeStateException(UpdateReferenceAdministrativeStateExceptionEnum.REASON_REQUIRED, "Se requiere indicar un motivo");
	}

}
