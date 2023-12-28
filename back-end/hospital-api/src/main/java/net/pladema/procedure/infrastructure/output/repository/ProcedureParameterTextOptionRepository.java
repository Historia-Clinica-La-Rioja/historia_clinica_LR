package net.pladema.procedure.infrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureParameterTextOption;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProcedureParameterTextOptionRepository extends SGXAuditableEntityJPARepository<ProcedureParameterTextOption, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT ppto.description " +
			"FROM ProcedureParameterTextOption ppto " +
			"WHERE ppto.procedureParameterId = :procedureParameterId and ppto.deleteable.deleted = false")
	List<String> getDescriptionsFromProcedureParameterId(@Param("procedureParameterId") Integer procedureParameterId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE ProcedureParameterTextOption ppto " +
			"SET ppto.deleteable.deleted = true, " +
			"ppto.deleteable.deletedOn = CURRENT_TIMESTAMP, " +
			"ppto.deleteable.deletedBy = ?#{ principal.userId } " +
			"WHERE ppto.procedureParameterId = :procedureParameterId")
	void deleteTextOptionFromProcedureParameterId(@Param("procedureParameterId") Integer procedureParameterId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT ppto " +
			"FROM ProcedureParameterTextOption ppto " +
			"WHERE ppto.procedureParameterId = :procedureParameterId and ppto.deleteable.deleted = false")
	List<ProcedureParameterTextOption> getFromProcedureParameterId(@Param("procedureParameterId") Integer procedureParameterId);
}
