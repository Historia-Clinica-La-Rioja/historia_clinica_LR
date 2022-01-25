package ar.lamansys.refcounterref.infraestructure.output.repository.referencehealthcondition;

import ar.lamansys.refcounterref.domain.referenceproblem.ReferenceProblemBo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReferenceHealthConditionRepository extends JpaRepository<ReferenceHealthCondition, ReferenceHealthConditionPk> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT new ar.lamansys.refcounterref.domain.referenceproblem.ReferenceProblemBo(hc.id, " +
            "s.sctid, s.pt, rhc.pk.referenceId) " +
            "FROM ReferenceHealthCondition rhc " +
            "JOIN HealthCondition hc ON (rhc.pk.healthConditionId = hc.id) " +
            "JOIN Snomed s ON (s.id = hc.snomedId)" +
            "WHERE rhc.pk.referenceId IN (:referenceIds)")
    List<ReferenceProblemBo> getReferencesProblems(@Param("referenceIds") List<Integer> referenceIds);

    @Transactional(readOnly = true)
    @Query(value = "SELECT new ar.lamansys.refcounterref.domain.referenceproblem.ReferenceProblemBo(hc.id, " +
            "s.sctid, s.pt, rhc.pk.referenceId) " +
            "FROM ReferenceHealthCondition rhc " +
            "JOIN HealthCondition hc ON (rhc.pk.healthConditionId = hc.id) " +
            "JOIN Snomed s ON (s.id = hc.snomedId) " +
            "WHERE hc.patientId = :patientId " +
            "AND rhc.pk.referenceId NOT IN (SELECT cr.referenceId FROM CounterReference cr)")
    List<ReferenceProblemBo> getReferencesProblemsByPatientId(@Param("patientId") Integer patientId);

}
