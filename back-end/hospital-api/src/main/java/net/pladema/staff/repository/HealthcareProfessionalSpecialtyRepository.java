package net.pladema.staff.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.staff.repository.domain.HealthcareProfessionalSpecialtyVo;
import net.pladema.staff.repository.domain.ProfessionalClinicalSpecialtyVo;
import net.pladema.staff.repository.entity.HealthcareProfessionalSpecialty;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthcareProfessionalSpecialtyRepository extends SGXAuditableEntityJPARepository<HealthcareProfessionalSpecialty, Integer> {

	@Transactional(readOnly = true)
	@Query( value = "SELECT (CASE WHEN COUNT(hps.id)> 0 THEN TRUE ELSE FALSE END) " +
			"FROM {h-schema}healthcare_professional_specialty hps " +
			"WHERE hps.healthcare_professional_id = :healthcareProfessionalId " +
			"AND hps.clinical_specialty_id = :clinicalSpecialtyId " +
			"AND hps.professional_specialty_id = :professionalSpecialtyId ", nativeQuery = true)
	boolean existsValues(@Param("healthcareProfessionalId") Integer healthcareProfessionalId,
						 @Param("clinicalSpecialtyId")  Integer clinicalSpecialtyId,
						 @Param("professionalSpecialtyId")  Integer professionalSpecialtyId);

    @Transactional(readOnly = true)
    @Query( "SELECT (CASE WHEN COUNT(hps.id) = 1 THEN TRUE ELSE FALSE END) " +
            "FROM HealthcareProfessionalSpecialty AS hps " +
            "WHERE hps.healthcareProfessionalId = :healthcareProfessionalId ")
    boolean hasOnlyOneSpecialty(@Param("healthcareProfessionalId") Integer healthcareProfessionalId);

    @Transactional(readOnly = true)
    @Query( "SELECT (CASE WHEN COUNT(d.healthcareProfessionalId) >= 1 THEN TRUE ELSE FALSE END)" +
            "FROM Diary d " +
            "JOIN DoctorsOffice doff ON d.doctorsOfficeId = doff.id " +
            "WHERE d.healthcareProfessionalId = :healthcareProfessionalId AND d.clinicalSpecialtyId = :clinicalSpecialtyId")
    boolean hasHealthcareProfessionalSpecialtyAffected(@Param("healthcareProfessionalId") Integer healthcareProfessionalId,
                                                       @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT NEW net.pladema.staff.repository.domain.ProfessionalClinicalSpecialtyVo" +
            "(hps.healthcareProfessionalId, cs) " +
            "FROM HealthcareProfessionalSpecialty hps "
            + "INNER JOIN ClinicalSpecialty cs ON hps.clinicalSpecialtyId = cs.id "
            + "WHERE hps.healthcareProfessionalId IN :professionalsIds "
            + "AND hps.deleteable.deleted = false")
    List<ProfessionalClinicalSpecialtyVo> getAllByProfessionals(@Param("professionalsIds") List<Integer> professionalsIds);

    @Transactional(readOnly = true)
    @Query(value = "SELECT NEW net.pladema.staff.repository.domain.HealthcareProfessionalSpecialtyVo" +
            "(hps.id,hps.healthcareProfessionalId, hps.professionalSpecialtyId, hps.clinicalSpecialtyId) " +
            "FROM HealthcareProfessionalSpecialty hps "
            + "WHERE hps.healthcareProfessionalId =:professionalId "
            + "AND hps.deleteable.deleted = false")
    List<HealthcareProfessionalSpecialtyVo> getAllByProfessional(@Param("professionalId") Integer professionalId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE healthcare_professional_specialty " +
            "SET deleted = false " +
            "WHERE id = :healthcareProfessionalSpecialtyId",  nativeQuery=true)
    void setDeletedFalse(@Param("healthcareProfessionalSpecialtyId") Integer healthcareProfessionalSpecialtyId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT * " +
			"FROM {h-schema} healthcare_professional_specialty hps " +
			"WHERE hps.healthcare_professional_id = :healthcareProfessionalId " +
			"AND hps.clinical_specialty_id = :clinicalSpecialtyId " +
			"AND hps.professional_specialty_id = :professionalSpecialtyId ", nativeQuery = true)
	Optional<HealthcareProfessionalSpecialty> findByUniqueKey(@Param("healthcareProfessionalId") Integer healthcareProfessionalId,
															  @Param("clinicalSpecialtyId")  Integer clinicalSpecialtyId,
															  @Param("professionalSpecialtyId")  Integer professionalSpecialtyId);


}
