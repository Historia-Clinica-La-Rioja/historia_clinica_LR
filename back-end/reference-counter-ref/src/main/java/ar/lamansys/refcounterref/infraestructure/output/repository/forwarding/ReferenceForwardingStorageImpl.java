package ar.lamansys.refcounterref.infraestructure.output.repository.forwarding;

import ar.lamansys.refcounterref.application.port.ReferenceForwardingStorage;

import ar.lamansys.refcounterref.domain.enums.EReferenceForwardingType;
import ar.lamansys.refcounterref.domain.reference.ReferenceForwardingBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReferenceForwardingStorageImpl implements ReferenceForwardingStorage {

	private final ReferenceForwardingRepository referenceForwardingRepository;

	private final SharedPersonPort sharedPersonPort;

	@Override
	public void save(Integer referenceId, String observation, short forwardingTypeId) {
		log.debug("Input parameters -> referenceId {}, observation {}, forwardingTypeId {} ", referenceId, observation, forwardingTypeId);
		referenceForwardingRepository.save(mapToEntity(referenceId, observation, forwardingTypeId));
	}

	@Override
	public ReferenceForwardingBo getForwarding(Integer forwardingId) {
		return mapToReferenceForwardingBo(referenceForwardingRepository.getById(forwardingId));
	}

	@Override
	public boolean hasRegionalForwarding(Integer referenceId) {
		var forwardings = referenceForwardingRepository.findByReferenceId(referenceId);
		return forwardings.stream()
				.anyMatch(f -> f.getType().equals(EReferenceForwardingType.REGIONAL));
	}

	@Override
	public boolean hasDomainForwarding(Integer referenceId) {
		var forwardings = referenceForwardingRepository.findByReferenceId(referenceId);
		return forwardings.stream()
				.anyMatch(f -> f.getType().equals(EReferenceForwardingType.DOMAIN));
	}

	@Override
	public ReferenceForwardingBo getForwardingByReferenceId(Integer referenceId) {
		log.debug("Input parameter -> referenceId {}", referenceId);
		var forwardings = referenceForwardingRepository.findByReferenceId(referenceId);
		return forwardings.stream()
				.findFirst()
				.map(rf -> {
					rf.setCreatedBy(sharedPersonPort.getCompletePersonNameById(rf.getPersonId()));
					return rf;
				}).orElse(null);
	}

	@Override
	public EReferenceForwardingType getLastForwardingReference(Integer referenceId) {
		log.debug("Input parameter -> referenceId {}", referenceId);
		var forwardings = referenceForwardingRepository.findByReferenceId(referenceId);
		if (!forwardings.isEmpty())
			return forwardings.stream().findFirst().get().getType();
		return null;
	}

	private ReferenceForwardingBo mapToReferenceForwardingBo(ReferenceForwarding entity) {
		return ReferenceForwardingBo.builder()
				.id(entity.getId())
				.referenceId(entity.getReference_id())
				.type(EReferenceForwardingType.getById(entity.getForwardingTypeId()))
				.userId(entity.getCreatedBy())
				.build();
	}

	private ReferenceForwarding mapToEntity(Integer referenceId, String observation, short forwardingTypeId) {
		return new ReferenceForwarding(referenceId, observation, forwardingTypeId);
	}

}
