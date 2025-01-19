package net.pladema.procedure.infrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcedureTemplateRepository extends SGXAuditableEntityJPARepository<ProcedureTemplate, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT pt " +
			"FROM ProcedureTemplate pt " +
			"WHERE pt.deleteable.deleted = false ")
	List<ProcedureTemplate> getAllActivesProcedureTemplates();

	@Transactional(readOnly = true)
	@Query(value = "SELECT pt " +
			"FROM ProcedureTemplate pt " +
			"WHERE pt.id IN (:procedureTemplateIds) and pt.deleteable.deleted = false")
	List<ProcedureTemplate> getAllActivesProcedureTemplateByIds(@Param("procedureTemplateIds") List<Integer> procedureTemplateIds);

	@Transactional(readOnly = true)
	@Query(value = "SELECT pt " +
			"FROM ProcedureTemplate pt " +
			"WHERE pt.id = :procedureTemplateId and pt.deleteable.deleted = false")
	Optional<ProcedureTemplate> findActiveProcedureTemplateById(@Param("procedureTemplateId") Integer procedureTemplateId);

	@Modifying
	@Query(value = "UPDATE ProcedureTemplate pt SET pt.statusId = :statusId WHERE pt.id = :procedureTemplateId")
    void updateStatus(@Param("procedureTemplateId")Integer procedureTemplateId, @Param("statusId")Short statusId);

	@Query(value = "SELECT pt FROM ProcedureTemplate pt WHERE pt.statusId IN :statusIds AND pt.deleteable.deleted = false")
	Page<ProcedureTemplate> findByStatusIdIn(@Param("statusIds") List<Short> statusIds, Pageable pageable);
}
