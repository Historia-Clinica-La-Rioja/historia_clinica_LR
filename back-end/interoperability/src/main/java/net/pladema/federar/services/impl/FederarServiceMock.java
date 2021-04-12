package net.pladema.federar.services.impl;

import net.pladema.federar.services.FederarService;
import net.pladema.federar.services.domain.FederarResourceAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@ConditionalOnProperty(
		value="ws.federar.enabled",
		havingValue = "false",
		matchIfMissing = true)
public class FederarServiceMock implements FederarService{

	private static final Logger LOG = LoggerFactory.getLogger(FederarServiceMock.class);

	@Override
	public Optional<Integer> federatePatient(FederarResourceAttributes attributes, Integer patientId) {
		LOG.debug("FEDERAR mocked: Patient {} won't federate", attributes);
		return Optional.empty();
	}
	
}
