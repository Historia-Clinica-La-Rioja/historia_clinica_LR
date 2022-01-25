package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.PrivateHealthInsurancePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PrivateHealthInsurancePlanRepository extends JpaRepository<PrivateHealthInsurancePlan, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT phip FROM PrivateHealthInsurancePlan as phip " +
            "WHERE phip.privateHealthInsuranceId = :privateHealthInsuranceId " +
            "AND phip.plan = :plan ")
    PrivateHealthInsurancePlan findByIdAndPlan(@Param("privateHealthInsuranceId") Integer privateHealthInsuranceId, @Param("plan") String plan);

    @Transactional(readOnly = true)
    @Query("SELECT phip FROM PrivateHealthInsurancePlan as phip " +
            "WHERE phip.privateHealthInsuranceId = :privateHealthInsuranceId ")
    List<PrivateHealthInsurancePlan> findByPrivateHealthInsuranceId(@Param("privateHealthInsuranceId") Integer privateHealthInsuranceId);
}