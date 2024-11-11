package ar.lamansys.refcounterref.infraestructure.output.repository.reference;

import java.util.List;
import java.util.Optional;

import ar.lamansys.refcounterref.domain.reference.ReferenceDataBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceRequestBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceStudyBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.shared.domain.reference.ReferencePhoneBo;
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
    @Query(value = "SELECT DISTINCT new ar.lamansys.refcounterref.domain.reference.ReferenceDataBo(r.id, oc.creationable.createdOn, " +
            "rn.description, cl.id, cl.description, cs.id, cs.name, i.id, i.name, i2.id, i2.name,"+
			"hp.personId, r.priority, r.serviceRequestId) " +
            "FROM Reference r " +
			"LEFT JOIN ReferenceClinicalSpecialty rcs ON (rcs.pk.referenceId = r.id) " +
            "JOIN OutpatientConsultation oc ON (r.encounterId = oc.id) " +
			"JOIN ClinicalSpecialty cs ON (oc.clinicalSpecialtyId = cs.id) " +
			"JOIN Institution i ON (oc.institutionId = i.id) " +
			"LEFT JOIN Institution i2 ON (r.destinationInstitutionId = i2.id) " +
            "LEFT JOIN CareLine cl ON (cl.id = r.careLineId) " +
			"LEFT JOIN CareLineRole clr ON (clr.careLineId = cl.id) " +
            "LEFT JOIN ReferenceNote rn ON (rn.id = r.referenceNoteId) " +
            "JOIN HealthcareProfessional hp ON (hp.id = oc.doctorId) " +
            "WHERE oc.patientId = :patientId " +
            "AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
            "AND rcs.pk.clinicalSpecialtyId IN (:clinicalSpecialtyIds) " +
            "AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId) " +
			"AND r.serviceRequestId is NULL " +
			"AND (cl.id IS NULL OR cl.classified IS FALSE OR (clr.roleId IN :loggedUserRoleIds AND cl.classified IS TRUE AND clr.deleteable.deleted IS FALSE)) " +
			"AND r.administrativeStateId = :administrativeStateId")
    List<ReferenceDataBo> getReferencesFromOutpatientConsultation(@Param("patientId") Integer patientId,
																  @Param("clinicalSpecialtyIds") List<Integer> clinicalSpecialtyIds,
																  @Param("loggedUserRoleIds") List<Short> loggedUserRoleIds,
																  @Param("administrativeStateId") Short administrativeStateId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT new ar.lamansys.refcounterref.domain.reference.ReferenceDataBo(r.id, oc.creationable.createdOn, " +
			"rn.description, cl.id, cl.description, cs.id, cs.name, i.id, i.name, i2.id, i2.name,"+
			"hp.personId , r.priority, r.serviceRequestId) " +
            "FROM Reference r " +
			"LEFT JOIN ReferenceClinicalSpecialty rcs ON (rcs.pk.referenceId = r.id) " +
            "JOIN OdontologyConsultation oc ON (r.encounterId = oc.id) " +
			"JOIN ClinicalSpecialty cs ON (oc.clinicalSpecialtyId = cs.id) " +
			"JOIN Institution i ON (oc.institutionId = i.id) " +
			"LEFT JOIN Institution i2 ON (r.destinationInstitutionId = i2.id) " +
            "LEFT JOIN CareLine cl ON (cl.id = r.careLineId) " +
			"LEFT JOIN CareLineRole clr ON (clr.careLineId = cl.id) " +
            "LEFT JOIN ReferenceNote rn ON (rn.id = r.referenceNoteId) " +
            "JOIN HealthcareProfessional hp ON (hp.id = oc.doctorId) " +
            "WHERE oc.patientId = :patientId " +
			"AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
            "AND rcs.pk.clinicalSpecialtyId IN (:clinicalSpecialtyIds) " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId) " +
			"AND r.serviceRequestId IS NULL " +
			"AND (cl.id IS NULL OR cl.classified IS FALSE OR (clr.roleId IN :loggedUserRoleIds AND cl.classified IS TRUE AND clr.deleteable.deleted IS FALSE)) " +
			"AND r.administrativeStateId = :administrativeStateId")
    List<ReferenceDataBo> getReferencesFromOdontologyConsultation(@Param("patientId") Integer patientId,
																  @Param("clinicalSpecialtyIds") List<Integer> clinicalSpecialtyIds,
																  @Param("loggedUserRoleIds") List<Short> loggedUserRoleIds,
																  @Param("administrativeStateId") Short administrativeStateId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT DISTINCT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.name, " +
			"oc.startDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId, r.destinationInstitutionId) " +
			"FROM Reference r " +
			"LEFT JOIN ReferenceClinicalSpecialty rcs ON (rcs.pk.referenceId = r.id) " +
			"JOIN OutpatientConsultation oc ON (r.encounterId = oc.id) " +
			"JOIN Institution i ON (oc.institutionId = i.id) " +
			"JOIN HealthcareProfessional hp ON (hp.id = oc.doctorId) " +
			"JOIN Person p ON (p.id = hp.personId) " +
			"JOIN PersonExtended pe ON (p.id = pe.id) " +
			"LEFT JOIN CareLine cl ON (cl.id = r.careLineId) " +
			"LEFT JOIN CareLineRole clr ON (clr.careLineId = cl.id) " +
			"WHERE oc.patientId = :patientId " +
			"AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
			"AND rcs.pk.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"AND r.careLineId = :careLineId " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId) " +
			"AND r.serviceRequestId IS NULL " +
			"AND ((clr.roleId IN (:userRoleIds) AND cl.classified IS TRUE AND clr.deleteable.deleted IS FALSE) OR cl.classified IS FALSE OR cl.classified IS NULL)")
	List<ReferenceSummaryBo> getReferencesSummaryFromOutpatientConsultationByClinicalSpecialtyIdAndCareLineId(@Param("patientId") Integer patientId,
																											  @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId,
																											  @Param("careLineId") Integer careLineId,
																											  @Param("userRoleIds") List<Short> userRoleIds);

	@Transactional(readOnly = true)
	@Query(value = "SELECT DISTINCT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.name, " +
			"oc.startDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId, r.destinationInstitutionId) " +
			"FROM Reference r " +
			"LEFT JOIN ReferenceClinicalSpecialty rcs ON (rcs.pk.referenceId = r.id) " +
			"JOIN OutpatientConsultation oc ON r.encounterId = oc.id " +
			"JOIN Institution i ON oc.institutionId = i.id " +
			"JOIN HealthcareProfessional hp ON hp.id = oc.doctorId " +
			"JOIN Person p ON p.id = hp.personId " +
			"JOIN PersonExtended pe ON p.id = pe.id " +
			"LEFT JOIN CareLine cl ON (cl.id = r.careLineId) " +
			"LEFT JOIN CareLineRole clr ON (clr.careLineId = cl.id) " +
			"WHERE oc.patientId = :patientId " +
			"AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
			"AND rcs.pk.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId) " +
			"AND r.serviceRequestId IS NULL " +
			"AND ((clr.roleId IN (:userRoleIds) AND cl.classified IS TRUE AND clr.deleteable.deleted IS FALSE) OR cl.classified IS FALSE OR cl.classified IS NULL)")
	List<ReferenceSummaryBo> getReferencesSummaryFromOutpatientConsultationByClinicalSpecialtyId(@Param("patientId") Integer patientId,
																								 @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId,
																								 @Param("userRoleIds") List<Short> userRoleIds);
	
	@Transactional(readOnly = true)
	@Query(value = "SELECT DISTINCT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.name, " +
			"oc.performedDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId, r.destinationInstitutionId) " +
			"FROM Reference r " +
			"LEFT JOIN ReferenceClinicalSpecialty rcs ON (rcs.pk.referenceId = r.id) " +
			"JOIN OdontologyConsultation oc ON r.encounterId = oc.id " +
			"JOIN Institution i ON oc.institutionId = i.id " +
			"JOIN HealthcareProfessional hp ON hp.id = oc.doctorId " +
			"JOIN Person p ON p.id = hp.personId " +
			"JOIN PersonExtended pe ON p.id = pe.id " +
			"LEFT JOIN CareLine cl ON (cl.id = r.careLineId) " +
			"LEFT JOIN CareLineRole clr ON (clr.careLineId = cl.id) " +
			"WHERE oc.patientId = :patientId " +
			"AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
			"AND rcs.pk.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"AND r.careLineId = :careLineId " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId) " +
			"AND r.serviceRequestId IS NULL " +
			"AND ((clr.roleId IN (:userRoleIds) AND cl.classified IS TRUE AND clr.deleteable.deleted IS FALSE) OR cl.classified IS FALSE OR cl.classified IS NULL)")
	List<ReferenceSummaryBo> getReferencesSummaryFromOdontologyConsultationByClinicalSpecialtyIdAndCareLineId(@Param("patientId") Integer patientId,
																											  @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId,
																											  @Param("careLineId") Integer careLineId,
																											  @Param("userRoleIds") List<Short> userRoles);

	@Transactional(readOnly = true)
	@Query(value = "SELECT DISTINCT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.name, " +
			"oc.performedDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId, r.destinationInstitutionId) " +
			"FROM Reference r " +
			"LEFT JOIN ReferenceClinicalSpecialty rcs ON (rcs.pk.referenceId = r.id) " +
			"JOIN OdontologyConsultation oc ON r.encounterId = oc.id " +
			"JOIN Institution i ON oc.institutionId = i.id " +
			"JOIN HealthcareProfessional hp ON hp.id = oc.doctorId " +
			"JOIN Person p ON p.id = hp.personId " +
			"JOIN PersonExtended pe ON p.id = pe.id " +
			"LEFT JOIN CareLine cl ON (cl.id = r.careLineId) " +
			"LEFT JOIN CareLineRole clr ON (clr.careLineId = cl.id) " +
			"WHERE oc.patientId = :patientId " +
			"AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
			"AND rcs.pk.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId) " +
			"AND r.serviceRequestId IS NULL " +
			"AND ((clr.roleId IN (:userRoleIds) AND cl.classified IS TRUE AND clr.deleteable.deleted IS FALSE) OR cl.classified IS FALSE OR cl.classified IS NULL)")
	List<ReferenceSummaryBo> getReferencesSummaryFromOdontologyConsultationByClinicalSpecialtyId(@Param("patientId") Integer patientId,
																								 @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId,
																								 @Param("userRoleIds") List<Short> userRoleIds);

	@Transactional(readOnly = true)
	@Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.name, " +
			"oc.startDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId, r.destinationInstitutionId) " +
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
			"LEFT JOIN CareLine cl ON (cl.id = r.careLineId) " +
			"LEFT JOIN CareLineRole clr ON (clr.careLineId = cl.id) " +
			"WHERE oc.patientId = :patientId " +
			"AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
			"AND r.careLineId = :careLineId " +
			"AND dr.snomedId = :practiceId " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId) " +
			"AND (d.deleteable.deleted = false OR d.deleteable.deleted is null)" +
			"AND (dr.deleteable.deleted = false OR dr.deleteable.deleted is null) " +
			"AND (sr.deleteable.deleted = false OR sr.deleteable.deleted is null) " +
			"AND ((clr.roleId IN (:userRoleIds) AND cl.classified IS TRUE AND clr.deleteable.deleted IS FALSE) OR cl.classified IS FALSE OR cl.classified IS NULL)")
	List<ReferenceSummaryBo> getReferencesSummaryFromOutpatientConsultationByPracticeIdAndCareLineId(@Param("patientId") Integer patientId,
																									 @Param("practiceId") Integer practiceId,
																									 @Param("careLineId") Integer careLineId,
																									 @Param("userRoleIds") List<Short> userRoleIds);
	@Transactional(readOnly = true)
	@Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.name, " +
			"oc.startDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId, r.destinationInstitutionId) " +
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
			"LEFT JOIN CareLine cl ON (cl.id = r.careLineId) " +
			"LEFT JOIN CareLineRole clr ON (clr.careLineId = cl.id) " +
			"WHERE oc.patientId = :patientId " +
			"AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
			"AND dr.snomedId = :practiceId " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId) " +
			"AND (d.deleteable.deleted = false OR d.deleteable.deleted is null)" +
			"AND (dr.deleteable.deleted = false OR dr.deleteable.deleted is null) " +
			"AND (sr.deleteable.deleted = false OR sr.deleteable.deleted is null) " +
			"AND ((clr.roleId IN (:userRoleIds) AND cl.classified IS TRUE AND clr.deleteable.deleted IS FALSE) OR cl.classified IS FALSE OR cl.classified IS NULL)")
	List<ReferenceSummaryBo> getReferencesSummaryFromOutpatientConsultationByPracticeId(@Param("patientId") Integer patientId,
																						@Param("practiceId") Integer practiceId,
																						@Param("userRoleIds") List<Short> userRoleIds);
																						
	@Transactional(readOnly = true)
	@Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.name, " +
			"oc.performedDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId, r.destinationInstitutionId) " +
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
			"LEFT JOIN CareLine cl ON (cl.id = r.careLineId) " +
			"LEFT JOIN CareLineRole clr ON (clr.careLineId = cl.id) " +
			"WHERE oc.patientId = :patientId " +
			"AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
			"AND r.careLineId = :careLineId " +
			"AND dr.snomedId = :practiceId " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId) " +
			"AND (d.deleteable.deleted = false OR d.deleteable.deleted is null)" +
			"AND (dr.deleteable.deleted = false OR dr.deleteable.deleted is null) " +
			"AND (sr.deleteable.deleted = false OR sr.deleteable.deleted is null) " +
			"AND ((clr.roleId IN (:userRoleIds) AND cl.classified IS TRUE AND clr.deleteable.deleted IS FALSE) OR cl.classified IS FALSE OR cl.classified IS NULL)")
	List<ReferenceSummaryBo> getReferencesSummaryFromOdontologyConsultationByPracticeIdAndCareLineId(@Param("patientId") Integer patientId,
																									 @Param("practiceId") Integer practiceId,
																									 @Param("careLineId") Integer careLineId,
																									 @Param("userRoleIds") List<Short> userRoleIds);

	@Transactional(readOnly = true)
	@Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.name, " +
			"oc.performedDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId, r.destinationInstitutionId) " +
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
			"LEFT JOIN CareLine cl ON (cl.id = r.careLineId) " +
			"LEFT JOIN CareLineRole clr ON (clr.careLineId = cl.id) " +
			"WHERE oc.patientId = :patientId " +
			"AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
			"AND dr.snomedId = :practiceId " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId) " +
			"AND (d.deleteable.deleted = false OR d.deleteable.deleted is null)" +
			"AND (dr.deleteable.deleted = false OR dr.deleteable.deleted is null) " +
			"AND (sr.deleteable.deleted = false OR sr.deleteable.deleted is null) " +
			"AND ((clr.roleId IN (:userRoleIds) AND cl.classified IS TRUE AND clr.deleteable.deleted IS FALSE) OR cl.classified IS FALSE OR cl.classified IS NULL)")
	List<ReferenceSummaryBo> getReferencesSummaryFromOdontologyConsultationByPracticeId(@Param("patientId") Integer patientId,
																						@Param("practiceId") Integer practiceId,
																						@Param("userRoleIds") List<Short> userRoleIds);

	@Transactional(readOnly = true)
	@Query(value = "SELECT DISTINCT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.name, " +
			"oc.startDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId, r.destinationInstitutionId) " +
			"FROM Reference r " +
			"LEFT JOIN ReferenceClinicalSpecialty rcs ON (rcs.pk.referenceId = r.id) " +
			"JOIN OutpatientConsultation oc ON r.encounterId = oc.id " +
			"JOIN Institution i ON oc.institutionId = i.id " +
			"JOIN HealthcareProfessional hp ON hp.id = oc.doctorId " +
			"JOIN Person p ON p.id = hp.personId " +
			"JOIN PersonExtended pe ON p.id = pe.id " +
			"JOIN ServiceRequest sr ON r.serviceRequestId = sr.id " +
			"JOIN Document d ON sr.id = d.sourceId " +
			"JOIN DocumentDiagnosticReport ddr ON d.id = ddr.pk.documentId " +
			"JOIN DiagnosticReport dr ON ddr.pk.diagnosticReportId = dr.id " +
			"LEFT JOIN CareLine cl ON (cl.id = r.careLineId) " +
			"LEFT JOIN CareLineRole clr ON (clr.careLineId = cl.id) " +
			"WHERE oc.patientId = :patientId " +
			"AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
			"AND (rcs.pk.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"	OR NOT EXISTS (SELECT 1 from ReferenceClinicalSpecialty rcs2 WHERE rcs2.pk.referenceId = r.id)) " +
			"AND r.careLineId = :careLineId " +
			"AND dr.snomedId = :practiceId " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId)" +
			"AND (d.deleteable.deleted = false OR d.deleteable.deleted is null)" +
			"AND (dr.deleteable.deleted = false OR dr.deleteable.deleted is null) " +
			"AND (sr.deleteable.deleted = false OR sr.deleteable.deleted is null) " +
			"AND ((clr.roleId IN (:userRoleIds) AND cl.classified IS TRUE AND clr.deleteable.deleted IS FALSE) OR cl.classified IS FALSE OR cl.classified IS NULL)")
	List<ReferenceSummaryBo> getReferencesSummaryFromOutpatientConsultationByClinicalSpecialtyIdAndPracticeIdAndCareLineId(@Param("patientId") Integer patientId,
																														   @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId,
																														   @Param("careLineId") Integer careLineId,
																														   @Param("practiceId") Integer practiceId,
																														   @Param("userRoleIds") List<Short> userRoleIds);
	@Transactional(readOnly = true)
	@Query(value = "SELECT DISTINCT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.name, " +
			"oc.startDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId, r.destinationInstitutionId) " +
			"FROM Reference r " +
			"LEFT JOIN ReferenceClinicalSpecialty rcs ON (rcs.pk.referenceId = r.id) " +
			"JOIN OutpatientConsultation oc ON r.encounterId = oc.id " +
			"JOIN Institution i ON oc.institutionId = i.id " +
			"JOIN HealthcareProfessional hp ON hp.id = oc.doctorId " +
			"JOIN Person p ON p.id = hp.personId " +
			"JOIN PersonExtended pe ON p.id = pe.id " +
			"JOIN ServiceRequest sr ON r.serviceRequestId = sr.id " +
			"JOIN Document d ON sr.id = d.sourceId " +
			"JOIN DocumentDiagnosticReport ddr ON d.id = ddr.pk.documentId " +
			"JOIN DiagnosticReport dr ON ddr.pk.diagnosticReportId = dr.id " +
			"LEFT JOIN CareLine cl ON (cl.id = r.careLineId) " +
			"LEFT JOIN CareLineRole clr ON (clr.careLineId = cl.id) " +
			"WHERE oc.patientId = :patientId " +
			"AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
			"AND (rcs.pk.clinicalSpecialtyId = :clinicalSpecialtyId OR NOT EXISTS (SELECT 1 from ReferenceClinicalSpecialty rcs2 WHERE rcs2.pk.referenceId = r.id)) " +
			"AND dr.snomedId = :practiceId " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId)" +
			"AND (d.deleteable.deleted = false OR d.deleteable.deleted is null)" +
			"AND (dr.deleteable.deleted = false OR dr.deleteable.deleted is null) " +
			"AND (sr.deleteable.deleted = false OR sr.deleteable.deleted is null) " +
			"AND ((clr.roleId IN (:userRoleIds) AND cl.classified IS TRUE AND clr.deleteable.deleted IS FALSE) OR cl.classified IS FALSE OR cl.classified IS NULL)")
	List<ReferenceSummaryBo> getReferencesSummaryFromOutpatientConsultationByClinicalSpecialtyIdAndPracticeId(@Param("patientId") Integer patientId,
																											  @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId,
																											  @Param("practiceId") Integer practiceId,
																											  @Param("userRoleIds") List<Short> userRoleIds);
																											  
	@Transactional(readOnly = true)
	@Query(value = "SELECT DISTINCT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.name, " +
			"oc.performedDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId, r.destinationInstitutionId) " +
			"FROM Reference r " +
			"LEFT JOIN ReferenceClinicalSpecialty rcs ON (rcs.pk.referenceId = r.id) " +
			"JOIN OdontologyConsultation oc ON r.encounterId = oc.id " +
			"JOIN Institution i ON oc.institutionId = i.id " +
			"JOIN HealthcareProfessional hp ON hp.id = oc.doctorId " +
			"JOIN Person p ON p.id = hp.personId " +
			"JOIN PersonExtended pe ON p.id = pe.id " +
			"JOIN ServiceRequest sr ON r.serviceRequestId = sr.id " +
			"JOIN Document d ON sr.id = d.sourceId " +
			"JOIN DocumentDiagnosticReport ddr ON d.id = ddr.pk.documentId " +
			"JOIN DiagnosticReport dr ON ddr.pk.diagnosticReportId = dr.id " +
			"LEFT JOIN CareLine cl ON (cl.id = r.careLineId) " +
			"LEFT JOIN CareLineRole clr ON (clr.careLineId = cl.id) " +
			"WHERE oc.patientId = :patientId " +
			"AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
			"AND (rcs.pk.clinicalSpecialtyId = :clinicalSpecialtyId OR NOT EXISTS (SELECT 1 from ReferenceClinicalSpecialty rcs2 WHERE rcs2.pk.referenceId = r.id)) " +
			"AND r.careLineId = :careLineId " +
			"AND dr.snomedId = :practiceId " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId) " +
			"AND (d.deleteable.deleted = false OR d.deleteable.deleted is null)" +
			"AND (dr.deleteable.deleted = false OR dr.deleteable.deleted is null) " +
			"AND (sr.deleteable.deleted = false OR sr.deleteable.deleted is null) " +
			"AND ((clr.roleId IN (:userRoleIds) AND cl.classified IS TRUE AND clr.deleteable.deleted IS FALSE) OR cl.classified IS FALSE OR cl.classified IS NULL)")
	List<ReferenceSummaryBo> getReferencesSummaryFromOdontologyConsultationByClinicalSpecialtyIdAndPracticeIdAndCareLineId(@Param("patientId") Integer patientId,
																														   @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId,
																														   @Param("careLineId") Integer careLineId,
																														   @Param("practiceId") Integer practiceId,
																														   @Param("userRoleIds") List<Short> userRoleIds);

	@Transactional(readOnly = true)
	@Query(value = "SELECT DISTINCT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.name, " +
			"oc.performedDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId, r.destinationInstitutionId) " +
			"FROM Reference r " +
			"LEFT JOIN ReferenceClinicalSpecialty rcs ON (rcs.pk.referenceId = r.id) " +
			"JOIN OdontologyConsultation oc ON r.encounterId = oc.id " +
			"JOIN Institution i ON oc.institutionId = i.id " +
			"JOIN HealthcareProfessional hp ON hp.id = oc.doctorId " +
			"JOIN Person p ON p.id = hp.personId " +
			"JOIN PersonExtended pe ON p.id = pe.id " +
			"JOIN ServiceRequest sr ON r.serviceRequestId = sr.id " +
			"JOIN Document d ON sr.id = d.sourceId " +
			"JOIN DocumentDiagnosticReport ddr ON d.id = ddr.pk.documentId " +
			"JOIN DiagnosticReport dr ON ddr.pk.diagnosticReportId = dr.id " +
			"LEFT JOIN CareLine cl ON (cl.id = r.careLineId) " +
			"LEFT JOIN CareLineRole clr ON (clr.careLineId = cl.id) " +
			"WHERE oc.patientId = :patientId " +
			"AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
			"AND (rcs.pk.clinicalSpecialtyId = :clinicalSpecialtyId OR NOT EXISTS (SELECT 1 from ReferenceClinicalSpecialty rcs2 WHERE rcs2.pk.referenceId = r.id)) " +
			"AND dr.snomedId = :practiceId " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId) " +
			"AND (d.deleteable.deleted = false OR d.deleteable.deleted is null)" +
			"AND (dr.deleteable.deleted = false OR dr.deleteable.deleted is null) " +
			"AND (sr.deleteable.deleted = false OR sr.deleteable.deleted is null) " +
			"AND ((clr.roleId IN (:userRoleIds) AND cl.classified IS TRUE AND clr.deleteable.deleted IS FALSE) OR cl.classified IS FALSE OR cl.classified IS NULL)")
	List<ReferenceSummaryBo> getReferencesSummaryFromOdontologyConsultationByClinicalSpecialtyIdAndPracticeId(@Param("patientId") Integer patientId,
																											  @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId,
																											  @Param("practiceId") Integer practiceId,
																											  @Param("userRoleIds") List<Short> userRoleIds);

	@Transactional(readOnly = true)
	@Query(value = "SELECT r.sourceTypeId FROM Reference r WHERE r.id = :referenceId")
	Integer getReferenceEncounterTypeId(@Param("referenceId") Integer referenceId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT DISTINCT new ar.lamansys.refcounterref.domain.reference.ReferenceDataBo(r.id, oc.patientId, oc.patientMedicalCoverageId, oc.id, " +
			"r.consultation, oc.creationable.createdOn, rn.description," +
			"cl.id, cl.description, cs.id, cs.name, " +
			"i.id, i.name, d.id, d.description, p.description, i2.id, i2.name, d2.id, d2.description,"+
			"hp.personId, r.priority, cr.closureTypeId, r.phonePrefix, r.phoneNumber, r.serviceRequestId," +
			"oc.creationable.createdBy, r.statusId, r.regulationStateId) " +
			"FROM Reference r " +
			"JOIN OutpatientConsultation oc ON (r.encounterId = oc.id) " +
			"JOIN ClinicalSpecialty cs ON (oc.clinicalSpecialtyId = cs.id) " +
			"JOIN Institution i ON (oc.institutionId = i.id)" +
			"JOIN Address a ON (a.id = i.addressId) " +
			"LEFT JOIN City c ON (c.id = a.cityId) " +
			"LEFT JOIN Department d ON (d.id = c.departmentId)" +
			"LEFT JOIN Province p ON (p.id = d.provinceId) " +
			"LEFT JOIN Institution i2 ON (r.destinationInstitutionId = i2.id)" +
			"LEFT JOIN Address a2 ON (i2.addressId = a2.id) " +
			"LEFT JOIN City c2 ON (c2.id = a2.cityId) " +
			"LEFT JOIN Department d2 ON (c2.departmentId = d2.id) " +
			"LEFT JOIN CareLine cl ON (cl.id = r.careLineId) " +
			"LEFT JOIN ReferenceNote rn ON (rn.id = r.referenceNoteId) " +
			"JOIN HealthcareProfessional hp ON (hp.id = oc.doctorId) " +
			"LEFT JOIN CounterReference cr ON (r.id = cr.referenceId) " +
			"WHERE r.id = :referenceId ")
	Optional<ReferenceDataBo> getReferenceDataFromOutpatientConsultation(@Param("referenceId") Integer referenceId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT DISTINCT new ar.lamansys.refcounterref.domain.reference.ReferenceDataBo(r.id, oc.patientId, oc.patientMedicalCoverageId, oc.id, " +
			"r.consultation, oc.creationable.createdOn, rn.description, " +
			"cl.id, cl.description, cs.id, cs.name," +
			"i.id, i.name, d.id, d.description, p.description, i2.id, i2.name, d2.id, d2.description,"+
			"hp.personId, r.priority, cr.closureTypeId, r.phonePrefix, r.phoneNumber, r.serviceRequestId, " +
			"oc.creationable.createdBy, r.statusId, r.regulationStateId) " +
			"FROM Reference r " +
			"JOIN OdontologyConsultation oc ON (r.encounterId = oc.id) " +
			"JOIN ClinicalSpecialty cs ON (oc.clinicalSpecialtyId = cs.id) " +
			"JOIN Institution i ON (oc.institutionId = i.id) " +
			"JOIN Address a ON (a.id = i.addressId) " +
			"LEFT JOIN City c ON (a.cityId = c.id) " +
			"LEFT JOIN Department d ON (d.id = c.departmentId) " +
			"LEFT JOIN Province p ON (p.id = d.provinceId) " +
			"LEFT JOIN Institution i2 ON (r.destinationInstitutionId = i2.id) " +
			"LEFT JOIN Address a2 ON (i2.addressId = a2.id) " +
			"LEFT JOIN City c2 ON (c2.id = a2.cityId) " +
			"LEFT JOIN Department d2 ON (c2.departmentId = d2.id) " +
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
	@Query("SELECT DISTINCT new ar.lamansys.refcounterref.domain.reference.ReferenceRequestBo(r.id, cl.id, cl.description, " +
			"r.priority, rn.description, cr.closureTypeId, cr.creationable.createdOn, cr.doctorId) " +
			"FROM Reference r " +
			"LEFT JOIN CareLine cl ON (r.careLineId = cl.id) " +
			"LEFT JOIN ReferenceNote rn ON (r.referenceNoteId = rn.id) " +
			"LEFT JOIN CounterReference cr ON (cr.referenceId = r.id) " +
			"WHERE r.serviceRequestId = :serviceRequestId")
	Optional<ReferenceRequestBo> getReferenceByServiceRequestId(@Param("serviceRequestId") Integer serviceRequestId);

	@Transactional(readOnly = true)
	@Query(" SELECT NEW ar.lamansys.sgh.shared.domain.reference.ReferencePhoneBo(r.phonePrefix, r.phoneNumber) " +
			"FROM Reference r " +
			"WHERE r.id = :referenceId")
	ReferencePhoneBo getReferencePhoneData(@Param("referenceId") Integer referenceId);

	@Transactional(readOnly = true)
	@Query("SELECT hrr.stateId " +
			"FROM Reference r " +
			"JOIN HistoricReferenceRegulation hrr ON (r.id = hrr.referenceId) " +
			"WHERE r.id = :referenceId " +
			"ORDER BY hrr.creationable.createdOn DESC")
	List<Short> getReferenceRegulationStateId(@Param("referenceId") Integer referenceId);

	@Transactional
	@Modifying
	@Query("UPDATE Reference r " +
			"SET r.deleteable.deleted = true " +
			", r.deleteable.deletedOn = CURRENT_TIMESTAMP " +
			", r.deleteable.deletedBy = ?#{ principal.userId } " +
			", r.statusId = :statusId " +
			"WHERE r.id = :referenceId")
	void deleteAndUpdateStatus(@Param("referenceId") Integer referenceId, @Param("statusId") Short statusId);

	@Transactional(readOnly = true)
	@Query("SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceStudyBo(s2.id, s2.sctid, s2.pt, s.id, s.sctid, s.pt, sr.categoryId) " +
			"FROM Reference r " +
			"JOIN ServiceRequest sr ON (r.serviceRequestId = sr.id) " +
			"JOIN Document d ON (sr.id = d.sourceId) " +
			"JOIN DocumentDiagnosticReport ddr ON (ddr.pk.documentId = d.id) " +
			"JOIN DiagnosticReport dr ON (ddr.pk.diagnosticReportId = dr.id) " +
			"JOIN Snomed s ON (s.id = dr.snomedId)" +
			"JOIN HealthCondition hc ON (dr.healthConditionId = hc.id)" +
			"JOIN Snomed s2 ON (s2.id = hc.snomedId) " +
			"WHERE r.id = :referenceId " +
			"AND d.typeId = " + DocumentType.ORDER)
	Optional<ReferenceStudyBo> getReferenceStudy(@Param("referenceId") Integer referenceId);

	@Transactional(readOnly = true)
	@Query("SELECT r.destinationInstitutionId " +
			"FROM Reference r " +
			"WHERE r.id = :referenceId")
	Integer getDestinationInstitutionId(@Param("referenceId") Integer referenceId);

	@Transactional(readOnly = true)
	@Query("SELECT oc.patientId " +
			"FROM Reference r " +
			"JOIN OutpatientConsultation oc ON (r.encounterId = oc.id) " +
			"WHERE r.id = :referenceId")
	Integer getPatientId(@Param("referenceId") Integer referenceId);

	@Transactional(readOnly = true)
	@Query("SELECT oc.institutionId " +
			"FROM Reference r " +
			"JOIN OutpatientConsultation oc ON (r.encounterId = oc.id) " +
			"WHERE r.id = :referenceId")
	Integer getOriginInstitutionId(@Param("referenceId") Integer referenceId);

	@Transactional(readOnly = true)
	@Query("SELECT r.serviceRequestId " +
			"FROM Reference r " +
			"WHERE r.id = :referenceId")
	Optional<Integer> getServiceRequestId(@Param("referenceId") Integer referenceId);
}