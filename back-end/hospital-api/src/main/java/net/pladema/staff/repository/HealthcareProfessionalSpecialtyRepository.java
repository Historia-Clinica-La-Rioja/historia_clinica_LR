package net.pladema.staff.repository;

import net.pladema.staff.repository.entity.HealthcareProfessionalSpecialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HealthcareProfessionalSpecialtyRepository extends JpaRepository<HealthcareProfessionalSpecialty, Integer> {


    @Transactional(readOnly = true)
    @Query( "SELECT (case when count(hps.id)> 0 then true else false end) " +
            "FROM HealthcareProfessionalSpecialty AS hps " +
            "WHERE hps.healthcareProfessionalId = :healthcareProfessionalId " +
            "AND hps.clinicalSpecialtyId = :clinicalSpecialtyId " +
            "AND hps.professionalSpecialtyId = :professionalSpecialtyId ")
    boolean existsValues(@Param("healthcareProfessionalId") Integer healthcareProfessionalId,
                         @Param("clinicalSpecialtyId")  Integer clinicalSpecialtyId,
                         @Param("professionalSpecialtyId")  Integer professionalSpecialtyId);

    @Transactional(readOnly = true)
    @Query( "SELECT (case when count(hps.id)= 1 then true else false end) " +
            "FROM HealthcareProfessionalSpecialty AS hps " +
            "WHERE hps.healthcareProfessionalId = :healthcareProfessionalId ")
    boolean hasOnlyOneSpecialty(@Param("healthcareProfessionalId") Integer healthcareProfessionalId);

}
