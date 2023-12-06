package ar.lamansys.refcounterref.application.updatereferenceregulationstate;

import ar.lamansys.refcounterref.application.port.HistoricReferenceRegulationStorage;
import ar.lamansys.refcounterref.application.updatereferenceregulationstate.exceptions.UpdateReferenceRegulationStateException;
import ar.lamansys.refcounterref.application.updatereferenceregulationstate.exceptions.UpdateReferenceRegulationStateExceptionEnum;
import ar.lamansys.refcounterref.domain.enums.EReferenceRegulationState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateReferenceRegulationState {

	private final HistoricReferenceRegulationStorage historicReferenceRegulationStorage;

	public Boolean run(Integer referenceId, Short stateId, String reason){
		log.debug("Input parameters -> referenceId {}, stateId {}, reason {}", referenceId, stateId, reason);
		validateStateChange(stateId, reason);
		Boolean result = historicReferenceRegulationStorage.updateReferenceRegulationState(referenceId, stateId, reason);
		log.debug("Output -> {}", result);
		return result;
	}

	public void validateStateChange(Short stateId, String reason){
		EReferenceRegulationState regulationStatus = EReferenceRegulationState.getById(stateId);
		if (regulationStatus.equals(EReferenceRegulationState.WAITING_APPROVAL))
			throw new UpdateReferenceRegulationStateException(UpdateReferenceRegulationStateExceptionEnum.INVALID_STATUS_ID, "El nuevo estado no es valido");
		if ((regulationStatus.equals(EReferenceRegulationState.REJECTED) || regulationStatus.equals(EReferenceRegulationState.SUGGESTED_REVISION)) && reason == null)
			throw new UpdateReferenceRegulationStateException(UpdateReferenceRegulationStateExceptionEnum.REASON_REQUIRED, "Se debe indicar un motivo");
		if (regulationStatus.equals(EReferenceRegulationState.APPROVED) && reason != null) {
			throw new UpdateReferenceRegulationStateException(UpdateReferenceRegulationStateExceptionEnum.REASON_MUST_BE_EMPTY, "No se debe indicar un motivo para aprobar la solicitud");
        }
	}

}
