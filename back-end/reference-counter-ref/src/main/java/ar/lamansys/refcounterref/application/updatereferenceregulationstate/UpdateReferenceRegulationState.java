package ar.lamansys.refcounterref.application.updatereferenceregulationstate;

import ar.lamansys.refcounterref.application.port.CounterReferenceStorage;
import ar.lamansys.refcounterref.application.port.HistoricReferenceRegulationStorage;
import ar.lamansys.refcounterref.application.port.ReferenceAppointmentStorage;
import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import ar.lamansys.refcounterref.application.updatereferenceregulationstate.exceptions.UpdateReferenceRegulationStateException;
import ar.lamansys.refcounterref.application.updatereferenceregulationstate.exceptions.UpdateReferenceRegulationStateExceptionEnum;
import ar.lamansys.refcounterref.domain.enums.EReferenceRegulationState;
import ar.lamansys.refcounterref.infraestructure.output.repository.reference.Reference;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedServiceRequestPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateReferenceRegulationState {

	private final HistoricReferenceRegulationStorage historicReferenceRegulationStorage;
	private final ReferenceStorage referenceStorage;
	private final CounterReferenceStorage counterReferenceStorage;
	private final ReferenceAppointmentStorage referenceAppointmentStorage;
	private final SharedServiceRequestPort sharedServiceRequestPort;

	public void run(Integer referenceId, Short stateId, String reason){
		log.debug("Input parameters -> referenceId {}, stateId {}, reason {}", referenceId, stateId, reason);
		assertContextValid(referenceId);
		validateStateChange(stateId, reason);
		if (stateId.equals(EReferenceRegulationState.REJECTED.getId()))
			cancelServiceRequest(referenceId);
		historicReferenceRegulationStorage.updateReferenceRegulationState(referenceId, stateId, reason);
	}

	private void validateStateChange(Short stateId, String reason){
		EReferenceRegulationState regulationState = EReferenceRegulationState.getById(stateId);
		if (regulationState.equals(EReferenceRegulationState.WAITING_APPROVAL))
			throw new UpdateReferenceRegulationStateException(UpdateReferenceRegulationStateExceptionEnum.INVALID_STATUS_ID, "El nuevo estado no es valido");
		if ((regulationState.equals(EReferenceRegulationState.REJECTED) || regulationState.equals(EReferenceRegulationState.SUGGESTED_REVISION)) && reason == null)
			throw new UpdateReferenceRegulationStateException(UpdateReferenceRegulationStateExceptionEnum.REASON_REQUIRED, "Se debe indicar un motivo");
		if (regulationState.equals(EReferenceRegulationState.APPROVED) && reason != null)
			throw new UpdateReferenceRegulationStateException(UpdateReferenceRegulationStateExceptionEnum.REASON_MUST_BE_EMPTY, "No se debe indicar un motivo para aprobar la solicitud");
	}

	private void assertContextValid(Integer referenceId) {
		var referenceHasClosure = counterReferenceStorage.existsCounterReference(referenceId);
		if (referenceHasClosure)
			throw new UpdateReferenceRegulationStateException(UpdateReferenceRegulationStateExceptionEnum.CLOSED_REFERENCE, "No es posible cambiar el estado de aprobación ya que la referencia se encuentra cerrada");
		boolean referenceHasAppointment = referenceAppointmentStorage.referenceHasAppointment(referenceId);
		if (referenceHasAppointment)
			throw new UpdateReferenceRegulationStateException(UpdateReferenceRegulationStateExceptionEnum.REFERENCE_HAS_APPOINTMENT, "No es posible cambiar el estado de aprobación de la referencia ya que la misma posee un turno asociado");
	}

	private void cancelServiceRequest(Integer referenceId){
        referenceStorage.findById(referenceId).map(Reference::getServiceRequestId).ifPresent(sharedServiceRequestPort::cancelServiceRequest);
    }

}
