package ar.lamansys.refcounterref.application.updatereferenceforwarding;

import ar.lamansys.refcounterref.application.port.ReferenceForwardingStorage;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateReferenceForwarding {

	private final ReferenceForwardingStorage referenceForwardingStorage;

	public void run(Integer forwardingId, String observation) {
		var forwarding = referenceForwardingStorage.getForwarding(forwardingId);
		referenceForwardingStorage.save(forwarding.getReferenceId(), observation, forwarding.getType().getId());
	}

}
