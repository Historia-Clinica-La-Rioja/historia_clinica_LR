package ar.lamansys.refcounterref.application.updatereferenceforwarding;

import ar.lamansys.refcounterref.application.port.CounterReferenceStorage;
import ar.lamansys.refcounterref.application.port.ReferenceForwardingStorage;
import ar.lamansys.refcounterref.application.referenceforwarding.exceptions.ReferenceForwardingException;
import ar.lamansys.refcounterref.application.referenceforwarding.exceptions.ReferenceForwardingExceptionEnum;
import ar.lamansys.refcounterref.domain.reference.ReferenceForwardingBo;
import ar.lamansys.sgx.shared.security.UserInfo;

import com.google.zxing.oned.OneDimensionalCodeWriter;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateReferenceForwarding {

	private final ReferenceForwardingStorage referenceForwardingStorage;

	private final CounterReferenceStorage counterReferenceStorage;

	public void run(Integer forwardingId, String observation) {
		if (forwardingId == null)
			throw new ReferenceForwardingException(ReferenceForwardingExceptionEnum.NULL_FORWARDING_ID, "El identificador de la derivación es obligatorio");

		var forwarding = referenceForwardingStorage.getForwarding(forwardingId);
		assertContextValid(forwarding, observation);

		referenceForwardingStorage.save(forwarding.getReferenceId(), observation, forwarding.getType().getId());
	}

	private void assertContextValid(ReferenceForwardingBo forwarding, String observation) {
		var referenceHasClosure = counterReferenceStorage.existsCounterReference(forwarding.getReferenceId());
		if (referenceHasClosure)
			throw new ReferenceForwardingException(ReferenceForwardingExceptionEnum.REFERENCE_HAS_CLOSURE, "La referencia posee un cierre");
		if (!forwarding.getUserId().equals(UserInfo.getCurrentAuditor()))
			throw new ReferenceForwardingException(ReferenceForwardingExceptionEnum.INVALID_USER, "La derivación solo puede ser editada por quien la haya iniciado");
		if (observation == null || observation.isBlank())
			throw new ReferenceForwardingException(ReferenceForwardingExceptionEnum.NULL_OBSERVATION, "El texto de observación es obligatorio");
	}

}
