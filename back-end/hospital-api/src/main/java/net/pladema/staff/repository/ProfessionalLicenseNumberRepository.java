package net.pladema.staff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.staff.repository.entity.ProfessionalLicenseNumber;

@Repository
public interface ProfessionalLicenseNumberRepository extends JpaRepository<ProfessionalLicenseNumber, Integer> {
	@Transactional(readOnly = true)
	@Query(value = "SELECT pln " +
			"FROM ProfessionalLicenseNumber pln " +
			"WHERE pln.healthcareProfessionalId IS NOT NULL " +
			"AND pln.healthcareProfessionalSpecialtyId IS NULL ")
	List<ProfessionalLicenseNumber> findAllHealthProfessionalRegistrationNumbers();
}
