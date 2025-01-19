package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.enums.EReferenceForwardingType;
import ar.lamansys.refcounterref.domain.reference.ReferenceForwardingBo;

public interface ReferenceForwardingStorage {

	void save(Integer referenceId, String observation, short forwardingTypeId);

	boolean hasRegionalForwarding(Integer referenceId);

	boolean hasDomainForwarding(Integer referenceId);

	ReferenceForwardingBo getForwarding(Integer forwardingId);

	ReferenceForwardingBo getForwardingByReferenceId(Integer referenceId);

	EReferenceForwardingType getLastForwardingReference(Integer referenceId);

}
