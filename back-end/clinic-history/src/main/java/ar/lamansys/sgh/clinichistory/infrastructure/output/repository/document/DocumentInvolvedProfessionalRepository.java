package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentInvolvedProfessional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DocumentInvolvedProfessionalRepository extends JpaRepository<DocumentInvolvedProfessional, Integer> {

	@Transactional
	@Modifying
	@Query("UPDATE DocumentInvolvedProfessional dip SET dip.signatureStatusId = :signatureStatusId, dip.statusUpdateDate = :currentDate WHERE dip.id = :documentInvolvedProfessionalId")
	void updateSignatureStatus(@Param("documentInvolvedProfessionalId") Integer documentInvolvedProfessionalId, @Param("signatureStatusId") Short signatureStatusId,
							   @Param("currentDate") LocalDate currentDate);

	@Transactional(readOnly = true)
	@Query("SELECT dip.signatureStatusId " +
			"FROM DocumentInvolvedProfessional dip " +
			"WHERE dip.healthcareProfessionalId = :healthcareProfessionalId " +
			"AND dip.documentId IN :documentIds")
    List<Short> getSignatureStatusIdByDocumentAndHealthcareProfessionalIds(@Param("healthcareProfessionalId") Integer healthcareProfessionalId, @Param("documentIds") List<Long> documentIds);

	@Transactional
	@Modifying
	@Query("UPDATE DocumentInvolvedProfessional dip SET dip.signatureStatusId = :signatureStatusId, dip.statusUpdateDate = :currentDate WHERE dip.documentId IN :documentIds AND dip.healthcareProfessionalId = :healthcareProfessionalId")
	void updateSignatureStatusByDocumentAndHealthcareProfessionalId(@Param("documentIds") List<Long> documentIds, @Param("healthcareProfessionalId") Integer healthcareProfessionalId,
																	@Param("signatureStatusId") Short signatureStatusId, @Param("currentDate") LocalDate currentDate);
}
