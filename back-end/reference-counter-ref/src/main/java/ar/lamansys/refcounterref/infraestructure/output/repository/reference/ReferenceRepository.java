package ar.lamansys.refcounterref.infraestructure.output.repository.reference;

import java.util.List;

import ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.refcounterref.domain.reference.ReferenceGetBo;

@Repository
public interface ReferenceRepository extends JpaRepository<Reference, Integer> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceGetBo(r.id, oc.startDate, " +
            "rn.id, rn.description, cl.id, cl.description, cs.id, cs.name, oc.doctorId, " +
            "p.firstName, px.nameSelfDetermination, p.lastName) " +
            "FROM Reference r " +
            "JOIN OutpatientConsultation oc ON (r.encounterId = oc.id) " +
            "JOIN ClinicalSpecialty cs ON (r.clinicalSpecialtyId = cs.id) " +
            "LEFT JOIN CareLine cl ON (cl.id = r.careLineId) " +
            "LEFT JOIN ReferenceNote rn ON (rn.id = r.referenceNoteId) " +
            "JOIN HealthcareProfessional hp ON (hp.id = oc.doctorId) " +
            "JOIN Person p ON (p.id = hp.personId) " +
			"LEFT JOIN PersonExtended px ON(px.id = p.id) " +
            "WHERE oc.patientId = :patientId " +
            "AND r.clinicalSpecialtyId IN (:clinicalSpecialtyIds)" +
            "AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId)")
    List<ReferenceGetBo> getReferencesFromOutpatientConsultation(@Param("patientId") Integer patientId, @Param("clinicalSpecialtyIds") List<Integer> clinicalSpecialtyIds);

    @Transactional(readOnly = true)
    @Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceGetBo(r.id, oc.performedDate, " +
            "rn.id, rn.description, cl.id, cl.description, cs.id, cs.name, oc.doctorId, " +
            "p.firstName, px.nameSelfDetermination, p.lastName) " +
            "FROM Reference r " +
            "JOIN OdontologyConsultation oc ON (r.encounterId = oc.id) " +
            "JOIN ClinicalSpecialty cs ON (r.clinicalSpecialtyId = cs.id) " +
            "LEFT JOIN CareLine cl ON (cl.id = r.careLineId) " +
            "LEFT JOIN ReferenceNote rn ON (rn.id = r.referenceNoteId) " +
            "JOIN HealthcareProfessional hp ON (hp.id = oc.doctorId) " +
            "JOIN Person p ON (p.id = hp.personId) " +
			"LEFT JOIN PersonExtended px ON(px.id = p.id) " +
            "WHERE oc.patientId = :patientId " +
            "AND r.clinicalSpecialtyId IN (:clinicalSpecialtyIds)" +
            "AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId)")
    List<ReferenceGetBo> getReferencesFromOdontologyConsultation(@Param("patientId") Integer patientId, @Param("clinicalSpecialtyIds") List<Integer> clinicalSpecialtyIds);

	@Transactional(readOnly = true)
	@Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.id, i.name, " +
			"oc.startDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId," +
			"r.phonePrefix, r.phoneNumber) " +
			"FROM Reference r " +
			"JOIN OutpatientConsultation oc ON r.encounterId = oc.id " +
			"JOIN Institution i ON oc.institutionId = i.id " +
			"JOIN HealthcareProfessional hp ON hp.id = oc.doctorId " +
			"JOIN Person p ON p.id = hp.personId " +
			"JOIN PersonExtended pe ON p.id = pe.id " +
			"WHERE oc.patientId = :patientId " +
			"AND r.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"AND r.careLineId = :careLineId " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId) " +
			"AND r.serviceRequestId IS NULL ")
	List<ReferenceSummaryBo> getReferencesSummaryFromOutpatientConsultationByClinicalSpecialtyId(@Param("patientId") Integer patientId,
																								 @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId,
																								 @Param("careLineId") Integer careLineId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.id, i.name, " +
			"oc.performedDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId," +
			"r.phonePrefix, r.phoneNumber) " +
			"FROM Reference r " +
			"JOIN OdontologyConsultation oc ON r.encounterId = oc.id " +
			"JOIN Institution i ON oc.institutionId = i.id " +
			"JOIN HealthcareProfessional hp ON hp.id = oc.doctorId " +
			"JOIN Person p ON p.id = hp.personId " +
			"JOIN PersonExtended pe ON p.id = pe.id " +
			"WHERE oc.patientId = :patientId " +
			"AND r.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"AND r.careLineId = :careLineId " +
			"AND r.id NOT IN (SELECT cr.referenceId  FROM CounterReference cr WHERE cr.patientId = :patientId) " +
			"AND r.serviceRequestId IS NULL")
	List<ReferenceSummaryBo> getReferencesSummaryFromOdontologyConsultationByClinicalSpecialtyId(@Param("patientId") Integer patientId,
																								 @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId,
																								 @Param("careLineId") Integer careLineId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.id, i.name, " +
			"oc.startDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId," +
			"r.phonePrefix, r.phoneNumber) " +
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
	@Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.id, i.name, " +
			"oc.performedDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId," +
			"r.phonePrefix, r.phoneNumber) " +
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
	@Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.id, i.name, " +
			"oc.startDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId," +
			"r.phonePrefix, r.phoneNumber) " +
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
	@Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo(r.id, i.id, i.name, " +
			"oc.performedDate, p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination, r.careLineId," +
			"r.phonePrefix, r.phoneNumber) " +
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

}