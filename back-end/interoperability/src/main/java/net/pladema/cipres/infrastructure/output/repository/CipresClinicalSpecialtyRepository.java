package net.pladema.cipres.infrastructure.output.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CipresClinicalSpecialtyRepository extends JpaRepository<CipresClinicalSpecialty, CipresClinicalSpecialtyPk> {

	@Query("SELECT ccs.pk.cipresClinicalSpecialtyId FROM CipresClinicalSpecialty ccs WHERE ccs.pk.clinicalSpecialtyId = :clinicalSpecialtyId")
	Optional<String> findByClinicalSpecialtyId(@Param("clinicalSpecialtyId") Integer clinicalSpecialtyId);


}
