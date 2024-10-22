package ar.lamansys.refcounterref.infraestructure.output.repository.counterreference;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceSummaryBo;
import ar.lamansys.refcounterref.domain.procedure.CounterReferenceProcedureBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;

import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CounterReferenceRepository extends JpaRepository<CounterReference, Integer> {

    @Query(value = " SELECT new ar.lamansys.refcounterref.domain.counterreference.CounterReferenceSummaryBo("
            + " cr.id, cr.performedDate, up.pk.personId, cs.name, n.description, i.name, cr.closureTypeId, cr.creationable.createdOn)"
            + "  FROM CounterReference cr"
			+ "  LEFT JOIN Institution i ON (cr.institutionId = i.id)"
            + "  LEFT JOIN ClinicalSpecialty cs ON (cr.clinicalSpecialtyId = cs.id) "
			+ "	 JOIN UserPerson up ON (cr.creationable.createdBy = up.pk.userId) "
            + "  LEFT JOIN Note n ON (cr.noteId = n.id) "
            + "  WHERE cr.referenceId = :referenceId")
    List<CounterReferenceSummaryBo> findByReferenceId(@Param("referenceId") Integer referenceId);

    @Query(value = "SELECT new ar.lamansys.refcounterref.domain.procedure.CounterReferenceProcedureBo("
            + " s.sctid, s.pt)"
            +"  FROM CounterReference cr"
            +"  JOIN Document d ON (d.sourceId = cr.id)"
            +"  JOIN DocumentProcedure dp ON (d.id = dp.pk.documentId)"
            +"  JOIN Procedure p ON (dp.pk.procedureId = p.id)"
            +"  JOIN Snomed s ON (p.snomedId = s.id) "
            +"  WHERE cr.id = :counterReferenceId"
            +"  AND d.statusId = '" + DocumentStatus.FINAL + "'"
            +"  AND d.typeId = "+ DocumentType.COUNTER_REFERENCE
            +"  AND d.sourceTypeId =" + SourceType.COUNTER_REFERENCE)
    List<CounterReferenceProcedureBo> getProcedures(@Param("counterReferenceId") Integer counterReferenceId);

	@Transactional(readOnly = true)
	@Query("SELECT cr.id " +
			"FROM CounterReference cr " +
			"WHERE cr.patientId IN :patientsIds")
	List<Integer> getCounterReferenceIdsFromPatients(@Param("patientsIds") List<Integer> patientsIds);

	@Transactional(readOnly = true)
	@Query("SELECT cr.patientMedicalCoverageId " +
			"FROM CounterReference cr " +
			"WHERE cr.patientMedicalCoverageId = :id")
	Optional<Integer> getPatientMedicalCoverageId(@Param("id") Integer id);

	@Query("SELECT (case when count(cr.id)>0 then true else false end)" +
			"FROM CounterReference cr " +
			"WHERE cr.referenceId = :referenceId")
	boolean existsByReferenceId(@Param("referenceId")Integer referenceId);
}