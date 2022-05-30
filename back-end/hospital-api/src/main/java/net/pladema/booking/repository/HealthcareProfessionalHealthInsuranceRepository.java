package net.pladema.booking.repository;

import net.pladema.booking.repository.entity.HealthcareProfessionalHealthInsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HealthcareProfessionalHealthInsuranceRepository extends JpaRepository<HealthcareProfessionalHealthInsurance, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT COUNT(hh.id) " +
            "FROM HealthcareProfessionalHealthInsurance hh " +
            "WHERE hh.healthcareProfessionalId = :healthcareProfessionalId " +
            "AND hh.medicalCoverageId = :medicalCoverageId")
    long findByProfessionalIdAndMedicalCoverageId(@Param("healthcareProfessionalId") Integer healthcareProfessionalId,
                                                     @Param("medicalCoverageId") Integer medicalCoverageId);
}
