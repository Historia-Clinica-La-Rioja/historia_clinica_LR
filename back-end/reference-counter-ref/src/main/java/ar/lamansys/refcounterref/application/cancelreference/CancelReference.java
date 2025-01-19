package ar.lamansys.refcounterref.application.cancelreference;

import ar.lamansys.refcounterref.application.cancelreference.exceptions.CancelReferenceException;
import ar.lamansys.refcounterref.application.cancelreference.exceptions.CancelReferenceExceptionEnum;
import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import ar.lamansys.refcounterref.domain.enums.EReferenceRegulationState;
import ar.lamansys.refcounterref.domain.enums.EReferenceStatus;
import ar.lamansys.refcounterref.domain.reference.ReferenceDataBo;
import ar.lamansys.refcounterref.infraestructure.output.repository.reference.Reference;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedServiceRequestPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CancelReference {

	private final ReferenceStorage referenceStorage;
	private final SharedServiceRequestPort sharedServiceRequestPort;

	public Boolean run (Integer userId, Integer referenceId){
		log.debug("Input parameters -> userId, referenceId");
		assertValidReference(userId, referenceId);
		cancelServiceRequest(referenceId);
		referenceStorage.deleteAndUpdateStatus(referenceId, EReferenceStatus.CANCELLED.getId());
		return Boolean.TRUE;
	}

	private void assertValidReference(Integer userId, Integer referenceId){
		ReferenceDataBo referenceData = referenceStorage.getReferenceData(referenceId).orElse(null);
		if (referenceData == null)
			throw new CancelReferenceException(CancelReferenceExceptionEnum.INVALID_REFERENCE_ID, String.format("La referencia con id %s no existe", referenceId));
		if (!referenceData.getCreatedBy().equals(userId))
			throw new CancelReferenceException(CancelReferenceExceptionEnum.INVALID_USER_ID, "Solo puede cancelar una referencia el usuario que la creó");
		if (!referenceData.getStatus().equals(EReferenceStatus.ACTIVE))
			throw new CancelReferenceException(CancelReferenceExceptionEnum.INVALID_REFERENCE_STATUS, "La referencia ya se encuentra cancelada");
		if (!referenceData.getRegulationState().equals(EReferenceRegulationState.SUGGESTED_REVISION))
			throw new CancelReferenceException(CancelReferenceExceptionEnum.INVALID_REFERENCE_REGULATION_STATE, "La referencia debe estar en revisión para poder cancelarla");
	}
	private void cancelServiceRequest(Integer referenceId){
		Optional<Integer> serviceRequestId = referenceStorage.findById(referenceId).map(Reference::getServiceRequestId);
        serviceRequestId.ifPresent(sharedServiceRequestPort::cancelServiceRequest);
	}

}
