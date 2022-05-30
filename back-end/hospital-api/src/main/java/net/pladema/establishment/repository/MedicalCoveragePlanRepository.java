package net.pladema.establishment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.MedicalCoveragePlan;

@Repository
public interface MedicalCoveragePlanRepository extends SGXAuditableEntityJPARepository<MedicalCoveragePlan, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT mcp FROM MedicalCoveragePlan as mcp " +
            "WHERE mcp.medicalCoverageId = :medicalCoverageId " +
            "AND LOWER(mcp.plan) = :plan ")
	Optional<MedicalCoveragePlan> findByIdAndPlan(@Param("medicalCoverageId") Integer medicalCoverageId, @Param("plan") String plan);

    @Transactional(readOnly = true)
    @Query("SELECT mcp FROM MedicalCoveragePlan as mcp " +
            "WHERE mcp.medicalCoverageId = :medicalCoverageId ")
    List<MedicalCoveragePlan> findAllByMedicalCoverageId(@Param("medicalCoverageId") Integer medicalCoverageId);

	@Transactional(readOnly = true)
	@Query("SELECT mcp FROM MedicalCoveragePlan as mcp " +
			"WHERE mcp.medicalCoverageId = :medicalCoverageId " +
			"AND mcp.deleteable.deleted = false" )
	List<MedicalCoveragePlan> findAllActiveByMedicalCoverageId(@Param("medicalCoverageId") Integer medicalCoverageId);

    @Transactional
    @Modifying
    @Query("DELETE FROM MedicalCoveragePlan mcp "+
            "WHERE mcp.id = :id ")
    void deleteMergedCoveragePlan(@Param("id") Integer id);

}