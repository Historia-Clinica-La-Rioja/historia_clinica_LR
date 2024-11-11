package ar.lamansys.refcounterref.application.port;

public interface HistoricReferenceAdministrativeStateStorage {

	Integer save(Integer referenceId, Short administrativeStateId, String reason);

	void updateReferenceAdministrativeState(Integer referenceId, Short administrativeStateId, String reason);

}
