package net.pladema.patient.repository;


import net.pladema.patient.repository.entity.PrivateHealthInsuranceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PrivateHealthInsuranceDetailsRepository extends JpaRepository<PrivateHealthInsuranceDetails, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT phid FROM PrivateHealthInsuranceDetails as phid " +
            "WHERE phid.planId = :planId")
    List<PrivateHealthInsuranceDetails> findAllByPlanId(@Param("planId") Integer planId);
}
