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
            "JOIN Reference r ON (rhc.pk.referenceId = r.id)" +
            "JOIN HealthCondition hc ON (rhc.pk.healthConditionId = hc.id) " +
            "JOIN Snomed s ON (s.id = hc.snomedId) " +
			"LEFT JOIN CareLine cl ON (r.careLineId = cl.id) " +
			"LEFT JOIN CareLineRole clr ON (clr.careLineId = cl.id) " +
            "WHERE hc.patientId = :patientId " +
            "AND (r.deleteable.deleted = FALSE OR r.deleteable.deleted IS NULL) " +
            "AND rhc.pk.referenceId NOT IN (SELECT cr.referenceId FROM CounterReference cr) " +
			"AND (cl.id IS NULL OR cl.classified IS FALSE OR (clr.roleId IN (:loggedUserRoleIds) AND cl.classified IS TRUE AND clr.deleteable.deleted IS FALSE)) " +
			"AND r.regulationStateId <> :regulationStateId ")
    List<ReferenceProblemBo> getReferencesProblemsByPatientId(@Param("patientId") Integer patientId,
															  @Param("loggedUserRoleIds") List<Short> loggedUserRoleIds,
															  @Param("regulationStateId") Short regulationStateId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT rhc.pk.referenceId " +
            "FROM ReferenceHealthCondition rhc " +
            "WHERE rhc.pk.healthConditionId = :healthConditionId")
    List<Integer> getReferenceIdsByHealthConditionId(@Param("healthConditionId") Integer healthConditionId);

}
