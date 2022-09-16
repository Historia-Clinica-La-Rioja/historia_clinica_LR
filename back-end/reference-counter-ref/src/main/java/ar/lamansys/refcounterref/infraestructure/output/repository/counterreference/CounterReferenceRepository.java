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

@Repository
public interface CounterReferenceRepository extends JpaRepository<CounterReference, Integer> {

    @Query(value = " SELECT new ar.lamansys.refcounterref.domain.counterreference.CounterReferenceSummaryBo("
            + " cr.id, cr.performedDate, hp.id, p.firstName, px.nameSelfDetermination, p.lastName, cs.name, n.description, i.name, cr.closureTypeId)"
            + "  FROM CounterReference cr"
			+ "  JOIN Institution i ON (cr.institutionId = i.id)"
            + "  JOIN ClinicalSpecialty cs ON (cr.clinicalSpecialtyId = cs.id)"
            + "  JOIN Document doc ON (doc.sourceId = cr.id)"
            + "  LEFT JOIN Note n ON (n.id = doc.evolutionNoteId)"
            + "  JOIN HealthcareProfessional hp ON (hp.id = cr.doctorId)"
            + "  JOIN Person p ON (p.id = hp.personId)"
			+ "  LEFT JOIN PersonExtended px ON (px.id = p.id)"
            + "  WHERE cr.referenceId = :referenceId"
            + "  AND doc.statusId = '" + DocumentStatus.FINAL + "'"
            + "  AND doc.sourceTypeId =" + SourceType.COUNTER_REFERENCE
            + "  AND doc.typeId =" + DocumentType.COUNTER_REFERENCE)
    Optional<CounterReferenceSummaryBo> findByReferenceId(@Param("referenceId") Integer referenceId);

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

}