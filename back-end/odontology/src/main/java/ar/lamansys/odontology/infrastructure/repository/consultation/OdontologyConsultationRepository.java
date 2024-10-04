package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface OdontologyConsultationRepository extends SGXAuditableEntityJPARepository<OdontologyConsultation, Integer> {

    @Query(" SELECT CASE WHEN COUNT(oc) > 0 THEN true ELSE false END " +
           " FROM OdontologyConsultation oc " +
           " WHERE patientId = :patientId ")
    boolean patientHasPreviousConsultations(@Param("patientId") Integer patientId);

	@Transactional(readOnly = true)
	@Query("SELECT oc.id " +
			"FROM OdontologyConsultation oc " +
			"WHERE oc.patientId IN :patientsIds")
	List<Integer> getOdontologyConsultationIdFromPatients(@Param("patientsIds")List<Integer> patientsIds);

	@Transactional(readOnly = true)
	@Query("SELECT oc " +
			"FROM OdontologyConsultation oc " +
			"WHERE oc.patientId = :patientId " +
			"ORDER BY oc.updateable.updatedOn ASC")
	List<OdontologyConsultation> getLastOdontologyConsultationFromPatient(@Param("patientId") Integer patientId);

	@Transactional(readOnly = true)
	@Query("SELECT oc.patientMedicalCoverageId " +
			"FROM OdontologyConsultation oc " +
			"WHERE oc.id = :id")
	Optional<Integer> getPatientMedicalCoverageId(@Param("id") Integer id);

	@Transactional(readOnly = true)
	@Query("SELECT d.id " +
			"FROM DocumentHealthCondition dhc " +
			"JOIN Document d ON dhc.pk.documentId = d.id " +
			"JOIN OdontologyConsultation oc ON d.sourceId = oc.id AND oc.patientId = d.patientId " +
			"WHERE dhc.pk.healthConditionId = :healthConditionId " +
			"AND d.typeId = '" + DocumentType.ODONTOLOGY + "'")
	Optional<Long> getOdontologyDocumentId(@Param("healthConditionId") Integer healthConditionId);
}
