package net.pladema.clinichistory.external.repository;

import net.pladema.clinichistory.external.repository.domain.ExternalClinicalHistory;
import net.pladema.clinichistory.external.service.domain.ExternalClinicalHistoryBo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ExternalClinicalHistoryRepository extends JpaRepository<ExternalClinicalHistory, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT new net.pladema.clinichistory.external.service.domain.ExternalClinicalHistoryBo(ec.id, ec.professionalSpecialty, " +
			"ec.consultationDate, ec.professionalName, ec.notes, ec.institution) " +
			"FROM ExternalClinicalHistory ec " +
			"JOIN Person pe ON ec.patientDocumentNumber = pe.identificationNumber " +
			"JOIN Patient pa ON pa.personId = pe.id " +
			"WHERE pa.id = :patientId " +
			"AND ec.patientDocumentType = pe.identificationTypeId " +
			"AND ec.patientGender= pe.genderId " +
			"ORDER BY ec.consultationDate DESC")
	List<ExternalClinicalHistoryBo> getAllExternalClinicalHistory(@Param("patientId") Integer patientId);

}
