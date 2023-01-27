package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.MedicationStatement;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import ar.lamansys.sgx.shared.migratable.SGXDocumentEntityRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MedicationStatementRepository extends SGXAuditableEntityJPARepository<MedicationStatement, Integer>, SGXDocumentEntityRepository<MedicationStatement> {

	@Override
	@Transactional(readOnly = true)
	@Query("SELECT ms " +
			"FROM DocumentMedicamentionStatement dms " +
			"JOIN MedicationStatement ms ON dms.pk.medicationStatementId = ms.id " +
			"WHERE dms.pk.documentId IN :documentIds")
	List<MedicationStatement> getEntitiesByDocuments(@Param("documentIds") List<Long> documentIds);

}
