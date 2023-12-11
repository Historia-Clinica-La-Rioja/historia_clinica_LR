package ar.lamansys.refcounterref.application.port;

public interface ReferenceForwardingStorage {

	void save(Integer referenceId, String observation, short forwardingTypeId);

}
