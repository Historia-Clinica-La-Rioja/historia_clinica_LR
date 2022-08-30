package net.pladema.staff.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.staff.repository.entity.ProfessionalProfessions;

@Repository
public interface ProfessionalProfessionRepository extends SGXAuditableEntityJPARepository<ProfessionalProfessions, Integer> {

	@Transactional(readOnly = true)
	@Query(value = " SELECT pp " +
			"FROM ProfessionalProfessions pp " +
			"WHERE pp.healthcareProfessionalId = :healthcareProfessionalId "+
			"AND pp.professionalSpecialtyId = :professionalSpecialtyId ")
	Optional<ProfessionalProfessions> findByHealthcareProfessionalIdAndProfessionalSpecialtyId(@Param("healthcareProfessionalId") Integer healthcareProfessionalId,
																							   @Param("professionalSpecialtyId") Integer professionalSpecialtyId);
	@Transactional(readOnly = true)
	@Query(value = " SELECT count(DISTINCT hp.id) " +
			"FROM ProfessionalProfessions hp " +
			"WHERE hp.healthcareProfessionalId = :healthcareProfessionalId " +
			"AND hp.deleteable.deleted = false")
    int countActiveByHealthcareProfessionalId(@Param("healthcareProfessionalId") Integer healthcareProfessionalId);
}
