package net.pladema.procedure.infrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureParameter;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcedureParameterRepository extends SGXAuditableEntityJPARepository<ProcedureParameter, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT pp.orderNumber " +
			"FROM ProcedureParameter pp " +
			"WHERE pp.procedureTemplateId = :procedureTemplateId " +
			"ORDER BY pp.orderNumber desc")
	List<Short> getLastOrderParameterFromProcedureTemplateId(@Param("procedureTemplateId") Integer procedureTemplateId);

	@Modifying
	@Query(value = "UPDATE ProcedureParameter pp " +
			"SET pp.orderNumber = pp.orderNumber-1 " +
			"WHERE pp.procedureTemplateId = :procedureTemplateId " +
			"AND pp.orderNumber > :orderNumberDeleted")
	void updateOrderNumberAfterDelete(@Param("procedureTemplateId") Integer procedureTemplateId, @Param("orderNumberDeleted") Short orderNumberDeleted);

	@Transactional(readOnly = true)
	@Query(value = "SELECT (CASE WHEN count(pp.id) > 0 THEN TRUE ELSE FALSE END) " +
			"FROM ProcedureParameter pp " +
			"WHERE pp.procedureTemplateId = :procedureTemplateId " +
			"AND pp.loincId = :loincId " +
			"AND pp.deleteable.deleted = false")
	boolean templateHasSpecificLoincId(@Param("procedureTemplateId") Integer procedureTemplateId, @Param("loincId") Integer loincId);

	@Transactional(readOnly = true)
	Optional<ProcedureParameter> findByProcedureTemplateIdAndOrderNumber(Integer id, Short order);

	List<ProcedureParameter> findByProcedureTemplateId(Integer parentProcedureTemplate);
}
