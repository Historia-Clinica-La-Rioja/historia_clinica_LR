package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;
import ar.lamansys.sgh.clinichistory.domain.document.DigitalSignatureDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.SnomedConceptBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentFile;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentFileRepository extends SGXAuditableEntityJPARepository<DocumentFile, Long> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT NEW ar.lamansys.sgh.clinichistory.domain.document.DigitalSignatureDocumentBo( " +
            "d.id, st, d.creationable.createdOn, p.firstName, p.lastName, p.otherLastNames, dt.id, dt.description, pp.firstName, pp.lastName, pp.otherLastNames) " +
            "FROM Document d " +
            "JOIN DocumentType dt ON (d.typeId = dt.id) " +
            "JOIN DocumentFile df ON (d.id = df.id) " +
            "JOIN SourceType st ON (d.sourceTypeId = st.id) " +
            "JOIN User u ON (d.creationable.createdBy = u.id)" +
            "JOIN UserPerson up ON (u.id = up.pk.userId) " +
            "JOIN Person p ON (up.pk.personId = p.id) " +
            "JOIN Patient pa ON (d.patientId = pa.id) " +
            "JOIN Person pp ON (pa.personId = pp.id) " +
            "WHERE d.institutionId = :institutionId " +
            "AND d.statusId = '" + DocumentStatus.FINAL + "' " +
            "AND d.typeId != '" + DocumentType.DIGITAL_RECIPE + "' " +
            "AND d.typeId != '" + DocumentType.SURGICAL_HOSPITALIZATION_REPORT + "' " +
            "AND d.typeId != '" + DocumentType.EMERGENCY_SURGICAL_REPORT + "' " +
            "AND d.typeId != '" + DocumentType.ANESTHETIC_REPORT + "' " +
            "AND d.typeId != '" + DocumentType.NURSING_EMERGENCY_CARE_EVOLUTION + "' " +
            "AND d.creationable.createdBy = :userId " +
            "AND df.signatureStatusId != -1 " +
            "AND df.signatureStatusId != 3")
	Page<DigitalSignatureDocumentBo> findDocumentsByUserAndInstitution(@Param("userId") Integer userId,
																	   @Param("institutionId") Integer institutionId,
																	   Pageable pageable);

	@Transactional(readOnly = true)
	@Query("SELECT NEW ar.lamansys.sgh.clinichistory.domain.document.SnomedConceptBo(s.pt, hc.main) " +
			"FROM DocumentHealthCondition dhc " +
			"JOIN HealthCondition hc ON (dhc.pk.healthConditionId = hc.id) " +
			"JOIN Snomed s ON (hc.snomedId = s.id) " +
			"WHERE dhc.pk.documentId = :documentId ")
	List<SnomedConceptBo> findSnomedConceptsByDocumentId(@Param("documentId") Long documentId);

	@Transactional
	@Modifying
	@Query("UPDATE DocumentFile df SET df.digitalSignatureHash = :hash WHERE df.id = :documentId")
	void updateDigitalSignatureHash(@Param("documentId") Long documentId, @Param("hash") String hash);

	@Transactional(readOnly = true)
	@Query(value = " SELECT EXISTS (" +
			"	SELECT 1 " +
			"	FROM document_file df " +
			"	WHERE df.signature_status_id > 0" +
			")", nativeQuery = true)
	boolean documentsWereAlreadySigned();

}
