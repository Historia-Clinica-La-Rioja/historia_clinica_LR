package net.pladema.staff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.staff.repository.entity.ProfessionalLicenseNumber;
import net.pladema.staff.service.domain.ELicenseNumberTypeBo;

@Repository
public interface ProfessionalLicenseNumberRepository extends JpaRepository<ProfessionalLicenseNumber, Integer> {
	@Transactional(readOnly = true)
	@Query(value = "SELECT pln " +
			"FROM ProfessionalLicenseNumber pln " +
			"WHERE pln.professionalProfessionId IS NOT NULL " +
			"AND pln.healthcareProfessionalSpecialtyId IS NULL ")
	List<ProfessionalLicenseNumber> findAllHealthProfessionalRegistrationNumbers();

	@Transactional(readOnly = true)
	@Query(value = "SELECT pln " +
			"FROM ProfessionalLicenseNumber pln " +
			"WHERE pln.professionalProfessionId IS NULL " +
			"AND pln.healthcareProfessionalSpecialtyId IS NOT NULL ")
	List<ProfessionalLicenseNumber> findAllHealthProfessionalSpecialtyLicenseNumbers();

	@Transactional(readOnly = true)
	@Query(value = "SELECT (CASE WHEN COUNT(pln.id) = 1 THEN TRUE ELSE FALSE END)  " +
			"FROM ProfessionalLicenseNumber pln " +
			"WHERE pln.professionalProfessionId = :professionalProfessionId " +
			"AND pln.healthcareProfessionalSpecialtyId IS NULL " +
			"AND pln.type =  :type ")
	boolean existsProfessionalLicense(@Param("professionalProfessionId") Integer professionalProfessionId,
						  @Param("type") ELicenseNumberTypeBo type);

	@Transactional(readOnly = true)
	@Query(value = "SELECT (CASE WHEN COUNT(pln.id) = 1 THEN TRUE ELSE FALSE END)  " +
			"FROM ProfessionalLicenseNumber pln " +
			"WHERE pln.professionalProfessionId IS NULL " +
			"AND pln.healthcareProfessionalSpecialtyId = :healthcareProfessionalSpecialtyId " +
			"AND pln.type =  :type ")
	boolean existsProfessionalSpecialtyLicense(@Param("healthcareProfessionalSpecialtyId") Integer healthcareProfessionalSpecialtyId,
									  @Param("type") ELicenseNumberTypeBo type);

	@Transactional(readOnly = true)
	@Query(value = "SELECT pln " +
			"FROM ProfessionalLicenseNumber pln " +
			"WHERE pln.professionalProfessionId IN " +
			"(SELECT pp.id FROM ProfessionalProfessions pp " +
			"WHERE pp.healthcareProfessionalId = :healthcareProfessionalId " +
			"AND pp.deleteable.deleted = false OR pp.deleteable.deleted IS NULL) " +
			"OR pln.healthcareProfessionalSpecialtyId IN " +
			"(SELECT hps.id FROM HealthcareProfessionalSpecialty hps " +
			"JOIN ProfessionalProfessions pp ON (pp.id = hps.professionalProfessionId) " +
			"WHERE pp.healthcareProfessionalId = :healthcareProfessionalId " +
			"AND hps.deleteable.deleted = false OR hps.deleteable.deleted IS NULL) ")
	List<ProfessionalLicenseNumber> findByHealthcareProfessionalId(@Param("healthcareProfessionalId") Integer healthcareProfessionalId);

}
