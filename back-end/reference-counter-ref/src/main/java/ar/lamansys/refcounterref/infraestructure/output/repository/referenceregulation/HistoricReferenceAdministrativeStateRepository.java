package ar.lamansys.refcounterref.infraestructure.output.repository.referenceregulation;

import ar.lamansys.refcounterref.infraestructure.output.repository.referenceregulation.entity.HistoricReferenceAdministrativeState;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoricReferenceAdministrativeStateRepository extends SGXAuditableEntityJPARepository<HistoricReferenceAdministrativeState, Integer> {

	@Query("SELECT hras " +
			"FROM HistoricReferenceAdministrativeState hras " +
			"WHERE hras.referenceId = :referenceId " +
			"ORDER BY hras.creationable.createdOn DESC")
	List<HistoricReferenceAdministrativeState> getByReferenceId(@Param("referenceId")Integer referenceId);

}
