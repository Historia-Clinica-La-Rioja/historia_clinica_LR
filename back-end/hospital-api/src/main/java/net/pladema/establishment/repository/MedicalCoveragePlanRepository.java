package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.MedicalCoveragePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MedicalCoveragePlanRepository extends JpaRepository<MedicalCoveragePlan, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT mcp FROM MedicalCoveragePlan as mcp " +
            "WHERE mcp.medicalCoverageId = :medicalCoverageId " +
            "AND LOWER(mcp.plan) = :plan ")
    MedicalCoveragePlan findByIdAndPlan(@Param("medicalCoverageId") Integer medicalCoverageId, @Param("plan") String plan);

    @Transactional(readOnly = true)
    @Query("SELECT mcp FROM MedicalCoveragePlan as mcp " +
            "WHERE mcp.medicalCoverageId = :medicalCoverageId ")
    List<MedicalCoveragePlan> findByMedicalCoverageId(@Param("medicalCoverageId") Integer medicalCoverageId);
}