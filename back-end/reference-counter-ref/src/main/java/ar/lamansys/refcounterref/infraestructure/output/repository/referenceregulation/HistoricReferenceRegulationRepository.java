package ar.lamansys.refcounterref.infraestructure.output.repository.referenceregulation;

import ar.lamansys.refcounterref.infraestructure.output.repository.referenceregulation.entity.HistoricReferenceRegulation;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HistoricReferenceRegulationRepository extends SGXAuditableEntityJPARepository<HistoricReferenceRegulation, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT hrr " +
			"FROM HistoricReferenceRegulation hrr " +
			"WHERE hrr.ruleId = :ruleId " +
			"ORDER BY hrr.creationable.createdOn DESC")
	List<HistoricReferenceRegulation> findByRuleId(@Param("ruleId")Integer ruleId);

	@Transactional(readOnly = true)
	@Query("SELECT hrr " +
			"FROM HistoricReferenceRegulation hrr " +
			"JOIN Reference r ON (r.id = hrr.referenceId) " +
			"WHERE hrr.ruleId = :ruleId " +
			"AND r.destinationInstitutionId IN :institutionIds " +
			"ORDER BY hrr.creationable.createdOn DESC")
	List<HistoricReferenceRegulation> findByRuleIdInInstitutions(@Param("ruleId")Integer ruleId, @Param("institutionIds")List<Integer> institutionIds);

	@Transactional
	@Modifying
	@Query("UPDATE HistoricReferenceRegulation AS hrr " +
			"SET hrr.ruleId = :ruleId," +
			"hrr.ruleLevel = :ruleLevel, " +
			"hrr.updateable.updatedOn = CURRENT_TIMESTAMP " +
			"WHERE hrr.ruleId IN :ruleIdsToReplace ")
	void updateRuleOnReferences(@Param("ruleId")Integer ruleId, @Param("ruleLevel")Short ruleLevel, @Param("ruleIdsToReplace")List<Integer> ruleIdsToReplace);

	@Transactional(readOnly = true)
	@Query("SELECT hrr " +
			"FROM HistoricReferenceRegulation hrr " +
			"WHERE hrr.referenceId = :referenceId " +
			"AND hrr.deleteable.deleted = FALSE " +
			"ORDER BY hrr.creationable.createdOn DESC")
	List<HistoricReferenceRegulation> getByReferenceId(@Param("referenceId") Integer referenceId);
}
