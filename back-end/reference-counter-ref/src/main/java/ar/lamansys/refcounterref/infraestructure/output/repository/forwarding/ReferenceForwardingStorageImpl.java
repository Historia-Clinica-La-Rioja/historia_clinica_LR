package ar.lamansys.refcounterref.infraestructure.output.repository.forwarding;

import ar.lamansys.refcounterref.application.port.ReferenceForwardingStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReferenceForwardingStorageImpl implements ReferenceForwardingStorage {

	private final ReferenceForwardingRepository referenceForwardingRepository;

	@Override
	public void save(Integer referenceId, String observation, short forwardingTypeId) {
		log.debug("Input parameters -> referenceId {}, observation {}, forwardingTypeId {} ", referenceId, observation, forwardingTypeId);
		referenceForwardingRepository.save(mapToEntity(referenceId, observation, forwardingTypeId));
	}

	private ReferenceForwarding mapToEntity(Integer referenceId, String observation, short forwardingTypeId) {
		return new ReferenceForwarding(referenceId, observation, forwardingTypeId);
	}

}
