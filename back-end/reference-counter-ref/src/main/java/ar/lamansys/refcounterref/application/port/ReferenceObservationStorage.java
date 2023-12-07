package ar.lamansys.refcounterref.application.port;

public interface ReferenceObservationStorage {

	void save(Integer referenceId, String observation);

}
