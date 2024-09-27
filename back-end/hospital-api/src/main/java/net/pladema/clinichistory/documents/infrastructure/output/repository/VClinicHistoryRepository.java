package net.pladema.clinichistory.documents.infrastructure.output.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import net.pladema.clinichistory.documents.domain.CHDocumentSummaryBo;
import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.VClinicHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VClinicHistoryRepository extends JpaRepository<VClinicHistory, Long> {

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.clinichistory.documents.domain.CHDocumentSummaryBo(d.id, d.patientId, ie.creationable.createdOn, COALESCE(pd.administrativeDischargeDate, pd.medicalDischargeDate), p.id, i.name, d.typeId, d.sourceTypeId, d.sourceTypeId)" +
			" FROM InternmentEpisode ie" +
			" LEFT JOIN PatientDischarge pd ON pd.internmentEpisodeId = ie.id" +
			" JOIN Document d ON d.sourceId = ie.id" +
			" JOIN Institution i ON d.institutionId = i.id" +
			" JOIN UserPerson up ON d.creationable.createdBy = up.pk.userId" +
			" JOIN Person p ON up.pk.personId = p.id" +
			" WHERE d.patientId = :patientId" +
			" AND d.sourceTypeId = '"+ SourceType.HOSPITALIZATION +"'" +
			" AND d.creationable.createdOn >= :startDate" +
			" AND COALESCE(pd.administrativeDischargeDate, pd.medicalDischargeDate) <= :endDate")
	List<CHDocumentSummaryBo> getInternmentPatientClinicHistory(@Param("patientId") Integer patientId,
													  			@Param("startDate") LocalDateTime startDate,
													  			@Param("endDate") LocalDateTime endDate);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.clinichistory.documents.domain.CHDocumentSummaryBo(d.id, ece.patientId, d.creationable.createdOn, COALESCE(ecd.administrativeDischargeOn, ecd.medicalDischargeOn), p.id, i.name, d.typeId, d.sourceTypeId, d.sourceTypeId)" +
			" FROM EmergencyCareEpisode ece" +
			" LEFT JOIN EmergencyCareDischarge ecd ON ece.id = ecd.emergencyCareEpisodeId" +
			" JOIN Document d ON d.sourceId = ece.id" +
			" JOIN Institution i ON ece.institutionId = i.id" +
			" JOIN UserPerson up ON d.creationable.createdBy = up.pk.userId" +
			" JOIN Person p ON up.pk.personId = p.id" +
			" WHERE d.patientId = :patientId" +
			" AND d.sourceTypeId = '"+ SourceType.EMERGENCY_CARE +"'" +
			" AND d.typeId = '"+ DocumentType.EMERGENCY_CARE_EVOLUTION_NOTE +"'" +
			" AND ece.creationable.createdOn >= :startDate" +
			" AND COALESCE(ecd.administrativeDischargeOn, ecd.medicalDischargeOn) <= :endDate")
	List<CHDocumentSummaryBo> getEmergencyCarePatientClinicHistory(@Param("patientId") Integer patientId,
																@Param("startDate") LocalDateTime startDate,
																@Param("endDate") LocalDateTime endDate);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.clinichistory.documents.domain.CHDocumentSummaryBo(d.id, oc.patientId, oc.creationable.createdOn, oc.creationable.createdOn, p.id, i.name, d.typeId, d.sourceTypeId, d.sourceTypeId)" +
			" FROM OutpatientConsultation oc" +
			" JOIN Document d ON d.id = oc.documentId" +
			" JOIN Institution i ON oc.institutionId = i.id " +
			" JOIN UserPerson up ON d.creationable.createdBy = up.pk.userId" +
			" JOIN Person p ON up.pk.personId = p.id" +
			" WHERE d.patientId = :patientId" +
			" AND oc.creationable.createdOn >= :startDate")
	List<CHDocumentSummaryBo> getOutpatientConsultationPatientClinicHistory(@Param("patientId") Integer patientId,
																   @Param("startDate") LocalDateTime startDate);

	@Transactional(readOnly = true)
	@Query(value = "SELECT d.id as documentId, nc.patient_id, nc.created_on as startDate, nc.created_on as endDate, p.id as personId, i.name, d.type_id, d.source_type_id as sourceTypeId, d.source_type_id as dSourceTypeId" +
			" FROM nursing_consultation nc" +
			" JOIN document d ON d.source_id = nc.id" +
			" JOIN institution i ON nc.institution_id = i.id " +
			" JOIN user_person up ON d.created_by = up.user_id" +
			" JOIN person p ON up.person_id = p.id" +
			" WHERE d.patient_id = :patientId" +
			" AND nc.created_on >= :startDate", nativeQuery = true)
	List<Object[]> getNursingOutpatientConsultationPatientClinicHistory(@Param("patientId") Integer patientId,
																@Param("startDate") LocalDateTime startDate);

	@Transactional(readOnly = true)
	@Query(value = "SELECT d.id as documentId, oc.patient_id, oc.created_on as startDate, oc.created_on as endDate, p.id as personId, i.name, d.type_id, d.source_type_id as sourceTypeId, d.source_type_id as dSourceTypeId" +
			" FROM odontology_consultation oc" +
			" JOIN document d ON d.source_id = oc.id" +
			" JOIN institution i ON oc.institution_id = i.id" +
			" JOIN user_person up ON d.created_by = up.user_id" +
			" JOIN person p ON up.person_id = p.id" +
			" WHERE oc.patient_id = :patientId" +
			" AND d.source_type_id = '"+ SourceType.ODONTOLOGY +"'" +
			" AND d.type_id = '"+ DocumentType.ODONTOLOGY +"'" +
			" AND oc.created_on >= :startDate", nativeQuery = true)
	List<Object[]> getOdontologyPatientClinicHistory(@Param("patientId") Integer patientId,
																@Param("startDate") LocalDateTime startDate);

	@Transactional(readOnly = true)
	@Query("SELECT s.pt " +
			"FROM Document d " +
			" JOIN DocumentHealthCondition dhc ON dhc.pk.documentId = d.id" +
			" JOIN HealthCondition hc ON dhc.pk.healthConditionId = hc.id" +
			" JOIN Snomed s ON s.id = hc.snomedId" +
			" WHERE d.id = :documentId")
	List<String> getProblems(@Param("documentId") Long documentId);
}
