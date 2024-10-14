package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.MedicationStatement;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import ar.lamansys.sgx.shared.migratable.SGXDocumentEntityRepository;

import org.springframework.data.jpa.repository.Modifying;
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

	@Transactional(readOnly = true)
	@Query("SELECT ms.prescriptionLineState " +
			"FROM MedicationStatement ms " +
			"WHERE ms.id = :medicationStatementId")
    Short fetchMedicationStatementLineStateById(@Param("medicationStatementId") Integer medicationStatementId);

	@Transactional
	@Modifying
	@Query("UPDATE MedicationStatement ms SET ms.prescriptionLineState = :stateId WHERE ms.id = :medicationStatementId")
	void updateMedicationStatementLineStateById(@Param("medicationStatementId") Integer medicationStatementId,@Param("stateId") short stateId);

	@Transactional(readOnly = true)
	@Query("SELECT q.value " +
			"FROM MedicationStatement ms " +
			"JOIN Dosage d ON (d.id = ms.dosageId) " +
			"JOIN Quantity q ON (q.id = d.doseQuantityId) " +
			"WHERE ms.id = :medicationStatementId")
	Double fetchMedicationStatementQuantityById(@Param("medicationStatementId") Integer medicationStatementId);

}
