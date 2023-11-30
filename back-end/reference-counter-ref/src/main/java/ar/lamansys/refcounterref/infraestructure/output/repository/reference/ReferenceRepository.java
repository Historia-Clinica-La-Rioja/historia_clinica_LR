package ar.lamansys.refcounterref.infraestructure.output.repository.reference;

import java.util.List;
import java.util.Optional;

import ar.lamansys.refcounterref.domain.reference.ReferenceDataBo;
import ar.lamansys.refcounterref.domain.reference.ReferencePhoneBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceRequestBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ReferenceRepository extends JpaRepository<Reference, Integer> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceDataBo(r.id, oc.creationable.createdOn, " +
            "rn.description, cl.id, cl.description, cs.id, cs.name, i.id, i.name, i2.id, i2.name, cs2.id, cs2.name, "+
			"hp.personId, r.priority, r.serviceRequestId) " +
            "FROM Reference r " +
            "JOIN OutpatientConsultation oc ON (r.encounterId = oc.id) " +
			"JOIN ClinicalSpecialty cs ON (oc.clinicalSpecialtyId = cs.id) " +
			"JOIN Institution i ON (oc.institutionId = i.id) " +
			"LEFT JOIN Institution i2 ON (r.destinationInstitutionId = i2.id) " +
			"LEFT JOIN ClinicalSpecialty cs2 ON (r.clinicalSpecialtyId = cs2.id) " +
            "LEFT JOIN CareLine cl ON (cl.id = r.careLineId) " +
			"LEFT JOIN CareLineRole clr ON (clr.careLineId = cl.id) " +
            "LEFT JOIN ReferenceNote rn ON (rn.id = r.referenceNoteId) " +
            "JOIN HealthcareProfessional hp ON (hp.id = oc.doctorId) " +
            "WHERE oc.patientId = :patientId " +
            "AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
            "AND r.clinicalSpecialtyId IN (:clinicalSpecialtyIds) " +
            "AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId) " +
			"AND r.serviceRequestId is NULL " +
			"AND (cl.id IS NULL OR cl.classified IS FALSE OR (clr.roleId IN :loggedUserRoleIds AND cl.classified IS TRUE AND clr.deleteable.deleted IS FALSE))")
    List<ReferenceDataBo> getReferencesFromOutpatientConsultation(@Param("patientId") Integer patientId,
																  @Param("clinicalSpecialtyIds") List<Integer> clinicalSpecialtyIds,
																  @Param("loggedUserRoleIds") List<Short> loggedUserRoleIds);

    @Transactional(readOnly = true)
    @Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceDataBo(r.id, oc.creationable.createdOn, " +
			"rn.description, cl.id, cl.description, cs.id, cs.name, i.id, i.name, i2.id, i2.name, cs2.id, cs2.name, "+
			"hp.personId , r.priority, r.serviceRequestId) " +
            "FROM Reference r " +
            "JOIN OdontologyConsultation oc ON (r.encounterId = oc.id) " +
			"JOIN ClinicalSpecialty cs ON (oc.clinicalSpecialtyId = cs.id) " +
			"JOIN Institution i ON (oc.institutionId = i.id) " +
			"LEFT JOIN Institution i2 ON (r.destinationInstitutionId = i2.id) " +
			"LEFT JOIN ClinicalSpecialty cs2 ON (r.clinicalSpecialtyId = cs2.id) " +
            "LEFT JOIN CareLine cl ON (cl.id = r.careLineId) " +
			"LEFT JOIN CareLineRole clr ON (clr.careLineId = cl.id) " +
            "LEFT JOIN ReferenceNote rn ON (rn.id = r.referenceNoteId) " +
            "JOIN HealthcareProfessional hp ON (hp.id = oc.doctorId) " +
            "WHERE oc.patientId = :patientId " +
			"AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
            "AND r.clinicalSpecialtyId IN (:clinicalSpecialtyIds) " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId) " +
			"AND r.serviceRequestId IS NULL " +
			"AND (cl.id IS NULL OR cl.classified IS FALSE OR (clr.roleId IN :loggedUserRoleIds AND cl.classified IS TRUE AND clr.deleteable.deleted IS FALSE))")
    List<ReferenceDataBo> getReferencesFromOdontologyConsultation(@Param("patientId") Integer patientId,
																  @Param("clinicalSpecialtyIds") List<Integer> clinicalSpecialtyIds,
																  @Param("loggedUserRoleIds") List<Short> loggedUserRoleIds);

	@Transactional(readOnly = true)
	@Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.name, " +
			"oc.startDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId) " +
			"FROM Reference r " +
			"JOIN OutpatientConsultation oc ON r.encounterId = oc.id " +
			"JOIN Institution i ON oc.institutionId = i.id " +
			"JOIN HealthcareProfessional hp ON hp.id = oc.doctorId " +
			"JOIN Person p ON p.id = hp.personId " +
			"JOIN PersonExtended pe ON p.id = pe.id " +
			"WHERE oc.patientId = :patientId " +
			"AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
			"AND r.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"AND r.careLineId = :careLineId " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId) " +
			"AND r.serviceRequestId IS NULL ")
	List<ReferenceSummaryBo> getReferencesSummaryFromOutpatientConsultationByClinicalSpecialtyId(@Param("patientId") Integer patientId,
																								 @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId,
																								 @Param("careLineId") Integer careLineId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.name, " +
			"oc.performedDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId) " +
			"FROM Reference r " +
			"JOIN OdontologyConsultation oc ON r.encounterId = oc.id " +
			"JOIN Institution i ON oc.institutionId = i.id " +
			"JOIN HealthcareProfessional hp ON hp.id = oc.doctorId " +
			"JOIN Person p ON p.id = hp.personId " +
			"JOIN PersonExtended pe ON p.id = pe.id " +
			"WHERE oc.patientId = :patientId " +
			"AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
			"AND r.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"AND r.careLineId = :careLineId " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId) " +
			"AND r.serviceRequestId IS NULL")
	List<ReferenceSummaryBo> getReferencesSummaryFromOdontologyConsultationByClinicalSpecialtyId(@Param("patientId") Integer patientId,
																								 @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId,
																								 @Param("careLineId") Integer careLineId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.name, " +
			"oc.startDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId) " +
			"FROM Reference r " +
			"JOIN OutpatientConsultation oc ON r.encounterId = oc.id " +
			"JOIN Institution i ON oc.institutionId = i.id " +
			"JOIN HealthcareProfessional hp ON hp.id = oc.doctorId " +
			"JOIN Person p ON p.id = hp.personId " +
			"JOIN PersonExtended pe ON p.id = pe.id " +
			"JOIN ServiceRequest sr ON r.serviceRequestId = sr.id " +
			"JOIN Document d ON sr.id = d.sourceId " +
			"JOIN DocumentDiagnosticReport ddr ON d.id = ddr.pk.documentId " +
			"JOIN DiagnosticReport dr ON ddr.pk.diagnosticReportId = dr.id " +
			"WHERE oc.patientId = :patientId " +
			"AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
			"AND r.careLineId = :careLineId " +
			"AND dr.snomedId = :practiceId " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId) " +
			"AND (d.deleteable.deleted = false OR d.deleteable.deleted is null)" +
			"AND (dr.deleteable.deleted = false OR dr.deleteable.deleted is null) " +
			"AND (sr.deleteable.deleted = false OR sr.deleteable.deleted is null)")
	List<ReferenceSummaryBo> getReferencesSummaryFromOutpatientConsultationByPracticeId(@Param("patientId") Integer patientId,
																						@Param("practiceId") Integer practiceId,
																						@Param("careLineId") Integer careLineId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.name, " +
			"oc.performedDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId) " +
			"FROM Reference r " +
			"JOIN OdontologyConsultation oc ON r.encounterId = oc.id " +
			"JOIN Institution i ON oc.institutionId = i.id " +
			"JOIN HealthcareProfessional hp ON hp.id = oc.doctorId " +
			"JOIN Person p ON p.id = hp.personId " +
			"JOIN PersonExtended pe ON p.id = pe.id " +
			"JOIN ServiceRequest sr ON r.serviceRequestId = sr.id " +
			"JOIN Document d ON sr.id = d.sourceId " +
			"JOIN DocumentDiagnosticReport ddr ON d.id = ddr.pk.documentId " +
			"JOIN DiagnosticReport dr ON ddr.pk.diagnosticReportId = dr.id " +
			"WHERE oc.patientId = :patientId " +
			"AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
			"AND r.careLineId = :careLineId " +
			"AND dr.snomedId = :practiceId " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId) " +
			"AND (d.deleteable.deleted = false OR d.deleteable.deleted is null)" +
			"AND (dr.deleteable.deleted = false OR dr.deleteable.deleted is null) " +
			"AND (sr.deleteable.deleted = false OR sr.deleteable.deleted is null)")
	List<ReferenceSummaryBo> getReferencesSummaryFromOdontologyConsultationByPracticeId(@Param("patientId") Integer patientId,
																						@Param("practiceId") Integer practiceId,
																						@Param("careLineId") Integer careLineId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.name, " +
			"oc.startDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId) " +
			"FROM Reference r " +
			"JOIN OutpatientConsultation oc ON r.encounterId = oc.id " +
			"JOIN Institution i ON oc.institutionId = i.id " +
			"JOIN HealthcareProfessional hp ON hp.id = oc.doctorId " +
			"JOIN Person p ON p.id = hp.personId " +
			"JOIN PersonExtended pe ON p.id = pe.id " +
			"JOIN ServiceRequest sr ON r.serviceRequestId = sr.id " +
			"JOIN Document d ON sr.id = d.sourceId " +
			"JOIN DocumentDiagnosticReport ddr ON d.id = ddr.pk.documentId " +
			"JOIN DiagnosticReport dr ON ddr.pk.diagnosticReportId = dr.id " +
			"WHERE oc.patientId = :patientId " +
			"AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
			"AND r.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"AND r.careLineId = :careLineId " +
			"AND dr.snomedId = :practiceId " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId)" +
			"AND (d.deleteable.deleted = false OR d.deleteable.deleted is null)" +
			"AND (dr.deleteable.deleted = false OR dr.deleteable.deleted is null) " +
			"AND (sr.deleteable.deleted = false OR sr.deleteable.deleted is null)")
	List<ReferenceSummaryBo> getReferencesSummaryFromOutpatientConsultationByClinicalSpecialtyIdAndPracticeId(@Param("patientId") Integer patientId,
																											  @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId,
																											  @Param("careLineId") Integer careLineId,
																											  @Param("practiceId") Integer practiceId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.name, " +
			"oc.performedDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId) " +
			"FROM Reference r " +
			"JOIN OdontologyConsultation oc ON r.encounterId = oc.id " +
			"JOIN Institution i ON oc.institutionId = i.id " +
			"JOIN HealthcareProfessional hp ON hp.id = oc.doctorId " +
			"JOIN Person p ON p.id = hp.personId " +
			"JOIN PersonExtended pe ON p.id = pe.id " +
			"JOIN ServiceRequest sr ON r.serviceRequestId = sr.id " +
			"JOIN Document d ON sr.id = d.sourceId " +
			"JOIN DocumentDiagnosticReport ddr ON d.id = ddr.pk.documentId " +
			"JOIN DiagnosticReport dr ON ddr.pk.diagnosticReportId = dr.id " +
			"WHERE oc.patientId = :patientId " +
			"AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
			"AND r.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"AND r.careLineId = :careLineId " +
			"AND dr.snomedId = :practiceId " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId) " +
			"AND (d.deleteable.deleted = false OR d.deleteable.deleted is null)" +
			"AND (dr.deleteable.deleted = false OR dr.deleteable.deleted is null) " +
			"AND (sr.deleteable.deleted = false OR sr.deleteable.deleted is null)")
	List<ReferenceSummaryBo> getReferencesSummaryFromOdontologyConsultationByClinicalSpecialtyIdAndPracticeId(@Param("patientId") Integer patientId,
																								 			  @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId,
																											  @Param("careLineId") Integer careLineId,
																								 			  @Param("practiceId") Integer practiceId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT r.sourceTypeId FROM Reference r WHERE r.id = :referenceId")
	Integer getReferenceEncounterTypeId(@Param("referenceId") Integer referenceId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT DISTINCT new ar.lamansys.refcounterref.domain.reference.ReferenceDataBo(r.id, oc.patientId, oc.creationable.createdOn, " +
			"rn.description, cl.id, cl.description, cs.id, cs.name, i.id, i.name, i2.id, i2.name, cs2.id, cs2.name, "+
			"hp.personId, r.priority, cr.closureTypeId, r.phonePrefix, r.phoneNumber, r.serviceRequestId) " +
			"FROM Reference r " +
			"JOIN OutpatientConsultation oc ON (r.encounterId = oc.id) " +
			"JOIN ClinicalSpecialty cs ON (oc.clinicalSpecialtyId = cs.id) " +
			"JOIN Institution i ON (oc.institutionId = i.id) " +
			"LEFT JOIN Institution i2 ON (r.destinationInstitutionId = i2.id) " +
			"LEFT JOIN ClinicalSpecialty cs2 ON (r.clinicalSpecialtyId = cs2.id) " +
			"LEFT JOIN CareLine cl ON (cl.id = r.careLineId) " +
			"LEFT JOIN ReferenceNote rn ON (rn.id = r.referenceNoteId) " +
			"JOIN HealthcareProfessional hp ON (hp.id = oc.doctorId) " +
			"LEFT JOIN CounterReference cr ON (r.id = cr.referenceId) " +
			"WHERE r.id = :referenceId ")
	Optional<ReferenceDataBo> getReferenceDataFromOutpatientConsultation(@Param("referenceId") Integer referenceId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT DISTINCT new ar.lamansys.refcounterref.domain.reference.ReferenceDataBo(r.id, oc.patientId, oc.creationable.createdOn, " +
			"rn.description, cl.id, cl.description, cs.id, cs.name, i.id, i.name, i2.id, i2.name, cs2.id, cs2.name, "+
			"hp.personId, r.priority, cr.closureTypeId, r.phonePrefix, r.phoneNumber, r.serviceRequestId) " +
			"FROM Reference r " +
			"JOIN OdontologyConsultation oc ON (r.encounterId = oc.id) " +
			"JOIN ClinicalSpecialty cs ON (oc.clinicalSpecialtyId = cs.id) " +
			"JOIN Institution i ON (oc.institutionId = i.id) " +
			"LEFT JOIN Institution i2 ON (r.destinationInstitutionId = i2.id) " +
			"LEFT JOIN ClinicalSpecialty cs2 ON (r.clinicalSpecialtyId = cs2.id) " +
			"LEFT JOIN CareLine cl ON (cl.id = r.careLineId) " +
			"LEFT JOIN ReferenceNote rn ON (rn.id = r.referenceNoteId) " +
			"JOIN HealthcareProfessional hp ON (hp.id = oc.doctorId) " +
			"LEFT JOIN CounterReference cr ON (r.id = cr.referenceId) " +
			"WHERE r.id = :referenceId ")
	Optional<ReferenceDataBo> getReferenceDataFromOdontologyConsultation(@Param("referenceId") Integer referenceId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE Reference r "
			+ "SET r.deleteable.deleted = true "
			+ ", r.deleteable.deletedOn = CURRENT_TIMESTAMP "
			+ ", r.deleteable.deletedBy = ?#{ principal.userId } "
			+ "WHERE r.id = :id" )
	void deleteById(@Param("id") @NonNull Integer id);
	
	@Transactional(readOnly = true)
	@Query("SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceRequestBo(r.id, cs.id, cs.name, cl.id, cl.description, r.priority, rn.description, cr.closureTypeId, cr.creationable.createdOn, cr.doctorId) " +
			"FROM Reference r " +
			"LEFT JOIN ClinicalSpecialty cs ON (r.clinicalSpecialtyId = cs.id) " +
			"LEFT JOIN CareLine cl ON (r.careLineId = cl.id) " +
			"LEFT JOIN ReferenceNote rn ON (r.referenceNoteId = rn.id) " +
			"LEFT JOIN CounterReference cr ON (cr.referenceId = r.id) " +
			"WHERE r.serviceRequestId = :serviceRequestId")
	Optional<ReferenceRequestBo> getReferenceByServiceRequestId(@Param("serviceRequestId") Integer serviceRequestId);

	@Transactional(readOnly = true)
	@Query(" SELECT NEW ar.lamansys.refcounterref.domain.reference.ReferencePhoneBo(r.phoneNumber, r.phonePrefix) " +
			"FROM Reference r " +
			"WHERE r.id = :referenceId")
	ReferencePhoneBo getReferencePhoneData(@Param("referenceId") Integer referenceId);

}