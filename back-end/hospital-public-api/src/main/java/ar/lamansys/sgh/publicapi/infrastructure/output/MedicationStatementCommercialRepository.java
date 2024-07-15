package ar.lamansys.sgh.publicapi.infrastructure.output;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
public interface MedicationStatementCommercialRepository extends SGXAuditableEntityJPARepository<MedicationStatementCommercial, Integer> {

	@Modifying
	@Transactional
	@Query("UPDATE MedicationStatementCommercial msc SET msc.deleteable.deleted = true, msc.deleteable.deletedOn = CURRENT_TIMESTAMP, msc.deleteable.deletedBy = ?#{ principal.userId } WHERE msc.medicationStatementId IN :medicationStatementIds")
	void logicalDeleteAllByMedicationStatementIds(@Param("medicationStatementIds") Set<Integer> medicationStatementIds);

}
