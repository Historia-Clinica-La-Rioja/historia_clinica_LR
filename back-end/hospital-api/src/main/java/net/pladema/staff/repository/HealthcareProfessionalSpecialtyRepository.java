package net.pladema.staff.repository;

import java.util.List;
import java.util.Optional;

import net.pladema.staff.repository.entity.ProfessionalProfessions;

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
			"WHERE hps.professional_profession_id = :professionalProfessionsId " +
			"AND hps.clinical_specialty_id = :clinicalSpecialtyId ", nativeQuery = true)
	boolean existsValues(@Param("professionalProfessionsId") Integer professionalProfessionsId,
						 @Param("clinicalSpecialtyId")  Integer clinicalSpecialtyId);

    @Transactional(readOnly = true)
    @Query( "SELECT (CASE WHEN COUNT(hps.id) = 1 THEN TRUE ELSE FALSE END) " +
            "FROM HealthcareProfessionalSpecialty AS hps " +
            "WHERE hps.professionalProfessionId = :professionalProfessionsId " +
			"AND hps.deleteable.deleted = false ")
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
            "(pp.healthcareProfessionalId, cs) " +
            "FROM HealthcareProfessionalSpecialty hps "
            + "INNER JOIN ClinicalSpecialty cs ON hps.clinicalSpecialtyId = cs.id "
			+ "INNER JOIN ProfessionalProfessions pp ON pp.id = hps.professionalProfessionId "
            + "WHERE pp.healthcareProfessionalId IN :professionalsIds "
            + "AND hps.deleteable.deleted = false")
    List<ProfessionalClinicalSpecialtyVo> getAllByProfessionals(@Param("professionalsIds") List<Integer> professionalsIds);

    @Transactional(readOnly = true)
    @Query(value = "SELECT NEW net.pladema.staff.repository.domain.HealthcareProfessionalSpecialtyVo" +
            "(hps.id,pp.healthcareProfessionalId, hps.professionalProfessionId, hps.clinicalSpecialtyId, cs.name) " +
            "FROM HealthcareProfessionalSpecialty hps "
			+ "INNER JOIN ProfessionalProfessions pp ON pp.id = hps.professionalProfessionId "
			+ "JOIN ClinicalSpecialty cs ON hps.clinicalSpecialtyId = cs.id "
            + "WHERE pp.id =:professionalId "
            + "AND hps.deleteable.deleted = false")
    List<HealthcareProfessionalSpecialtyVo> getAllByProfessional(@Param("professionalId") Integer professionalId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT NEW net.pladema.staff.repository.domain.ProfessionalClinicalSpecialtyVo" +
			"(pp.healthcareProfessionalId, cs) " +
			"FROM HealthcareProfessionalSpecialty hps "
			+ "INNER JOIN ClinicalSpecialty cs ON hps.clinicalSpecialtyId = cs.id "
			+ "INNER JOIN ProfessionalProfessions pp ON pp.id = hps.professionalProfessionId "
			+ "INNER JOIN Diary d ON d.clinicalSpecialtyId = cs.id "
			+ "INNER JOIN DoctorsOffice do ON do.id = d.doctorsOfficeId "
			+ "WHERE d.active is true "
			+ "AND do.institutionId = :institutionId "
			+ "AND pp.healthcareProfessionalId IN :professionalsIds "
			+ "AND hps.deleteable.deleted = false ")
	List<ProfessionalClinicalSpecialtyVo> getAllByActiveDiaryAndProfessionals(
			@Param("professionalsIds") List<Integer> professionalsIds,
			@Param("institutionId") Integer institutionId
	);

	@Transactional(readOnly = true)
	@Query(value = " SELECT d.healthcareProfessionalId " +
					"FROM Diary d " +
					"INNER JOIN DoctorsOffice do ON (do.id = d.doctorsOfficeId) " +
					"WHERE d.active is true " +
					"AND do.institutionId = :institutionId ")
	List<Integer> getProfessionalsByActiveDiary(@Param("institutionId") Integer institutionId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE healthcare_professional_specialty " +
            "SET deleted = false " +
            "WHERE id = :healthcareProfessionalSpecialtyId",  nativeQuery=true)
    void setDeletedFalse(@Param("healthcareProfessionalSpecialtyId") Integer healthcareProfessionalSpecialtyId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT hps " +
			"FROM HealthcareProfessionalSpecialty hps " +
			"WHERE hps.professionalProfessionId = :professionalProfessionsId " +
			"AND hps.clinicalSpecialtyId = :clinicalSpecialtyId ")
	Optional<HealthcareProfessionalSpecialty> findByUniqueKey(@Param("professionalProfessionsId") Integer professionalProfessionsId,
															  @Param("clinicalSpecialtyId")  Integer clinicalSpecialtyId);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE HealthcareProfessionalSpecialty e  "
			+ "SET e.deleteable.deleted = true "
			+ ", e.deleteable.deletedOn = CURRENT_TIMESTAMP "
			+ ", e.deleteable.deletedBy = ?#{ principal.userId } "
			+ "WHERE e.professionalProfessionId = :professionalProfessionId ")
    void deleteByProfessionalProfessionId(@Param("professionalProfessionId") Integer professionalProfessionId);

	@Query(value = "SELECT hps FROM HealthcareProfessionalSpecialty hps " +
			"WHERE hps.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"AND hps.professionalProfessionId = :professionalProfessionId")
	Optional<HealthcareProfessionalSpecialty> findByProfessionAndSpecialty(@Param("professionalProfessionId") Integer professionalProfessionId, @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId);
}
