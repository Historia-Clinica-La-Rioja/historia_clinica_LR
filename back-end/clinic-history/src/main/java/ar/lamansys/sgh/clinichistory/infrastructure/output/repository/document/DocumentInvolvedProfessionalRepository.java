package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentInvolvedProfessional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
public interface DocumentInvolvedProfessionalRepository extends JpaRepository<DocumentInvolvedProfessional, Integer> {

	@Transactional
	@Modifying
	@Query("UPDATE DocumentInvolvedProfessional dip SET dip.signatureStatusId = :signatureStatusId, dip.statusUpdateDate = :currentDate WHERE dip.id = :documentInvolvedProfessionalId")
	void updateSignatureStatus(@Param("documentInvolvedProfessionalId") Integer documentInvolvedProfessionalId, @Param("signatureStatusId") Short signatureStatusId,
							   @Param("currentDate") LocalDate currentDate);

}
