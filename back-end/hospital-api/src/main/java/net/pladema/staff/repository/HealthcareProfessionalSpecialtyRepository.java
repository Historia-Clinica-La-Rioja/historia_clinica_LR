package net.pladema.staff.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.staff.repository.domain.HealthcareProfessionalSpecialtyVo;
import net.pladema.staff.repository.domain.ProfessionalClinicalSpecialtyVo;
import net.pladema.staff.repository.entity.HealthcareProfessionalSpecialty;

@Repository
public interface HealthcareProfessionalSpecialtyRepository extends SGXAuditableEntityJPARepository<HealthcareProfessionalSpecialty, Integer> {

	@Transactional(readOnly = true)
	@Query( value = "SELECT (CASE WHEN COUNT(hps.id)> 0 THEN TRUE ELSE FALSE END) " +
			"FROM {h-schema}healthcare_professional_specialty hps " +
			"WHERE hps.professional_professions_id = :professionalProfessionsId " +
			"AND hps.clinical_specialty_id = :clinicalSpecialtyId ", nativeQuery = true)
	boolean existsValues(@Param("professionalProfessionsId") Integer professionalProfessionsId,
						 @Param("clinicalSpecialtyId")  Integer clinicalSpecialtyId);

    @Transactional(readOnly = true)
    @Query( "SELECT (CASE WHEN COUNT(hps.id) = 1 THEN TRUE ELSE FALSE END) " +
            "FROM HealthcareProfessionalSpecialty AS hps " +
            "WHERE hps.professionalProfessionsId = :professionalProfessionsId ")
    boolean hasOnlyOneSpecialty(@Param("professionalProfessionsId") Integer professionalProfessionsId);

    @Transactional(readOnly = true)
    @Query( "SELECT (CASE WHEN COUNT(d.healthcareProfessionalId) >= 1 THEN TRUE ELSE FALSE END)" +
            "FROM Diary d " +
            "JOIN DoctorsOffice doff ON d.doctorsOfficeId = doff.id " +
            "WHERE d.healthcareProfessionalId = :healthcareProfessionalId AND d.clinicalSpecialtyId = :clinicalSpecialtyId")
    boolean hasHealthcareProfessionalSpecialtyAffected(@Param("healthcareProfessionalId") Integer healthcareProfessionalId,
                                                       @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT NEW net.pladema.staff.repository.domain.ProfessionalClinicalSpecialtyVo" +
            "(hps.professionalProfessionsId, cs) " +
            "FROM HealthcareProfessionalSpecialty hps "
            + "INNER JOIN ClinicalSpecialty cs ON hps.clinicalSpecialtyId = cs.id "
			+ "INNER JOIN ProfessionalProfessions pp ON pp.id = hps.professionalProfessionsId "
            + "WHERE pp.healthcareProfessionalId IN :professionalsIds "
            + "AND hps.deleteable.deleted = false")
    List<ProfessionalClinicalSpecialtyVo> getAllByProfessionals(@Param("professionalsIds") List<Integer> professionalsIds);

    @Transactional(readOnly = true)
    @Query(value = "SELECT NEW net.pladema.staff.repository.domain.HealthcareProfessionalSpecialtyVo" +
            "(hps.id,pp.healthcareProfessionalId, pp.professionalSpecialtyId, hps.clinicalSpecialtyId) " +
            "FROM HealthcareProfessionalSpecialty hps "
			+ "INNER JOIN ProfessionalProfessions pp ON pp.id = hps.professionalProfessionsId "
			+ "JOIN HealthcareProfessional hp ON (pp.healthcareProfessionalId = hp.id)"
            + "WHERE hps.id =:professionalId "
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
			"WHERE hps.professional_professions_id = :professionalProfessionsId " +
			"AND hps.clinical_specialty_id = :clinicalSpecialtyId ", nativeQuery = true)
	Optional<HealthcareProfessionalSpecialty> findByUniqueKey(@Param("professionalProfessionsId") Integer professionalProfessionsId,
															  @Param("clinicalSpecialtyId")  Integer clinicalSpecialtyId);


}
